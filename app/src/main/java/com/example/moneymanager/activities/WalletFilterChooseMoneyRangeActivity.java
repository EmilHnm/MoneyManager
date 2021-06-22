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

public class WalletFilterChooseMoneyRangeActivity extends AppCompatActivity {

    TextView tv_all, tv_more_than, tv_less_than, tv_in_range, tv_at;
    ImageButton ib_back;
    public static final int ID_SET_HIGHER_MONEY_DIALOG = 1;
    public static final int ID_SET_LOWER_MONEY_DIALOG = 2;
    public static final int ID_SET_IN_RANGE_MONEY_DIALOG = 3;
    public static final int ID_SET_MONEY_DIALOG = 4;


    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        Dialog dialog = null;
        switch(id) {
            case ID_SET_HIGHER_MONEY_DIALOG :
                dialog = createSetMoreThanMoneyDialog();
                break;
            case ID_SET_LOWER_MONEY_DIALOG :
                dialog = createSetLessThanMoneyDialog();
                break;
            case ID_SET_IN_RANGE_MONEY_DIALOG :
                dialog = createSetInRangeDialog();
                break;
            case ID_SET_MONEY_DIALOG :
                dialog = createSetAtMoneyDialog();
                break;
        }
        return dialog;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
    }

    public Dialog createSetMoreThanMoneyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_wallet_filter_choose_money, null);
        EditText et_money = view.findViewById(R.id.wallet_filter_choose_money_et_money);
        builder.setView(view)
                .setTitle("Nhập số tiền")
                .setNeutralButton("Nhập", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result",et_money.getText().toString());
                        setResult(1,returnIntent);
                        finish();
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
    public Dialog createSetLessThanMoneyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_wallet_filter_choose_money, null);
        EditText et_money = view.findViewById(R.id.wallet_filter_choose_money_et_money);
        builder.setView(view)
                .setTitle("Nhập số tiền")
                .setNeutralButton("Nhập", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result", et_money.getText().toString());
                        setResult(2,returnIntent);
                        finish();
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }

    public Dialog createSetInRangeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_wallet_filter_choose_money_range, null);
        EditText et_from_money = view.findViewById(R.id.wallet_filter_choose_money_range_et_from_money);
        EditText et_to_money = view.findViewById(R.id.wallet_filter_choose_money_range_et_to_money);
        builder.setView(view)
                .setTitle("Nhập số tiền")
                .setNeutralButton("Nhập", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("moreThan",et_from_money.getText().toString());
                        returnIntent.putExtra("lessThan",et_to_money.getText().toString());
                        setResult(3,returnIntent);
                        finish();
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }

    public Dialog createSetAtMoneyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_wallet_filter_choose_money, null);
        EditText et_money = view.findViewById(R.id.wallet_filter_choose_money_et_money);
        builder.setView(view)
                .setTitle("Nhập số tiền")
                .setNeutralButton("Nhập", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result",et_money.getText().toString());
                        setResult(4,returnIntent);
                        finish();
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }

    public void getView() {
        tv_all = findViewById(R.id.wallet_filter_choose_money_range_tv_all);
        tv_more_than = findViewById(R.id.wallet_filter_choose_money_range_tv_more_than);
        tv_less_than = findViewById(R.id.wallet_filter_choose_money_range_tv_less_than);
        tv_in_range = findViewById(R.id.wallet_filter_choose_money_range_tv_in_range);
        tv_at = findViewById(R.id.wallet_filter_choose_money_range_tv_at);
        ib_back = findViewById(R.id.wallet_filter_choose_money_range_ib_back);
    }

    public void setEventListener() {
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
                setResult(0,returnIntent);
                finish();
            }
        });
        tv_more_than.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(ID_SET_HIGHER_MONEY_DIALOG);
            }
        });
        tv_less_than.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(ID_SET_LOWER_MONEY_DIALOG);
            }
        });
        tv_in_range.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(ID_SET_IN_RANGE_MONEY_DIALOG);
            }
        });
        tv_at.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(ID_SET_MONEY_DIALOG);
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_filter_choose_money_range);
        getView();
        setEventListener();
    }
}
