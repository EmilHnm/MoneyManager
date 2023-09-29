package com.example.moneymanager.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.moneymanager.R;
import com.example.moneymanager.constant.SharedPrefConstant;
import com.example.moneymanager.methods.SharedMethods;
import com.example.moneymanager.models.Transaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTransactionActivity extends AppCompatActivity{
    private static int ADD_CATEGORY_REQUEST_CODE = 2000;
    public static String ADD_CATEGORY_ID_EXTRA_NAME = "categoryId";
    public static String TRANSACTION_ADD_EXTRA_NAME = "transaction";

    ImageButton ib_back;
    TextView tv_category, tv_date, tv_wallet, tv_appointment, tv_add_transaction;
    EditText et_money, et_note, et_person, et_event, et_with;
    ImageView iv_category;
    DatePickerDialog.OnDateSetListener tv_add_date_listener, tv_add_appointment_listener;
    //-1 = chưa chọn nhóm
    int transactionCategoryId = -1;

    private void getViews(){
        ib_back = findViewById(R.id.add_transaction_ib_back);
        tv_category = findViewById(R.id.add_transaction_tv_category);
        tv_date = findViewById(R.id.add_transaction_tv_date);
        tv_wallet = findViewById(R.id.add_transaction_tv_wallet);
        tv_appointment = findViewById(R.id.add_transaction_tv_appointment);
        et_money = findViewById(R.id.add_transaction_et_money);
        et_note = findViewById(R.id.add_transaction_et_note);
        et_with = findViewById(R.id.add_transaction_et_with);
        et_event = findViewById(R.id.add_transaction_et_event);
        iv_category = findViewById(R.id.add_transaction_iv_category);
        tv_add_transaction = findViewById(R.id.add_transaction_tv_add_transaction);
    }

    //Đăng ký bộ lắng nghe khi ấn OK chọn ngày cho add date và add appointment
    //Phải đăng ký riêng do sử dụng 2 lịch khác nhau
    private void registerDateListener(){
        tv_add_date_listener = new DatePickerDialog.OnDateSetListener() {
            //Hàm này chạy khi ấn OK, trả lại year, month, dayOfMonth user chọn
            //Gọi lại hàm displayDateToTextView() để hiển thị ngày lên cho user
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tv_date.setText(SharedMethods.formatDate(year, month, dayOfMonth));
                tv_date.setTextColor(getResources().getColor(R.color.black));
            }
        };

//        tv_add_appointment_listener = new DatePickerDialog.OnDateSetListener() {
//            //Hàm này chạy khi ấn OK, trả lại year, month, dayOfMonth user chọn
//            //Gọi hàm displayDateToTextView() để hiển thị ngày lên cho user
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                tv_appointment.setText(SharedMethods.formatDate(year, month, dayOfMonth));
//            }
//        };
    }

    private void displayTodayDate(){
        Calendar calendar = Calendar.getInstance();
        int currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        tv_date.setText(SharedMethods.formatDate(currentYear, currentMonth, currentDayOfMonth));
    }

    private void setEventListener(){
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0,0);
            }
        });
        tv_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addTransactionCategoryIntent = new Intent(AddTransactionActivity.this, AddTransactionCategoryActivity.class);
                startActivityForResult(addTransactionCategoryIntent, ADD_CATEGORY_REQUEST_CODE);
                overridePendingTransition(0,0);
            }
        });
        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Gọi hàm createDatePickerDialog(context, listener) để tạo lịch
                createDatePickerDialog(AddTransactionActivity.this, tv_add_date_listener);
            }
        });
