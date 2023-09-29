package com.example.moneymanager.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moneymanager.R;
import com.example.moneymanager.dialog.WalletFilterMoneyRangeDialog;
import com.example.moneymanager.dialog.WalletFilterTimeRangeDialog;
import org.w3c.dom.Text;

public class WalletFilterMoneyActivity extends AppCompatActivity {

    TextView tv_all, tv_more_than, tv_less_than, tv_in_range, tv_exact;
    ImageButton ib_back;

    //Mã dialog
    //Dialog WalletFilterMoneyRange phải check dữ liệu nên tạo riêng
    private static final int MORE_THAN_MONEY_DIALOG_ID = 1;
    private static final int LESS_THAN_MONEY_DIALOG_ID = 2;
//    private static final int IN_RANGE_MONEY_DIALOG_ID = 3;
    private static final int EXACT_MONEY_DIALOG_ID = 4;

    //Mã result trả về cho WalletFilterActivity
    public static final int ALL_MONEY_RESULT_CODE = 5;
    public static final int MORE_THAN_MONEY_RESULT_CODE = 6;
    public static final int LESS_THAN_MONEY_RESULT_CODE = 7;
    public static final int IN_RANGE_MONEY_RESULT_CODE = 8;
    public static final int EXACT_MONEY_RESULT_CODE = 9;

    //Key extra trả về cho WalletFilterActivity
    public static final String MORE_THAN_MONEY_EXTRA_KEY = "moreThan";
    public static final String LESS_THAN_MONEY_EXTRA_KEY = "lessThan";
    public static final String EXACT_MONEY_EXTRA_KEY = "exact";

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        Dialog dialog = null;
        switch(id) {
            case MORE_THAN_MONEY_DIALOG_ID :
                dialog = MoreThanMoneyDialog();
                break;
            case LESS_THAN_MONEY_DIALOG_ID :
                dialog = LessThanMoneyDialog();
                break;
//            case IN_RANGE_MONEY_DIALOG_ID :
//                dialog = InRangeMoneyDialog();
//                break;
            case EXACT_MONEY_DIALOG_ID :
                dialog = ExactMoneyDialog();
                break;
        }
        return dialog;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
    }

    private Dialog MoreThanMoneyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_wallet_filter_money, null);
        TextView tv_title = view.findViewById(R.id.wallet_filter_money_tv_title);
        EditText et_more_than = view.findViewById(R.id.wallet_filter_money_et_money);

        tv_title.setText("Lớn hơn");
        builder.setView(view)
                .setTitle("Nhập số tiền")
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setNeutralButton("Nhập", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra(MORE_THAN_MONEY_EXTRA_KEY, et_more_than.getText().toString());
                        setResult(MORE_THAN_MONEY_RESULT_CODE, returnIntent);
                        finish();
                    }
                });
        return builder.create();
    }
    private Dialog LessThanMoneyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_wallet_filter_money, null);
        TextView tv_title = view.findViewById(R.id.wallet_filter_money_tv_title);
        EditText et_less_than = view.findViewById(R.id.wallet_filter_money_et_money);

        tv_title.setText("Nhỏ hơn");
        builder.setView(view)
                .setTitle("Nhập số tiền")
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setNeutralButton("Nhập", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra(LESS_THAN_MONEY_EXTRA_KEY, et_less_than.getText().toString());
                        setResult(LESS_THAN_MONEY_RESULT_CODE, returnIntent);
                        finish();
                    }
                });
        return builder.create();
    }

//    private Dialog InRangeMoneyDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        View view = inflater.inflate(R.layout.dialog_wallet_filter_money_range, null);
//        EditText et_more_than = view.findViewById(R.id.wallet_filter_money_range_et_more_than);
//        EditText et_less_than = view.findViewById(R.id.wallet_filter_money_range_et_less_than);
//        builder.setView(view)
//                .setTitle("Nhập số tiền")
//                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                })
//                .setNeutralButton("Nhập", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent returnIntent = new Intent();
//                        returnIntent.putExtra(MORE_THAN_MONEY_EXTRA_KEY, et_more_than.getText().toString());
//                        returnIntent.putExtra(LESS_THAN_MONEY_EXTRA_KEY, et_less_than.getText().toString());
//                        setResult(IN_RANGE_MONEY_RESULT_CODE, returnIntent);
//                        finish();
//                    }
//                });
//        return builder.create();
//    }

    private Dialog ExactMoneyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_wallet_filter_money, null);
        TextView tv_title = view.findViewById(R.id.wallet_filter_money_tv_title);
        EditText et_money = view.findViewById(R.id.wallet_filter_money_et_money);

        tv_title.setText("Chính xác");
        builder.setView(view)
                .setTitle("Nhập số tiền")
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setNeutralButton("Nhập", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra(EXACT_MONEY_EXTRA_KEY, et_money.getText().toString());
                        setResult(EXACT_MONEY_RESULT_CODE, returnIntent);
                        finish();
                    }
                });
        return builder.create();
    }

    private void getViews() {
        tv_all = findViewById(R.id.wallet_filter_money_tv_all);
        tv_more_than = findViewById(R.id.wallet_filter_money_tv_more_than);
        tv_less_than = findViewById(R.id.wallet_filter_money_tv_less_than);
        tv_in_range = findViewById(R.id.wallet_filter_money_tv_in_range);
        tv_exact = findViewById(R.id.wallet_filter_money_tv_exact);
        ib_back = findViewById(R.id.wallet_filter_money_ib_back);
    }

    private void setEventListener() {
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(ALL_MONEY_RESULT_CODE, returnIntent);
                finish();
            }
        });

        tv_more_than.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(MORE_THAN_MONEY_DIALOG_ID);
            }
        });

        tv_less_than.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(LESS_THAN_MONEY_DIALOG_ID);
            }
        });

        tv_in_range.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WalletFilterMoneyRangeDialog walletFilterMoneyRangeDialog = new WalletFilterMoneyRangeDialog();
                walletFilterMoneyRangeDialog.show(getSupportFragmentManager(),null);
            }
        });

        tv_exact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(EXACT_MONEY_DIALOG_ID);
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_filter_money);
        getViews();
        setEventListener();
    }
}
