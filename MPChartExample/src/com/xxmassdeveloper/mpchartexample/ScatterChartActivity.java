package com.xxmassdeveloper.mpchartexample;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.charts.ScatterChart.ScatterShape;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterDataSet;
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

public class ScatterChartActivity extends DemoBase implements OnSeekBarChangeListener,
    OnChartValueSelectedListener {

  private ScatterChart mChart;
  private SeekBar mSeekBarX, mSeekBarY;
  private TextView tvX, tvY;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_scatterchart);

    tvX = (TextView) findViewById(R.id.tvXMax);
    tvY = (TextView) findViewById(R.id.tvYMax);

    mSeekBarX = (SeekBar) findViewById(R.id.seekBar1);
    mSeekBarX.setOnSeekBarChangeListener(this);

    mSeekBarY = (SeekBar) findViewById(R.id.seekBar2);
    mSeekBarY.setOnSeekBarChangeListener(this);

    mChart = (ScatterChart) findViewById(R.id.chart1);

    // specify the shapes for the datasets, one shape per dataset
    mChart.setScatterShapes(new ScatterShape[] { ScatterShape.SQUARE, ScatterShape.TRIANGLE, ScatterShape.CIRCLE });

    mChart.setOnChartValueSelectedListener(this);

    mChart.setYLabelCount(6);
    mChart.setTouchEnabled(true);
    mChart.setHighlightEnabled(true);
    mChart.setDrawYValues(false);

    mChart.setDragEnabled(true);

    mChart.setMaxVisibleValueCount(200);
    mChart.setPinchZoom(true);

    mSeekBarX.setProgress(45);
    mSeekBarY.setProgress(100);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.scatter, menu);
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
    case R.id.actionToggleHighlight: {
      if (mChart.isHighlightEnabled())
        mChart.setHighlightEnabled(false);
      else
        mChart.setHighlightEnabled(true);
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
    case R.id.actionToggleFilter: {

      Approximator a = new Approximator(ApproximatorType.DOUGLAS_PEUCKER, 25);

      if (!mChart.isFilteringEnabled()) {
        mChart.enableFiltering(a);
      } else {
        mChart.disableFiltering();
      }
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
    for (int i = 0; i < mSeekBarX.getProgress(); i++) {
      xVals.add((long) i);
    }

    ArrayList<Entry> yVals1 = new ArrayList<Entry>();
    ArrayList<Entry> yVals2 = new ArrayList<Entry>();
    ArrayList<Entry> yVals3 = new ArrayList<Entry>();

    for (int i = 0; i < mSeekBarX.getProgress(); i++) {
      float val = (float) (Math.random() * mSeekBarY.getProgress()) + 3;
      yVals1.add(new Entry(val, i));
    }

    for (int i = 0; i < mSeekBarX.getProgress(); i++) {
      float val = (float) (Math.random() * mSeekBarY.getProgress()) + 3;
      yVals2.add(new Entry(val, i));
    }

    for (int i = 0; i < mSeekBarX.getProgress(); i++) {
      float val = (float) (Math.random() * mSeekBarY.getProgress()) + 3;
      yVals3.add(new Entry(val, i));
    }

    // create a dataset and give it a type
    ScatterDataSet set1 = new ScatterDataSet(yVals1, "DS 1");
    ScatterDataSet set2 = new ScatterDataSet(yVals2, "DS 2");
    ScatterDataSet set3 = new ScatterDataSet(yVals3, "DS 3");

    ArrayList<ScatterDataSet> dataSets = new ArrayList<ScatterDataSet>();
    dataSets.add(set1); // add the datasets
    dataSets.add(set2);
    dataSets.add(set3);

    int i = 0;
    for (DataSet set : dataSets) {
      set.getDrawingSpec().getBasicPaint().setColor(getResources().getColor(Colors.VORDIPLOM_COLORS[i % Colors.VORDIPLOM_COLORS.length]));
      i++;
    }

    // create a data object with the datasets
    ChartData<ScatterDataSet> data = new ChartData<ScatterDataSet>(xVals, dataSets);

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
    // TODO Auto-generated method stub

  }

  @Override
  public void onStopTrackingTouch(SeekBar seekBar) {
    // TODO Auto-generated method stub

  }
}
