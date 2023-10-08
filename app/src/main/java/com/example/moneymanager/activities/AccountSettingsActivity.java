package com.example.moneymanager.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.moneymanager.R;
import com.example.moneymanager.constant.SharedPrefConstant;
import com.example.moneymanager.dialog.AccountSettingsChangePasswordDialog;
import com.example.moneymanager.dialog.AccountSettingsChangeWalletNameDialog;
//import com.huawei.hmf.tasks.OnCompleteListener;
//import com.huawei.hmf.tasks.OnFailureListener;
//import com.huawei.hmf.tasks.OnSuccessListener;
//import com.huawei.hmf.tasks.Task;
//import com.huawei.hms.common.ApiException;
//import com.huawei.hms.support.account.AccountAuthManager;
//import com.huawei.hms.support.account.request.AccountAuthParams;
//import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
//import com.huawei.hms.support.account.service.AccountAuthService;

public class AccountSettingsActivity extends AppCompatActivity {
    ImageButton ib_back;
    TextView tv_change_password, tv_signout, tv_change_wallet_name;
//    AccountAuthService service;
//    AccountAuthParams authParams;

    private void getViews(){
        ib_back = findViewById(R.id.account_settings_ib_back);
        tv_change_password = findViewById(R.id.account_settings_tv_change_password);
        tv_signout = findViewById(R.id.account_settings_tv_signout);
        tv_change_wallet_name = findViewById(R.id.account_settings_tv_change_wallet_name);

    }

    private void clearSigningInInformation(SharedPreferences.Editor signingInEditor){
        //Xoá thông tin
        signingInEditor.clear();

        //Lưu file
        signingInEditor.apply();

        //Về màn hình ban đầu
        Intent firstLoadingIntent = new Intent(AccountSettingsActivity.this, FirstLoadingActivity.class);
        startActivity(firstLoadingIntent);
        overridePendingTransition(0,0);
        finish();
    }

    private void setEventListener() {
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0,0);
            }
        });

        tv_change_wallet_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountSettingsChangeWalletNameDialog changeWalletNameDialog = new AccountSettingsChangeWalletNameDialog();
                changeWalletNameDialog.show(getSupportFragmentManager(), "");
            }
        });

        tv_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountSettingsChangePasswordDialog changePasswordDialog = new AccountSettingsChangePasswordDialog();
                changePasswordDialog.show(getSupportFragmentManager(),"");
            }
        });

        tv_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lấy file người dùng đang đăng nhập
                SharedPreferences sharedPreferencesSigningIn = getSharedPreferences(SharedPrefConstant.SIGNING_IN, MODE_PRIVATE);
                SharedPreferences.Editor signingInEditor = sharedPreferencesSigningIn.edit();
                //Check xem người dùng có phải huawei không
//                Boolean isHuawei = sharedPreferencesSigningIn.getBoolean(SharedPrefConstant.SIGNING_IN_IS_HUAWEI, false);

//                if(isHuawei){
//                    //Đăng xuất huawei
//                    Task<Void> signOutTask = service.signOut();
//                    signOutTask.addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//
//                            //Nếu đăng xuất thành công thì revoke authorization
////                            Log.i("signoutok", "signOut Success");
////                            service.cancelAuthorization().addOnCompleteListener(new OnCompleteListener<Void>() {
////                                @Override
////                                public void onComplete(Task<Void> task) {
////                                    if (task.isSuccessful()) {
////                                        Log.i("revokeok", "onSuccess: ");
////                                    } else {
////                                        Exception exception = task.getException();
////                                        if (exception instanceof ApiException){
////                                            int statusCode = ((ApiException) exception).getStatusCode();
////                                            Log.i("revokefail", "onFailure: " + statusCode);
////                                        }
////                                    }
////                                }
////                            });
//                            Toast.makeText(AccountSettingsActivity.this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
//                            cancelAuthorization();
//                            clearSigningInInformation(signingInEditor);
//                        }
//                    })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(Exception e) {
//                                    Log.i("signoutfail", "signOut fail");
//                                    Toast.makeText(AccountSettingsActivity.this, "Đăng xuất thất bại!", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                }
                //Nếu không phải huawei
//                else{
                    Toast.makeText(AccountSettingsActivity.this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();

                    clearSigningInInformation(signingInEditor);
//                }
            }
        });
    }
//    private void cancelAuthorization() {
//        authParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
//                .setProfile()
//                .setAuthorizationCode()
//                .createParams();
//        service= AccountAuthManager.getService(AccountSettingsActivity.this, authParams);
//        Task<Void> task = service.cancelAuthorization();
//        task.addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Log.i("revokeok", "cancelAuthorization success");
//            }
//        });
//        task.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(Exception e) {
//                Log.i("revokefail", "cancelAuthorization failure：" + e.getClass().getSimpleName());
//            }
//        });
//    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        getViews();
        setEventListener();
//        service = AccountAuthManager.getService(AccountSettingsActivity.this, authParams);
    }
}
