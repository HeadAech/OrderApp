package com.example.orderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.slider.Slider;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner spinner;
    Spinner keyboardSpinner;
    Spinner mouseSpinner;
    Spinner headsetSpinner;

    CheckBox keyboardCheckbox;
    CheckBox mouseCheckbox;
    CheckBox headsetCheckbox;

    RelativeLayout keyboardAddonsLayout;
    Slider keyboardSlider;
    TextView keyboardSliderText;
    TextView keyboardNameView;
    TextView keyboardDescriptionView;
    ImageView keyboardImageView;
    TextView keyboardPriceView;

    RelativeLayout mouseAddonsLayout;
    Slider mouseSlider;
    TextView mouseSliderText;
    TextView mouseNameView;
    TextView mouseDescriptionView;
    ImageView mouseImageView;
    TextView mousePriceView;
    TextView orderTotalView;

    RelativeLayout headsetAddonsLayout;
    Slider headsetSlider;
    TextView headsetSliderText;
    TextView headsetnameView;
    TextView headsetDescriptionView;
    ImageView headsetImageView;
    TextView headsetPriceView;

    ArrayList<Integer> ids = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> imgs = new ArrayList<>();
    ArrayList<String> descriptions = new ArrayList<>();
    ArrayList<String> prices = new ArrayList<>();
    ArrayList<Integer> qtys = new ArrayList<>();
    ArrayList<Bitmap> imageBms = new ArrayList<>();

    ImageView orderImage;
    TextView pcName;
    TextView pcDescription;
    TextView inStockNum;
    TextView priceText;

    Slider pcSlider;
    TextView pcSliderText;

    HashMap<String, ArrayList<Accessory>> akcesoriaHashMap = new HashMap<>();

    DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.US);
    DecimalFormat decimalFormat = new DecimalFormat("#.##", decimalFormatSymbols);

    ArrayList<ArrayList<String>> accImg = new ArrayList<>();
    ArrayList<ArrayList<String>> accNames = new ArrayList<>();
    ArrayList<ArrayList<Bitmap>> accBms = new ArrayList<>();

    Button addToCartBtn;

    Zestaw pickedPcItem;
    Accessory pickedKeyboardItem;
    Accessory pickedMouseItem;
    Accessory pickedHeadsetItem;

    ArrayList<Zestaw> zestawy;

    SharedPreferences prefs;
    int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = this.getSharedPreferences("com.example.orderapp", Context.MODE_PRIVATE);
        userId = prefs.getInt("user_id", 0);

        keyboardAddonsLayout = findViewById(R.id.klawiaturaAddonsLayout);
        mouseAddonsLayout = findViewById(R.id.myszkaAddonsLayout);
        headsetAddonsLayout = findViewById(R.id.headsetAddonsLayout);

        //connection to local database
        DBHandler dbHandler = new DBHandler(MainActivity.this);

        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        zestawy = dbHandler.readAllZestawy();
        ArrayList<Accessory> akcesoria = dbHandler.readAllAkcesoria();

        pcSlider = findViewById(R.id.sliderPc);
        pcSliderText = findViewById(R.id.pcSliderPicked);

        pcSlider.setStepSize(1);
        pcSlider.setValueFrom(1);

        pcSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                Float val = slider.getValue();
                pcSliderText.setText(decimalFormat.format(val));
                updateOrderTotal();
            }
        });

        keyboardSlider = findViewById(R.id.keyboardSlider);
        keyboardSliderText = findViewById(R.id.keyboardSliderText);

        keyboardSlider.setStepSize(1);
        keyboardSlider.setValueFrom(1);

        keyboardSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                Float val = slider.getValue();
                keyboardSliderText.setText(decimalFormat.format(val));
                updateOrderTotal();
            }
        });

        mouseSlider = findViewById(R.id.mouseSlider);
        mouseSliderText = findViewById(R.id.mouseSliderText);

        mouseSlider.setStepSize(1);
        mouseSlider.setValueFrom(1);

        mouseSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                Float val = slider.getValue();
                mouseSliderText.setText(decimalFormat.format(val));
                updateOrderTotal();
            }
        });

        headsetSlider = findViewById(R.id.headsetSlider);
        headsetSliderText = findViewById(R.id.headsetSliderText);

        headsetSlider.setStepSize(1);
        headsetSlider.setValueFrom(1);

        headsetSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                Float val = slider.getValue();
                headsetSliderText.setText(decimalFormat.format(val));
                updateOrderTotal();
            }
        });

        keyboardCheckbox = findViewById(R.id.klawiatura_checkbox);
        mouseCheckbox = findViewById(R.id.myszka_checkbox);
        headsetCheckbox = findViewById(R.id.headset_checkbox);
        inStockNum = findViewById(R.id.inStockNumber);
        priceText = findViewById(R.id.priceText);

