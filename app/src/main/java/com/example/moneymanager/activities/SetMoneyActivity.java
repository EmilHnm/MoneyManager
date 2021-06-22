package com.example.moneymanager.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.moneymanager.R;
import com.example.moneymanager.constant.SharedPrefConstant;

public class SetMoneyActivity extends AppCompatActivity {
    TextView tv_next;
    EditText et_money;

    private void getViews(){
        tv_next = findViewById(R.id.set_money_tv_next);
        et_money = findViewById(R.id.set_money_et_money);
    }

    private void navigateToWalletIfAlreadySetMoney(){
        //Lấy file người dùng đang đăng nhập
        SharedPreferences sharedPreferencesSigningIn = getSharedPreferences(SharedPrefConstant.SIGNING_IN, MODE_PRIVATE);
        String username = sharedPreferencesSigningIn.getString(SharedPrefConstant.SIGNING_IN_USERNAME, "");
        //Lấy ví người dùng đang đăng nhập
        SharedPreferences sharedPreferencesTransaction = getSharedPreferences(username, MODE_PRIVATE);
        Boolean isSetMoney = sharedPreferencesTransaction.getBoolean(SharedPrefConstant.WALLET_IS_SET_MONEY, false);
        if(isSetMoney){
            Intent walletIntent = new Intent(this, WalletActivity.class);
            startActivity(walletIntent);
            finish();
        }
    }

    private void setEventListener(){
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String money = et_money.getText().toString();
                if(money.isEmpty()){
                    et_money.setHint("Hãy nhập số tiền hợp lệ");
                    et_money.setHintTextColor(getResources().getColor(R.color.red_form_error));
                    et_money.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red_form_error)));
                }
                else{
                    //Lấy file người dùng đang đăng nhập
                    SharedPreferences sharedPreferencesSigningIn = getSharedPreferences(SharedPrefConstant.SIGNING_IN, MODE_PRIVATE);
                    String username = sharedPreferencesSigningIn.getString(SharedPrefConstant.SIGNING_IN_USERNAME, "");
                    //Lấy ví người dùng đang đăng nhập
                    SharedPreferences sharedPreferencesTransaction = getSharedPreferences(username, MODE_PRIVATE);
                    SharedPreferences.Editor transactionEditor = sharedPreferencesTransaction.edit();

                    //Lưu số tiền nhập
                    transactionEditor.putInt(SharedPrefConstant.WALLET_MONEY, Integer.parseInt(money));
                    transactionEditor.putInt(SharedPrefConstant.WALLET_MONEY_DEFAULT, Integer.parseInt(money));
                    //Lưu đã nhập tiền lần đầu
                    transactionEditor.putBoolean(SharedPrefConstant.WALLET_IS_SET_MONEY, true);
                    transactionEditor.apply();

                    Intent walletIntent = new Intent(SetMoneyActivity.this, WalletActivity.class);
                    startActivity(walletIntent);
                    finish();

                }
            }
        });
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_money);
        getViews();
        navigateToWalletIfAlreadySetMoney();
        setEventListener();
    }
}
