package com.example.administrator.employeeapp.Fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.administrator.employeeapp.Adapter.HistoryAdapter;
import com.example.administrator.employeeapp.CallAPI.APIClient;
import com.example.administrator.employeeapp.CallAPI.RetrofitInterface;
import com.example.administrator.employeeapp.Contract.CreateBranchContract;
import com.example.administrator.employeeapp.Contract.HistoryContract;
import com.example.administrator.employeeapp.Model.Account;
import com.example.administrator.employeeapp.Model.History;
import com.example.administrator.employeeapp.Model.Review;
import com.example.administrator.employeeapp.Presenter.CreateBranchPresenter;
import com.example.administrator.employeeapp.Presenter.HistoryPresenter;
import com.example.administrator.employeeapp.R;
import com.example.administrator.employeeapp.Utils.DatabaseHelper;
import com.example.administrator.employeeapp.Utils.GetAddressHelper;
import com.example.administrator.employeeapp.Utils.MyDatabaseHelper;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

public class CreateBranchFragment extends Fragment implements CreateBranchContract.View {

    private CreateBranchContract.Presenter createBranchPresenter;
    private AlertDialog waitingDialog;
    private AlertDialog.Builder waitingDialogBuilder;
    private AlertDialog.Builder noticeDialog;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Account account;
    private TextView nameTxt;
    private TextView phoneTxt;
    private TextView capacityTxt;
    private TextView restOfAddressTxt;
    private TextView noteTxt;
    private TimePicker openHourTimePicker;
    private TimePicker closeHourTimePicker;
    private Spinner startWorkingDateSpinner;
    private Spinner endWorkingDateSpinner;
    private Spinner citySpinner;
    private Spinner districtSpinner;
    private Spinner wardSpinner;
    private Button createBranchBtn;
    private MyDatabaseHelper database;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_create_branch, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar2);
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Tạo cơ sở");
        nameTxt = (TextView) view.findViewById(R.id.branchNameTxt);
        phoneTxt = (TextView) view.findViewById(R.id.branchPhoneTxt);
        capacityTxt = (TextView) view.findViewById(R.id.branchCapacityTxt);
        restOfAddressTxt = (TextView) view.findViewById(R.id.branchAddressTxt);
        startWorkingDateSpinner = (Spinner) view.findViewById(R.id.workingDateStartSpinner);
        endWorkingDateSpinner = (Spinner) view.findViewById(R.id.workingDateEndSpinner);
        citySpinner = (Spinner) view.findViewById(R.id.citySpinner);
        districtSpinner = (Spinner) view.findViewById(R.id.districtSpinner);
        wardSpinner = (Spinner) view.findViewById(R.id.wardSpinner);
        openHourTimePicker = (TimePicker) view.findViewById(R.id.branchOpenTimePicker);
        closeHourTimePicker = (TimePicker) view.findViewById(R.id.branchCloseTimePicker);
        noteTxt = (TextView) view.findViewById(R.id.noteTxt);
        createBranchBtn = (Button) view.findViewById(R.id.createBranchBtn);
        createBranchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateForm()) return;
                showProgressBar();
                String name = nameTxt.getText().toString();
                String phone = phoneTxt.getText().toString();
                String capacity = capacityTxt.getText().toString();
                String startWorkingDate = startWorkingDateSpinner.getSelectedItem().toString();
                String endWorkingDate = endWorkingDateSpinner.getSelectedItem().toString();
                String city = citySpinner.getSelectedItem().toString();
                String district = districtSpinner.getSelectedItem().toString();
                String ward = wardSpinner.getSelectedItem().toString();
                String restOfAddress = restOfAddressTxt.getText().toString();
                String openHour;
                String closeHour;
                if (android.os.Build.VERSION.SDK_INT >= 23) {
                    openHour = Integer.toString(openHourTimePicker.getHour()) + ":" + Integer.toString(openHourTimePicker.getMinute());
                    closeHour = Integer.toString(closeHourTimePicker.getHour()) + ":" + Integer.toString(closeHourTimePicker.getMinute());
                } else {
                    openHour = Integer.toString(openHourTimePicker.getCurrentHour()) + ":" + Integer.toString(openHourTimePicker.getCurrentMinute());
                    closeHour = Integer.toString(closeHourTimePicker.getCurrentHour()) + ":" + Integer.toString(closeHourTimePicker.getCurrentMinute());
                }
                String note = noteTxt.getText().toString().trim();
                createBranchPresenter.createBranch(account.getToken(), name, city, district, ward, restOfAddress, phone, Integer.parseInt(capacity), openHour, closeHour, startWorkingDate + "-" + endWorkingDate, note);
            }
        });
        database = new MyDatabaseHelper(getActivity());
        setUpSpinner();
        assignDialog();
        sharedPreferences = this.getActivity().getSharedPreferences("data", MODE_PRIVATE);
        String accountString = sharedPreferences.getString("MyAccount", "empty");
        Gson gson = new Gson();
        account = new Account();
        if (!accountString.equals("empty")) {
            account = gson.fromJson(accountString, Account.class);
        }
        createBranchPresenter = new CreateBranchPresenter(this, account);
        return view;
    }

    private void assignDialog() {
        noticeDialog = new AlertDialog.Builder(getActivity());
        waitingDialogBuilder = new AlertDialog.Builder(getActivity());
    }

    public void setUpSpinner() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.dayfOfWeek, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                startWorkingDateSpinner.setAdapter(adapter);
                endWorkingDateSpinner.setAdapter(adapter);
                GetAddressHelper getAddressHelper = new GetAddressHelper();
                getAddressHelper = database.getCity();
                ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(
                        getActivity(),
                        android.R.layout.simple_spinner_item,
                        getAddressHelper.getCityName()
                );
                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ArrayList<String> cityID = new ArrayList<>();
                cityID = getAddressHelper.getCityID();
                citySpinner.setAdapter(cityAdapter);
                final ArrayList<String> finalCityID = cityID;
                citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        GetAddressHelper getAddressHelper2 = new GetAddressHelper();
                        getAddressHelper2 = database.getDistrict(finalCityID.get(position));
                        Log.d("1abc", "finalCityID.get(position): " + finalCityID.get(position));
                        final ArrayList<String> districtName = getAddressHelper2.getDistrictName();
                        final ArrayList<String> districtID = getAddressHelper2.getDistrictID();
                        ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(
                                getActivity(),
                                android.R.layout.simple_spinner_item,
                                districtName
                        );
                        districtSpinner.setAdapter(districtAdapter);

                        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                GetAddressHelper getAddressHelper2 = new GetAddressHelper();
                                getAddressHelper2 = database.getWard(districtID.get(position));
                                ArrayList<String> wardName = new ArrayList<String>();
                                wardName = getAddressHelper2.getWardName();
                                ArrayAdapter<String> wardAdapter = new ArrayAdapter<String>(
                                        getActivity(),
                                        android.R.layout.simple_spinner_item,
                                        wardName
                                );
                                wardSpinner.setAdapter(wardAdapter);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parentView) {
                                // your code here
                            }

                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                    }
                });
            }
        }).start();

    }

    public Boolean validateForm() {
        //Bo sung phone validate, chieu dai cua password
        Boolean validFlag = true;
        String name = nameTxt.getText().toString().trim();
        String phone = phoneTxt.getText().toString().trim();
        String capacity = capacityTxt.getText().toString().trim();
        String restOfAddress = restOfAddressTxt.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            nameTxt.setError("Không được để trống Email.");
            validFlag = false;
        } else {
            nameTxt.setError(null);
        }
        if (TextUtils.isEmpty(phone) || phone.length() < 8 || phone.length() > 15) {
            phoneTxt.setError("Số điện thoại không đúng định dạng hoặc bị để trống");
            validFlag = false;
        } else {
            phoneTxt.setError(null);
        }
        if (TextUtils.isEmpty(capacity)) {
            capacityTxt.setError("Không được để trống sức chứa.");
            validFlag = false;
        } else {
            capacityTxt.setError(null);
        }
        if (TextUtils.isEmpty(restOfAddress)) {
            restOfAddressTxt.setError("Không được để trống.");
            validFlag = false;
        } else {
            restOfAddressTxt.setError(null);
        }
        return validFlag;
    }

    @Override
    public void showDialog(String message) {
        noticeDialog.setMessage(message)
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        noticeDialog.show();
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

    public void performActionAsync() {
        new Thread(new Runnable() {
            @Override
            public void run() {
            }
        }).start();
    }

}
