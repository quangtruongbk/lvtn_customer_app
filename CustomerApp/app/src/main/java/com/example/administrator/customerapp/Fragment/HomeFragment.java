package com.example.administrator.customerapp.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.customerapp.Adapter.BranchAdapter;
import com.example.administrator.customerapp.CallAPI.APIClient;
import com.example.administrator.customerapp.CallAPI.RetrofitInterface;
import com.example.administrator.customerapp.Model.Branch;
import com.example.administrator.customerapp.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RetrofitInterface callAPIService;
    private RecyclerView branchRecyclerView;
    private BranchAdapter branchAdapter;
    private ArrayList<Branch> branchArrayList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        branchRecyclerView = (RecyclerView) view.findViewById(R.id.branchRecyclerView);
        getBranch();
        return view;
    }

    public void getBranch(){
        callAPIService.getBranch().enqueue(new Callback<ArrayList<Branch>>() {
            @Override
            public void onResponse(Call<ArrayList<Branch>> call, Response<ArrayList<Branch>> response) {
                if(response.isSuccessful()) {
                    ArrayList<Branch> branch = new ArrayList<Branch>();
                    branch = response.body();
                    if(branch!=null) {
                        branchAdapter = new BranchAdapter(branch, getActivity());
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        branchRecyclerView.setLayoutManager(layoutManager);
                        branchRecyclerView.setAdapter(branchAdapter);
                    }
                }else {
                    int statusCode  = response.code();
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Branch>> call, Throwable t) {
                Log.d("1abc", "error loading from API" + t.toString());
            }
        });
    }

}
