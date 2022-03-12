package com.example.orderapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> imgs;
    ArrayList<String> descriptions;
    ArrayList<Bitmap> imageBms;
    LayoutInflater layoutInflater;
    ImageView imageView;
    TextView textView;

    public MyAdapter(Context context, ArrayList<String> imgs, ArrayList<String> descriptions, ArrayList<Bitmap> imageBms){
        super();
        this.context = context;
        this.imgs = imgs;
        this.descriptions = descriptions;
        this.imageBms = imageBms;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return imgs.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public TextView getTextView() {
        return textView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.my_spinner_items, null);
        imageView = convertView.findViewById(R.id.imageView);
        textView = convertView.findViewById(R.id.textView);
//
//        byte[] imageBytes = Base64.decode(imgs.get(position), Base64.DEFAULT);
//        Bitmap bm = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        imageView.setImageBitmap(imageBms.get(position));
//        imageView.setImageResource(R.drawable.zestaw_1);
        textView.setText(descriptions.get(position));

        return convertView;
    }


}
