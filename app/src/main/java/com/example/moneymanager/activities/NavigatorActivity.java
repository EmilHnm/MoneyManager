package com.example.moneymanager.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.moneymanager.constant.SharedPrefConstant;

public class NavigatorActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Activity dùng để chuyển trang, nếu đang đăng nhập thì vào Wallet, không thì vào trang FirstLoad
        //Lấy thông tin có đang đăng nhập không
        SharedPreferences sharedPreferencesSigningIn = getSharedPreferences(SharedPrefConstant.SIGNING_IN, MODE_PRIVATE);
        String signingIn = sharedPreferencesSigningIn.getString(SharedPrefConstant.SIGNING_IN_STATUS, "");
        //Chuyển trang

        //Nếu đã đăng nhập
        if(signingIn.equals(SharedPrefConstant.SIGNING_IN_STATUS_VALUE)){
            //Kiểm tra xem nhập số tiền lần đầu chưa
            String username = sharedPreferencesSigningIn.getString(SharedPrefConstant.SIGNING_IN_USERNAME, "");
            SharedPreferences sharedPreferencesTransaction = getSharedPreferences(username, MODE_PRIVATE);
            boolean walletIsSetMoney = sharedPreferencesTransaction.getBoolean(SharedPrefConstant.WALLET_IS_SET_MONEY, false);

            if(walletIsSetMoney){
                Intent walletIntent = new Intent(this, WalletActivity.class);
                startActivity(walletIntent);
                overridePendingTransition(0,0);
                finish();
            }
            else{
                Intent setMoneyIntent = new Intent(this, SetMoneyActivity.class);
                startActivity(setMoneyIntent);
                overridePendingTransition(0,0);
                finish();
            }

        }
        //Chưa đăng nhập thì cho quay về màn hình load
        else{
            Intent firstLoadingIntent = new Intent(this, FirstLoadingActivity.class);
            startActivity(firstLoadingIntent);
            overridePendingTransition(0,0);
            finish();
        }
    }
}