//        tv_appointment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Gọi hàm createDatePickerDialog(context, listener) để tạo lịch
//                createDatePickerDialog(AddTransactionActivity.this, tv_add_appointment_listener);
//            }
//        });

        tv_add_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validInformation = true;
                //Check ngày k đc quá ngày hiện tại
                String transactionDate = tv_date.getText().toString();
                Date dTransactionDate = null;
                Date dCurrentDate = new Date();

                try {
                    dTransactionDate = new SimpleDateFormat("dd/MM/yyyy").parse(transactionDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(dTransactionDate.after(dCurrentDate)){
                    tv_date.setText("Ngày nhập không quá ngày hiện tại");
                    tv_date.setTextColor(getResources().getColor(R.color.red_form_error));
                    validInformation = false;
                }

                //Check chưa chọn category
                if(transactionCategoryId == -1){
                    tv_category.setText("Hãy chọn nhóm");
                    tv_category.setTextColor(getResources().getColor(R.color.red_form_error));
                    validInformation = false;
                }


                if(validInformation){
                    //Lấy người dùng đang đăng nhập
                    SharedPreferences sharedPreferencesSigningIn = getSharedPreferences(SharedPrefConstant.SIGNING_IN, MODE_PRIVATE);
                    String username = sharedPreferencesSigningIn.getString(SharedPrefConstant.SIGNING_IN_USERNAME, "");

                    //Lấy file dữ liệu wallet người dùng (Tên file là tên username)
                    SharedPreferences sharedPreferencesTransaction = getSharedPreferences(username, MODE_PRIVATE);
                    SharedPreferences.Editor transactionEditor = sharedPreferencesTransaction.edit();

                    int totalTransactions = sharedPreferencesTransaction.getInt(SharedPrefConstant.TRANSACTION_TOTAL, 0);
                    int nextTransactionId = totalTransactions + 1;
                    int walletMoney = sharedPreferencesTransaction.getInt(SharedPrefConstant.WALLET_MONEY, 0);
                    int transactionMoney = Integer.parseInt(et_money.getText().toString());
                    String transactionNote = et_note.getText().toString();
                    String transactionWith = et_with.getText().toString();

                    //Cập nhật tiền trong ví
                    //Check giao dịch trừ hay cộng tiền, có thể check id hoặc theo màu ô editText
                    if(et_money.getCurrentTextColor() == getResources().getColor(R.color.red_form_error))
                        walletMoney -= transactionMoney;
                    else
                        walletMoney += transactionMoney;


                    //Lưu vào file
                    transactionEditor.putInt(SharedPrefConstant.TRANSACTION_TOTAL, nextTransactionId);
                    transactionEditor.putInt(String.format("%s_%d", SharedPrefConstant.TRANSACTION_ID, nextTransactionId), nextTransactionId);
                    transactionEditor.putInt(String.format("%s_%d", SharedPrefConstant.TRANSACTION_CATEGORY_ID, nextTransactionId), transactionCategoryId);
                    transactionEditor.putInt(String.format("%s_%d", SharedPrefConstant.TRANSACTION_MONEY, nextTransactionId), transactionMoney);
                    transactionEditor.putString(String.format("%s_%d", SharedPrefConstant.TRANSACTION_DATE, nextTransactionId), transactionDate);
                    transactionEditor.putString(String.format("%s_%d", SharedPrefConstant.TRANSACTION_NOTE, nextTransactionId), transactionNote);
                    transactionEditor.putString(String.format("%s_%d", SharedPrefConstant.TRANSACTION_WITH, nextTransactionId), transactionWith);
                    transactionEditor.putInt(SharedPrefConstant.WALLET_MONEY, walletMoney);
                    transactionEditor.apply();
                    //Tạo object transaction mới
                    Transaction transaction = new Transaction(nextTransactionId, transactionCategoryId, transactionMoney, transactionDate, transactionNote, transactionWith);
                    //Gửi đến trang wallet qua intent
                    Intent walletIntent = new Intent(AddTransactionActivity.this, WalletActivity.class);
                    walletIntent.putExtra(TRANSACTION_ADD_EXTRA_NAME, transaction);
                    setResult(RESULT_OK, walletIntent);
                    finish();
                }
            }
        });
    }


    //Tạo DialogDatePicker (cái lịch), nhận tham số là bộ lắng nghe cho từng lịch
    private void createDatePickerDialog(Context context, DatePickerDialog.OnDateSetListener listener){
        Calendar calendar = Calendar.getInstance();
        int currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, listener, currentYear, currentMonth, currentDayOfMonth);
        datePickerDialog.show();
    }

    private void setMoneyTextColor (int categoryItemId) {
        if (categoryItemId < 20 || categoryItemId == 26 || categoryItemId == 27) {
            et_money.setTextColor(getResources().getColor(R.color.red_form_error));
            et_money.setBackgroundTintList(getResources().getColorStateList(R.color.design_default_color_error));
        }
        else if (categoryItemId >= 20) {
            et_money.setTextColor(getResources().getColor(R.color.green_main));
            et_money.setBackgroundTintList(getResources().getColorStateList(R.color.green_main));
        }
    }

    //Hàm chạy khi finish activity đc gọi bằng startActivityForResult()
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_CATEGORY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                transactionCategoryId = data.getIntExtra(ADD_CATEGORY_ID_EXTRA_NAME, 0);

                SharedMethods.setCategoryInformation(transactionCategoryId, iv_category, tv_category);
                setMoneyTextColor(transactionCategoryId);
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        getViews();
        registerDateListener();
        displayTodayDate();
        setEventListener();
    }
}
