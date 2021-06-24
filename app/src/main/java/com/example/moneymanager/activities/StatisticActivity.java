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

import java.text.DecimalFormat;
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
            tv_revenue, tv_expenditure,
            tv_wallet_name, tv_wallet_money;
    LinearLayout ll_time_picker;
    MonthYearPickerDialogFragment dialogFragment;

    PieChart pc_revenue, pc_expenditure;

    //Tạo mảng chúa giá trị giao dịch
    int expenditure_value[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    int revenue_value[] = {0,0,0,0,0,0};
    //Tạo mảng chúa giá trị màu giao dịch
    int expenditure_color[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    int revenue_color[] = {0,0,0,0,0,0};
    //Tạo mảng chúa tên giao dịch
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
        tv_wallet_name = findViewById(R.id.statistic_tv_wallet_name);
        tv_wallet_money = findViewById(R.id.statistic_tv_money);

    }

    private void displayUserInformation(){
        //Lấy file người dùng đang đăng nhập
        SharedPreferences sharedPreferenceSigningIn = getSharedPreferences(SharedPrefConstant.SIGNING_IN, MODE_PRIVATE);
        //Set tên ví
        String walletName = sharedPreferenceSigningIn.getString(SharedPrefConstant.SIGNING_IN_WALLET_NAME, "");
        tv_wallet_name.setText(walletName);

        //Set số tiền trong ví
        String username = sharedPreferenceSigningIn.getString(SharedPrefConstant.SIGNING_IN_USERNAME, "");
        SharedPreferences sharedPreferencesTransaction = getSharedPreferences(username, MODE_PRIVATE);
        int money = sharedPreferencesTransaction.getInt(SharedPrefConstant.WALLET_MONEY, 0);

        //Set màu tiền
        if(money > 0)
            tv_wallet_money.setTextColor(getResources().getColor(R.color.green_main));
        else
            tv_wallet_money.setTextColor(getResources().getColor(R.color.red_form_error));

        //Format tiền từ 100000 thành 100.000 đ
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        String formattedMoney = decimalFormat.format(money);

        tv_wallet_money.setText(formattedMoney + " đ");
    }

    private void setDateLitener() {
        Calendar calendar = Calendar.getInstance();
        yearSelected = calendar.get(Calendar.YEAR);
        monthSelected = calendar.get(Calendar.MONTH);
        //Đặt tiêu đề
        String customTitle = "Chọn thời gian";
        //Đặt ngôn ngữ
        Locale locale = new Locale("vi");
        //Đặt dạng hiển thị tháng
        MonthFormat monthFormat = MonthFormat.LONG;
        //Tạo lịch
        dialogFragment = MonthYearPickerDialogFragment
                .getInstance(monthSelected, yearSelected, customTitle, locale, monthFormat);
        dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int year, int monthOfYear) {
                tv_time_picker_print.setText((monthOfYear+1) + "/ " +year);
                try {
                    //HIển thị chart theo tháng
                    setRevenuePieChartByDate(year, monthOfYear);
                    setExpenditurePieChartByDate(year, monthOfYear);
                    //Hiển thị thông tin tổng quát theo tháng
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
        //Lấy thông tin người đăng nhập
        SharedPreferences sharedPreferencesSigningIn = getSharedPreferences(SharedPrefConstant.SIGNING_IN,MODE_PRIVATE);
        String userName = sharedPreferencesSigningIn.getString(SharedPrefConstant.SIGNING_IN_USERNAME, "");
        SharedPreferences sharedPreferencesTransaction = getSharedPreferences(userName,MODE_PRIVATE);
        //Lấy số dư đầu
        int WalletMoneyDefault = sharedPreferencesTransaction.getInt(SharedPrefConstant.WALLET_MONEY_DEFAULT, 0);
        //Lấy số dư cuối
        int WalletMoney = sharedPreferencesTransaction.getInt(SharedPrefConstant.WALLET_MONEY, 0);
        tv_first_surplus.setText(String.valueOf(WalletMoneyDefault));
        tv_latest_surplus.setText(String.valueOf(WalletMoney));
    }

    private void getRevenueExpenditure () {
        //Lấy thông tin người dùng đăng nhập
        SharedPreferences sharedPreferencesSigningIn = getSharedPreferences(SharedPrefConstant.SIGNING_IN, MODE_PRIVATE);
        String username = sharedPreferencesSigningIn.getString(SharedPrefConstant.SIGNING_IN_USERNAME, "");
        SharedPreferences sharedPreferencesTransaction = getSharedPreferences(username, MODE_PRIVATE);

        //Lấy tổng số giao dịch
        int totalTransactions = sharedPreferencesTransaction.getInt(SharedPrefConstant.TRANSACTION_TOTAL, 0);
        for(int i = 1; i <= totalTransactions; ++i){
            //Lấy ID và số tiền của giao dịch
            int transactionCategoryId = sharedPreferencesTransaction.getInt(String.format("%s_%d", SharedPrefConstant.TRANSACTION_CATEGORY_ID, i), 0);
            int transactionMoney = sharedPreferencesTransaction.getInt(String.format("%s_%d", SharedPrefConstant.TRANSACTION_MONEY, i), 0);
            if (transactionCategoryId < 20 || transactionCategoryId == 26 || transactionCategoryId == 27) {
                expenditure = expenditure + transactionMoney;
            } else if (transactionCategoryId >= 20) {
                revenue = revenue + transactionMoney;
            }

            //Đổi sang định dạng 000,000
            DecimalFormat decimalFormat = new DecimalFormat("###,###");
            String revenue_money = decimalFormat.format(revenue);
            String expenditure_money = decimalFormat.format(expenditure);
            tv_revenue.setText(revenue_money);
            tv_expenditure.setText(expenditure_money);
        }
    }

    private void getRevenueExpenditureByDate (int year, int monthOfYear) throws ParseException {
        //Lấy thông tin người dùng đăng nhập
        SharedPreferences sharedPreferencesSigningIn = getSharedPreferences(SharedPrefConstant.SIGNING_IN, MODE_PRIVATE);
        String username = sharedPreferencesSigningIn.getString(SharedPrefConstant.SIGNING_IN_USERNAME, "");
        SharedPreferences sharedPreferencesTransaction = getSharedPreferences(username, MODE_PRIVATE);
        //Lấy tổng số giao dịch
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
            //Đổi sang định dạng 000,000
            DecimalFormat decimalFormat = new DecimalFormat("###,###");
            String revenue_money = decimalFormat.format(revenue);
            String expenditure_money = decimalFormat.format(expenditure);
            tv_revenue.setText(revenue_money);
            tv_expenditure.setText(expenditure_money);
        }
    }

    //Truyền vào id và giá trị giao dịch,
    // Dựa theo id để lưu  giá trị, màu, tiêu đề tương ứng
    // vào mảng trên
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

        //Vòng tròn giữa biểu đồ
        pc_revenue.setDrawHoleEnabled(true);
        pc_revenue.setHoleColor(Color.WHITE);
        //Vòng tròn mờ giữa biểu đồ
        pc_revenue.setTransparentCircleColor(Color.WHITE);
        pc_revenue.setTransparentCircleAlpha(110);

        pc_revenue.setHoleRadius(40f); //kích thước hình tròn
        pc_revenue.setTransparentCircleRadius(61f); // kích thước hình tròn mờ

        pc_revenue.setRotationAngle(0); // góc nghiệng của biểu đồ

        pc_revenue.setRotationEnabled(false); // Vô hiệu hóa xoay
        pc_revenue.setHighlightPerTapEnabled(true); // Đạt phóng to khi chọn
        pc_revenue.animateXY(1000,2000); // Animation hiển thị

        pc_revenue.setDrawEntryLabels(false); // Vô hiệu hóa mô tả trong biểu đồ

        //Vô hiệu hóa phần chú thích
        Legend lg = pc_revenue.getLegend();
        lg.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        lg.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        lg.setOrientation(Legend.LegendOrientation.VERTICAL);
        lg.setDrawInside(false);
        lg.setEnabled(false);

        //Lấy thông tin người dùng đăng nhập
        SharedPreferences sharedPreferencesSigningIn = getSharedPreferences(SharedPrefConstant.SIGNING_IN, MODE_PRIVATE);
        String username = sharedPreferencesSigningIn.getString(SharedPrefConstant.SIGNING_IN_USERNAME, "");
        SharedPreferences sharedPreferencesTransaction = getSharedPreferences(username, MODE_PRIVATE);
        //Lấy số giao dịch
        int totalTransactions = sharedPreferencesTransaction.getInt(SharedPrefConstant.TRANSACTION_TOTAL, 0);
        for(int i = 1; i <= totalTransactions; ++i){
            //Lấy id và giá trị của giao dịch
            int transactionCategoryId = sharedPreferencesTransaction.getInt(String.format("%s_%d", SharedPrefConstant.TRANSACTION_CATEGORY_ID, i), 0);
            int transactionMoney = sharedPreferencesTransaction.getInt(String.format("%s_%d", SharedPrefConstant.TRANSACTION_MONEY, i), 0);
            if (transactionCategoryId >= 20) {
                //Lưu thông tin giao dịch vào mảng dựa theo id
                setRevenueItemValueColor(transactionCategoryId, transactionMoney);
            }
        }

        //Tạo list dữ liệu và màu cho chart
        ArrayList<PieEntry> revenue_entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            if (revenue_value[i] != 0) {
                //Thêm thông tin vào Arraylisst
                revenue_entries.add(new PieEntry(revenue_value[i],revenue_title[i]));
                colors.add(revenue_color[i]);
            }
        }

        //Tạo dữ liệu cho piechart
        PieDataSet dataSet = new PieDataSet(revenue_entries, "");
        dataSet.setSliceSpace(0f); //Khoảng cách giữa từng miếng
        dataSet.setSelectionShift(5f); // Kích cỡ tăng khi đc chọn
        dataSet.setValueTextSize(15f); // Kích cỡ chữ giá trị
        dataSet.setColors(colors); // Màu cho miếng
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE); //Đua phần mô tả giá trị ra ngoài

        PieData revenue_pie_data = new PieData(dataSet); //lưu giữ liệu

        pc_revenue.setData(revenue_pie_data); // truyền dữ liệu vào chart
        pc_revenue.setOnChartValueSelectedListener(new OnChartValueSelectedListener()
        {
            @Override
            public void onValueSelected(Entry e, Highlight h)
            {
                PieEntry pe = (PieEntry) e;
                //Hiện thông tin khi nhấn vào từng miếng qua toast
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
        //Kiểm tra nếu dữ liệu trống thì thông báo
        if(revenue_entries.isEmpty()) {
            pc_revenue.setCenterText("Dữ liệu trống");
        } else {
            pc_revenue.setCenterText("");
        }
    }

    private void setRevenuePieChartByDate (int year, int monthOfYear) throws ParseException {
        pc_revenue.setUsePercentValues(true);
        pc_revenue.getDescription().setEnabled(false);
        pc_revenue.setExtraOffsets(5, 10, 5, 5);

        pc_revenue.setDragDecelerationFrictionCoef(0.95f);

        pc_revenue.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        //Vòng tròn giữa biểu đồ
        pc_revenue.setDrawHoleEnabled(true);
        pc_revenue.setHoleColor(Color.WHITE);
        //Vòng tròn mờ giữa biểu đồ

        pc_revenue.setTransparentCircleColor(Color.WHITE);
        pc_revenue.setTransparentCircleAlpha(110);

        pc_revenue.setHoleRadius(40f);//kích thước hình tròn
        pc_revenue.setTransparentCircleRadius(61f); // kích thước hình tròn mờ

        pc_revenue.setRotationAngle(0);// góc nghiệng của biểu đồ

        pc_revenue.setRotationEnabled(false);// Vô hiệu hóa xoay
        pc_revenue.setHighlightPerTapEnabled(true);// Đạt phóng to khi chọn
        pc_revenue.animateXY(1000,2000);// Animation hiển thị

        pc_revenue.setDrawEntryLabels(false);// Vô hiệu hóa mô tả trong biểu đồ

        //Vô hiệu hóa phần chú thích
        Legend lg = pc_revenue.getLegend();
        lg.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        lg.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        lg.setOrientation(Legend.LegendOrientation.VERTICAL);
        lg.setDrawInside(false);
        lg.setEnabled(false);

        //Xóa mảng dữ liệu
        for (int i = 0; i < 6; i++) {
            revenue_value[i] = 0;
        }

        //Lấy thông tin người dùng đăng nhập
        SharedPreferences sharedPreferencesSigningIn = getSharedPreferences(SharedPrefConstant.SIGNING_IN, MODE_PRIVATE);
        String username = sharedPreferencesSigningIn.getString(SharedPrefConstant.SIGNING_IN_USERNAME, "");
        SharedPreferences sharedPreferencesTransaction = getSharedPreferences(username, MODE_PRIVATE);
        //Lấy số giao dịch
        int totalTransactions = sharedPreferencesTransaction.getInt(SharedPrefConstant.TRANSACTION_TOTAL, 0);
        for(int i = 1; i <= totalTransactions; ++i){
            //Lấy id và giá trị của giao dịch
            int transactionCategoryId = sharedPreferencesTransaction.getInt(String.format("%s_%d", SharedPrefConstant.TRANSACTION_CATEGORY_ID, i), 0);
            int transactionMoney = sharedPreferencesTransaction.getInt(String.format("%s_%d", SharedPrefConstant.TRANSACTION_MONEY, i), 0);
            //Lấy thời gian cùa giao dịch
            String transactionDate = sharedPreferencesTransaction.getString(String.format("%s_%d", SharedPrefConstant.TRANSACTION_DATE, i), null);
            Date date =new SimpleDateFormat("dd/MM/yyyy").parse(transactionDate);
            if (transactionCategoryId >= 20) {
                if (date.getMonth() == monthOfYear && date.getYear() == year) { //Kiểm gia thời gian giao dịch r lưu vào mảng
                    setRevenueItemValueColor(transactionCategoryId, transactionMoney);
                }
            }
        }

        //Tạo list dữ liệu và màu cho chart
        ArrayList<PieEntry> revenue_entries = new ArrayList<>();
        revenue_entries.clear(); //xóa dữ liệu cũ
        ArrayList<Integer> colors = new ArrayList<>();
        colors.clear();//xóa dữ liệu cũ
        for (int i = 0; i < 6; i++) {
            if (revenue_value[i] != 0) {
                //Thêm thông tin vào Arraylisst
                revenue_entries.add(new PieEntry(revenue_value[i],revenue_title[i]));
                colors.add(revenue_color[i]);
            }
        }
        //Tạo dữ liệu cho pechart
        PieDataSet dataSet = new PieDataSet(revenue_entries, "");
        dataSet.setSliceSpace(0f);//Khoảng cách giữa từng miếng
        dataSet.setSelectionShift(5f);// Kích cỡ tăng khi đc chọn
        dataSet.setValueTextSize(15f); // Kích cỡ chữ giá trị
        dataSet.setColors(colors);// Màu cho miếng
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);//Đua phần mô tả giá trị ra ngoài

        PieData revenue_pie_data = new PieData(dataSet);//lưu giữ liệu

        pc_revenue.setData(revenue_pie_data);// truyền dữ liệu vào chart
        pc_revenue.setOnChartValueSelectedListener(new OnChartValueSelectedListener()
        {
            @Override
            public void onValueSelected(Entry e, Highlight h)
            {
                PieEntry pe = (PieEntry) e;
                //Hiện thông tin khi nhấn vào từng miếng qua toast
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
        //Kiểm tra nếu dữ liệu trống thì thông báo
        if(revenue_entries.isEmpty()) {
            pc_revenue.setCenterText("Dữ liệu trống");
        } else {
            pc_revenue.setCenterText("");
        }
    }



    public void setExpenditurePieChart () {
        pc_expenditure.setUsePercentValues(true);
        pc_expenditure.getDescription().setEnabled(false);
        pc_expenditure.setExtraOffsets(5, 10, 5, 5);

        pc_expenditure.setDragDecelerationFrictionCoef(0.95f);

        pc_expenditure.setExtraOffsets(20.f, 0.f, 20.f, 0.f);
        //Vòng tròn giữa biểu đồ

        pc_expenditure.setDrawHoleEnabled(true);
        pc_expenditure.setHoleColor(Color.WHITE);
        //Vòng tròn mờ giữa biểu đồ

        pc_expenditure.setTransparentCircleColor(Color.WHITE);
        pc_expenditure.setTransparentCircleAlpha(110);

        pc_expenditure.setHoleRadius(40f);//kích thước hình tròn
        pc_expenditure.setTransparentCircleRadius(61f);// kích thước hình tròn mờ

        pc_expenditure.setRotationAngle(0);// góc nghiệng của biểu đồ

        pc_expenditure.setRotationEnabled(false);// Vô hiệu hóa xoay
        pc_expenditure.setHighlightPerTapEnabled(true);// Đạt phóng to khi chọn

        pc_expenditure.setDrawEntryLabels(false);// Vô hiệu hóa mô tả trong biểu đồ
        pc_expenditure.animateXY(1000,2000);// Animation hiển thị
        //Vô hiệu hóa phần chú thích

        Legend lg = pc_expenditure.getLegend();
        lg.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        lg.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        lg.setOrientation(Legend.LegendOrientation.VERTICAL);
        lg.setDrawInside(false);
        lg.setEnabled(false);

        //Lấy thông tin người dùng đăng nhập

        SharedPreferences sharedPreferencesSigningIn = getSharedPreferences(SharedPrefConstant.SIGNING_IN, MODE_PRIVATE);
        String username = sharedPreferencesSigningIn.getString(SharedPrefConstant.SIGNING_IN_USERNAME, "");
        SharedPreferences sharedPreferencesTransaction = getSharedPreferences(username, MODE_PRIVATE);
        //Lấy số giao dịch

        int totalTransactions = sharedPreferencesTransaction.getInt(SharedPrefConstant.TRANSACTION_TOTAL, 0);
        for(int i = 1; i <= totalTransactions; ++i){
            //Lấy id và giá trị của giao dịch

            int transactionCategoryId = sharedPreferencesTransaction.getInt(String.format("%s_%d", SharedPrefConstant.TRANSACTION_CATEGORY_ID, i), 0);
            int transactionMoney = sharedPreferencesTransaction.getInt(String.format("%s_%d", SharedPrefConstant.TRANSACTION_MONEY, i), 0);
            if (transactionCategoryId < 20 || transactionCategoryId == 26 || transactionCategoryId == 27) {
                //Lưu thông tin giao dịch vào mảng dựa theo id
                setRevenueItemValueColor(transactionCategoryId, transactionMoney);
            }
        }
        //Tạo list dữ liệu và màu cho chart

        ArrayList<PieEntry> expenditure_entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        for (int i = 0; i < 19; i++) {
            if (expenditure_value[i] != 0) {
                //Thêm thông tin vào Arraylisst
                expenditure_entries.add(new PieEntry(expenditure_value[i],expenditure_title[i]));
                colors.add(expenditure_color[i]);
            }
        }
        //Tạo dữ liệu cho piechart

        PieDataSet dataSet = new PieDataSet(expenditure_entries, "Election Results");
        dataSet.setSliceSpace(0f);//Khoảng cách giữa từng miếng
        dataSet.setSelectionShift(5f);// Kích cỡ tăng khi đc chọn
        dataSet.setValueTextSize(15f);// Kích cỡ chữ giá trị
        dataSet.setColors(colors);// Màu cho miếng
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);//Đua phần mô tả giá trị ra ngoài

        PieData revenue_pie_data = new PieData(dataSet);//lưu giữ liệu

        pc_expenditure.setData(revenue_pie_data);// truyền dữ liệu vào chart

        pc_expenditure.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry pe = (PieEntry) e;
                //Hiện thông tin khi nhấn vào từng miếng qua toast
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
        //Kiểm tra nếu dữ liệu trống thì thông báo

        if(expenditure_entries.isEmpty()) {
            pc_expenditure.setCenterText("Dữ liệu trống");
        } else {
            pc_expenditure.setCenterText("");
        }
    }

    public void setExpenditurePieChartByDate (int year, int monthOfYear) throws ParseException {
        pc_expenditure.setUsePercentValues(true);
        pc_expenditure.getDescription().setEnabled(false);
        pc_expenditure.setExtraOffsets(5, 10, 5, 5);

        pc_expenditure.setDragDecelerationFrictionCoef(0.95f);

        pc_expenditure.setExtraOffsets(20.f, 0.f, 20.f, 0.f);
        //Vòng tròn giữa biểu đồ

        pc_expenditure.setDrawHoleEnabled(true);
        pc_expenditure.setHoleColor(Color.WHITE);
        //Vòng tròn mờ giữa biểu đồ

        pc_expenditure.setTransparentCircleColor(Color.WHITE);
        pc_expenditure.setTransparentCircleAlpha(110);

        pc_expenditure.setHoleRadius(40f);//kích thước hình tròn
        pc_expenditure.setTransparentCircleRadius(61f);// kích thước hình tròn mờ

        pc_expenditure.setRotationAngle(0);// góc nghiệng của biểu đồ

        pc_expenditure.setRotationEnabled(false);// Vô hiệu hóa xoay
        pc_expenditure.setHighlightPerTapEnabled(true);// Đạt phóng to khi chọn

        pc_expenditure.setDrawEntryLabels(false);// Vô hiệu hóa mô tả trong biểu đồ
        pc_expenditure.animateXY(1000,2000);// Animation hiển thị

        //Vô hiệu hóa phần chú thích
        Legend lg = pc_expenditure.getLegend();
        lg.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        lg.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        lg.setOrientation(Legend.LegendOrientation.VERTICAL);
        lg.setDrawInside(false);
        lg.setEnabled(false);

        //Xóa mảng dữ liệu
        for (int i = 0; i < 19; i++) {
            expenditure_value[i] = 0;
        }
        //Lấy thông tin người dùng đăng nhập
        SharedPreferences sharedPreferencesSigningIn = getSharedPreferences(SharedPrefConstant.SIGNING_IN, MODE_PRIVATE);
        String username = sharedPreferencesSigningIn.getString(SharedPrefConstant.SIGNING_IN_USERNAME, "");
        SharedPreferences sharedPreferencesTransaction = getSharedPreferences(username, MODE_PRIVATE);
        //Lấy số giao dịch
        int totalTransactions = sharedPreferencesTransaction.getInt(SharedPrefConstant.TRANSACTION_TOTAL, 0);
        for(int i = 1; i <= totalTransactions; ++i){
            //Lấy thời gian cùa giao dịch
            String transactionDate = sharedPreferencesTransaction.getString(String.format("%s_%d", SharedPrefConstant.TRANSACTION_DATE, i), null);
            Date date =new SimpleDateFormat("dd/MM/yyyy").parse(transactionDate);
            //Lấy id và giá trị của giao dịch
            int transactionCategoryId = sharedPreferencesTransaction.getInt(String.format("%s_%d", SharedPrefConstant.TRANSACTION_CATEGORY_ID, i), 0);
            int transactionMoney = sharedPreferencesTransaction.getInt(String.format("%s_%d", SharedPrefConstant.TRANSACTION_MONEY, i), 0);
            if (transactionCategoryId < 20 || transactionCategoryId == 26 || transactionCategoryId == 27) {//Kiểm gia thời gian giao dịch r lưu vào mảng
                if (date.getMonth() == monthOfYear && date.getYear() == year) {
                    setRevenueItemValueColor(transactionCategoryId, transactionMoney);
                }
            }
        }
        //Tạo list dữ liệu và màu cho chart
        ArrayList<PieEntry> expenditure_entries = new ArrayList<>();
        expenditure_entries.clear();//xóa dữ liệu cũ
        ArrayList<Integer> colors = new ArrayList<>();
        colors.clear();//xóa dữ liệu cũ
        for (int i = 0; i < 19; i++) {
            if (expenditure_value[i] != 0) {
                //Thêm thông tin vào Arraylisst
                expenditure_entries.add(new PieEntry(expenditure_value[i],expenditure_title[i]));
                colors.add(expenditure_color[i]);
            }
        }
        //Tạo dữ liệu cho pechart

        PieDataSet dataSet = new PieDataSet(expenditure_entries, "Election Results");
        dataSet.setSliceSpace(0f);//Khoảng cách giữa từng miếng
        dataSet.setSelectionShift(5f);// Kích cỡ tăng khi đc chọn
        dataSet.setValueTextSize(15f);// Kích cỡ chữ giá trị
        dataSet.setColors(colors);// Màu cho miếng
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);//Đua phần mô tả giá trị ra ngoài

        PieData revenue_pie_data = new PieData(dataSet);//lưu giữ liệu

        pc_expenditure.setData(revenue_pie_data);// truyền dữ liệu vào chart

        pc_expenditure.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry pe = (PieEntry) e;
                //Hiện thông tin khi nhấn vào từng miếng qua toast

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
        //Kiểm tra nếu dữ liệu trống thì thông báo
        if(expenditure_entries.isEmpty()) {
            pc_expenditure.setCenterText("Dữ liệu trống");
        } else {
            pc_expenditure.setCenterText("");
        }
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
        displayUserInformation();
        getWalletMoney();
        getRevenueExpenditure();
        setDateLitener();
        setRevenuePieChart();
        setExpenditurePieChart();
    }

}
