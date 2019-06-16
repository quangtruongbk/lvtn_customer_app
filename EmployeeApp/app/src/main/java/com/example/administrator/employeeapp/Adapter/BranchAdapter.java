package com.example.administrator.employeeapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.administrator.employeeapp.Contract.HomeContract;
import com.example.administrator.employeeapp.Fragment.QueueFragment;
import com.example.administrator.employeeapp.Model.Account;
import com.example.administrator.employeeapp.Model.Branch;
import com.example.administrator.employeeapp.Model.Employee;
import com.example.administrator.employeeapp.Model.Queue;
import com.example.administrator.employeeapp.R;
import com.example.administrator.employeeapp.Utils.GetAddressHelper;
import com.example.administrator.employeeapp.Utils.MyDatabaseHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.RecyclerViewHolder> {
    private ArrayList<Branch> branchList = new ArrayList<Branch>();
    private Context context;
    private AlertDialog.Builder changeInfoBranchDialogBuilder;
    private AlertDialog changeInfoBranchDialog;
    private FragmentManager fragmentManager;
    private MyDatabaseHelper database;
    private HomeContract.Presenter homePresenter;
    private Account account;
    private Employee employee;

    public BranchAdapter(ArrayList<Branch> data, Context context, HomeContract.Presenter presenter, Account account, Employee employee) {
        this.branchList = data;
        this.context = context;
        this.database = new MyDatabaseHelper(context);
        changeInfoBranchDialogBuilder = new AlertDialog.Builder(context);
        homePresenter = presenter;
        this.account = account;
        this.employee = employee;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.branch_row, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        holder.nameTxt.setText(branchList.get(position).getName());
        holder.addressTxt.setText("Địa chỉ: " + branchList.get(position).getAddress().getRest() + ", " + branchList.get(position).getAddress().getWard() + ", " + branchList.get(position).getAddress().getDistrict() + ", " + branchList.get(position).getAddress().getCity());
        holder.phoneTxt.setText("Số điện thoại: " + branchList.get(position).getPhone().toString());
        if (branchList.get(position).getStatus().toString() != null) {
            if (branchList.get(position).getStatus().toString().equals("0"))
                holder.statusTxt.setText("Tình trạng: Đóng cửa");
            if (branchList.get(position).getStatus().toString().equals("1"))
                holder.statusTxt.setText("Tình trạng: Đang nhận khách");
            if (branchList.get(position).getStatus().toString().equals("-1"))
                holder.statusTxt.setText("Tình trạng: Đã khóa");
        }
        String openHourHour = branchList.get(position).getOpentime().split(":")[0];
        String openHourMinute = branchList.get(position).getOpentime().split(":")[1];
        String closeHourHour = branchList.get(position).getClosetime().split(":")[0];
        String closeHourMinute = branchList.get(position).getClosetime().split(":")[1];

        if (Integer.parseInt(openHourHour) < 10) openHourHour = "0" + openHourHour;
        if (Integer.parseInt(openHourMinute) < 10) openHourMinute = "0" + openHourMinute;
        if (Integer.parseInt(closeHourHour) < 10) closeHourHour = "0" + closeHourHour;
        if (Integer.parseInt(closeHourMinute) < 10) closeHourMinute = "0" + closeHourMinute;

        holder.openHourTxt.setText("Giờ hoạt động: " + openHourHour + ":" + openHourMinute + "-" + closeHourHour + ":" + closeHourMinute);
        holder.workingDateTxt.setText("Ngày hoạt động: " + branchList.get(position).getWorkingDate());
        holder.noteTxt.setText("Ghi chú: " + branchList.get(position).getNote());
        if (employee.getRole().checkHideBranch(branchList.get(position).getId())) {
            holder.wholeBranchRow.setVisibility(View.GONE);
        }
        holder.branchRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("branchID", branchList.get(position).getId());
                args.putString("branchName", branchList.get(position).getName());
                QueueFragment queueFragment = new QueueFragment();
                queueFragment.setArguments(args);
                ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.frameFragment, queueFragment).addToBackStack(null).commit();
            }
        });
        if (!employee.getRole().checkEditBranch(branchList.get(position).getId()) && !employee.getRole().checkControlBranch(branchList.get(position).getId())) {
            holder.moreBtn.setVisibility(View.GONE);
        }
        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, holder.moreBtn);
                popup.inflate(R.menu.branch_menu);
                Menu popupMenu = popup.getMenu();
                if (!employee.getRole().checkEditBranch(branchList.get(position).getId())) {
                    popupMenu.findItem(R.id.editBtn).setVisible(false);
                }
                if (!employee.getRole().checkControlBranch(branchList.get(position).getId())) {
                    popupMenu.findItem(R.id.closeBranchBtn).setVisible(false);
                    popupMenu.findItem(R.id.lockBranchBtn).setVisible(false);
                }
                if (branchList.get(position).getStatus().equals("-1")){
                    popupMenu.findItem(R.id.lockBranchBtn).setVisible(false);
                }
                if (branchList.get(position).getStatus().equals("0")) {
                    popupMenu.findItem(R.id.closeBranchBtn).setTitle("Mở cửa");
                } else if (branchList.get(position).getStatus().equals("1")) {
                    popupMenu.findItem(R.id.closeBranchBtn).setTitle("Đóng cửa");
                } else if (branchList.get(position).getStatus().equals("-1")) {
                    popupMenu.findItem(R.id.closeBranchBtn).setTitle("Gỡ khóa và mở cửa");
                }
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.editBtn:
                                showChangeInfoBranchDialog(branchList.get(position));
                                return true;
                            case R.id.closeBranchBtn:
                                if (branchList.get(position).getStatus().equals("1"))
                                    homePresenter.closeOpenBranch(account.getToken(), branchList.get(position).getId(), "0");
                                else if (branchList.get(position).getStatus().equals("0") || branchList.get(position).getStatus().equals("-1"))
                                    homePresenter.closeOpenBranch(account.getToken(), branchList.get(position).getId(), "1");
                                return true;
                            case R.id.lockBranchBtn:
                                homePresenter.closeOpenBranch(account.getToken(), branchList.get(position).getId(), "-1");
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return branchList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt;
        TextView addressTxt;
        TextView statusTxt;
        TextView phoneTxt;
        TextView openHourTxt;
        TextView workingDateTxt;
        TextView noteTxt;
        LinearLayout branchRow;
        LinearLayout wholeBranchRow;
        ImageView moreBtn;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            nameTxt = (TextView) itemView.findViewById(R.id.nameTxt);
            addressTxt = (TextView) itemView.findViewById(R.id.addressTxt);
            phoneTxt = (TextView) itemView.findViewById(R.id.phoneTxt);
            statusTxt = (TextView) itemView.findViewById(R.id.statusTxt);
            branchRow = (LinearLayout) itemView.findViewById(R.id.branchRow);
            wholeBranchRow = (LinearLayout) itemView.findViewById(R.id.wholeBranchRow);
            openHourTxt = (TextView) itemView.findViewById(R.id.openHoursTxt);
            workingDateTxt = (TextView) itemView.findViewById(R.id.openDayTxt);
            noteTxt = (TextView) itemView.findViewById(R.id.noteTxt);
            moreBtn = (ImageView) itemView.findViewById(R.id.moreBtn);
        }
    }

    public void showChangeInfoBranchDialog(final Branch branch) {
        final TextView nameTxt;
        final TextView phoneTxt;
        final TextView capacityTxt;
        final TextView restOfAddressTxt;
        final TextView noteTxt;
        final TimePicker openHourTimePicker;
        final TimePicker closeHourTimePicker;
        final Spinner startWorkingDateSpinner;
        final Spinner endWorkingDateSpinner;
        final Spinner citySpinner;
        final Spinner districtSpinner;
        final Spinner wardSpinner;
        Button changeInfoBranchBtn;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.change_info_branch_dialog, null);
        changeInfoBranchDialogBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                changeInfoBranchDialog.dismiss();
            }
        });
        changeInfoBranchDialogBuilder.setView(view);
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.dayfOfWeek, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                startWorkingDateSpinner.setAdapter(adapter);
                endWorkingDateSpinner.setAdapter(adapter);
                if (branch.getWorkingDate() != null && !branch.getWorkingDate().equals("")) {
                    String startWorkingDate = branch.getWorkingDate().split("-")[0];
                    String endWorkingDate = branch.getWorkingDate().split("-")[1];
                    if (startWorkingDate != null) {
                        int spinnerPosition = adapter.getPosition(startWorkingDate);
                        startWorkingDateSpinner.setSelection(spinnerPosition);
                    }
                    if (endWorkingDate != null) {
                        int spinnerPosition = adapter.getPosition(endWorkingDate);
                        endWorkingDateSpinner.setSelection(spinnerPosition);
                    }
                }
                GetAddressHelper getAddressHelper = new GetAddressHelper();
                getAddressHelper = database.getCity();
                final ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(
                        context,
                        android.R.layout.simple_spinner_item,
                        getAddressHelper.getCityName()
                );
                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ArrayList<String> cityID = new ArrayList<>();
                cityID = getAddressHelper.getCityID();
                citySpinner.setAdapter(cityAdapter);

                //Start set current Spinner
                if (branch.getAddress().getCity() != null) {
                    int spinnerPosition = cityAdapter.getPosition(branch.getAddress().getCity());
                    citySpinner.setSelection(spinnerPosition, false);
                }
                String currentCityID = "";
                if (branch.getAddress().getCity() != null) {
                    currentCityID = database.getCityID(branch.getAddress().getCity());
                }

                GetAddressHelper getAddressHelper2 = new GetAddressHelper();
                getAddressHelper2 = database.getDistrict(currentCityID);
                final ArrayList<String> districtName = getAddressHelper2.getDistrictName();
                final ArrayList<String> districtID = getAddressHelper2.getDistrictID();
                ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(
                        context,
                        android.R.layout.simple_spinner_item,
                        districtName
                );
                districtSpinner.setAdapter(districtAdapter);
                if (branch.getAddress().getDistrict() != null) {
                    int spinnerPosition = districtAdapter.getPosition(branch.getAddress().getDistrict());
                    districtSpinner.setSelection(spinnerPosition, false);
                }
                String currentDistrictID = "";
                if (branch.getAddress().getDistrict() != null) {
                    currentDistrictID = database.getDistrictID(branch.getAddress().getDistrict());
                }
                getAddressHelper2 = database.getWard(currentDistrictID);
                ArrayList<String> wardName = new ArrayList<String>();
                wardName = getAddressHelper2.getWardName();
                ArrayAdapter<String> wardAdapter = new ArrayAdapter<String>(
                        context,
                        android.R.layout.simple_spinner_item,
                        wardName
                );
                wardSpinner.setAdapter(wardAdapter);

                if (branch.getAddress().getWard() != null) {
                    int spinnerPosition = wardAdapter.getPosition(branch.getAddress().getWard());
                    wardSpinner.setSelection(spinnerPosition);
                }
                final ArrayList<String> finalCityID = cityID;
                citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        GetAddressHelper getAddressHelper2 = new GetAddressHelper();
                        getAddressHelper2 = database.getDistrict(finalCityID.get(position));
                        final ArrayList<String> districtName = getAddressHelper2.getDistrictName();
                        final ArrayList<String> districtID = getAddressHelper2.getDistrictID();
                        ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(
                                context,
                                android.R.layout.simple_spinner_item,
                                districtName
                        );
                        districtSpinner.setAdapter(districtAdapter);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                    }
                });

                districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        String districtName = districtSpinner.getSelectedItem().toString();
                        String districtID = database.getDistrictID(districtName);
                        GetAddressHelper getAddressHelper2 = new GetAddressHelper();
                        getAddressHelper2 = database.getWard(districtID);
                        ArrayList<String> wardName = new ArrayList<String>();
                        wardName = getAddressHelper2.getWardName();
                        ArrayAdapter<String> wardAdapter = new ArrayAdapter<String>(
                                context,
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
        }).start();
        noteTxt = (TextView) view.findViewById(R.id.noteTxt);
        changeInfoBranchBtn = (Button) view.findViewById(R.id.changeInfoBranchBtn);
        nameTxt.setText(branch.getName());
        restOfAddressTxt.setText(branch.getAddress().getRest());
        phoneTxt.setText(branch.getPhone());
        noteTxt.setText(branch.getNote());
        capacityTxt.setText(branch.getCapacity().toString());
        if (branch.getOpentime() != null && branch.getClosetime() != null) {
            String openHourHour = branch.getOpentime().split(":")[0];
            String openHourMinute = branch.getOpentime().split(":")[1];
            String closeHourHour = branch.getClosetime().split(":")[0];
            String closeHourMinute = branch.getClosetime().split(":")[1];
            if (android.os.Build.VERSION.SDK_INT >= 23) {
                openHourTimePicker.setHour(Integer.parseInt(openHourHour));
                openHourTimePicker.setMinute(Integer.parseInt(openHourMinute));
                closeHourTimePicker.setHour(Integer.parseInt(closeHourHour));
                closeHourTimePicker.setMinute(Integer.parseInt(closeHourMinute));
            } else {
                openHourTimePicker.setCurrentHour(Integer.parseInt(openHourHour));
                openHourTimePicker.setCurrentMinute(Integer.parseInt(openHourMinute));
                closeHourTimePicker.setCurrentHour(Integer.parseInt(closeHourHour));
                closeHourTimePicker.setCurrentMinute(Integer.parseInt(closeHourMinute));
            }
        }
        changeInfoBranchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                if (validFlag == true) {
                    String startWorkingDate = startWorkingDateSpinner.getSelectedItem().toString();
                    String endWorkingDate = endWorkingDateSpinner.getSelectedItem().toString();
                    String city = citySpinner.getSelectedItem().toString();
                    String district = districtSpinner.getSelectedItem().toString();
                    String ward = wardSpinner.getSelectedItem().toString();
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
                    homePresenter.changeInfoBranch(account.getToken(), branch.getId(), name, city, district, ward, restOfAddress, phone, Integer.parseInt(capacity), openHour, closeHour, startWorkingDate + "-" + endWorkingDate, note);
                }
            }
        });
        changeInfoBranchDialog = changeInfoBranchDialogBuilder.show();
    }

}
