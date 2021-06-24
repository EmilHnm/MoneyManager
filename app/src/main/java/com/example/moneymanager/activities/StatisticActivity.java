package com.example.moneymanager.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.moneymanager.R;
import com.example.moneymanager.constant.SharedPrefConstant;
import com.example.moneymanager.methods.SharedMethods;
import com.github.dewinjm.monthyearpicker.MonthFormat;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialog;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StatisticActivity extends AppCompatActivity{
    BottomNavigationView bnv_menu;
    FloatingActionButton fab_add_transaction;
    TextView tv_first_surplus, tv_latest_surplus, tv_time_picker_print,
            tv_revenue, tv_expenditure;
    LinearLayout ll_time_picker;
    MonthYearPickerDialogFragment dialogFragment;


    PieChart pc_revenue, pc_expenditure;

    int expenditure_value[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    int revenue_value[] = {0,0,0,0,0,0};

    int expenditure_color[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    int revenue_color[] = {0,0,0,0,0,0};

    String expenditure_title[] = {"","","","","","","","","","","","","","","","","","","","",""};
    String revenue_title[] = {"","","","","",""};
    int yearSelected;
    int monthSelected;
    int revenue, expenditure;

    private void getViews(){
        bnv_menu = findViewById(R.id.statistic_bnv_menu);
        fab_add_transaction = findViewById(R.id.statistic_fab_add_transaction);
        tv_first_surplus = findViewById(R.id.statistic_tv_first_surplus);
        tv_latest_surplus = findViewById(R.id.statistic_tv_latest_surplus);
        ll_time_picker = findViewById(R.id.statistic_first_ll_time_picker);
        tv_time_picker_print = findViewById(R.id.statistic_first_tv_time_picker_print);
        tv_revenue = findViewById(R.id.statistic_tv_revenue);
        tv_expenditure = findViewById(R.id.statistic_tv_expenditure);
        pc_revenue = findViewById(R.id.statistic_pc_revenue);
        pc_expenditure = findViewById(R.id.statistic_pc_expenditure);

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
                try {
                    setRevenuePieChartByDate(year, monthOfYear);
                    setExpenditurePieChartByDate(year, monthOfYear);
                    getRevenueExpenditureByDate(year, monthOfYear);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
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
    private void getWalletMoney(){
        SharedPreferences sharedPreferencesSigningIn = getSharedPreferences(SharedPrefConstant.SIGNING_IN,MODE_PRIVATE);
        String userName = sharedPreferencesSigningIn.getString(SharedPrefConstant.SIGNING_IN_USERNAME, "");
        SharedPreferences sharedPreferencesTransaction = getSharedPreferences(userName,MODE_PRIVATE);
        int WalletMoneyDefault = sharedPreferencesTransaction.getInt(SharedPrefConstant.WALLET_MONEY_DEFAULT, 0);
        int WalletMoney = sharedPreferencesTransaction.getInt(SharedPrefConstant.WALLET_MONEY, 0);
        tv_first_surplus.setText(String.valueOf(WalletMoneyDefault));
        tv_latest_surplus.setText(String.valueOf(WalletMoney));
    }

    private void getRevenueExpenditure () {
        SharedPreferences sharedPreferencesSigningIn = getSharedPreferences(SharedPrefConstant.SIGNING_IN, MODE_PRIVATE);
        String username = sharedPreferencesSigningIn.getString(SharedPrefConstant.SIGNING_IN_USERNAME, "");
        SharedPreferences sharedPreferencesTransaction = getSharedPreferences(username, MODE_PRIVATE);
        int totalTransactions = sharedPreferencesTransaction.getInt(SharedPrefConstant.TRANSACTION_TOTAL, 0);
        for(int i = 1; i <= totalTransactions; ++i){
            int transactionCategoryId = sharedPreferencesTransaction.getInt(String.format("%s_%d", SharedPrefConstant.TRANSACTION_CATEGORY_ID, i), 0);
            int transactionMoney = sharedPreferencesTransaction.getInt(String.format("%s_%d", SharedPrefConstant.TRANSACTION_MONEY, i), 0);
            if (transactionCategoryId < 20 || transactionCategoryId == 26 || transactionCategoryId == 27) {
                expenditure = expenditure + transactionMoney;
            } else if (transactionCategoryId >= 20) {
                revenue = revenue + transactionMoney;
            }
            tv_revenue.setText(Integer.toString(revenue));
            tv_expenditure.setText(Integer.toString(expenditure));
        }
    }

    private void getRevenueExpenditureByDate (int year, int monthOfYear) throws ParseException {
        SharedPreferences sharedPreferencesSigningIn = getSharedPreferences(SharedPrefConstant.SIGNING_IN, MODE_PRIVATE);
        String username = sharedPreferencesSigningIn.getString(SharedPrefConstant.SIGNING_IN_USERNAME, "");
        SharedPreferences sharedPreferencesTransaction = getSharedPreferences(username, MODE_PRIVATE);
        int totalTransactions = sharedPreferencesTransaction.getInt(SharedPrefConstant.TRANSACTION_TOTAL, 0);
        for(int i = 1; i <= totalTransactions; ++i){
            int transactionCategoryId = sharedPreferencesTransaction.getInt(String.format("%s_%d", SharedPrefConstant.TRANSACTION_CATEGORY_ID, i), 0);
            int transactionMoney = sharedPreferencesTransaction.getInt(String.format("%s_%d", SharedPrefConstant.TRANSACTION_MONEY, i), 0);
            String transactionDate = sharedPreferencesTransaction.getString(String.format("%s_%d", SharedPrefConstant.TRANSACTION_DATE, i), null);
            Date date =new SimpleDateFormat("dd/MM/yyyy").parse(transactionDate);
            expenditure = 0 ;
            revenue = 0 ;
            if (date.getMonth() == monthOfYear && date.getYear() == year) {
                if (transactionCategoryId < 20 || transactionCategoryId == 26 || transactionCategoryId == 27) {
                    expenditure = expenditure + transactionMoney;
                } else if (transactionCategoryId >= 20) {
                    revenue = revenue + transactionMoney;
                }
            }
            tv_revenue.setText(Integer.toString(revenue));
            tv_expenditure.setText(Integer.toString(expenditure));
        }
    }

    public void setRevenueItemValueColor(int id, int value) {
        switch (id) {
            case 1: expenditure_value[id-1] = expenditure_value[id-1] + value;
                expenditure_color[id-1] = getColor(R.color.m1);
                expenditure_title[id-1] = "Ăn uống";
                break;
            case 2: expenditure_value[id-1] = expenditure_value[id-1] + value;
                expenditure_color[id-1] = getColor(R.color.m2);
                expenditure_title[id-1] = "Di chuyển";
                break;
            case 3: expenditure_value[id-1] = expenditure_value[id-1] + value;
                expenditure_color[id-1] = getColor(R.color.m3);
                expenditure_title[id-1] = "Hóa đơn";
                break;
            case 4: expenditure_value[id-1] = expenditure_value[id-1] + value;
                expenditure_color[id-1] = getColor(R.color.m4);
                expenditure_title[id-1] = "Trang trí, sửa nhà";
                break;
            case 5: expenditure_value[id-1] = expenditure_value[id-1] + value;
                expenditure_color[id-1] = getColor(R.color.m5);
                expenditure_title[id-1] = "Bảo dưỡng xe";
                break;
            case 6: expenditure_value[id-1] = expenditure_value[id-1] + value;
                expenditure_color[id-1] = getColor(R.color.m6);
                expenditure_title[id-1] = "Sức khỏe";
                break;
            case 7: expenditure_value[id-1] = expenditure_value[id-1] + value;
                expenditure_color[id-1] = getColor(R.color.m7);
                expenditure_title[id-1] = "Học tập";
                break;
            case 8: expenditure_value[id-1] = expenditure_value[id-1] + value;
                expenditure_color[id-1] = getColor(R.color.m8);
                expenditure_title[id-1] = "Đồ gia dụng";
                break;
            case 9: expenditure_value[id-1] = expenditure_value[id-1] + value;
                expenditure_color[id-1] = getColor(R.color.m9);
                expenditure_title[id-1] = "Bảo hiểm";
                break;
            case 10: expenditure_value[id-1] = expenditure_value[id-1] + value;
                expenditure_color[id-1] = getColor(R.color.m10);
                expenditure_title[id-1] = "Đồ dùng cá nhân";
                break;
            case 11: expenditure_value[id-1] = expenditure_value[id-1] + value;
                expenditure_color[id-1] = getColor(R.color.m11);
                expenditure_title[id-1] = "Vật nuôi";
                break;
            case 12: expenditure_value[id-1] = expenditure_value[id-1] + value;
                expenditure_color[id-1] = getColor(R.color.m12);
                expenditure_title[id-1] = "Dịch vụ gia đình";
                break;
            case 13: expenditure_value[id-1] = expenditure_value[id-1] + value;
                expenditure_color[id-1] = getColor(R.color.m13);
                expenditure_title[id-1] = "Thể thao";
                break;
            case 14: expenditure_value[id-1] = expenditure_value[id-1] + value;
                expenditure_color[id-1] = getColor(R.color.m14);
                expenditure_title[id-1] = "Làm đẹp";
                break;
            case 15: expenditure_value[id-1] = expenditure_value[id-1] + value;
                expenditure_color[id-1] = getColor(R.color.m15);
                expenditure_title[id-1] = "Dịch vụ trược tuyến";
                break;
            case 16: expenditure_value[id-1] = expenditure_value[id-1] + value;
                expenditure_color[id-1] = getColor(R.color.m16);
                expenditure_title[id-1] = "Giải trí";
                break;
            case 17: expenditure_value[id-1] = expenditure_value[id-1] + value;
                expenditure_color[id-1] = getColor(R.color.m17);
                expenditure_title[id-1] = "Trả lãi";
                break;
            case 18: expenditure_value[id-1] = expenditure_value[id-1] + value;
                expenditure_color[id-1] = getColor(R.color.m18);
                expenditure_title[id-1] = "Chuyển tiền đi";
                break;
            case 19: expenditure_value[id-1] = expenditure_value[id-1] + value;
                expenditure_color[id-1] = getColor(R.color.m19);
                expenditure_title[id-1] = "Các khoản chi khác";
                break;
            case 20: revenue_value[0] = revenue_value[0] + value;
                revenue_color[0] = getColor(R.color.m20);
                revenue_title[0] = "Lương";
                break;
            case 21: revenue_value[1] = revenue_value[1] + value;
                revenue_color[1] = getColor(R.color.m21);
                revenue_title[1] = "Thu lãi";
                break;
            case 22: revenue_value[2] = revenue_value[2] + value;
                revenue_color[2] = getColor(R.color.m22);
                revenue_title[2] = "Chuyển tiền đến";
                break;
            case 23: revenue_value[3] = revenue_value[3] + value;
                revenue_color[3] = getColor(R.color.m23);
                revenue_title[3] = "Các khoản thu khác";
                break;
            case 24: revenue_value[4] = revenue_value[3] + value;
                revenue_color[4] = getColor(R.color.m24);
                revenue_title[4] = "Thu nợ";
                break;
            case 25: revenue_value[5] = revenue_value[5] + value;
                revenue_color[5] = getColor(R.color.m25);
                revenue_title[5] = "Các khoản thu khác";
                break;
            case 26: expenditure_value[19] = expenditure_value[19] + value;
                expenditure_color[19] = getColor(R.color.m26);
                expenditure_title[19] = "Cho vay";
                break;
            case 27: expenditure_value[20] = expenditure_value[20] + value;
                expenditure_color[20] = getColor(R.color.m27);
                expenditure_title[20] = "Trả nợ";
                break;
        }
    }

    public void setRevenuePieChart () {
        pc_revenue.setUsePercentValues(true);
        pc_revenue.getDescription().setEnabled(false);
        pc_revenue.setExtraOffsets(5, 10, 5, 5);

        pc_revenue.setDragDecelerationFrictionCoef(0.95f);

        pc_revenue.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        pc_revenue.setDrawHoleEnabled(true);
        pc_revenue.setHoleColor(Color.WHITE);

        pc_revenue.setTransparentCircleColor(Color.WHITE);
        pc_revenue.setTransparentCircleAlpha(110);

        pc_revenue.setHoleRadius(40f);
        pc_revenue.setTransparentCircleRadius(61f);

        pc_revenue.setRotationAngle(0);

        pc_revenue.setRotationEnabled(false);
        pc_revenue.setHighlightPerTapEnabled(true);
        pc_revenue.animateXY(1000,2000);

        pc_revenue.setDrawEntryLabels(false);
        Legend lg = pc_revenue.getLegend();
        lg.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        lg.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        lg.setOrientation(Legend.LegendOrientation.VERTICAL);
        lg.setDrawInside(false);
        lg.setEnabled(false);


        SharedPreferences sharedPreferencesSigningIn = getSharedPreferences(SharedPrefConstant.SIGNING_IN, MODE_PRIVATE);
        String username = sharedPreferencesSigningIn.getString(SharedPrefConstant.SIGNING_IN_USERNAME, "");
        SharedPreferences sharedPreferencesTransaction = getSharedPreferences(username, MODE_PRIVATE);
        int totalTransactions = sharedPreferencesTransaction.getInt(SharedPrefConstant.TRANSACTION_TOTAL, 0);
        for(int i = 1; i <= totalTransactions; ++i){
            int transactionCategoryId = sharedPreferencesTransaction.getInt(String.format("%s_%d", SharedPrefConstant.TRANSACTION_CATEGORY_ID, i), 0);
            int transactionMoney = sharedPreferencesTransaction.getInt(String.format("%s_%d", SharedPrefConstant.TRANSACTION_MONEY, i), 0);
            if (transactionCategoryId >= 20) {
                setRevenueItemValueColor(transactionCategoryId, transactionMoney);
            }
        }
        ArrayList<PieEntry> revenue_entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            if (revenue_value[i] != 0) {
                revenue_entries.add(new PieEntry(revenue_value[i],revenue_title[i]));
                colors.add(revenue_color[i]);
            }
        }
        PieDataSet dataSet = new PieDataSet(revenue_entries, "");
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(5f);
        dataSet.setValueTextSize(15f);
        dataSet.setColors(colors);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData revenue_pie_data = new PieData(dataSet);

        pc_revenue.setData(revenue_pie_data);
        pc_revenue.setOnChartValueSelectedListener(new OnChartValueSelectedListener()
        {
            @Override
            public void onValueSelected(Entry e, Highlight h)
            {
                PieEntry pe = (PieEntry) e;

                Toast.makeText(StatisticActivity.this, "Nhóm "
                        + pe.getLabel()
                        + " có tổng "
                        + e.getY()
                        + "đ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected()
            {

            }
        });
    }

    private void setRevenuePieChartByDate (int year, int monthOfYear) throws ParseException {
        pc_revenue.setUsePercentValues(true);
        pc_revenue.getDescription().setEnabled(false);
        pc_revenue.setExtraOffsets(5, 10, 5, 5);

        pc_revenue.setDragDecelerationFrictionCoef(0.95f);

        pc_revenue.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        pc_revenue.setDrawHoleEnabled(true);
        pc_revenue.setHoleColor(Color.WHITE);

        pc_revenue.setTransparentCircleColor(Color.WHITE);
        pc_revenue.setTransparentCircleAlpha(110);

        pc_revenue.setHoleRadius(40f);
        pc_revenue.setTransparentCircleRadius(61f);

        pc_revenue.setRotationAngle(0);

        pc_revenue.setRotationEnabled(false);
        pc_revenue.setHighlightPerTapEnabled(true);
        pc_revenue.animateXY(1000,2000);

        pc_revenue.setDrawEntryLabels(false);
        Legend lg = pc_revenue.getLegend();
        lg.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        lg.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        lg.setOrientation(Legend.LegendOrientation.VERTICAL);
        lg.setDrawInside(false);
        lg.setEnabled(false);
        for (int i = 0; i < 6; i++) {
            revenue_value[i] = 0;
        }

        SharedPreferences sharedPreferencesSigningIn = getSharedPreferences(SharedPrefConstant.SIGNING_IN, MODE_PRIVATE);
        String username = sharedPreferencesSigningIn.getString(SharedPrefConstant.SIGNING_IN_USERNAME, "");
        SharedPreferences sharedPreferencesTransaction = getSharedPreferences(username, MODE_PRIVATE);
        int totalTransactions = sharedPreferencesTransaction.getInt(SharedPrefConstant.TRANSACTION_TOTAL, 0);
        for(int i = 1; i <= totalTransactions; ++i){
            int transactionCategoryId = sharedPreferencesTransaction.getInt(String.format("%s_%d", SharedPrefConstant.TRANSACTION_CATEGORY_ID, i), 0);
            int transactionMoney = sharedPreferencesTransaction.getInt(String.format("%s_%d", SharedPrefConstant.TRANSACTION_MONEY, i), 0);
            String transactionDate = sharedPreferencesTransaction.getString(String.format("%s_%d", SharedPrefConstant.TRANSACTION_DATE, i), null);
            Date date =new SimpleDateFormat("dd/MM/yyyy").parse(transactionDate);
            if (transactionCategoryId >= 20) {
                if (date.getMonth() == monthOfYear && date.getYear() == year) {
                    setRevenueItemValueColor(transactionCategoryId, transactionMoney);
                }
            }
        }
        ArrayList<PieEntry> revenue_entries = new ArrayList<>();
        revenue_entries.clear();
        ArrayList<Integer> colors = new ArrayList<>();
        colors.clear();
        for (int i = 0; i < 6; i++) {
            if (revenue_value[i] != 0) {
                revenue_entries.add(new PieEntry(revenue_value[i],revenue_title[i]));
                colors.add(revenue_color[i]);
            }
        }
        PieDataSet dataSet = new PieDataSet(revenue_entries, "");
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(5f);
        dataSet.setValueTextSize(15f);
        dataSet.setColors(colors);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData revenue_pie_data = new PieData(dataSet);

        pc_revenue.setData(revenue_pie_data);
        pc_revenue.setOnChartValueSelectedListener(new OnChartValueSelectedListener()
        {
            @Override
            public void onValueSelected(Entry e, Highlight h)
            {
                PieEntry pe = (PieEntry) e;

                Toast.makeText(StatisticActivity.this, "Nhóm "
                        + pe.getLabel()
                        + " có tổng "
                        + e.getY()
                        + "đ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected()
            {

            }
        });
    }



    public void setExpenditurePieChart () {
        pc_expenditure.setUsePercentValues(true);
        pc_expenditure.getDescription().setEnabled(false);
        pc_expenditure.setExtraOffsets(5, 10, 5, 5);

        pc_expenditure.setDragDecelerationFrictionCoef(0.95f);

        pc_expenditure.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        pc_expenditure.setDrawHoleEnabled(true);
        pc_expenditure.setHoleColor(Color.WHITE);

        pc_expenditure.setTransparentCircleColor(Color.WHITE);
        pc_expenditure.setTransparentCircleAlpha(110);

        pc_expenditure.setHoleRadius(40f);
        pc_expenditure.setTransparentCircleRadius(61f);

        pc_expenditure.setRotationAngle(0);

        pc_expenditure.setRotationEnabled(false);
        pc_expenditure.setHighlightPerTapEnabled(true);

        pc_expenditure.setDrawEntryLabels(false);
        pc_expenditure.animateXY(1000,2000);

        Legend lg = pc_expenditure.getLegend();
        lg.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        lg.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        lg.setOrientation(Legend.LegendOrientation.VERTICAL);
        lg.setDrawInside(false);
        lg.setEnabled(false);


        SharedPreferences sharedPreferencesSigningIn = getSharedPreferences(SharedPrefConstant.SIGNING_IN, MODE_PRIVATE);
        String username = sharedPreferencesSigningIn.getString(SharedPrefConstant.SIGNING_IN_USERNAME, "");
        SharedPreferences sharedPreferencesTransaction = getSharedPreferences(username, MODE_PRIVATE);
        int totalTransactions = sharedPreferencesTransaction.getInt(SharedPrefConstant.TRANSACTION_TOTAL, 0);
        for(int i = 1; i <= totalTransactions; ++i){
            int transactionCategoryId = sharedPreferencesTransaction.getInt(String.format("%s_%d", SharedPrefConstant.TRANSACTION_CATEGORY_ID, i), 0);
            int transactionMoney = sharedPreferencesTransaction.getInt(String.format("%s_%d", SharedPrefConstant.TRANSACTION_MONEY, i), 0);
            if (transactionCategoryId < 20 || transactionCategoryId == 26 || transactionCategoryId == 27) {
                setRevenueItemValueColor(transactionCategoryId, transactionMoney);
            }
        }
        ArrayList<PieEntry> revenue_entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        for (int i = 0; i < 19; i++) {
            if (expenditure_value[i] != 0) {
                revenue_entries.add(new PieEntry(expenditure_value[i],expenditure_title[i]));
                colors.add(expenditure_color[i]);
            }
        }
        PieDataSet dataSet = new PieDataSet(revenue_entries, "Election Results");
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(5f);
        dataSet.setValueTextSize(15f);
        dataSet.setColors(colors);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData revenue_pie_data = new PieData(dataSet);

        pc_expenditure.setData(revenue_pie_data);

        pc_expenditure.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry pe = (PieEntry) e;
                Toast.makeText(StatisticActivity.this, "Nhóm "
                        + pe.getLabel()
                        + " có tổng "
                        + e.getY()
                        + "đ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

    }

    public void setExpenditurePieChartByDate (int year, int monthOfYear) throws ParseException {
        pc_expenditure.setUsePercentValues(true);
        pc_expenditure.getDescription().setEnabled(false);
        pc_expenditure.setExtraOffsets(5, 10, 5, 5);

        pc_expenditure.setDragDecelerationFrictionCoef(0.95f);

        pc_expenditure.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        pc_expenditure.setDrawHoleEnabled(true);
        pc_expenditure.setHoleColor(Color.WHITE);

        pc_expenditure.setTransparentCircleColor(Color.WHITE);
        pc_expenditure.setTransparentCircleAlpha(110);

        pc_expenditure.setHoleRadius(40f);
        pc_expenditure.setTransparentCircleRadius(61f);

        pc_expenditure.setRotationAngle(0);

        pc_expenditure.setRotationEnabled(false);
        pc_expenditure.setHighlightPerTapEnabled(true);

        pc_expenditure.setDrawEntryLabels(false);
        pc_expenditure.animateXY(1000,2000);

        Legend lg = pc_expenditure.getLegend();
        lg.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        lg.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        lg.setOrientation(Legend.LegendOrientation.VERTICAL);
        lg.setDrawInside(false);
        lg.setEnabled(false);

        for (int i = 0; i < 19; i++) {
            expenditure_value[i] = 0;
        }

        SharedPreferences sharedPreferencesSigningIn = getSharedPreferences(SharedPrefConstant.SIGNING_IN, MODE_PRIVATE);
        String username = sharedPreferencesSigningIn.getString(SharedPrefConstant.SIGNING_IN_USERNAME, "");
        SharedPreferences sharedPreferencesTransaction = getSharedPreferences(username, MODE_PRIVATE);
        int totalTransactions = sharedPreferencesTransaction.getInt(SharedPrefConstant.TRANSACTION_TOTAL, 0);
        for(int i = 1; i <= totalTransactions; ++i){
            String transactionDate = sharedPreferencesTransaction.getString(String.format("%s_%d", SharedPrefConstant.TRANSACTION_DATE, i), null);
            Date date =new SimpleDateFormat("dd/MM/yyyy").parse(transactionDate);
            int transactionCategoryId = sharedPreferencesTransaction.getInt(String.format("%s_%d", SharedPrefConstant.TRANSACTION_CATEGORY_ID, i), 0);
            int transactionMoney = sharedPreferencesTransaction.getInt(String.format("%s_%d", SharedPrefConstant.TRANSACTION_MONEY, i), 0);
            if (transactionCategoryId < 20 || transactionCategoryId == 26 || transactionCategoryId == 27) {
                if (date.getMonth() == monthOfYear && date.getYear() == year) {
                    setRevenueItemValueColor(transactionCategoryId, transactionMoney);
                }
            }
        }
        ArrayList<PieEntry> revenue_entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        for (int i = 0; i < 19; i++) {
            if (expenditure_value[i] != 0) {
                revenue_entries.add(new PieEntry(expenditure_value[i],expenditure_title[i]));
                colors.add(expenditure_color[i]);
            }
        }
        PieDataSet dataSet = new PieDataSet(revenue_entries, "Election Results");
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(5f);
        dataSet.setValueTextSize(15f);
        dataSet.setColors(colors);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData revenue_pie_data = new PieData(dataSet);

        pc_expenditure.setData(revenue_pie_data);

        pc_expenditure.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry pe = (PieEntry) e;
                Toast.makeText(StatisticActivity.this, "Nhóm "
                        + pe.getLabel()
                        + " có tổng "
                        + e.getY()
                        + "đ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

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
        getWalletMoney();
        getRevenueExpenditure();
        setDateLitener();
        setRevenuePieChart();
        setExpenditurePieChart();
    }

}
