package com.example.orderapp;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.stream.Collectors;

public class CartActivity extends AppCompatActivity {

    private final static int MY_REQUEST_CODE = 1;

    Button openConfiguratorBtn;
    Cart cart;

    ListView listView;

    DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.US);
    DecimalFormat decimalFormat = new DecimalFormat("#.##", decimalFormatSymbols);

    HashMap<String, Object> hashMap;
    ArrayList<HashMap<String, Object>> list = new ArrayList<>();

    SharedPreferences prefs;

    ArrayList<ArrayList<Integer>> pcIds;
    ArrayList<ArrayList<Integer>> accIds;

    Button submitOrderBtn;

    ArrayList<Zestaw> zestawy;
    ArrayList<Accessory> akcesoria;

    ScrollView scrollView;
    LinearLayout linearLayout;

    TextView cartOrderTotalView;

    int number = 0;
    DBHandler dbHandler;
    TextView emptyCartView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        prefs = this.getSharedPreferences("com.example.orderapp", Context.MODE_PRIVATE);
        userId = prefs.getInt("user_id", 0);

//        listView = findViewById(R.id.listView);
        dbHandler = new DBHandler(getApplicationContext());
        scrollView = findViewById(R.id.cart_scroll_view);
        linearLayout = findViewById(R.id.cartLinearLayout);
        emptyCartView = findViewById(R.id.emptyCartMessage);
        cartOrderTotalView = findViewById(R.id.cartOrderTotal);

        submitOrderBtn = findViewById(R.id.submitOrderBtn);

        number = dbHandler.getLastNrZestawuNumber(userId);
