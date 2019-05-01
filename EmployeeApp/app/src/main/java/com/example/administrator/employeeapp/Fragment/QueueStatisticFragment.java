package com.example.administrator.employeeapp.Fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.employeeapp.Activity.MyMarkerView;
import com.example.administrator.employeeapp.Adapter.EmployeeListForStatisticAdapter;
import com.example.administrator.employeeapp.Adapter.QueueListForStatisticAdapter;
import com.example.administrator.employeeapp.Adapter.ReviewListForStatisticAdapter;
import com.example.administrator.employeeapp.Contract.BranchStatisticContract;
import com.example.administrator.employeeapp.Contract.QueueStatisticContract;
import com.example.administrator.employeeapp.Model.Account;
import com.example.administrator.employeeapp.Model.Employee;
import com.example.administrator.employeeapp.Model.Statistic;
import com.example.administrator.employeeapp.Presenter.BranchStatisticPresenter;
import com.example.administrator.employeeapp.Presenter.QueueStatisticPresenter;
import com.example.administrator.employeeapp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class QueueStatisticFragment extends Fragment implements QueueStatisticContract.View {
    private TextView pathTxt;
    private TextView numberOfRequestTxt;
    private TextView numberOfDoneRequestTxt;
    private TextView numberOfCancelRequestTxt;
    private TextView noOfInLineTimeTxt;
    private TextView noOfWaitingTxt;
    private TextView noOfUsingTxt;
    private TextView usingTimeTxt;

    private LineChart lineChart;
    private PieChart pieChart;

    private QueueStatisticContract.Presenter presenter;
    private AlertDialog waitingDialog;
    private AlertDialog.Builder waitingDialogBuilder;
    private AlertDialog.Builder noticeDialog;
    private AlertDialog createQueueDialog;
    private AlertDialog.Builder createQueueDialogBuilder;
    private SharedPreferences sharedPreferences;
    private Account account;
    private Employee employee;
    private String branchName;
    private String queueID;
    private String queueName;
    private Integer day;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.queue_statistic_fragment, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar2);
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Thống kê và dữ liệu");
        assignDialog();
        branchName = getArguments().getString("branchName");
        queueID = getArguments().getString("queueID");
        queueName = getArguments().getString("queueName");
        day = getArguments().getInt("day");
        sharedPreferences = this.getActivity().getSharedPreferences("data", MODE_PRIVATE);
        String accountString = sharedPreferences.getString("MyAccount", "empty");
        Gson gson = new Gson();
        account = new Account();
        if (!accountString.equals("empty")) {
            account = gson.fromJson(accountString, Account.class);
        }
        String employeeString = sharedPreferences.getString("Employee", "empty");
        employee = new Employee();
        if (!employeeString.equals("empty")) {
            employee = gson.fromJson(employeeString, Employee.class);
        }
        pathTxt = (TextView) view.findViewById(R.id.pathTxt);
        pathTxt.setText(branchName + " > " + queueName);
        numberOfRequestTxt = (TextView) view.findViewById(R.id.numberOfRequestTxt);
        numberOfDoneRequestTxt = (TextView) view.findViewById(R.id.numberOfDoneRequestTxt);
        numberOfCancelRequestTxt = (TextView) view.findViewById(R.id.numberOfCancelRequestTxt);
        noOfInLineTimeTxt = (TextView) view.findViewById(R.id.noOfInLineTimeTxt);
        noOfWaitingTxt = (TextView) view.findViewById(R.id.noOfWaitingTxt);
        noOfUsingTxt = (TextView) view.findViewById(R.id.noOfUsingTxt);
        usingTimeTxt = (TextView) view.findViewById(R.id.usingTimeTxt);
        lineChart = view.findViewById(R.id.chart);
        lineChart.setTouchEnabled(true);
        lineChart.setPinchZoom(true);
        MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.custom_marker_view);
        mv.setChartView(lineChart);
        lineChart.setMarker(mv);
        pieChart = (PieChart) view.findViewById(R.id.piechart);

        presenter = new QueueStatisticPresenter(this, account);
        presenter.getQueueStatistic(account.getToken(), queueID, day);
        return view;
    }

    private void assignDialog() {
        noticeDialog = new AlertDialog.Builder(getActivity());
        waitingDialogBuilder = new AlertDialog.Builder(getActivity());
        createQueueDialogBuilder = new AlertDialog.Builder(getActivity());
    }

    @Override
    public void showDialog(String message, Boolean isSuccess) {
        LayoutInflater inflater = getLayoutInflater();
        if (isSuccess) {
            View layout = inflater.inflate(R.layout.custom_toast_success,
                    (ViewGroup) getActivity().findViewById(R.id.custom_toast_container));
            TextView text = (TextView) layout.findViewById(R.id.toastTxt);
            text.setText(message);
            Toast toast = new Toast(getActivity());
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        } else {
            View layout = inflater.inflate(R.layout.custom_toast_fail,
                    (ViewGroup) getActivity().findViewById(R.id.custom_toast_container));
            TextView text = (TextView) layout.findViewById(R.id.toastTxt);
            text.setText(message);
            Toast toast = new Toast(getActivity());
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        }
    }

    @Override
    @TargetApi(21)
    public void showProgressBar() {
        waitingDialogBuilder.setView(R.layout.waiting_dialog);
        waitingDialogBuilder.setCancelable(false);
        waitingDialog = waitingDialogBuilder.show();
    }

    @Override
    public void hideProgressBar() {
        waitingDialog.dismiss();
    }

    @Override
    public void setUpComponent(Statistic statistic) {
        numberOfRequestTxt.setText(statistic.getNoOfRequest().toString());
        numberOfDoneRequestTxt.setText(statistic.getNoOfDone().toString());
        numberOfCancelRequestTxt.setText(statistic.getNoOfCancel().toString());
        noOfWaitingTxt.setText(statistic.getNoOfWaiting().toString());
        noOfUsingTxt.setText(statistic.getNoOfUsing().toString());
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        if (statistic.getInLineTime() != null)
            noOfInLineTimeTxt.setText((statistic.getInLineTime() - statistic.getInLineTime() % 60) / 60 + " phút " + String.format("%.02f", statistic.getInLineTime() % 60).replace(',', '.') + " giây");
        if (statistic.getUsingTime() != null)
            usingTimeTxt.setText((statistic.getUsingTime() - statistic.getUsingTime() % 60) / 60 + " phút " + String.format("%.02f", statistic.getUsingTime() % 60).replace(',', '.') + " giây");
    }

    @Override
    public void renderLineChart(Statistic statistic) {
        ArrayList<Entry> noOfRequest = new ArrayList<Entry>();
        List<Integer> temp = new ArrayList<Integer>();
        for (int i = statistic.getNoOfRequestPerDay().size() - 1; i >= 0; i--) {
            temp.add(statistic.getNoOfRequestPerDay().get(i));
        }
        if (temp != null) {
            statistic.setNoOfRequestPerDay(temp);
        }
        for (int i = 0; i < statistic.getNoOfRequestPerDay().size(); i++) {
            noOfRequest.add(new Entry(i, statistic.getNoOfRequestPerDay().get(i)));
        }

        LineDataSet lineDataSet1 = new LineDataSet(noOfRequest, "Lượt đăng ký vào hàng đợi");
        lineDataSet1.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet1.setColors(Color.BLACK);
        lineDataSet1.setLineWidth(3);
        lineDataSet1.setHighlightEnabled(true);
        lineDataSet1.setCircleColor(Color.BLACK);
        lineDataSet1.setCircleRadius(6);
        lineDataSet1.setCircleHoleRadius(3);
        lineDataSet1.setDrawHighlightIndicators(true);
        lineDataSet1.setHighLightColor(Color.RED);
        lineDataSet1.setValueTextSize(12f);
        lineDataSet1.setValueTextColor(Color.BLACK);
        lineDataSet1.setMode(LineDataSet.Mode.LINEAR);

        ArrayList<Entry> noOfDone = new ArrayList<Entry>();
        List<Integer> temp2 = new ArrayList<Integer>();
        for (int i = statistic.getNoOfDonePerDay().size() - 1; i >= 0; i--) {
            temp2.add(statistic.getNoOfDonePerDay().get(i));
        }
        if (temp2 != null) {
            statistic.setNoOfDonePerDay(temp2);
        }
        for (int i = 0; i < statistic.getNoOfDonePerDay().size(); i++) {
            noOfDone.add(new Entry(i, statistic.getNoOfDonePerDay().get(i)));
        }


        LineDataSet lineDataSet2 = new LineDataSet(noOfDone, "Hoàn thành");
        lineDataSet2.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet2.setColors(Color.rgb(80,220,100));
        lineDataSet2.setLineWidth(3);
        lineDataSet2.setHighlightEnabled(true);
        lineDataSet2.setCircleColor(Color.rgb(80,220,100));
        lineDataSet2.setCircleRadius(6);
        lineDataSet2.setCircleHoleRadius(3);
        lineDataSet2.setDrawHighlightIndicators(true);
        lineDataSet2.setHighLightColor(Color.RED);
        lineDataSet2.setValueTextSize(12f);
        lineDataSet2.setValueTextColor(Color.BLACK);
        lineDataSet2.setMode(LineDataSet.Mode.LINEAR);

        ArrayList<Entry> noOfCancel = new ArrayList<Entry>();
        List<Integer> temp3 = new ArrayList<Integer>();
        for (int i = statistic.getNoOfCancelPerDay().size() - 1; i >= 0; i--) {
            temp3.add(statistic.getNoOfCancelPerDay().get(i));
        }
        if (temp3 != null) {
            statistic.setNoOfCancelPerDay(temp3);
        }
        for (int i = 0; i < statistic.getNoOfCancelPerDay().size(); i++) {
            noOfCancel.add(new Entry(i, statistic.getNoOfCancelPerDay().get(i)));
        }


        LineDataSet lineDataSet3 = new LineDataSet(noOfCancel, "Hủy");
        lineDataSet3.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet3.setColors(Color.rgb(255,223,0));
        lineDataSet3.setLineWidth(3);
        lineDataSet3.setHighlightEnabled(true);
        lineDataSet3.setCircleColor(Color.rgb(255,223,0));
        lineDataSet3.setCircleRadius(6);
        lineDataSet3.setCircleHoleRadius(3);
        lineDataSet3.setDrawHighlightIndicators(true);
        lineDataSet3.setHighLightColor(Color.RED);
        lineDataSet3.setValueTextSize(12f);
        lineDataSet3.setValueTextColor(Color.BLACK);
        lineDataSet3.setMode(LineDataSet.Mode.LINEAR);


        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(lineDataSet1);
        dataSets.add(lineDataSet2);
        dataSets.add(lineDataSet3);

        LineData data = new LineData(dataSets);

        lineChart.setData(data);
        lineChart.animateY(1000);
        lineChart.getDescription().setText("Line Comparison Chart");

        Legend legend = lineChart.getLegend();
        legend.setStackSpace(5);
        legend.setTextColor(Color.BLACK);

        // xAxis customization
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(false);
        //   xAxis.setDrawGridLines(true);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setDrawLabels(false);
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setTextColor(Color.BLACK);

    }

    @Override
    public void renderPieChart(Statistic statistic) {
        pieChart.setUsePercentValues(true);
        List<PieEntry> yvalues = new ArrayList<PieEntry>();
        yvalues.add(new PieEntry(statistic.getNoOfDone(), "Hoàn thành"));
        yvalues.add(new PieEntry(statistic.getNoOfCancel(), "Hủy"));
        yvalues.add(new PieEntry(statistic.getNoOfUsing(), "Đang sử dụng"));
        yvalues.add(new PieEntry(statistic.getNoOfWaiting(), "Còn chờ"));
        PieDataSet dataSet = new PieDataSet(yvalues, null);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.DKGRAY);
        pieChart.setData(data);
        pieChart.notifyDataSetChanged();
        pieChart.invalidate();
        pieChart.getDescription().setText("Tỉ lệ % số lượt đăng ký đã hoàn thành, bị hủy, đang sử dụng và còn chờ");
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(58f);
        pieChart.setDrawEntryLabels(false);
        pieChart.setHoleRadius(58f);
    }
}
