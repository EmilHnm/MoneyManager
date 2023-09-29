package com.example.moneymanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.moneymanager.methods.SharedMethods;
import com.example.moneymanager.models.Transaction;
import com.example.moneymanager.R;

import java.util.ArrayList;

public class TransactionAdapter extends ArrayAdapter {
    private Context context;
    private int resource;
    private ArrayList<Transaction> transactions;

    public TransactionAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Transaction> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.transactions = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, parent, false);

        ImageView iv_category = convertView.findViewById(R.id.item_transaction_iv_category);
        TextView tv_category = convertView.findViewById(R.id.item_transaction_tv_category);
        TextView tv_note = convertView.findViewById(R.id.item_transaction_tv_note);
        TextView tv_money = convertView.findViewById(R.id.item_transaction_tv_money);
        TextView tv_date = convertView.findViewById(R.id.item_transaction_tv_date);

        Transaction transaction = transactions.get(position);
        int transactionCategoryId = transaction.getCategoryId();
        SharedMethods.setCategoryInformation(transactionCategoryId, iv_category, tv_category);
        tv_note.setText(transaction.getNote());
        tv_money.setText(String.valueOf(transaction.getMoney()));

        if (transactionCategoryId < 20 || transactionCategoryId == 26 || transactionCategoryId == 27) {
            tv_money.setTextColor(context.getResources().getColor(R.color.red_form_error));
            tv_money.setBackgroundTintList(context.getResources().getColorStateList(R.color.design_default_color_error));
        }
        else if (transactionCategoryId >= 20) {
            tv_money.setTextColor(context.getResources().getColor(R.color.green_main));
            tv_money.setBackgroundTintList(context.getResources().getColorStateList(R.color.green_main));
        }

        tv_date.setText(transaction.getDate());

        return convertView;
    }
}
