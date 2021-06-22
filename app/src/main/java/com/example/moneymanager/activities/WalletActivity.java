package com.example.moneymanager.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.moneymanager.R;
import com.example.moneymanager.adapter.TransactionAdapter;
import com.example.moneymanager.constant.SharedPrefConstant;
import com.example.moneymanager.methods.SharedMethods;
import com.example.moneymanager.models.Transaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class WalletActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    public static int TRANSACTION_ADD_CODE = 123;
    public static String TRANSACTION_ADD_EXTRA_NAME = "transaction";
    BottomNavigationView bnv_menu;
    FloatingActionButton fab_add_transaction;
    TextView tv_wallet_name, tv_wallet_money;
    ImageView iv_calendar, iv_filter;
    ListView lv_transaction;
    ArrayList<Transaction> transactions = new ArrayList<>();
    TransactionAdapter transactionAdapter;

    private void getViews(){
        bnv_menu = findViewById(R.id.wallet_bnv_menu);
        fab_add_transaction = findViewById(R.id.wallet_fab_add_transaction);
        tv_wallet_name = findViewById(R.id.wallet_tv_wallet_name);
        iv_calendar = findViewById(R.id.wallet_iv_calendar);
        iv_filter = findViewById(R.id.wallet_iv_filter);
        lv_transaction = findViewById(R.id.wallet_lv_transaction);
        tv_wallet_money = findViewById(R.id.wallet_tv_money);
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

    private void displayTransactionListView(){
        //Lấy file người dùng đang đăng nhập
        SharedPreferences sharedPreferenceSigningIn = getSharedPreferences(SharedPrefConstant.SIGNING_IN, MODE_PRIVATE);
        String username = sharedPreferenceSigningIn.getString(SharedPrefConstant.SIGNING_IN_USERNAME, "");
        //Lấy file lưu thông tin giao dịch
        SharedPreferences sharedPreferencesTransaction = getSharedPreferences(username, MODE_PRIVATE);
        int totalTransactions = sharedPreferencesTransaction.getInt(SharedPrefConstant.TRANSACTION_TOTAL, 0);

        for(int i=1; i<= totalTransactions; ++i){
            int transactionId = sharedPreferencesTransaction.getInt(String.format("%s_%d", SharedPrefConstant.TRANSACTION_ID, i), 0);
            int categoryId = sharedPreferencesTransaction.getInt(String.format("%s_%d", SharedPrefConstant.TRANSACTION_CATEGORY_ID, i), 0);
            int money = sharedPreferencesTransaction.getInt(String.format("%s_%d", SharedPrefConstant.TRANSACTION_MONEY, i), 0);
            String date = sharedPreferencesTransaction.getString(String.format("%s_%d", SharedPrefConstant.TRANSACTION_DATE, i), "");
            String note = sharedPreferencesTransaction.getString(String.format("%s_%d", SharedPrefConstant.TRANSACTION_NOTE, i), "");

            Transaction transaction = new Transaction(transactionId, categoryId, money, date, note);
            transactions.add(transaction);
        }

        transactionAdapter = new TransactionAdapter(this, R.layout.item_transaction, transactions);
        lv_transaction.setAdapter(transactionAdapter);
    }

    private void setEventListener(){
        //Hàm này được gọi mỗi khi có item trên menu được ấn
        bnv_menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_statistic:
                        Intent statisticIntent = new Intent(WalletActivity.this, StatisticActivity.class);
                        startActivity(statisticIntent);
                        overridePendingTransition(0,0);
                        finish();
                        break;
                    case R.id.item_plan:
                        Intent planIntent = new Intent(WalletActivity.this, PlanActivity.class);
                        startActivity(planIntent);
                        overridePendingTransition(0,0);
                        finish();
                        break;
                    case R.id.item_account:
                        Intent accountIntent = new Intent(WalletActivity.this, AccountActivity.class);
                        startActivity(accountIntent);
                        overridePendingTransition(0,0);
                        finish();
                        break;
                }
                return false;
            }
        });

        iv_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        iv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent walletFilterIntent = new Intent(WalletActivity.this, WalletFilterActivity.class);
                startActivityForResult(walletFilterIntent, 1);
                overridePendingTransition(0,0);
            }
        });

        fab_add_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addTransactionIntent = new Intent(WalletActivity.this, AddTransactionActivity.class);
                startActivityForResult(addTransactionIntent, TRANSACTION_ADD_CODE);
                overridePendingTransition(0,0);
            }
        });
    }

    public void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();

        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, currentYear, currentMonth, currentDayOfMonth);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String selectedDate = SharedMethods.formatDate(year, month, dayOfMonth);
        SharedPreferences sharedPreferencesSigningIn = getSharedPreferences(SharedPrefConstant.SIGNING_IN, MODE_PRIVATE);
        String username = sharedPreferencesSigningIn.getString(SharedPrefConstant.SIGNING_IN_USERNAME, "");
        SharedPreferences sharedPreferencesTransaction = getSharedPreferences(username, MODE_PRIVATE);
        int totalTransactions = sharedPreferencesTransaction.getInt(SharedPrefConstant.TRANSACTION_TOTAL, 0);
        //Xoá hết item trong list hiện tại để hiện list theo ngày
        transactions.clear();

        for(int i = 1; i <= totalTransactions; ++i){
            String transactionDate = sharedPreferencesTransaction.getString(String.format("%s_%d", SharedPrefConstant.TRANSACTION_DATE, i), "");
            if(selectedDate.equals(transactionDate)){
                int transactionId = sharedPreferencesTransaction.getInt(String.format("%s_%d", SharedPrefConstant.TRANSACTION_ID, i), 0);
                int transactionCategoryId = sharedPreferencesTransaction.getInt(String.format("%s_%d", SharedPrefConstant.TRANSACTION_CATEGORY_ID, i), 0);
                int transactionMoney = sharedPreferencesTransaction.getInt(String.format("%s_%d", SharedPrefConstant.TRANSACTION_MONEY, i), 0);
                String transactionNote = sharedPreferencesTransaction.getString(String.format("%s_%d", SharedPrefConstant.TRANSACTION_NOTE, i), "");

                transactions.add(new Transaction(transactionId, transactionCategoryId, transactionMoney, transactionDate, transactionNote));
            }
        }
        transactionAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TRANSACTION_ADD_CODE && resultCode == RESULT_OK){
            Transaction transaction = (Transaction) data.getSerializableExtra(TRANSACTION_ADD_EXTRA_NAME);
            transactions.add(transaction);
            //Đã khai báo adapter ở displayTransactionListView()
            transactionAdapter.notifyDataSetChanged(); //Update listview
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        getViews();
        displayUserInformation();
        displayTransactionListView();
        //Không đổi chỗ 2 dòng dưới cho nhau
        //Hàm setSelected item hoạt động như đang ấn vào menu đó => trigger event onNavigationItemSelected => bug
        //=> set item được chọn trước khi set event
        SharedMethods.setNavigationMenu(bnv_menu, R.id.item_wallet);
        setEventListener();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //Update lại thông tin người dùng mỗi khi restart
        displayUserInformation();
    }
}
