package com.example.moneymanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moneymanager.R;



public class WalletFilterActivity extends AppCompatActivity{
    private static final int MONEY_REQUEST_CODE = 1;
    private static final int TIME_REQUEST_CODE = 2;

    public static final String ALL_CATEGORY = "Tất cả";
    public static final String INCOME_CATEGORY = "Khoản thu";
    public static final String EXPENSE_CATEGORY = "Khoản chi";

    //Key extra trả về WalletActivity
    public static final String EXACT_MONEY_EXTRA_KEY = "exactMoney";
    public static final String LESS_THAN_MONEY_EXTRA_KEY = "lessThanMoney";
    public static final String MORE_THAN_MONEY_EXTRA_KEY = "moreThanMoney";

    public static final String EXACT_TIME_EXTRA_KEY = "exactTime";
    public static final String AFTER_TIME_EXTRA_KEY = "afterTime";
    public static final String BEFORE_TIME_EXTRA_KEY = "beforeTime";

    public static final String NOTE_EXTRA_KEY = "note";

    public static final String CATEGORY_EXTRA_KEY = "category";

    public static final String WITH_EXTRA_KEY = "with";


    ImageButton ib_back;
    String[] rangeCategory = {"Tất cả", "Khoản chi", "Khoản thu"};

    Spinner sp_category;
    TextView tv_range_money, tv_range_time, tv_search;
    EditText et_note, et_with;


    String category = "",
            exact_time = "", before_time = "", after_time = "",
            exact_money = "", more_than_money = "", less_than_money = "",
            note = "", with = "";



    private void getViews() {
        tv_range_money = findViewById(R.id.wallet_filter_tv_money_range);
        tv_range_time = findViewById(R.id.wallet_filter_tv_range_time);
        sp_category = findViewById(R.id.wallet_filter_sp_category);
        ib_back = findViewById(R.id.add_transaction_category_ib_back);
        tv_search = findViewById(R.id.wallet_filter_tv_search);
        et_note = findViewById(R.id.wallet_filter_et_note);
        et_with = findViewById(R.id.wallet_filter_et_with);
    }