//        cart = new Cart();

        openConfiguratorBtn = findViewById(R.id.open_configurator);
        openConfiguratorBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            number++;
            intent.putExtra("number", number);
            startActivityForResult(intent, MY_REQUEST_CODE);

        });

        submitOrderBtn.setOnClickListener(v -> {
            ArrayList<Integer> zamowieniePC = new ArrayList<>();
            for (int i = 0; i < pcIds.size(); i++) {
                zamowieniePC.addAll(pcIds.get(i));
            }
            ArrayList<Integer> zamowienieAccs = new ArrayList<>();
            for (int i = 0; i < accIds.size(); i++) {
                zamowienieAccs.addAll(accIds.get(i));
            }
            Double orderTotal = Double.parseDouble((String) cartOrderTotalView.getText());
            Log.i("YYY", "ZAMOWIONE PCS: " + zamowieniePC);
            Log.i("YYY", "ZAMOWIONE ACCS: " + zamowienieAccs);

            RelativeLayout rl = (RelativeLayout) getLayoutInflater().inflate(R.layout.order_dialog_layout, null);
            Button submitDialogBtn = new Button(this);
            EditText companyName = new EditText(this);
            for (int x = 0; x < rl.getChildCount(); x++) {
                if (rl.getChildAt(x) instanceof Button && rl.getChildAt(x).getId() == R.id.submitOrderBtnDialog)
                    submitDialogBtn = (Button) rl.getChildAt(x);
                if (rl.getChildAt(x) instanceof EditText && rl.getChildAt(x).getId() == R.id.companyNameEditText)
                    companyName = (EditText) rl.getChildAt(x);
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.submit_order_dialog);
            builder.setView(rl);

            AlertDialog dialog = builder.create();
            dialog.show();

            EditText finalCompanyName = companyName;
            submitDialogBtn.setOnClickListener(v1 -> {
//                Log.i("YYY", finalCompanyName.getText().toString());
                String companyNameString = finalCompanyName.getText().toString();
                dbHandler.submitOrder(zamowieniePC, zamowienieAccs, orderTotal, companyNameString, userId);
                dbHandler.clearCartTable(userId);
                updateCart();
                dialog.dismiss();

                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle(R.string.thank_you);
                builder1.setMessage(R.string.thank_you_for_ordering);
                builder1.show();
                Confirmation confirmation = new Confirmation(this);
                User user = dbHandler.getUserById(userId);
                if(prefs.getBoolean("sms_permission", false)){
                    confirmation.sendSMS(user.getPhone(), orderTotal);
                    Toast.makeText(this, "SENT", Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(this, getResources().getString(R.string.sms_disabled), Toast.LENGTH_LONG).show();
                }
            });
        });

        zestawy = dbHandler.readAllZestawy();
        akcesoria = dbHandler.readAllAkcesoria();

        updateCart();

    }

    @SuppressLint({"NonConstantResourceId", "CommitPrefEdits"})
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.oautorze:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.about_app_text);
                builder.setMessage(R.string.about_app_description);
                builder.show();
                return true;
            case R.id.zamowienia:
                Intent intent = new Intent(this, OrderHistoryActivity.class);
                startActivity(intent);
                return true;
            case R.id.logOut:
                prefs.edit().putBoolean("logged_in", false).apply();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("RtlHardcoded")
    private void updateCart() {
        Cart cart = dbHandler.readCartFromDb(userId);
        Double total = (double) 0;
        Log.i("XXX", cart.toString());
        linearLayout.removeAllViews();

        if (cart.getPcIdsLength() == 0) {
            emptyCartView.setVisibility(View.VISIBLE);
            submitOrderBtn.setEnabled(false);
        } else {
            emptyCartView.setVisibility(View.GONE);
            submitOrderBtn.setEnabled(true);
        }

        pcIds = cart.getPcIds();
        accIds = cart.getAccesoryIds();
        ArrayList<ArrayList<Zestaw>> zestaws = new ArrayList<>();
        ArrayList<ArrayList<Accessory>> accessories = new ArrayList<>();
        ArrayList<Integer> numeryZestawow = cart.getNrZestawuArr();

        for (int i = 0; i < pcIds.size(); i++) {
            ArrayList<Zestaw> tempZestawArr = new ArrayList<>();
            for (int y = 0; y < pcIds.get(i).size(); y++) {
                tempZestawArr.add(zestawy.get(pcIds.get(i).get(y)));
            }
            zestaws.add(tempZestawArr);
        }

        for (int i = 0; i < accIds.size(); i++) {
            ArrayList<Accessory> tempZestawArr = new ArrayList<>();
            for (int y = 0; y < accIds.get(i).size(); y++) {
                tempZestawArr.add(akcesoria.get(accIds.get(i).get(y)));
            }
            accessories.add(tempZestawArr);
        }

        for (int i = 0; i < zestaws.size(); i++) {
            Zestaw pc = zestaws.get(i).get(0);
            int qty = zestaws.get(i).size();
            RelativeLayout ll = (RelativeLayout) getLayoutInflater().inflate(R.layout.cart_item_layout, null);
//                LinearLayout lx = new LinearLayout(this);
//                lx.addView(ll);
//
//                for(int x = 0; x < ll.getChildCount(); x++){
//                    Log.i("XXX", String.valueOf(ll.getChildAt(x)));
//                }
            ImageView iv = new ImageView(this);
            TextView tv = new TextView(this);
            TextView descTV = new TextView(this);
            TextView pcPriceTV = new TextView(this);
            TextView pcQtyTV = new TextView(this);
            Button removeItemBtn = new Button(this);
            LinearLayout accLayout = new LinearLayout(this);

            for (int x = 0; x < ll.getChildCount(); x++) {
                if (ll.getChildAt(x) instanceof ImageView && ll.getChildAt(x).getId() == R.id.imageViewCart)
                    iv = (ImageView) ll.getChildAt(x);
                if (ll.getChildAt(x) instanceof TextView && ll.getChildAt(x).getId() == R.id.textViewCart)
                    tv = (TextView) ll.getChildAt(x);
                if (ll.getChildAt(x) instanceof TextView && ll.getChildAt(x).getId() == R.id.cartDescriptionPc)
                    descTV = (TextView) ll.getChildAt(x);
                if (ll.getChildAt(x) instanceof TextView && ll.getChildAt(x).getId() == R.id.pcCartPrice)
                    pcPriceTV = (TextView) ll.getChildAt(x);
                if (ll.getChildAt(x) instanceof TextView && ll.getChildAt(x).getId() == R.id.cartPcQty)
                    pcQtyTV = (TextView) ll.getChildAt(x);
                if (ll.getChildAt(x) instanceof Button && ll.getChildAt(x).getId() == R.id.deleteBtnCart)
                    removeItemBtn = (Button) ll.getChildAt(x);
                if (ll.getChildAt(x) instanceof LinearLayout && ll.getChildAt(x).getId() == R.id.accessoriesLayout)
                    accLayout = (LinearLayout) ll.getChildAt(x);

            }
            iv.setImageBitmap(pc.getImageBM());
            tv.setText(pc.getName());
            descTV.setText(pc.getDescription());
            pcQtyTV.setText(String.valueOf(qty));
            removeItemBtn.setTag(numeryZestawow.get(i));
            pcPriceTV.setText(decimalFormat.format(pc.getPrice()));
            total += qty * pc.getPrice();

            removeItemBtn.setOnClickListener(v -> {
                Log.i("XXX", "TAG: " + v.getTag());
                dbHandler.removeZestawFromCart((Integer) v.getTag());
                updateCart();
            });

            HashMap<Accessory, Integer> hashMap = new HashMap<>();
            accessories.get(i).stream()
                    .collect(Collectors.groupingBy(s -> s))
                    .forEach((k, v) -> hashMap.put(k, v.size()));
            for (int o = 0; o < hashMap.size(); o++) {
                Accessory accessory = (Accessory) hashMap.keySet().toArray()[o];

                RelativeLayout accItem = (RelativeLayout) getLayoutInflater().inflate(R.layout.cart_item_accesory_layout, null);

                ImageView accIV = new ImageView(this);
                TextView accTV = new TextView(this);
                TextView accQtyTV = new TextView(this);
                TextView accPriceTV = new TextView(this);

                for (int x = 0; x < accItem.getChildCount(); x++) {
                    if (accItem.getChildAt(x) instanceof ImageView && accItem.getChildAt(x).getId() == R.id.accesoryImageViewCart)
                        accIV = (ImageView) accItem.getChildAt(x);
                    if (accItem.getChildAt(x) instanceof TextView && accItem.getChildAt(x).getId() == R.id.accesoryTextViewCart)
                        accTV = (TextView) accItem.getChildAt(x);
                    if (accItem.getChildAt(x) instanceof TextView && accItem.getChildAt(x).getId() == R.id.accesoryQtyCart)
                        accQtyTV = (TextView) accItem.getChildAt(x);
                    if (accItem.getChildAt(x) instanceof TextView && accItem.getChildAt(x).getId() == R.id.accesoryCartPrice)
                        accPriceTV = (TextView) accItem.getChildAt(x);
                }
                accIV.setImageBitmap(accessory.getImageBm());
                accTV.setText(accessory.getName());
                accQtyTV.setText(String.valueOf(hashMap.get(accessory)));
                accLayout.addView(accItem);
                accPriceTV.setText(decimalFormat.format(accessory.getCena()));
                total += Integer.parseInt((String) accQtyTV.getText()) * accessory.getCena();
            }

            ((LinearLayout) linearLayout).addView(ll);
        }
        cartOrderTotalView.setText(decimalFormat.format(total));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == MainActivity.RESULT_OK) {
            if (requestCode == MY_REQUEST_CODE) {
                if (data != null) {
                    updateCart();
                }
            }
        }
    }
}