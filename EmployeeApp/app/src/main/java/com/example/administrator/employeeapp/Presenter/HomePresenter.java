package com.example.administrator.employeeapp.Presenter;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.administrator.employeeapp.CallAPI.APIClient;
import com.example.administrator.employeeapp.CallAPI.RetrofitInterface;
import com.example.administrator.employeeapp.Contract.HomeContract;
import com.example.administrator.employeeapp.Model.Account;
import com.example.administrator.employeeapp.Model.Branch;
import com.example.administrator.employeeapp.Model.Employee;
import com.example.administrator.employeeapp.Model.SupportedModel.Address;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class HomePresenter implements HomeContract.Presenter {
    private RetrofitInterface callAPIService;
    private HomeContract.View mView;
    private Account account;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public HomePresenter(@NonNull HomeContract.View mView, Account account) {
        this.mView = mView;
        this.account = account;
    }

    /***************************************************
     Function: getBranchFromServer
     Creator: Quang Truong
     Description: Get Branch List from server
     *************************************************/
    @Override
    public void getBranchFromServer() {
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.getBranch().enqueue(new Callback<ArrayList<Branch>>() {
            @Override
            public void onResponse(Call<ArrayList<Branch>> call, Response<ArrayList<Branch>> response) {
                mView.hideProgressBar();
                if (response.code() == 200) {
                    ArrayList<Branch> newBranch = new ArrayList<Branch>();
                    newBranch = response.body();
                    if (newBranch != null) {
                        mView.setUpAdapter(newBranch);
                    }
                } else if (response.code() == 500) {
                    mView.showDialog("Không thể lấy được danh sách cơ sở do lỗi hệ thống. Xin vui lòng thử lại!", false);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Branch>> call, Throwable t) {
                t.printStackTrace();
                mView.hideProgressBar();
                mView.showDialog("Không thể kết nối được với máy chủ!", false);
            }
        });
    }

    /***************************************************
     Function: changeInfoBranch
     Creator: Quang Truong
     Description: Change Information of a branch
     *************************************************/
    @Override
    public void changeInfoBranch(String token, String branchID, String name, String city, String district, String ward, String restAddress, String phone,
                                 Integer capacity, String openHour, String closeHour, String workingDay, String note) {
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.changeInfoBranch(account.getToken(), branchID, name, city, district, ward, restAddress, phone, capacity, openHour, closeHour, workingDay, note).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mView.hideProgressBar();
                if (response.code() == 200) {
                    mView.showDialog("Thay đổi thông tin cơ sở thành công!", true);
                    getBranchFromServer();
                } else if (response.code() == 500) {
                    mView.showDialog("Thay đổi thông tin cơ sở thất bại do lỗi hệ thống", false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
                mView.hideProgressBar();
                mView.showDialog("Kết nối với máy chủ thất bại", false);
            }
        });
    }

    /***************************************************
     Function: closeOpenBranch
     Creator: Quang Truong
     Description: Close or Open a branch
     *************************************************/
    @Override
    public void closeOpenBranch(String token, String branchID, String newStatus) {
        mView.showProgressBar();
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        if (newStatus.equals("0")) {
            callAPIService.changeStatusBranch(account.getToken(), branchID, "0").enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    mView.hideProgressBar();
                    if (response.code() == 200) {
                        mView.showDialog("Đóng cửa cơ sở thành công!", true);
                        getBranchFromServer();
                    } else if (response.code() == 409) {
                        mView.showDialog("Đóng cửa cơ sở thất bại do còn một vài hàng đợi còn nhận khách hoặc còn một vài lượt đăng ký còn chờ hoặc đang sử dụng dịch vụ.", false);
                    } else if (response.code() == 404) {
                        mView.showDialog("Không thể thực hiện tác vụ này, có vẻ như đã có gì thay đổi với cơ sở này. Xin vui lòng kiểm tra lại.", false);
                    } else if (response.code() == 500) {
                        mView.showDialog("Thay đổi thông tin cơ sở thất bại do lỗi hệ thống", false);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                    mView.hideProgressBar();
                    mView.showDialog("Kết nối với máy chủ thất bại", false);
                }
            });
        } else if (newStatus.equals("1")) {
            callAPIService.changeStatusBranch(account.getToken(), branchID, "1").enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    mView.hideProgressBar();
                    if (response.code() == 200) {
                        mView.showDialog("Mở cửa cơ sở thành công!", true);
                        getBranchFromServer();
                    } else if (response.code() == 404) {
                        mView.showDialog("Không thể thực hiện tác vụ này, có vẻ như đã có gì thay đổi với cơ sở này. Xin vui lòng kiểm tra lại.", false);
                    } else if (response.code() == 500) {
                        mView.showDialog("Thay đổi thông tin cơ sở thất bại do lỗi hệ thống", false);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                    mView.hideProgressBar();
                    mView.showDialog("Kết nối với máy chủ thất bại", false);
                }
            });
        } else if (newStatus.equals("-1")) {
            callAPIService.changeStatusBranch(account.getToken(), branchID, "-1").enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    mView.hideProgressBar();
                    if (response.code() == 200) {
                        mView.showDialog("Khóa cơ sở thành công!", true);
                        getBranchFromServer();
                    } else if (response.code() == 409) {
                        mView.showDialog("Khóa cửa cơ sở thất bại do còn một vài hàng đợi còn nhận khách hoặc còn một vài lượt đăng ký còn chờ hoặc đang sử dụng dịch vụ.", false);
                    } else if (response.code() == 404) {
                        mView.showDialog("Không thể thực hiện tác vụ này, có vẻ như đã có gì thay đổi với cơ sở này. Xin vui lòng kiểm tra lại.", false);
                    } else if (response.code() == 500) {
                        mView.showDialog("Thay đổi thông tin cơ sở thất bại do lỗi hệ thống", false);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                    mView.hideProgressBar();
                    mView.showDialog("Kết nối với máy chủ thất bại", false);
                }
            });
        }
    }

    /***************************************************
     Function: getAccount
     Creator: Quang Truong
     Description: Update newest Account Information
     *************************************************/
    @Override
    public void getAccount(String token, String accountID) {
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.getAccount(token, accountID).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        account = response.body();
                        getEmployeeInfo(account.getToken(), account.getId(), account);
                    }
                } else if (response.code() == 500) {
                    mView.hideProgressBar();
                    mView.showDialog("Lấy dữ liệu tài khoản thất bại do lỗi hệ thống", false);
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                t.printStackTrace();
                mView.hideProgressBar();
                mView.showDialog("Kết nối với máy chủ thất bại", false);
            }
        });
    }

    /***************************************************
     Function: getEmployeeInfo
     Creator: Quang Truong
     Description: Get Information of an employee
     *************************************************/
    @Override
    public void getEmployeeInfo(String token, String accountID, final Account account) {
        callAPIService = APIClient.getClient().create(RetrofitInterface.class);
        callAPIService.getEmployeeInfo(token, accountID).enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                if (response.code() == 200) {
                    mView.hideProgressBar();
                    if (response.body() != null) {
                        Employee employee;
                        employee = response.body();
                        if (employee != null && account != null) {
                            mView.updateEmployeeAccount(account, employee);
                        }
                        Log.d("6abc", "Account String done" + account.getName());

                    }
                } else if (response.code() == 500) {
                    mView.showDialog("Lấy dữ liệu về quyền và vai trò của tài khoản thất bại do lỗi hệ thống", false);
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                t.printStackTrace();
                mView.hideProgressBar();
                mView.showDialog("Kết nối với máy chủ thất bại", false);
            }
        });
    }
}
