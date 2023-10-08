package com.example.moneymanager.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.moneymanager.R;
import com.example.moneymanager.constant.HuaweiConstant;
import com.example.moneymanager.constant.SharedPrefConstant;
import com.example.moneymanager.methods.SharedMethods;
//import com.huawei.hmf.tasks.Task;
//import com.huawei.hms.common.ApiException;
//import com.huawei.hms.support.account.AccountAuthManager;
//import com.huawei.hms.support.account.request.AccountAuthParams;
//import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
//import com.huawei.hms.support.account.result.AuthAccount;
//import com.huawei.hms.support.account.service.AccountAuthService;

public class SigninActivity extends AppCompatActivity {
    public static final String ACTIVITY_NAME = "SigninActivity";

    ImageButton ib_back;
    TextView tv_signup, tv_error_username, tv_error_password;
    Button btn_signin;
    EditText et_username, et_password;
    LinearLayout ll_huawei;
//    AccountAuthParams authParams;
//    AccountAuthService service;

    private void getViews(){
        ib_back = findViewById(R.id.signin_ib_back);
        tv_signup = findViewById(R.id.signin_tv_signup);
        btn_signin = findViewById(R.id.signin_btn_signin);
        et_username = findViewById(R.id.signin_et_username);
        et_password = findViewById(R.id.signin_et_password);
        tv_error_username = findViewById(R.id.signin_tv_error_username);
        tv_error_password = findViewById(R.id.signin_tv_error_password);
        ll_huawei = findViewById(R.id.signin_ll_signin_huawei);

    }

    private void displayFormError(EditText et, TextView tv_error, String error, boolean[] errors){
        //Thanh border bottom EditText
        et.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red_form_error)));
        tv_error.setText(error);
        tv_error.setVisibility(View.VISIBLE);
        errors[0] = true;
    }

    private void removeFormError(EditText et, TextView tv_error){
        et.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey_main)));
        tv_error.setText("");
        tv_error.setVisibility(View.GONE);
    }

    private void setEventListener(){
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0,0);
            }
        });

        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lấy tên activity gọi trang này
                String callingActivity = getIntent().getStringExtra("source");

                //Tránh mở đi mở lại
                //Nếu trang này được mở từ Signup => chỉ cần thoát activity hiên tại để quay lại Signup
                if(callingActivity.equals(SignupActivity.ACTIVITY_NAME)){
                    finish();
                    overridePendingTransition(0,0);
                }
                //Nếu trang này được mở từ FirstLoad => Mở mới trang Signup
                else if(callingActivity.equals(FirstLoadingActivity.ACTIVITY_NAME)){
                    Intent signupIntent = new Intent(SigninActivity.this, SignupActivity.class);
                    signupIntent.putExtra("source", ACTIVITY_NAME);
                    startActivity(signupIntent);
                    overridePendingTransition(0,0);
                }
            }
        });

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                boolean[] errors = {false};


                //Check username
                if(username.isEmpty())
                    displayFormError(et_username, tv_error_username, "Tên tài khoản không được trống", errors);
                else if(username.length() < 5)
                    displayFormError(et_username, tv_error_username, "Tài khoản không được dưới 5 kí tự", errors);
                else
                    removeFormError(et_username, tv_error_username);

                //Check password
                if(password.isEmpty())
                    displayFormError(et_password, tv_error_password, "Mật khẩu không được trống", errors);
                else if(password.length() < 5)
                    displayFormError(et_password, tv_error_password, "Mật khẩu không được dưới 5 kí tự", errors);
                else
                    removeFormError(et_password, tv_error_password);


                if(!errors[0]){
                    //Lấy danh sách user
                    //User được lưu dưới dạng key-value: key dạng "username_1", "password_2", .. với 1,2 là số thứ tự user
                    SharedPreferences sharedPreferencesUsersList = getSharedPreferences(SharedPrefConstant.USERS_LIST, MODE_PRIVATE);
                    //Lấy tổng user
                    SharedPreferences sharedPreferencesUsersTotal = getSharedPreferences(SharedPrefConstant.USERS_TOTAL, MODE_PRIVATE);
                    int usersTotal = sharedPreferencesUsersTotal.getInt(SharedPrefConstant.USERS_TOTAL_VALUE, 0);
                    //Check xem có username trong danh sách không
                    boolean usernameFound = false;

                    for(int i=1; i<=usersTotal; ++i){
                        @SuppressLint("DefaultLocale") String usernameInList = sharedPreferencesUsersList.getString(String.format("%s_%d",SharedPrefConstant.USER_USERNAME,i),"");
                        if(username.equals(usernameInList)){
                            usernameFound = true;
                            //Nếu có username trong danh sách thì check password
                            @SuppressLint("DefaultLocale") String passwordInList = sharedPreferencesUsersList.getString(String.format("%s_%d",SharedPrefConstant.USER_PASSWORD,i),"");
                            if(password.equals(passwordInList)){
                                //Lấy file lưu thông tin user đang đăng nhập
                                SharedPreferences sharedPreferencesSigningIn = getSharedPreferences(SharedPrefConstant.SIGNING_IN, MODE_PRIVATE);
                                SharedPreferences.Editor signingInEditor = sharedPreferencesSigningIn.edit();

                                //Set status đăng nhập thành true
                                signingInEditor.putString(SharedPrefConstant.SIGNING_IN_STATUS, SharedPrefConstant.SIGNING_IN_STATUS_VALUE);

                                //Set username đăng nhập
                                signingInEditor.putString(SharedPrefConstant.SIGNING_IN_USERNAME, username);


                                //Set walletName đăng nhập
                                @SuppressLint("DefaultLocale") String walletName = sharedPreferencesUsersList.getString(String.format("%s_%d",SharedPrefConstant.USER_WALLET_NAME,i), "");
                                signingInEditor.putString(SharedPrefConstant.SIGNING_IN_WALLET_NAME, walletName);

                                //Set email đăng nhập
                                @SuppressLint("DefaultLocale") String email = sharedPreferencesUsersList.getString(String.format("%s_%d",SharedPrefConstant.USER_EMAIL,i), "");
                                signingInEditor.putString(SharedPrefConstant.SIGNING_IN_EMAIL, email);

                                //Set isHuawei đăng nhập
                                @SuppressLint("DefaultLocale") boolean isHuawei = sharedPreferencesUsersList.getBoolean(String.format("%s_%d", SharedPrefConstant.USER_IS_HUAWEI, i), false);
                                signingInEditor.putBoolean(SharedPrefConstant.SIGNING_IN_IS_HUAWEI, isHuawei);

                                //Lưu thay đổi file
                                signingInEditor.apply();

                                Intent setMoneyIntent = new Intent(SigninActivity.this, SetMoneyActivity.class);
                                startActivity(setMoneyIntent);
                                overridePendingTransition(0,0);
                                finish();
                                break;
                            }
                            else{
                                displayFormError(et_password, tv_error_password, "Sai mật khẩu", errors);
                                break;
                            }
                        }
                    }
                    if(!usernameFound)
                        displayFormError(et_username, tv_error_username, "Tài khoản không tồn tại", errors);
                }
            }
        });

