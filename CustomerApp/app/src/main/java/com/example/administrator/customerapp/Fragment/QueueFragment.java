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
import com.example.administrator.customerapp.Adapter.QueueAdapter;
import com.example.administrator.customerapp.CallAPI.APIClient;
import com.example.administrator.customerapp.CallAPI.RetrofitInterface;
import com.example.administrator.customerapp.Model.Branch;
import com.example.administrator.customerapp.Model.Queue;
import com.example.administrator.customerapp.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QueueFragment extends Fragment {

    private RetrofitInterface callAPIService;
    private RecyclerView queueRecyclerView;
    private QueueAdapter queueAdapter;
    private ArrayList<Queue> queueArrayList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.queue_fragment, container, false);
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        queueRecyclerView = (RecyclerView) view.findViewById(R.id.queueRecyclerView);
        String branchID = getArguments().getString("branchID");

        if(branchID!=null) {
            Log.d("1abc", branchID);
            getQueue(branchID);
        }
        return view;
    }

    public void getQueue(String branchID){
        callAPIService.getQueue(branchID).enqueue(new Callback<ArrayList<Queue>>() {
            @Override
            public void onResponse(Call<ArrayList<Queue>> call, Response<ArrayList<Queue>> response) {
                if(response.isSuccessful()) {
                    ArrayList<Queue> queue = new ArrayList<Queue>();
                    queue = response.body();
                    if(queue!=null) {
                        queueAdapter = new QueueAdapter(queue);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        queueRecyclerView.setLayoutManager(layoutManager);
                        queueRecyclerView.setAdapter(queueAdapter);
                    }
                }else {
                    int statusCode  = response.code();
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Queue>> call, Throwable t) {
                Log.d("1abc", "error loading from API" + t.toString());
            }
        });
    }

}
