package com.example.orderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.view.ViewCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderHistoryItemActivity extends AppCompatActivity {

    ScrollView scrollView;
    LinearLayout ll;

    TextView orderTotalView;
    TextView orderDateView;
    TextView forCompanyText;
    TextView companyName;

    DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.US);
    DecimalFormat decimalFormat = new DecimalFormat("#.##", decimalFormatSymbols);

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_order_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.shareView:
                shareView();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_item);

        scrollView = findViewById(R.id.orderHistoryItemScrollView);
        ll = findViewById(R.id.orderHistoryItemLinearLayout);

        orderDateView = findViewById(R.id.orderHistoryItemDate);
        orderTotalView = findViewById(R.id.orderHistoryItemTotal);
        forCompanyText = findViewById(R.id.forCompanyText);
        companyName = findViewById(R.id.companyNameOrderHistory);

        DBHandler dbHandler = new DBHandler(this);
        ArrayList<Zestaw> zestawy = dbHandler.readAllZestawy();
        ArrayList<Accessory> accessories = dbHandler.readAllAkcesoria();
        
        Bundle extras = getIntent().getExtras();
        Order order = null;
        if(extras != null){
            order = (Order) extras.getSerializable("order");
        }
        Log.i("YYY", order.toString());
        ArrayList<Integer> pcIds = order.getPcIds();
        ArrayList<Integer> accIds = order.getAccIds();
        Double orderTotal = order.getPrice();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        orderTotalView.setText(decimalFormat.format(orderTotal));
        orderDateView.setText(order.getDate().format(dateFormat));

        if(order.getCompanyName() == null || order.getCompanyName().equals("")){
            forCompanyText.setVisibility(View.GONE);
            companyName.setVisibility(View.GONE);
        }else {
            forCompanyText.setVisibility(View.VISIBLE);
            companyName.setVisibility(View.VISIBLE);
            companyName.setText(order.getCompanyName());
        }

        ArrayList<Zestaw> orderZestawy = new ArrayList<>();
        ArrayList<Accessory> orderAkcesoria = new ArrayList<>();

        for(int i = 0; i < pcIds.size(); i++){
            orderZestawy.add(zestawy.get(pcIds.get(i)));
        }

        for(int i = 0; i < accIds.size(); i++){
            orderAkcesoria.add(accessories.get(accIds.get(i)));
        }

        HashMap<Zestaw, Integer> hashMapPc = new HashMap<>();
        orderZestawy.stream()
                .collect(Collectors.groupingBy(s -> s))
                .forEach((k, v) -> hashMapPc.put(k, v.size()));

        HashMap<Accessory, Integer> hashMapAcc = new HashMap<>();
        orderAkcesoria.stream()
                .collect(Collectors.groupingBy(s -> s))
                .forEach((k, v) -> hashMapAcc.put(k, v.size()));


        for(int i = 0; i < hashMapPc.size(); i++){
            Zestaw pc = (Zestaw) hashMapPc.keySet().toArray()[i];
            int qty = hashMapPc.get(pc);

            RelativeLayout rl = (RelativeLayout) getLayoutInflater().inflate(R.layout.order_history_item_layout, null);

            ImageView iv = new ImageView(this);
            TextView tv = new TextView(this);
            TextView qtyTV = new TextView(this);
            TextView descTV = new TextView(this);
            TextView priceTV = new TextView(this);
            TextView priceTotalTV = new TextView(this);

            for (int x = 0; x < rl.getChildCount(); x++) {
                if (rl.getChildAt(x) instanceof ImageView && rl.getChildAt(x).getId() == R.id.imageViewOrderHistoryItem)
                    iv = (ImageView) rl.getChildAt(x);
                if (rl.getChildAt(x) instanceof TextView && rl.getChildAt(x).getId() == R.id.textViewOrderHistoryItem)
                    tv = (TextView) rl.getChildAt(x);
                if (rl.getChildAt(x) instanceof TextView && rl.getChildAt(x).getId() == R.id.qtyOrderHistoryItem)
                    qtyTV = (TextView) rl.getChildAt(x);
                if (rl.getChildAt(x) instanceof TextView && rl.getChildAt(x).getId() == R.id.orderHistoryItemDescription)
                    descTV = (TextView) rl.getChildAt(x);
                if (rl.getChildAt(x) instanceof TextView && rl.getChildAt(x).getId() == R.id.itemOrderHistoryTotal)
                    priceTotalTV = (TextView) rl.getChildAt(x);
                if (rl.getChildAt(x) instanceof TextView && rl.getChildAt(x).getId() == R.id.priceOrderHistoryItem)
                    priceTV = (TextView) rl.getChildAt(x);
            }

            iv.setImageBitmap(pc.getImageBM());
            tv.setText(pc.getName());
            qtyTV.setText(String.valueOf(qty));
            descTV.setText(pc.getDescription());
            priceTV.setText(decimalFormat.format(pc.getPrice()));
            priceTotalTV.setText(String.valueOf(qty * pc.getPrice()));
            ll.addView(rl);
        }

        for(int i = 0; i < hashMapAcc.size(); i++){
            Accessory accessory = (Accessory) hashMapAcc.keySet().toArray()[i];
            int qty = hashMapAcc.get(accessory);

            RelativeLayout rl = (RelativeLayout) getLayoutInflater().inflate(R.layout.order_history_item_layout, null);

            ImageView iv = new ImageView(this);
            TextView tv = new TextView(this);
            TextView qtyTV = new TextView(this);
            TextView descTV = new TextView(this);
            TextView priceTV = new TextView(this);
            TextView priceTotalTV = new TextView(this);

            for (int x = 0; x < rl.getChildCount(); x++) {
                if (rl.getChildAt(x) instanceof ImageView && rl.getChildAt(x).getId() == R.id.imageViewOrderHistoryItem)
                    iv = (ImageView) rl.getChildAt(x);
                if (rl.getChildAt(x) instanceof TextView && rl.getChildAt(x).getId() == R.id.textViewOrderHistoryItem)
                    tv = (TextView) rl.getChildAt(x);
                if (rl.getChildAt(x) instanceof TextView && rl.getChildAt(x).getId() == R.id.qtyOrderHistoryItem)
                    qtyTV = (TextView) rl.getChildAt(x);
                if (rl.getChildAt(x) instanceof TextView && rl.getChildAt(x).getId() == R.id.orderHistoryItemDescription)
                    descTV = (TextView) rl.getChildAt(x);
                if (rl.getChildAt(x) instanceof TextView && rl.getChildAt(x).getId() == R.id.itemOrderHistoryTotal)
                    priceTotalTV = (TextView) rl.getChildAt(x);
                if (rl.getChildAt(x) instanceof TextView && rl.getChildAt(x).getId() == R.id.priceOrderHistoryItem)
                    priceTV = (TextView) rl.getChildAt(x);
            }

            iv.setImageBitmap(accessory.getImageBm());
            tv.setText(accessory.getName());
            qtyTV.setText(String.valueOf(qty));
            descTV.setText(accessory.getDescription());
            priceTV.setText(decimalFormat.format(accessory.getCena()));
            priceTotalTV.setText(String.valueOf(qty * accessory.getCena()));
            ll.addView(rl);
        }
    }

    private void shareView(){
        RelativeLayout rl = findViewById(R.id.orderHistoryItemView);
        View view = rl.getRootView();
        view.post(new Runnable() {
            @SuppressLint("SdCardPath")
            @Override
            public void run() {
                Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(returnedBitmap);
                Drawable bgDrawable = view.getBackground();
                if (bgDrawable != null)
                    bgDrawable.draw(canvas);
                else
                    canvas.drawColor(Color.WHITE);
                view.draw(canvas);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                returnedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "title");
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        values);

                OutputStream outstream;
                try {
                    outstream = getContentResolver().openOutputStream(uri);
                    returnedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                    outstream.close();
                } catch (Exception e) {
                    System.err.println(e.toString());
                }

                share.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(share, "Share Image"));
            }
        });

    }
}