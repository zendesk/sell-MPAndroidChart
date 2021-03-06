package com.xxmassdeveloper.mpchartexample;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.ChartData.LabelFormatter;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.Legend.LegendPosition;
import com.github.mikephil.charting.utils.MulticolorDrawingSpec;
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase;
import com.xxmassdeveloper.mpchartexample.utils.Colors;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import java.util.ArrayList;

public class PieChartActivity extends DemoBase implements OnSeekBarChangeListener, OnChartValueSelectedListener {

  private PieChart mChart;
  private SeekBar mSeekBarX, mSeekBarY;
  private TextView tvX, tvY;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_piechart);

    tvX = (TextView) findViewById(R.id.tvXMax);
    tvY = (TextView) findViewById(R.id.tvYMax);

    mSeekBarX = (SeekBar) findViewById(R.id.seekBar1);
    mSeekBarY = (SeekBar) findViewById(R.id.seekBar2);

    mSeekBarX.setOnSeekBarChangeListener(this);
    mSeekBarY.setOnSeekBarChangeListener(this);

    mChart = (PieChart) findViewById(R.id.chart1);

    Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

    mChart.setValueTypeface(tf);
    mChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));

    // set a space between the slices
    mChart.setSliceSpace(3f);

    mChart.setHoleRadius(60f);

    mChart.setDrawYValues(true);
    mChart.setDrawCenterText(true);

    mChart.setDescription("This is a description.");
    mChart.setDrawHoleEnabled(true);

    // draws the corresponding description value into the slice
    mChart.setDrawXValues(true);
    mChart.setTouchEnabled(true);

    // display percentage values
    mChart.setUsePercentValues(true);

    // add a selection listener
    mChart.setOnChartValueSelectedListener(this);

    mSeekBarX.setProgress(5);
    mSeekBarY.setProgress(100);

    Legend l = mChart.getLegend();
    l.setPosition(LegendPosition.RIGHT_OF_CHART);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.pie, menu);
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
    case R.id.actionTogglePercent: {
      if (mChart.isUsePercentValuesEnabled())
        mChart.setUsePercentValues(false);
      else
        mChart.setUsePercentValues(true);
      mChart.invalidate();
      break;
    }
    case R.id.actionToggleHole: {
      if (mChart.isDrawHoleEnabled())
        mChart.setDrawHoleEnabled(false);
      else
        mChart.setDrawHoleEnabled(true);
      mChart.invalidate();
      break;
    }
    case R.id.actionDrawCenter: {
      if (mChart.isDrawCenterTextEnabled())
        mChart.setDrawCenterText(false);
      else
        mChart.setDrawCenterText(true);
      mChart.invalidate();
      break;
    }
    case R.id.actionToggleXVals: {
      if (mChart.isDrawXValuesEnabled())
        mChart.setDrawXValues(false);
      else
        mChart.setDrawXValues(true);
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

    tvX.setText("" + (mSeekBarX.getProgress()));
    tvY.setText("" + (mSeekBarY.getProgress()));

    ArrayList<Entry> yVals1 = new ArrayList<Entry>();

    // IMPORTANT: In a PieChart, no values (Entry) should have the same xIndex (even if from different DataSets), since no values can be drawn above each other.
    for (int i = 0; i < mSeekBarX.getProgress(); i++) {
      float mult = (mSeekBarY.getProgress());
      float val = (float) (Math.random() * mult) + mult / 5;// + (float) ((mult * 0.1) / 10);
      yVals1.add(new Entry(val, i));
    }

    ArrayList<Long> xVals = new ArrayList<Long>();

    for (int i = 0; i < mSeekBarX.getProgress(); i++) {
      xVals.add((long) (i + 1));
    }

    PieDataSet set1 = new PieDataSet(yVals1, "Content");
    set1.getDrawingSpec().setColors(MulticolorDrawingSpec.fromResources(this, Colors.FRESH_COLORS));

    ArrayList<PieDataSet> dataSets = new ArrayList<PieDataSet>();
    dataSets.add(set1);

    ChartData<PieDataSet> data = new ChartData<PieDataSet>(xVals, dataSets, new LabelFormatter() {
      @Override
      public String formatValue(long value) {
        return "Text" + value;
      }
    });
    mChart.setData(data);

    // undo all highlights
    mChart.highlightValues(null);

    // set a text for the chart center
    mChart.setCenterText("Total Value\n" + (int) mChart.getYValueSum() + "\n(all slices)");
    mChart.invalidate();
  }

  @Override
  public void onValuesSelected(Entry[] values, Highlight[] highs) {

    StringBuffer a = new StringBuffer();

    for (int i = 0; i < values.length; i++) {
      a.append("val: " + values[i].getVal() + ", x-ind: " + highs[i].getXIndex() + ", dataset-ind: " + highs[i].getDataSetIndex() + "\n");
    }

    Log.i("PieChart", "Selected: " + a.toString());
  }

  @Override
  public void onNothingSelected() {
    Log.i("PieChart", "nothing selected");
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
