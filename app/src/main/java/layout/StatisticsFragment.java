package layout;


import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import se.mah.aliona.watchmywallet.MainActivity;
import se.mah.aliona.watchmywallet.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatisticsFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static final String ALL_TIME_CHECKED = "all_time_checked";
    private static final String START_DATE = "start_date";
    private static final String END_DATE = "end_date";

    private HorizontalBarChart mHorizontalChart;
    private TextView mTopMessage;
    private TextView mBalanceMessage;
    private PieChart mExpendituresPieChart;
    private PieChart mIncomePieChart;
    private CheckBox mCheckBoxAllTime;
    private Button mStartDateButton;
    private Button mEndDateButton;

    private ArrayList<PieEntry> mExpendituresEntries;
    private ArrayList<PieEntry> mIncomeEntries;
    private ArrayList<BarEntry> mHorizontalChartEntries;

    private boolean mAllTimeChecked = true;
    private long mStartDate;
    private long mEndDate;

    protected Typeface mTfRegular;
    protected Typeface mTfLight;


    public StatisticsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(this.toString(), "ON START");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(this.toString(), "ON RESUME");
    }

    @Override
    protected void restoreState(Bundle savedInstanceState) {
        Log.i(this.toString(), "Saved instance is not null!!!!!!!!!!!!!!!");
        mAllTimeChecked = savedInstanceState.getBoolean(ALL_TIME_CHECKED);
        mStartDate = savedInstanceState.getLong(START_DATE);
        mEndDate = savedInstanceState.getLong(END_DATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(this.toString(), "ON CREATE VIEW");
        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        }
        cleanContainer(container);
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        initialiseUI(view);
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.appbar_button_refresh_transfers).setVisible(false);
        menu.findItem(R.id.appbar_button_refresh_charts).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.appbar_button_refresh_charts:
                requestRefreshCharts();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initialiseUI(View view) {
        mExpendituresEntries = mMainActivity.getDataForExpPieChart(mStartDate,mEndDate);
        mIncomeEntries = mMainActivity.getDataForIncPieChart(mStartDate,mEndDate);
        mHorizontalChartEntries = mMainActivity.getDataForHorizontalChart(mStartDate,mEndDate);

        mTopMessage = view.findViewById(R.id.stats_message_top);
        mTopMessage.setText(
                "Here is how your economy looks, " + mName + ". Choose to see for all time or specific dates."
        );
        mBalanceMessage = view.findViewById(R.id.stats_balance_message);

        mStartDateButton = view.findViewById(R.id.stats_button_start_date);
        mStartDateButton.setOnClickListener(this);
        mStartDateButton.setEnabled(!mAllTimeChecked);

        mEndDateButton = view.findViewById(R.id.stats_button_end_date);
        mEndDateButton.setOnClickListener(this);
        mEndDateButton.setEnabled(!mAllTimeChecked);

        mCheckBoxAllTime = view.findViewById(R.id.stats_check_box_select_all_time);
        mCheckBoxAllTime.setOnCheckedChangeListener(this);
        mCheckBoxAllTime.setChecked(mAllTimeChecked);

        if (mStartDate != 0) {
            mStartDateButton.setText(MainActivity.prettify(mStartDate));
        }

        if (mEndDate != 0) {
            mEndDateButton.setText(MainActivity.prettify(mEndDate));
        }

        initialiseHorizontalChart(view);
        initialiseExpPieChart(view);
        initialiseIncPieChart(view);
    }

    private void initialiseIncPieChart(View view) {
        mIncomePieChart = view.findViewById(R.id.inc_pie_chart);

        mIncomePieChart.setUsePercentValues(true);
        mIncomePieChart.getDescription().setEnabled(false);
        mIncomePieChart.setExtraOffsets(5, 10, 5, 5);

        mIncomePieChart.setDragDecelerationFrictionCoef(0.95f);

        mIncomePieChart.setCenterTextTypeface(mTfLight);

        mIncomePieChart.setDrawHoleEnabled(true);
        mIncomePieChart.setHoleColor(Color.WHITE);

        mIncomePieChart.setTransparentCircleColor(Color.WHITE);
        mIncomePieChart.setTransparentCircleAlpha(110);

        mIncomePieChart.setHoleRadius(58f);
        mIncomePieChart.setTransparentCircleRadius(61f);

        mIncomePieChart.setDrawCenterText(true);

        mIncomePieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mIncomePieChart.setRotationEnabled(true);
        mIncomePieChart.setHighlightPerTapEnabled(true);

        setIncomePieChartData(mIncomeEntries);

        mIncomePieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mHorizontalChart.spin(2000, 0, 360);

        Legend l = mIncomePieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mIncomePieChart.setEntryLabelColor(Color.BLACK);
        mIncomePieChart.setEntryLabelTypeface(mTfRegular);
        mIncomePieChart.setEntryLabelTextSize(12f);
    }

    private void initialiseExpPieChart(View view) {
        mExpendituresPieChart = view.findViewById(R.id.exp_pie_chart);

        mExpendituresPieChart.setUsePercentValues(true);
        mExpendituresPieChart.getDescription().setEnabled(false);
        mExpendituresPieChart.setExtraOffsets(5, 10, 5, 5);

        mExpendituresPieChart.setDragDecelerationFrictionCoef(0.95f);

        mExpendituresPieChart.setCenterTextTypeface(mTfLight);
//        mExpendituresPieChart.setCenterText(generateCenterSpannableText());

        mExpendituresPieChart.setDrawHoleEnabled(true);
        mExpendituresPieChart.setHoleColor(Color.WHITE);

        mExpendituresPieChart.setTransparentCircleColor(Color.WHITE);
        mExpendituresPieChart.setTransparentCircleAlpha(110);

        mExpendituresPieChart.setHoleRadius(58f);
        mExpendituresPieChart.setTransparentCircleRadius(61f);

        mExpendituresPieChart.setDrawCenterText(true);

        mExpendituresPieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mExpendituresPieChart.setRotationEnabled(true);
        mExpendituresPieChart.setHighlightPerTapEnabled(true);

        setExpenditurePieChartData(mExpendituresEntries);

        mExpendituresPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mHorizontalChart.spin(2000, 0, 360);

        Legend l = mExpendituresPieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//        l.setOrientation(Legend.LegendOrientation.VERTICAL);
//        l.setDrawInside(false);
//        l.setEnabled(false);

        // entry label styling
        mExpendituresPieChart.setEntryLabelColor(Color.BLACK);
        mExpendituresPieChart.setEntryLabelTypeface(mTfRegular);
        mExpendituresPieChart.setEntryLabelTextSize(12f);
    }

    private void initialiseHorizontalChart(View view) {
        mHorizontalChart = view.findViewById(R.id.horizontal_chart);

        mHorizontalChart.setDrawBarShadow(false);
        mHorizontalChart.setDrawValueAboveBar(true);
        mHorizontalChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mHorizontalChart.setMaxVisibleValueCount(10);

        // scaling can now only be done on x- and y-axis separately
        mHorizontalChart.setPinchZoom(false);

        // draw shadows for each bar that show the maximum value
        // mHorizontalChart.setDrawBarShadow(true);

        mHorizontalChart.setDrawGridBackground(false);

        XAxis xl = mHorizontalChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTypeface(mTfLight);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGranularity(10f);

        YAxis yl = mHorizontalChart.getAxisLeft();
        yl.setTypeface(mTfLight);
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yl.setInverted(true);

        YAxis yr = mHorizontalChart.getAxisRight();
        yr.setTypeface(mTfLight);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yr.setInverted(true);

        setHorizontalChartData(mHorizontalChartEntries);
        mHorizontalChart.setFitBars(true);
        mHorizontalChart.animateY(2500);

        Legend l = mHorizontalChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);
    }

    private void setExpenditurePieChartData(ArrayList<PieEntry> dbEntries) {

        ArrayList<PieEntry> entries = dbEntries;
        for (PieEntry entry : entries) {
            Log.i(this.toString(), entry.getLabel() + " " + entry.getValue());
        }

        PieDataSet dataSet = new PieDataSet(entries, "Your expenses");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);


        ArrayList<Integer> colors = new ArrayList<Integer>();