//        keyboardCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            Log.i("XXX", getResources().getResourceEntryName(buttonView.getId()) + isChecked);
//        });

        for(Zestaw zestaw : zestawy){
            ids.add(zestaw.getId());
            names.add(zestaw.getName());
            imgs.add(zestaw.getZdjecieBase64());
            descriptions.add(zestaw.getDescription());

            Float price = zestaw.getPrice();

            prices.add(decimalFormat.format(price));
            qtys.add(zestaw.getQty());
            imageBms.add(zestaw.getImageBM());
        }

        akcesoriaHashMap.put("klawiatura", new ArrayList<>());
        akcesoriaHashMap.put("myszka", new ArrayList<>());
        akcesoriaHashMap.put("headset", new ArrayList<>());


        for(Accessory accessory : akcesoria){
//            namesAccessory.add(accessory.getName());
//            descriptionsAccessory.add(accessory.getDescription());
//            imgsAccessory.add(accessory.getImageBase64());
//            pricesAccessory.add(accessory.getCena());
//            qtysAccessory.add(accessory.getQty());
//            imageBmsAccessory.add(accessory.getImageBm());
//            purposesAccessory.add(accessory.getPurpose());
            String purpose = accessory.getPurpose();
            Objects.requireNonNull(akcesoriaHashMap.get(purpose)).add(accessory);
        }

        MyAdapter myAdapter = new MyAdapter(getApplicationContext(), imgs, names, imageBms);
        spinner.setAdapter(myAdapter);

        orderImage = findViewById(R.id.orderImage);
        pcName = findViewById(R.id.configurationName);
        pcDescription = findViewById(R.id.configurationDescription);

        //accessories spinners
        keyboardSpinner = findViewById(R.id.spinnerKeyboard);
        keyboardSpinner.setOnItemSelectedListener(this);

        mouseSpinner = findViewById(R.id.spinnerMouse);
        mouseSpinner.setOnItemSelectedListener(this);

        headsetSpinner = findViewById(R.id.spinnerHeadset);
        headsetSpinner.setOnItemSelectedListener(this);

        ArrayList<String> imagesKeyboard = new ArrayList<>();
        ArrayList<String> namesKeyboard = new ArrayList<>();
        ArrayList<Bitmap> bitmapsKeyboard = new ArrayList<>();
        for(Accessory accessory : Objects.requireNonNull(akcesoriaHashMap.get("klawiatura"))){
            imagesKeyboard.add(accessory.getImageBase64());
            namesKeyboard.add(accessory.getName());
            bitmapsKeyboard.add(accessory.getImageBm());
        }
        accImg.add(imagesKeyboard);
        accNames.add(namesKeyboard);
        accBms.add(bitmapsKeyboard);
        MyAdapter keyboardAdapter = new MyAdapter(getApplicationContext(), accImg.get(0), accNames.get(0), accBms.get(0));
        keyboardSpinner.setAdapter(keyboardAdapter);

        ArrayList<String> imagesMouse = new ArrayList<>();
        ArrayList<String> namesMouse = new ArrayList<>();
        ArrayList<Bitmap> bitmapsMouse = new ArrayList<>();
        for(Accessory accessory: Objects.requireNonNull(akcesoriaHashMap.get("myszka"))){
            imagesMouse.add(accessory.getImageBase64());
            namesMouse.add(accessory.getName());
            bitmapsMouse.add(accessory.getImageBm());
        }
        accImg.add(imagesMouse);
        accNames.add(namesMouse);
        accBms.add(bitmapsMouse);
        MyAdapter mouseAdapter = new MyAdapter(getApplicationContext(), accImg.get(1), accNames.get(1), accBms.get(1));
        mouseSpinner.setAdapter(mouseAdapter);

        ArrayList<String> imagesHeadset = new ArrayList<>();
        ArrayList<String> namesHeadset = new ArrayList<>();
        ArrayList<Bitmap> bitmapsHeadset = new ArrayList<>();
        for(Accessory accessory : Objects.requireNonNull(akcesoriaHashMap.get("headset"))){
            imagesHeadset.add(accessory.getImageBase64());
            namesHeadset.add(accessory.getName());
            bitmapsHeadset.add(accessory.getImageBm());
        }
        accImg.add(imagesHeadset);
        accNames.add(namesHeadset);
        accBms.add(bitmapsHeadset);
        MyAdapter headsetAdapter = new MyAdapter(getApplicationContext(), accImg.get(2), accNames.get(2), accBms.get(2));
        headsetSpinner.setAdapter(headsetAdapter);

        //keyboard
        keyboardNameView = findViewById(R.id.keyboardName);
        keyboardDescriptionView = findViewById(R.id.keyboardDescription);
        keyboardImageView = findViewById(R.id.keyboardImage);
        keyboardPriceView = findViewById(R.id.keyboardPrice);

        //mouse
        mouseNameView = findViewById(R.id.mouseName);
        mouseDescriptionView = findViewById(R.id.mouseDescription);
        mouseImageView = findViewById(R.id.mouseImage);
        mousePriceView = findViewById(R.id.mousePrice);

        //headset
        headsetnameView = findViewById(R.id.headsetName);
        headsetDescriptionView = findViewById(R.id.headsetDescription);
        headsetImageView = findViewById(R.id.headsetImage);
        headsetPriceView = findViewById(R.id.headsetPrice);
        orderTotalView = findViewById(R.id.sumPrice);

        addToCartBtn = findViewById(R.id.addToCartBtn);

        int number = 0;
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            number = extras.getInt("number");
        }
