package com.example.orderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Date;

public class OrderHistoryActivity extends AppCompatActivity {

    ArrayList<Order> orders;
    ArrayAdapter<Order> arrayAdapter;
    ListView listView;

    TextView noOrdersText;

    SharedPreferences prefs;

    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        DBHandler dbHandler = new DBHandler(this);

        noOrdersText = findViewById(R.id.noOrdersText);
        prefs = this.getSharedPreferences("com.example.orderapp", Context.MODE_PRIVATE);
        userId = prefs.getInt("user_id", 0);

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        listView = findViewById(R.id.listViewOrderHistory);

        orders = dbHandler.readOrderHistory(userId);

        if(orders.size() == 0){
            noOrdersText.setVisibility(View.VISIBLE);
            return;
        }
        noOrdersText.setVisibility(View.GONE);

        ArrayList<String> strings = new ArrayList<>();
        for(Order order : orders){
            //            Log.i("YYY", order.toString());
            String s = "Zamówienie z dnia: " +
                    order.getDate().format(dateFormat);
            strings.add(s);
        }

        arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, strings);
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, OrderHistoryItemActivity.class);
            intent.putExtra("order", orders.get(position));
            startActivity(intent);
        });
    }

    //TODO cały layout na historię zamówień;
    //TODO rejestracja/ logowanie, preferencje
    //TODO powiadomienia email lub sms, zobaczymy czy wyjdzie
}