//        for (int c : ColorTemplate.VORDIPLOM_COLORS)
//            colors.add(c);

        for (int c : ColorTemplate.MATERIAL_COLORS)
            colors.add(c);

//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

//        dataSet.setValueLinePart1OffsetPercentage(80.f);
//        dataSet.setValueLinePart1Length(0.2f);
//        dataSet.setValueLinePart2Length(0.4f);
//        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(mTfLight);
        mExpendituresPieChart.setData(data);

        // undo all highlights
        mExpendituresPieChart.highlightValues(null);

        mExpendituresPieChart.invalidate();
    }

    private void setIncomePieChartData(ArrayList<PieEntry> dbEntries) {

        ArrayList<PieEntry> entries = dbEntries;

        PieDataSet dataSet = new PieDataSet(entries, "Your income");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.MATERIAL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(mTfLight);
        mIncomePieChart.setData(data);

        // undo all highlights
        mIncomePieChart.highlightValues(null);

        mIncomePieChart.invalidate();
    }

    private void setHorizontalChartData(ArrayList<BarEntry> list) {
        BarDataSet set1;
        BarDataSet set2;
        float barWidth = 0.6f; // x2 dataset

        ArrayList<BarEntry> exp = new ArrayList<>();
        ArrayList<BarEntry> inc = new ArrayList<>();

        float expenditureTotal = 0;
        float incomeTotal = 0;
        for (BarEntry entry : list) {
            if (entry.getX() == 0f) {
                expenditureTotal = entry.getY();
                exp.add(entry);
            } else if (entry.getX() == 1f) {
                incomeTotal = entry.getY();
                inc.add(entry);
            }
        }

        setBalanceMessage(expenditureTotal, incomeTotal);

        set1 = new BarDataSet(exp, "Expenses");
        ArrayList<Integer> colours = new ArrayList<>();
        colours.add(ContextCompat.getColor(getContext(), R.color.expenditureColor));
        set1.setColors(colours);
        set1.setDrawIcons(false);

        set2 = new BarDataSet(inc, "Income");
        ArrayList<Integer> colours2 = new ArrayList<>();
        colours2.add(ContextCompat.getColor(getContext(), R.color.colorAccentGreen));
        set2.setColors(colours2);
        set2.setDrawIcons(false);

        BarData data = new BarData(set1, set2);
        data.setValueTextSize(10f);
        data.setValueTypeface(mTfLight);
        data.setBarWidth(barWidth);
        mHorizontalChart.setData(data);
        mHorizontalChart.notifyDataSetChanged();
        mHorizontalChart.invalidate();
    }

    private void setBalanceMessage(float exp, float inc) {
        String msg;
        if (exp > inc) {
            msg = "Oooops! Looks like you went under budget in this period.";
        } else if (inc > exp) {
            msg = "Well done! You spent less than you earned in this period.";
        } else {
            msg = "It's hard to believe, but you have spent exactly the same amount of money that you've earned in this period.";
        }
        mBalanceMessage.setText(msg);
    }

    private void requestRefreshCharts() {
        if (mCheckBoxAllTime.isChecked()) {
            mExpendituresEntries = mMainActivity.getDataForExpPieChart(0,0);
            mIncomeEntries = mMainActivity.getDataForIncPieChart(0,0);
            mHorizontalChartEntries = mMainActivity.getDataForHorizontalChart(0,0);
        } else {
            if (mStartDate == 0 || mEndDate == 0) {
                Snackbar snackbar = Snackbar.make(this.getView(), "One of the dates is missing!", Snackbar.LENGTH_SHORT);
                snackbar.show();
            } else if (mEndDate < mStartDate) {
                Snackbar snackbar = Snackbar.make(this.getView(), "End date can't be before start date!", Snackbar.LENGTH_SHORT);
                snackbar.show();
            } else {
                mExpendituresEntries = mMainActivity.getDataForExpPieChart(mStartDate, mEndDate);
                mIncomeEntries = mMainActivity.getDataForIncPieChart(mStartDate, mEndDate);
                mHorizontalChartEntries = mMainActivity.getDataForHorizontalChart(mStartDate, mEndDate);
            }
        }
        setExpenditurePieChartData(mExpendituresEntries);
        setIncomePieChartData(mIncomeEntries);
        setHorizontalChartData(mHorizontalChartEntries);
    }

    @Override
    public void onClick(View view) {
        if (view == mStartDateButton) {
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            DatePickerDialog picker = new DatePickerDialog(getContext(), new StartDatePickerListener(),
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            picker.show();
        } else if (view == mEndDateButton) {
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            DatePickerDialog picker = new DatePickerDialog(getContext(), new EndDatePickerListener(),
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            picker.show();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        if (checked) {
            mStartDateButton.setEnabled(false);
            mEndDateButton.setEnabled(false);
        } else if (!checked) {
            mStartDateButton.setEnabled(true);
            mEndDateButton.setEnabled(true);
        }
    }

    private class StartDatePickerListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            // year, month, day
            String year = String.valueOf(i);
            String month = String.valueOf(i1+1);
            String day = String.valueOf(i2);

            if (month.length() == 1) {
                month = "0".concat(month);
            }
            if (day.length() == 1) {
                day = "0".concat(day);
            }
            StringBuilder stringBuilder = new StringBuilder(year + month +day);
            mStartDate = Long.valueOf(stringBuilder.toString());
            mStartDateButton.setText(mMainActivity.prettify(mStartDate));
        }
    }

    private class EndDatePickerListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            // year, month, day
            String year = String.valueOf(i);
            String month = String.valueOf(i1+1);
            String day = String.valueOf(i2);

            if (month.length() == 1) {
                month = "0".concat(month);
            }
            if (day.length() == 1) {
                day = "0".concat(day);
            }
            StringBuilder stringBuilder = new StringBuilder(year + month +day);
            mEndDate = Long.valueOf(stringBuilder.toString());
            mEndDateButton.setText(mMainActivity.prettify(mEndDate));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(this.toString(), "ON PAUSE");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(this.toString(), "ON STOP");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i(this.toString(), "ON SAVE INSTANCE");
        super.onSaveInstanceState(outState);
        outState.putBoolean(ALL_TIME_CHECKED, mAllTimeChecked);
        outState.putLong(START_DATE, mStartDate);
        outState.putLong(END_DATE, mEndDate);
    }
}
