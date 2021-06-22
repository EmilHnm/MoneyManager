package com.example.moneymanager.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.moneymanager.R;
import com.example.moneymanager.constant.SharedPrefConstant;
import com.example.moneymanager.methods.SharedMethods;
import com.github.dewinjm.monthyearpicker.MonthFormat;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialog;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Calendar;
import java.util.Locale;

public class StatisticActivity extends AppCompatActivity{
    BottomNavigationView bnv_menu;
    FloatingActionButton fab_add_transaction;
    TextView tv_first_surplus, tv_latest_surplus, tv_time_picker_print;
    LinearLayout ll_time_picker;
    MonthYearPickerDialogFragment dialogFragment;
    int yearSelected;
    int monthSelected;

    private void getViews(){
        bnv_menu = findViewById(R.id.statistic_bnv_menu);
        fab_add_transaction = findViewById(R.id.statistic_fab_add_transaction);
        tv_first_surplus = findViewById(R.id.statistic_tv_first_surplus);
        tv_latest_surplus = findViewById(R.id.statistic_tv_latest_surplus);
        ll_time_picker = findViewById(R.id.statistic_first_ll_time_picker);
        tv_time_picker_print = findViewById(R.id.statistic_first_tv_time_picker_print);
    }


    private void setDateLitener() {
        Calendar calendar = Calendar.getInstance();
        yearSelected = calendar.get(Calendar.YEAR);
        monthSelected = calendar.get(Calendar.MONTH);
        String customTitle = "Chọn thời gian";
        Locale locale = new Locale("vi");
        MonthFormat monthFormat = MonthFormat.LONG;
        dialogFragment = MonthYearPickerDialogFragment
                .getInstance(monthSelected, yearSelected, customTitle, locale, monthFormat);

        dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int year, int monthOfYear) {
                tv_time_picker_print.setText((monthOfYear+1) + "/ " +year);
            }
        });
    }

    private void setEventListener(){
        //Hàm này được gọi mỗi khi có item trên menu được ấn
        bnv_menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_wallet:
                        Intent walletIntent = new Intent(StatisticActivity.this, WalletActivity.class);
                        startActivity(walletIntent);
                        overridePendingTransition(0,0);
                        finish();
                        break;
                    case R.id.item_plan:
                        Intent planIntent = new Intent(StatisticActivity.this, PlanActivity.class);
                        startActivity(planIntent);
                        overridePendingTransition(0,0);
                        finish();
                        break;
                    case R.id.item_account:
                        Intent accountIntent = new Intent(StatisticActivity.this, AccountActivity.class);
                        startActivity(accountIntent);
                        overridePendingTransition(0,0);
                        finish();
                        break;
                }
                return false;
            }
        });

        fab_add_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addTransactionIntent = new Intent(StatisticActivity.this, AddTransactionActivity.class);
                startActivity(addTransactionIntent);
                overridePendingTransition(0,0);
            }
        });

        ll_time_picker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                dialogFragment.show(getSupportFragmentManager(), null);

            }
        });
    }
    private void setWalletMoney(){
        SharedPreferences sharedPreferencesSigningIn = getSharedPreferences(SharedPrefConstant.SIGNING_IN,MODE_PRIVATE);
        String userName = sharedPreferencesSigningIn.getString(SharedPrefConstant.SIGNING_IN_USERNAME, "");
        SharedPreferences sharedPreferencesTransaction = getSharedPreferences(userName,MODE_PRIVATE);
        int WalletMoneyDefault = sharedPreferencesTransaction.getInt(SharedPrefConstant.WALLET_MONEY_DEFAULT, 0);
        int WalletMoney = sharedPreferencesTransaction.getInt(SharedPrefConstant.WALLET_MONEY, 0);
//        tv_first_surplus.setText(WalletMoneyDefault);
        tv_latest_surplus.setText(String.valueOf(WalletMoney));
    }




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        getViews();
        //Không đổi chỗ 2 dòng dưới cho nhau
        //Hàm setSelected item hoạt động như đang ấn vào menu đó => trigger event onNavigationItemSelected => bug
        //=> set item được chọn trước khi set event
        SharedMethods.setNavigationMenu(bnv_menu, R.id.item_statistic);
        setEventListener();
        setWalletMoney();
        setDateLitener();


    }

}