//        Intent prevIntent = getIntent();
//        Cart cart = (Cart) prevIntent.getSerializableExtra("cart");

        Cart finalCart = new Cart();
        int finalNumber = number;
        addToCartBtn.setOnClickListener(v -> {
            boolean pickedKeyboard = keyboardCheckbox.isChecked();
            boolean pickedMouse = mouseCheckbox.isChecked();
            boolean pickedHeadset = headsetCheckbox.isChecked();

            ArrayList<Integer> pickedAccs = new ArrayList<>();
            int keyboardNum = 0;
            int mouseNum = 0;
            int headsetNum = 0;

            if(pickedKeyboard) {
                keyboardNum = Integer.parseInt((String) keyboardSliderText.getText());
                for (int i = 0; i < keyboardNum; i++){
                    pickedAccs.add(akcesoria.indexOf(pickedKeyboardItem));
                }
            }
            if(pickedMouse){
                mouseNum = Integer.parseInt((String) mouseSliderText.getText());
                for (int i = 0; i < mouseNum; i++){
                    pickedAccs.add(akcesoria.indexOf(pickedMouseItem));
                }
            }
            if(pickedHeadset){
                headsetNum = Integer.parseInt((String) headsetSliderText.getText());
                for (int i = 0; i < headsetNum; i++){
                    pickedAccs.add(akcesoria.indexOf(pickedHeadsetItem));
                }
            }

            ArrayList<Integer> pickedPc = new ArrayList<>();
            for(int i = 0; i < Integer.parseInt((String) pcSliderText.getText()); i++){
                pickedPc.add(zestawy.indexOf(pickedPcItem));
            }

            double totalPrice = Double.parseDouble((String) orderTotalView.getText());

            finalCart.addToCart(pickedPc, pickedAccs, totalPrice, finalNumber);
            dbHandler.addCartToDb(finalCart, userId);
            Intent intent = new Intent();
//            intent.putExtra("cart", (Cart) finalCart);
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
    }

    @SuppressLint({"SetTextI18n", "ResourceType"})
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(getApplicationContext(), descriptions.get(position), Toast.LENGTH_SHORT).show();


        int parentId = parent.getId();
        String parentName = getResources().getResourceEntryName(parentId);
//        Log.i("XXX", parent.getSelectedItem().toString());
        switch (parentName){
            case "spinner":
                pcSlider.setValue(1);
                pcSliderText.setText(String.valueOf(1));
                orderImage.setImageBitmap(imageBms.get(position));
                pcName.setText(names.get(position));
                pcDescription.setText(descriptions.get(position));
                inStockNum.setText(String.valueOf(qtys.get(position)));
                priceText.setText(prices.get(position));
                pcSlider.setValueTo(qtys.get(position));
                pickedPcItem = zestawy.get(position);

                updateOrderTotal();
                break;
            case "spinnerKeyboard":
                ArrayList<Accessory> ks = akcesoriaHashMap.get("klawiatura");
                keyboardSlider.setValue(1);
                keyboardSliderText.setText(String.valueOf(1));
                keyboardSlider.setValueTo(ks.get(position).getQty());
                keyboardNameView.setText(ks.get(position).getName());
                keyboardDescriptionView.setText(ks.get(position).getDescription());
                keyboardImageView.setImageBitmap(ks.get(position).getImageBm());
                keyboardPriceView.setText(decimalFormat.format(ks.get(position).getCena()));
                pickedKeyboardItem = ks.get(position);
                updateOrderTotal();
                break;
            case "spinnerMouse":
                ArrayList<Accessory> ms = akcesoriaHashMap.get("myszka");
                mouseSlider.setValue(1);
                mouseSliderText.setText(String.valueOf(1));
                mouseSlider.setValueTo(ms.get(position).getQty());
                mouseNameView.setText(ms.get(position).getName());
                mouseDescriptionView.setText(ms.get(position).getDescription());
                mouseImageView.setImageBitmap(ms.get(position).getImageBm());
                mousePriceView.setText(decimalFormat.format(ms.get(position).getCena()));
                pickedMouseItem = ms.get(position);
                updateOrderTotal();
                break;
            case "spinnerHeadset":
                ArrayList<Accessory> hs = akcesoriaHashMap.get("headset");
                headsetSlider.setValue(1);
                headsetSliderText.setText(String.valueOf(1));
                headsetSlider.setValueTo(hs.get(position).getQty());
                headsetnameView.setText(hs.get(position).getName());
                headsetDescriptionView.setText(hs.get(position).getDescription());
                headsetImageView.setImageBitmap(hs.get(position).getImageBm());
                headsetPriceView.setText(decimalFormat.format(hs.get(position).getCena()));
                pickedHeadsetItem = hs.get(position);
                updateOrderTotal();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @SuppressLint("NonConstantResourceId")
    public void onCheckboxClicked(View checkbox){
        boolean checked = ((CheckBox) checkbox).isChecked();

        switch(checkbox.getId()){
            case R.id.klawiatura_checkbox:
                if(!checked){
                    keyboardAddonsLayout.setVisibility(View.GONE);
                    keyboardSlider.setValue(1);
                    keyboardSliderText.setText(String.valueOf(0));
                    break;
                }
                keyboardAddonsLayout.setVisibility(View.VISIBLE);
                keyboardSliderText.setText(String.valueOf(1));
                break;
            case R.id.myszka_checkbox:
                if(!checked){
                    mouseAddonsLayout.setVisibility(View.GONE);
                    mouseSlider.setValue(1);
                    mouseSliderText.setText(String.valueOf(0));
                    break;
                }
                mouseAddonsLayout.setVisibility(View.VISIBLE);
                mouseSliderText.setText(String.valueOf(1));
                break;
            case R.id.headset_checkbox:
                if(!checked){
                    headsetAddonsLayout.setVisibility(View.GONE);
                    headsetSlider.setValue(1);
                    headsetSliderText.setText(String.valueOf(0));
                    break;
                }
                headsetAddonsLayout.setVisibility(View.VISIBLE);
                headsetSliderText.setText(String.valueOf(1));
                break;
        }
    }

    private void updateOrderTotal(){
        double orderTotal = 0;

        double pcPrice = Double.parseDouble((String) priceText.getText());

        double headsetQty = Integer.parseInt((String) headsetSliderText.getText());
        double keyboardQty = Integer.parseInt((String) keyboardSliderText.getText());
        double mouseQty = Integer.parseInt((String) mouseSliderText.getText());
        double pcQty = Integer.parseInt((String) pcSliderText.getText());

        if(headsetCheckbox.isChecked()){
            double headsetPrice = Double.parseDouble((String) headsetPriceView.getText());
            orderTotal += headsetPrice * headsetQty;
        }
        if(keyboardCheckbox.isChecked()){
            double keyboardPrice = Double.parseDouble((String) keyboardPriceView.getText());
            orderTotal += keyboardPrice * keyboardQty;
        }
        if(mouseCheckbox.isChecked()){
            double mousePrice = Double.parseDouble((String) mousePriceView.getText());
            orderTotal += mousePrice * mouseQty;
        }

        orderTotal += pcPrice * pcQty;

        orderTotalView.setText(decimalFormat.format(orderTotal));

    }

}