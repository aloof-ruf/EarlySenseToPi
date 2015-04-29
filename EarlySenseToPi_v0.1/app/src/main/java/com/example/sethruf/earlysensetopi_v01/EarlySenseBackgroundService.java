package com.example.sethruf.earlysensetopi_v01;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;

import com.example.sethruf.earlysensetopi_v01.Database.EarlySenseDao;
import com.example.sethruf.earlysensetopi_v01.Database.EarlySenseDbHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by Seth.Ruf on 24/04/2015.
 */
public class EarlySenseBackgroundService extends Service {

    private final String PI_ADDRESS = "5C:F3:70:63:9A:E1";
    private final UUID SERIAL_PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    //private final UUID SERIAL_PORT_UUID = UUID.fromString("1E0CA4EA-299D-4335-93EB-27FCFE7fA848");
    private final String MESSAGE = "{\n" +
            "\"messageType\":\"req\",\n" +
            "\"startT\": 0,\n" +
            "\"endT\" : 0\n" +
            "}";
    private BluetoothAdapter adapter;
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private InputStream piToDevice;
    private OutputStream deviceToPi;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("Service", "Started Service");

        new Thread(new Runnable(){
            @Override
            public void run(){
                collectEarlySenseInformation();
            }
        }).start();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void collectEarlySenseInformation(){

        // get adapter
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null){
            if (adapter.isEnabled()){
                device = adapter.getRemoteDevice(PI_ADDRESS);

                boolean everythingIsGoingWell = true;

                // try to connect
                try {
                    socket = device.createRfcommSocketToServiceRecord(SERIAL_PORT_UUID);
                    socket.connect();
                }
                catch(IOException e){
                    Log.e("Service Error", "could not connect with device");
                    e.printStackTrace();
                    everythingIsGoingWell = false;
                }

                if (everythingIsGoingWell) {
                    try {
                        piToDevice = socket.getInputStream();
                        deviceToPi = socket.getOutputStream();

                        if (piToDevice != null && deviceToPi != null) {

                            deviceToPi.write(MESSAGE.getBytes("UTF-8"));
                            Thread.sleep(250);

                            BufferedReader r = new BufferedReader(new InputStreamReader(piToDevice));
                            String line;
                            Log.d("btDEBUG", "Start Read");
                            while ((line = r.readLine()) != null) {
                                Log.d("btDEBUG", "Line");
                                handleMessage(line);
                            }

                        }
                    }
                    catch (IOException | InterruptedException e) {
                        Log.e("Service Error", "stream error");
                        e.printStackTrace();
                        everythingIsGoingWell = false;
                    }
                }

                if (everythingIsGoingWell) {
                    try {
                        if (piToDevice != null) {
                            piToDevice.close();
                            piToDevice = null;
                        }

                        if (deviceToPi != null) {
                            deviceToPi.close();
                            deviceToPi = null;
                        }

                        Thread.sleep(1000);

                        if (socket != null) {
                            socket.close();
                            socket = null;
                        }
                    }
                    catch (InterruptedException | IOException e) {
                        Log.e("Service Error", "error closing stream");
                        e.printStackTrace();
                        everythingIsGoingWell = false;
                    }
                }
            }
            else {
                // send a 'bluetooth not enabled' message
                Log.e("Service Error", "bluetooth is not enabled");
            }
        }
        else {
            // throw a no adapter error or at least say something!
            Log.e("Service Error", "there is no bluetooth!");
        }

    }

    private void handleMessage(String jsonData){
        EarlySenseDao earlySenseDb = new EarlySenseDao(getApplicationContext());

        JsonObject jsonResult = new JsonParser().parse(jsonData).getAsJsonObject();
        JsonArray payload = jsonResult.getAsJsonArray("data");
        Log.d("Number of data elements", "" + payload.size());

        for (int i = 0; i< payload.size(); i++){
            int timestamp = payload.get(i).getAsJsonObject().get("t").getAsInt();
            int heartRate = payload.get(i).getAsJsonObject().get("hr").getAsInt();
            int respiratoryRate = payload.get(i).getAsJsonObject().get("rr").getAsInt();
            int movementLevel = payload.get(i).getAsJsonObject().get("ml").getAsInt();
            int inBed = payload.get(i).getAsJsonObject().get("inBed").getAsInt();

            EarlySenseReading reading = new EarlySenseReading(timestamp,
                    heartRate,
                    respiratoryRate,
                    movementLevel,
                    inBed != 0);

            earlySenseDb.insert(reading);
        }
    }

}