//        ll_huawei.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                authParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM).setAuthorizationCode().createParams();
//                service = AccountAuthManager.getService(SigninActivity.this, authParams);
//                startActivityForResult(service.getSignInIntent(), HuaweiConstant.HUAWEI_AUTHORIZATION_CODE);
//            }
//        });
    }

    //Phần request code == HUAWEI_AUTHORIZATION_CODE trùng với file bên signup
    // nhớ sửa cả 2 file nếu thay đổi
    @SuppressLint("DefaultLocale")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Process the authorization result to obtain the authorization code from AuthAccount.
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == HuaweiConstant.HUAWEI_AUTHORIZATION_CODE) {
//            Task<AuthAccount> authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data);
//            if (authAccountTask.isSuccessful()) {
        // The sign-in is successful, and the user's ID information and authorization code are obtained.
//                AuthAccount authAccount = authAccountTask.getResult();
//
//                //Lấy username = unionId do nó có vẻ là duy nhất và k thay đổi
//                String username = authAccount.getUnionId();
//                //Lấy tên ví
//                String walletName = authAccount.getFamilyName() + " " + authAccount.getGivenName();
//
//                //Lấy danh sách user
//                //User được lưu dưới dạng key-value: key dạng "username_1", "password_2", .. với 1,2 là số thứ tự user
//                SharedPreferences sharedPreferencesUsersList = getSharedPreferences(SharedPrefConstant.USERS_LIST, MODE_PRIVATE);
//                SharedPreferences.Editor usersListEditor = sharedPreferencesUsersList.edit();
//
//                //Lấy tổng user
//                SharedPreferences sharedPreferencesUsersTotal = getSharedPreferences(SharedPrefConstant.USERS_TOTAL, MODE_PRIVATE);
//                SharedPreferences.Editor usersTotalEditor = sharedPreferencesUsersTotal.edit();
//                int usersTotal = sharedPreferencesUsersTotal.getInt(SharedPrefConstant.USERS_TOTAL_VALUE, 0);
//
//                //Biến kiểm tra xem có tìm được user đăng nhập với user trong danh sách không
//                boolean usernameFound = false;
//                //Check trùng username
//                for(int i=1; i<=usersTotal; ++i){
//                    if(username.equals(sharedPreferencesUsersList.getString(String.format("%s_%d", SharedPrefConstant.USER_USERNAME, i), ""))){
//                        //Lấy file lưu thông tin user đang đăng nhập
//                        SharedPreferences sharedPreferencesSigningIn = getSharedPreferences(SharedPrefConstant.SIGNING_IN, MODE_PRIVATE);
//                        SharedPreferences.Editor signingInEditor = sharedPreferencesSigningIn.edit();
//
//                        //Set status đăng nhập thành true
//                        signingInEditor.putString(SharedPrefConstant.SIGNING_IN_STATUS, SharedPrefConstant.SIGNING_IN_STATUS_VALUE);
//
//                        //Set username đăng nhập
//                        signingInEditor.putString(SharedPrefConstant.SIGNING_IN_USERNAME, username);
//
//                        //Set name đăng nhập
//                        signingInEditor.putString(SharedPrefConstant.SIGNING_IN_WALLET_NAME, walletName);
//
//                        //Set isHuawei đăng nhập
//                        signingInEditor.putBoolean(SharedPrefConstant.SIGNING_IN_IS_HUAWEI, true);
//
//                        //Lưu thay đổi file
//                        signingInEditor.apply();
//
//                        //Tìm thấy user trong danh sách
//                        usernameFound = true;
//
//                        Intent setMoneyIntent = new Intent(this, SetMoneyActivity.class);
//                        startActivity(setMoneyIntent);
//                        overridePendingTransition(0,0);
//                        finish();
//                        break;
//                    }
//                }
//                if (!usernameFound) {
//                    //Tăng tổng user và ghi vào danh sách
//                    usersTotal++;
//                    usersTotalEditor.putString(SharedPrefConstant.USERS_TOTAL_VALUE, String.valueOf(usersTotal));
//                    usersTotalEditor.apply();
//
//                    //Lưu thông tin user
//                    usersListEditor.putString(String.format("%s_%d", SharedPrefConstant.USER_USERNAME, usersTotal), username);
//                    //Lấy luôn username làm tên ví nếu đăng ký thông thường
//                    usersListEditor.putString(String.format("%s_%d", SharedPrefConstant.USER_WALLET_NAME, usersTotal), walletName);
//                    //Có là huawei
//                    usersListEditor.putString(String.format("%s_%d", SharedPrefConstant.USER_IS_HUAWEI, usersTotal), "true");
//                    //Lưu file
//                    usersListEditor.apply();
//
//                    //Tự động đăng nhập
//
//                    //Lấy file lưu thông tin user đang đăng nhập
//                    SharedPreferences sharedPreferencesSigningIn = getSharedPreferences(SharedPrefConstant.SIGNING_IN, MODE_PRIVATE);
//                    SharedPreferences.Editor signingInEditor = sharedPreferencesSigningIn.edit();
//
//                    //Set status đăng nhập thành true
//                    signingInEditor.putString(SharedPrefConstant.SIGNING_IN_STATUS, SharedPrefConstant.SIGNING_IN_STATUS_VALUE);
//
//                    //Set username đăng nhập
//                    signingInEditor.putString(SharedPrefConstant.SIGNING_IN_USERNAME, username);
//
//                    //Set name đăng nhập
//                    signingInEditor.putString(SharedPrefConstant.SIGNING_IN_WALLET_NAME, walletName);
//
//                    //Set isHuawei đăng nhập
//                    signingInEditor.putBoolean(SharedPrefConstant.SIGNING_IN_IS_HUAWEI, true);
//
//                    //Lưu file
//                    signingInEditor.apply();
//
//
//                    Intent setMoneyIntent = new Intent(this, SetMoneyActivity.class);
//                    startActivity(setMoneyIntent);
//                    overridePendingTransition(0,0);
//                    finish();
//                }
//            } else {
//                // Đăng nhập thất bại
//                Toast.makeText(this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
//            }
//        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        getViews();
        setEventListener();
    }
}