    private void setEventListener(){
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0,0);
            }
        });
        tv_range_money.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WalletFilterActivity.this, WalletFilterMoneyActivity.class);
                startActivityForResult(intent, MONEY_REQUEST_CODE);
            }
        });
        tv_range_time.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WalletFilterActivity.this, WalletFilterTimeActivity.class);
                startActivityForResult(intent, TIME_REQUEST_CODE);
            }
        });
        tv_search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Gửi dữ liệu filter về WalletActivity
                Intent intentBackWallet = new Intent(WalletFilterActivity.this, WalletActivity.class);
                note = et_note.getText().toString();
                with = et_with.getText().toString();

                intentBackWallet.putExtra(EXACT_TIME_EXTRA_KEY, exact_time);
                intentBackWallet.putExtra(BEFORE_TIME_EXTRA_KEY, before_time);
                intentBackWallet.putExtra(AFTER_TIME_EXTRA_KEY, after_time);
                intentBackWallet.putExtra(EXACT_MONEY_EXTRA_KEY, exact_money);
                intentBackWallet.putExtra(MORE_THAN_MONEY_EXTRA_KEY, more_than_money);
                intentBackWallet.putExtra(LESS_THAN_MONEY_EXTRA_KEY, less_than_money);
                intentBackWallet.putExtra(NOTE_EXTRA_KEY, note);
                intentBackWallet.putExtra(CATEGORY_EXTRA_KEY, category);
                intentBackWallet.putExtra(WITH_EXTRA_KEY, with);
                setResult(RESULT_OK, intentBackWallet);
                finish();
                overridePendingTransition(0,0);

            }
        });
    }

    //Hàm xử lý dữ kiệu trả về từ trang WalletFilterMoneyActivity
    public void getActivityResultMoney(int resultCode, Intent data) {
        switch (resultCode) {
            case WalletFilterMoneyActivity.ALL_MONEY_RESULT_CODE:
                more_than_money = "";
                exact_money = "";
                less_than_money = "";
                tv_range_money.setText("Tất cả");
                break;
            case WalletFilterMoneyActivity.MORE_THAN_MONEY_RESULT_CODE:
                less_than_money = "";
                exact_money = "";
                more_than_money = data.getStringExtra(WalletFilterMoneyActivity.MORE_THAN_MONEY_EXTRA_KEY);
                tv_range_money.setText("Lớn hơn " + more_than_money + "đ");
                break;
            case WalletFilterMoneyActivity.LESS_THAN_MONEY_RESULT_CODE:
                more_than_money = "";
                exact_money = "";
                less_than_money = data.getStringExtra(WalletFilterMoneyActivity.LESS_THAN_MONEY_EXTRA_KEY);
                tv_range_money.setText("Nhỏ hơn " + less_than_money + "đ");
                break;
            case WalletFilterMoneyActivity.IN_RANGE_MONEY_RESULT_CODE:
                exact_money = "";
                more_than_money = data.getStringExtra(WalletFilterMoneyActivity.MORE_THAN_MONEY_EXTRA_KEY);
                less_than_money = data.getStringExtra(WalletFilterMoneyActivity.LESS_THAN_MONEY_EXTRA_KEY);
                tv_range_money.setText("Từ " + more_than_money + "đ đến " + less_than_money + "đ");
                break;
            case WalletFilterMoneyActivity.EXACT_MONEY_RESULT_CODE:
                less_than_money = "";
                more_than_money = "";
                exact_money = data.getStringExtra(WalletFilterMoneyActivity.EXACT_MONEY_EXTRA_KEY);
                tv_range_money.setText(exact_money + "đ");
                break;
        }
    }

    //Hàm xử lý dữ liệu trả về từ trang WalletFilterTimeActivity
    public void getActivityResultTime(int resultCode, Intent data) {
        switch (resultCode) {
            case WalletFilterTimeActivity.ALL_TIME_RESULT_CODE:
                exact_time = "";
                before_time = "";
                after_time = "";
                tv_range_time.setText("Tất cả");
                break;
            case WalletFilterTimeActivity.AFTER_TIME_RESULT_CODE:
                exact_time = "";
                before_time = "";
                after_time = data.getStringExtra(WalletFilterTimeActivity.AFTER_TIME_EXTRA_KEY);
                tv_range_time.setText("Sau " + after_time);
                break;
            case WalletFilterTimeActivity.BEFORE_TIME_RESULT_CODE:
                after_time = "";
                exact_time = "";
                before_time = data.getStringExtra(WalletFilterTimeActivity.BEFORE_TIME_EXTRA_KEY);
                tv_range_time.setText("Trước " + before_time);
                break;
            case WalletFilterTimeActivity.IN_RANGE_TIME_RESULT_CODE:
                exact_time = "";
                before_time = data.getStringExtra(WalletFilterTimeActivity.BEFORE_TIME_EXTRA_KEY);
                after_time = data.getStringExtra(WalletFilterTimeActivity.AFTER_TIME_EXTRA_KEY);
                tv_range_time.setText("Từ " + after_time + " đến " + before_time);
                break;
            case WalletFilterTimeActivity.EXACT_TIME_RESULT_CODE:
                after_time = "";
                before_time = "";
                exact_time = data.getStringExtra(WalletFilterTimeActivity.EXACT_TIME_EXTRA_KEY);
                tv_range_time.setText(exact_time);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MONEY_REQUEST_CODE:
                getActivityResultMoney(resultCode,data);
                break;
            case TIME_REQUEST_CODE:
                getActivityResultTime(resultCode,data);
                break;
        }
    }

    public void getSpinner() {
        ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(WalletFilterActivity.this,
                R.layout.spinner_wallet_filter, rangeCategory);
        adapterCategory.setDropDownViewResource(R.layout.spinner_wallet_filter_dropdown);
        sp_category.setAdapter(adapterCategory);
        sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: category = ALL_CATEGORY;
                        break;
                    case 1: category = EXPENSE_CATEGORY;
                        break;
                    case 2: category = INCOME_CATEGORY;
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_filter);
        getViews();
        setEventListener();
        getSpinner();
    }
}
