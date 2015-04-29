package com.example.sethruf.earlysensetopi_v01;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.example.sethruf.earlysensetopi_v01.Database.EarlySenseDao;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ValueFormatter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

    // chart
    private LineChart chart;
    private LineChart movementLevelChart;
    private LineChart xAxisOnlyChart;
    private ArrayList<String> xVals;
    private ArrayList<Entry> heartRateVals;
    private ArrayList<Entry> respiratoryRateVals;
    private ArrayList<Entry> movementLevelVals;

    //private ScaleGestureDetector pinchZoom;

    /*private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            Log.d("Scale Zoom", "" + detector.getCurrentSpan());
            return true;
        }
    }*/

    class HeartRateFormatter implements ValueFormatter {
        private DecimalFormat decimalFormat;
        public HeartRateFormatter(){
            decimalFormat = new DecimalFormat("#0");
        }

        @Override
        public String getFormattedValue(float value){
            return decimalFormat.format(value);
        }
    }

    class RespiratoryRateFormatter implements ValueFormatter {
        private DecimalFormat decimalFormat;
        public RespiratoryRateFormatter(){
            decimalFormat = new DecimalFormat("#0");
        }

        @Override
        public String getFormattedValue(float value){
            return decimalFormat.format(value);
        }
    }

    class MovementLevelFormatter implements ValueFormatter {
        private DecimalFormat decimalFormat;
        public MovementLevelFormatter(){
            decimalFormat = new DecimalFormat("0");
        }

        @Override
        public String getFormattedValue(float value){
            return decimalFormat.format(value);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chart = (LineChart) findViewById(R.id.readings_chart);
        xAxisOnlyChart = (LineChart) findViewById(R.id.x_axis_only_chart);
        movementLevelChart = (LineChart) findViewById(R.id.readings_chart_movement_level);

        ChartsView entireView = (ChartsView) findViewById(R.id.entire_chart_view);

        //pinchZoom = new ScaleGestureDetector(entireView., new ScaleListener());

        xVals = new ArrayList<String>();
        heartRateVals = new ArrayList<Entry>();
        respiratoryRateVals = new ArrayList<Entry>();
        movementLevelVals = new ArrayList<Entry>();

        buildChart();
        buildXAxisOnlyChart();
        buildMovementLevelChart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume(){
        super.onResume();

        refreshReadings(null);
    }

    public void refreshReadings(View view){

        EarlySenseDao earlySenseDb = new EarlySenseDao(this);
        EarlySenseReading[] readings = earlySenseDb.getAll();

        xVals.clear();
        heartRateVals.clear();
        respiratoryRateVals.clear();
        movementLevelVals.clear();

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

        for (int i = 0; i < readings.length; i++) {
            Date timestampDate = new Date((long)(readings[i].getTimestamp()) * 1000);
            xVals.add(formatter.format(timestampDate));
            heartRateVals.add(new Entry(readings[i].getHeartRate(), i));
            respiratoryRateVals.add(new Entry(readings[i].getRespiratoryRate(), i));
            movementLevelVals.add(new Entry(readings[i].getMovementLevel(), i));
            Log.d("Database information", "Index[" + i + "] " + readings[i].getTimestamp());
        }

        chart.notifyDataSetChanged();
        chart.invalidate();
    }

    private void buildChart(){
        float textSize = 12f;
        float xAxisTextSize = 12f;
        float lineWidth = 6f;
        float circleSize = 5f;

        float heartRateMinValue = 0.0f;
        float heartRateMaxValue = 100.0f;

        float respiratoryRateMinValue = 0.0f;
        float respiratoryRateMaxValue = 100.0f;

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawLabels(false);

        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        yAxisLeft.setTextSize(textSize);
        yAxisLeft.setAxisMinValue(heartRateMinValue);
        yAxisLeft.setAxisMaxValue(heartRateMaxValue);
        yAxisLeft.setStartAtZero(false);
        yAxisLeft.setValueFormatter(new HeartRateFormatter());

        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        yAxisRight.setTextSize(textSize);
        yAxisRight.setAxisMinValue(respiratoryRateMinValue);
        yAxisRight.setAxisMaxValue(respiratoryRateMaxValue);
        yAxisRight.setStartAtZero(false);
        yAxisRight.setValueFormatter(new RespiratoryRateFormatter());

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();

        LineDataSet heartRateSet = new LineDataSet(heartRateVals, "Heart Rate");
        heartRateSet.setCircleColor(getResources().getColor(R.color.light_blue_600));
        heartRateSet.setCircleSize(circleSize);
        heartRateSet.setColor(getResources().getColor(R.color.light_blue_600));
        heartRateSet.setLineWidth(lineWidth);
        heartRateSet.setDrawValues(false);
        heartRateSet.setDrawFilled(false);
        dataSets.add(heartRateSet);

        LineDataSet respiratoryRateSet = new LineDataSet(respiratoryRateVals, "Respiratory Rate");
        respiratoryRateSet.setCircleColor(getResources().getColor(R.color.amber_700));
        respiratoryRateSet.setCircleSize(circleSize);
        respiratoryRateSet.setColor(getResources().getColor(R.color.amber_700));
        respiratoryRateSet.setLineWidth(lineWidth);
        respiratoryRateSet.setDrawValues(false);
        respiratoryRateSet.setDrawFilled(false);
        dataSets.add(respiratoryRateSet);

        LineData data = new LineData(xVals, dataSets);
        chart.setDescription("");
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);
        chart.zoom(1f, 1f, 0, 0);
        chart.setHighlightEnabled(false);
        chart.setData(data);
        chart.getLegend().setEnabled(false);
    }

    private void buildMovementLevelChart(){
        float textSize = 12f;
        float lineWidth = 6f;
        float circleSize = 5f;

        float movementMinValue = 0.0f;
        float movementMaxValue = 100.0f;

        XAxis xAxis = movementLevelChart.getXAxis();
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setDrawLabels(false);

        YAxis yAxisLeft = movementLevelChart.getAxisLeft();
        yAxisLeft.setTextSize(textSize);
        yAxisLeft.setAxisMinValue(movementMinValue);
        yAxisLeft.setAxisMaxValue(movementMaxValue);
        yAxisLeft.setStartAtZero(true);
        yAxisLeft.setValueFormatter(new MovementLevelFormatter());
        yAxisLeft.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);

        YAxis yAxisRight = movementLevelChart.getAxisRight();
        yAxisRight.setTextSize(textSize);
        yAxisRight.setAxisMinValue(movementMinValue);
        yAxisRight.setAxisMaxValue(movementMaxValue);
        yAxisRight.setStartAtZero(true);
        yAxisRight.setValueFormatter(new MovementLevelFormatter());
        yAxisRight.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();

        LineDataSet movementLevelSet = new LineDataSet(movementLevelVals, "Movement Level");
        movementLevelSet.setCircleColor(getResources().getColor(R.color.Violet));
        movementLevelSet.setCircleSize(circleSize);
        movementLevelSet.setColor(getResources().getColor(R.color.Violet));
        movementLevelSet.setLineWidth(lineWidth);
        movementLevelSet.setDrawValues(false);
        movementLevelSet.setDrawFilled(false);
        dataSets.add(movementLevelSet);

        LineData data = new LineData(xVals, dataSets);
        movementLevelChart.setPadding(0, 0, 0, 0);
        movementLevelChart.setDescription("");
        movementLevelChart.setScaleEnabled(false);
        movementLevelChart.setPinchZoom(false);
        movementLevelChart.zoom(1f, 1f, 0, 0);
        movementLevelChart.setHighlightEnabled(false);
        movementLevelChart.setData(data);
        movementLevelChart.getLegend().setEnabled(false);
    }

    private void buildXAxisOnlyChart(){
        float textSize = 12f;
        float xAxisTextSize = 12f;
        float lineWidth = 6f;
        float circleSize = 5f;

        XAxis xAxis = xAxisOnlyChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setTextSize(xAxisTextSize);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setAvoidFirstLastClipping(true);

        YAxis yAxisLeft = xAxisOnlyChart.getAxisLeft();
        yAxisLeft.setTextSize(textSize);
        yAxisLeft.setStartAtZero(true);
        yAxisLeft.setDrawAxisLine(false);
        yAxisLeft.setValueFormatter(new MovementLevelFormatter());
        yAxisLeft.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        yAxisLeft.setDrawLabels(false);

        YAxis yAxisRight = xAxisOnlyChart.getAxisRight();
        yAxisRight.setTextSize(textSize);
        yAxisRight.setStartAtZero(true);
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setValueFormatter(new MovementLevelFormatter());
        yAxisRight.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        yAxisRight.setDrawLabels(false);

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();

        LineDataSet movementLevelSet = new LineDataSet(movementLevelVals, "Movement Level");
        movementLevelSet.setCircleColor(getResources().getColor(R.color.Violet));
        movementLevelSet.setCircleSize(circleSize);
        movementLevelSet.setColor(getResources().getColor(R.color.Violet));
        movementLevelSet.setLineWidth(lineWidth);
        movementLevelSet.setDrawValues(false);
        movementLevelSet.setDrawFilled(false);
        dataSets.add(movementLevelSet);

        LineData data = new LineData(xVals, dataSets);
        xAxisOnlyChart.setDescription("");
        xAxisOnlyChart.setScaleEnabled(false);
        xAxisOnlyChart.setPinchZoom(false);
        xAxisOnlyChart.zoom(1f, 1f, 0, 0);
        xAxisOnlyChart.setHighlightEnabled(false);
        xAxisOnlyChart.setData(data);
        xAxisOnlyChart.getLegend().setEnabled(false);
    }

    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        pinchZoom.onTouchEvent(event);
        return true;
    }*/
}
