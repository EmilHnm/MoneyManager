package com.example.moneymanager.activities;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.moneymanager.R;
//import com.huawei.agconnect.config.AGConnectServicesConfig;
//import com.huawei.hms.aaid.HmsInstanceId;
//import com.huawei.hms.common.ApiException;

public class FirstLoadingActivity extends AppCompatActivity {
    static final String ACTIVITY_NAME = "FirstLoadingActivity";
    Button btn_signup, btn_signin;

    private void getViews(){
        btn_signup = findViewById(R.id.first_loading_btn_signup);
        btn_signin = findViewById(R.id.first_loading_btn_signin);
    }

    private void setEventListener(){
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupIntent = new Intent(FirstLoadingActivity.this, SignupActivity.class);
                signupIntent.putExtra("source", ACTIVITY_NAME);
                startActivity(signupIntent);
                overridePendingTransition(0,0);
            }
        });

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signinIntent = new Intent(FirstLoadingActivity.this, SigninActivity.class);
                signinIntent.putExtra("source", ACTIVITY_NAME);
                startActivity(signinIntent);
                overridePendingTransition(0,0);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_loading);
        getViews();
        setEventListener();
//        getToken();
    }
//    private void getToken(){
//        new Thread(){
//            public void run(){
//                try{
//                    String appID= AGConnectServicesConfig.fromContext(FirstLoadingActivity.this).getString("client/app_id");
//                    String tokenScope="HCM";
//                    String token= HmsInstanceId.getInstance(FirstLoadingActivity.this).getToken(appID,tokenScope);
//                    Log.i("token","get token: "+token);
//                    if(!TextUtils.isEmpty(token)){
//                        sendRegTokenToServer(token);
//
//                    }
//
//                }catch(ApiException e){
//                    Log.e("tokenfail","get token failed"+ e);
//
//                }
//            }
//        }.start();
//    }
    private void sendRegTokenToServer(String token) {
        Log.i("sendtoken","sending token to server"+token);
    }
}