package com.xxmassdeveloper.mpchartexample;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.filter.Approximator;
import com.github.mikephil.charting.data.filter.Approximator.ApproximatorType;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.XLabels;
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase;
import com.xxmassdeveloper.mpchartexample.utils.Colors;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import java.util.ArrayList;

public class MultiLineChartActivity extends DemoBase implements OnSeekBarChangeListener,
    OnChartValueSelectedListener {

  private LineChart mChart;
  private SeekBar mSeekBarX, mSeekBarY;
  private TextView tvX, tvY;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_linechart);

    tvX = (TextView) findViewById(R.id.tvXMax);
    tvY = (TextView) findViewById(R.id.tvYMax);

    mSeekBarX = (SeekBar) findViewById(R.id.seekBar1);
    mSeekBarX.setOnSeekBarChangeListener(this);

    mSeekBarY = (SeekBar) findViewById(R.id.seekBar2);
    mSeekBarY.setOnSeekBarChangeListener(this);

    mChart = (LineChart) findViewById(R.id.chart1);
    mChart.setOnChartValueSelectedListener(this);

    // mChart.setStartAtZero(true);

    // disable the drawing of values into the chart
    mChart.setDrawYValues(false);

    mChart.setCircleSize(5f);
    mChart.setYLabelCount(6);

    // enable value highlighting
    mChart.setHighlightEnabled(true);

    // enable touch gestures
    mChart.setTouchEnabled(true);

    // enable scaling and dragging
    mChart.setDragEnabled(true);

    // if disabled, scaling can be done on x- and y-axis separately
    mChart.setPinchZoom(false);

    mSeekBarX.setProgress(45);
    mSeekBarY.setProgress(100);

    //        Legend l = mChart.getLegend();
    //        l.setPosition(LegendPosition.RIGHT_OF_CHART);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.line, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {
    case R.id.actionToggleValues: {
      if (mChart.isDrawYValuesEnabled())
        mChart.setDrawYValues(false);
      else
        mChart.setDrawYValues(true);
      mChart.invalidate();
      break;
    }
    case R.id.actionTogglePinch: {
      if (mChart.isPinchZoomEnabled())
        mChart.setPinchZoom(false);
      else
        mChart.setPinchZoom(true);

      mChart.invalidate();
      break;
    }
    case R.id.actionToggleHighlight: {
      if (mChart.isHighlightEnabled())
        mChart.setHighlightEnabled(false);
      else
        mChart.setHighlightEnabled(true);
      mChart.invalidate();
      break;
    }
    case R.id.actionToggleFilled: {
      if (mChart.isDrawFilledEnabled())
        mChart.setDrawFilled(false);
      else
        mChart.setDrawFilled(true);
      mChart.invalidate();
      break;
    }
    case R.id.actionToggleCircles: {
      if (mChart.isDrawCirclesEnabled())
        mChart.setDrawCircles(false);
      else
        mChart.setDrawCircles(true);
      mChart.invalidate();
      break;
    }
    case R.id.actionToggleFilter: {

      // the angle of filtering is 35°
      Approximator a = new Approximator(ApproximatorType.DOUGLAS_PEUCKER, 35);

      if (!mChart.isFilteringEnabled()) {
        mChart.enableFiltering(a);
      } else {
        mChart.disableFiltering();
      }
      mChart.invalidate();
      break;
    }
    case R.id.actionToggleStartzero: {
      if (mChart.isStartAtZeroEnabled())
        mChart.setStartAtZero(false);
      else
        mChart.setStartAtZero(true);

      mChart.invalidate();
      break;
    }
    case R.id.actionToggleAdjustXLegend: {
      XLabels xLabels = mChart.getXLabels();

      if (xLabels.isAdjustXLabelsEnabled())
        xLabels.setAdjustXLabels(false);
      else
        xLabels.setAdjustXLabels(true);

      mChart.invalidate();
      break;
    }
    case R.id.actionSave: {
      // mChart.saveToGallery("title"+System.currentTimeMillis());
      mChart.saveToPath("title" + System.currentTimeMillis(), "");
      break;
    }
    }
    return true;
  }

  @Override
  public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    tvX.setText("" + (mSeekBarX.getProgress() + 1));
    tvY.setText("" + (mSeekBarY.getProgress()));

    ArrayList<Long> xVals = new ArrayList<Long>();
    for (long i = 0; i < mSeekBarX.getProgress(); i++) {
      xVals.add(i);
    }

    ArrayList<Double[]> values = new ArrayList<Double[]>();

    for (int z = 0; z < 3; z++) {

      Double[] vals = new Double[mSeekBarX.getProgress()];

      for (int i = 0; i < mSeekBarX.getProgress(); i++) {
        double val = (Math.random() * mSeekBarY.getProgress()) + 3;
        vals[i] = val;
      }

      values.add(vals);
    }

    ArrayList<LineDataSet> dataSets = makeDataSets(values);
    int i = 0;
    for (DataSet set : dataSets) {
      set.getDrawingSpec().getBasicPaint().setColor(getResources().getColor(Colors.VORDIPLOM_COLORS[i % Colors.VORDIPLOM_COLORS.length]));
      i++;
    }

    ChartData<LineDataSet> data = new ChartData<LineDataSet>(xVals, dataSets);
    mChart.setData(data);
    mChart.invalidate();
  }

  @Override
  public void onValuesSelected(Entry[] values, Highlight[] highlights) {
    Log.i("VALS SELECTED",
        "Value: " + values[0].getVal() + ", xIndex: " + highlights[0].getXIndex()
            + ", DataSet index: " + highlights[0].getDataSetIndex());
  }

  @Override
  public void onNothingSelected() {
    // TODO Auto-generated method stub

  }

  @Override
  public void onStartTrackingTouch(SeekBar seekBar) {
  }

  @Override
  public void onStopTrackingTouch(SeekBar seekBar) {
  }


  /**
   * Convenience method to create multiple DataSets of different types with
   * various double value arrays. Each double array represents the data of one
   * DataSet with a type created by this method, starting at 0 (and
   * incremented).
   *
   * @param yValues
   * @return
   */
  public static ArrayList<LineDataSet> makeDataSets(ArrayList<Double[]> yValues) {

    ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();

    for (int i = 0; i < yValues.size(); i++) {

      Double[] curValues = yValues.get(i);

      ArrayList<Entry> entries = new ArrayList<Entry>();

      for (int j = 0; j < curValues.length; j++) {
        entries.add(new Entry(curValues[j].floatValue(), j));
      }

      dataSets.add(new LineDataSet(entries, "DS " + i));
    }

    return dataSets;
  }

}
