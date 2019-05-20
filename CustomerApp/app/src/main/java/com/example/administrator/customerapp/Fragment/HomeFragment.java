package com.example.administrator.customerapp.Fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.customerapp.Activity.MainActivity;
import com.example.administrator.customerapp.Adapter.BranchAdapter;
import com.example.administrator.customerapp.Contract.HomeContract;
import com.example.administrator.customerapp.Model.Branch;
import com.example.administrator.customerapp.Model.SupportedModel.Address;
import com.example.administrator.customerapp.Model.SupportedModel.FilterAddress;
import com.example.administrator.customerapp.Presenter.HomePresenter;
import com.example.administrator.customerapp.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements HomeContract.View {
    private RecyclerView branchRecyclerView;
    private BranchAdapter branchAdapter;
    private ArrayList<Branch> branchArrayList;
    private FloatingActionButton filterFab;
    private TextView pathTxt;
    private TextView cancelFilterTxt;
    private HomeContract.Presenter homePresenter;
    private AlertDialog waitingDialog;
    private AlertDialog.Builder waitingDialogBuilder;
    private AlertDialog.Builder noticeDialog;
    private AlertDialog filterDialog;
    private AlertDialog.Builder filterDialogBuilder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar2);
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Trang chủ");
        branchRecyclerView = (RecyclerView) view.findViewById(R.id.branchRecyclerView);
        filterFab = (FloatingActionButton) view.findViewById(R.id.filterFab);
        pathTxt = (TextView) view.findViewById(R.id.pathTxt);
        cancelFilterTxt = (TextView) view.findViewById(R.id.cancelFilterTxt);
        assignDialog();
        homePresenter = new HomePresenter(this);
        filterFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homePresenter.getFilterList();
            }
        });
        cancelFilterTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pathTxt.setVisibility(View.GONE);
                cancelFilterTxt.setVisibility(View.GONE);
                homePresenter.getBranchFromServer();
            }
        });
        homePresenter.getBranchFromServer();
        return view;
    }

    private void assignDialog() {
        noticeDialog = new AlertDialog.Builder(getActivity());
        waitingDialogBuilder = new AlertDialog.Builder(getActivity());
        filterDialogBuilder = new AlertDialog.Builder(getActivity());
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
    public void setUpAdapter(ArrayList<Branch> branch) {
        if (branch != null) {
            ArrayList<Branch> temp = new ArrayList<>();
            for (int i = 0; i < branch.size(); i++) {
                if (!branch.get(i).getStatus().equals("-1")) temp.add(branch.get(i));
            }
            branchAdapter = new BranchAdapter(temp, getActivity());
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        branchRecyclerView.setLayoutManager(layoutManager);
        branchRecyclerView.setAdapter(branchAdapter);
    }


    @Override
    public void setUpFilter(final ArrayList<Address> address) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.filter_dialog, null);
        filterDialogBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                filterDialog.dismiss();
            }
        });
        filterDialogBuilder.setView(v);
        final Spinner citySpinner = (Spinner) v.findViewById(R.id.citySpinner);
        final Spinner districtSpinner = (Spinner) v.findViewById(R.id.districtSpinner);
        final Button filterBtn = (Button) v.findViewById(R.id.filterBtn);

        ArrayList<String> cityList = new ArrayList<>();
        for (int i = 0; i < address.size(); i++) cityList.add(address.get(i).getCity());
        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, cityList);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        citySpinner.setAdapter(adapter);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Address temp = new Address();
                ArrayList<String> districtList = temp.getDistrictFromCityFilter(address, citySpinner.getSelectedItem().toString());
                if (districtList != null) {
                    ArrayAdapter<String> districtAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, districtList);
                    districtAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                    districtSpinner.setAdapter(districtAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pathTxt.setVisibility(View.VISIBLE);
                cancelFilterTxt.setVisibility(View.VISIBLE);
                if (districtSpinner.getSelectedItemPosition() == 0) {
                    homePresenter.filterBranch(citySpinner.getSelectedItem().toString(), "");
                    pathTxt.setText("Các cơ sở thuộc: " + citySpinner.getSelectedItem().toString());
                } else {
                    homePresenter.filterBranch(citySpinner.getSelectedItem().toString(), districtSpinner.getSelectedItem().toString());
                    pathTxt.setText("Các cơ sở thuộc: " + citySpinner.getSelectedItem().toString() + ", " + districtSpinner.getSelectedItem().toString());
                }
                if (filterDialog.isShowing()) filterDialog.dismiss();
            }
        });
        filterDialog = filterDialogBuilder.show();
    }

}
