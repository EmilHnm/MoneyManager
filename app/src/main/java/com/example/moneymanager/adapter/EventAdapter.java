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
import com.example.moneymanager.R;
import com.example.moneymanager.methods.SharedMethods;
import com.example.moneymanager.models.Event;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends ArrayAdapter {
    private Context context;
    private int resource;
    private ArrayList<Event> events;

    public EventAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Event> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.events = objects;
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

        Event event = events.get(position);

        int eventCategoryId = event.getCategoryId();

        SharedMethods.setCategoryInformation(eventCategoryId, iv_category, tv_category);

        tv_note.setText(event.getNote());

        tv_money.setText(String.valueOf(event.getMoney()));

        if (eventCategoryId < 20 || eventCategoryId == 26 || eventCategoryId == 27) {
            tv_money.setTextColor(context.getResources().getColor(R.color.red_form_error));
            tv_money.setBackgroundTintList(context.getResources().getColorStateList(R.color.design_default_color_error));
        }
        else if (eventCategoryId >= 20) {
            tv_money.setTextColor(context.getResources().getColor(R.color.green_main));
            tv_money.setBackgroundTintList(context.getResources().getColorStateList(R.color.green_main));
        }

        tv_date.setText(event.getDate());

        return convertView;
    }
}
