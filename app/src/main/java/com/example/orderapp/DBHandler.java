package com.example.orderapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Region;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private Context context;

    //zestawy
    String[] nazwyZestawow = {
            "Komputer Beast",
            "Komputer Beast+",
            "Komputer Beast Pro"
    };

    String[] opisy = {
            "Intel Core i5 12400 6 X 2,4 GHz 16GB DDR4 SSD 250GB M.2 + HDD 1TB Sata GTX 1050 Ti 4GB", //cena 2999 PLN
            "Intel Core i7 11700 8 X 2,9 GHz 16GB DDR4 SSD 500GB M.2 + GTX 1660 6GB",                 //cena 4999 PLN
            "RYZEN 9 3900X 12 X 3 3,8 GHz 32GB DDR4 SSD 500GB M.2 + GTX 2060 6GB"                     //cena 6999 PLN
    };

    float[] ceny = {
            2999,
            4999,
            6999
    };

    int[] ilosci = {
            10,
            15,
            6
    };

    int[] images = {
            R.drawable.zestaw_4,
            R.drawable.zestaw_5,
            R.drawable.zestaw_6
    };

    //akcesoria
    String[] nazwyAkcesoriow = {
            "TypeMaster",
            "ClickinGod",
            "Lightning McQueen",
            "Sliding Into Your DMs",
            "Halo słyszymy się",
            "Nie rozumiem"
    };
    String[] opisyAkcesoriow = {
            "IBM Model M",
            "Microsoft SideWinder X6",
            "Sakar International, Disney Pixar Cars 2 Optical Mouse",
            "Logitech MX Vertical",
            "Razer Kraken Pro 2",
            "Microsoft Xbox One Chat Headset"
    };
    float[] cenyAkcesoriow = {
            199,
            299,
            549,
            29,
            (float) 6.99,
            259
    };
    int[] ilosciAkcesoriow = {
            5,
            9,
            10,
            100,
            43,
            20
    };
    int[] imagesAkcesoriow = {
            R.drawable.keyboard_1,
            R.drawable.keyboard_2,
            R.drawable.mouse_1,
            R.drawable.mouse_2,
            R.drawable.headset_1,
            R.drawable.headset_2
    };

    public DBHandler(@Nullable Context context) {
        super(context, "OrderApp", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String TABLE_NAME = "magazyn";
        String ID_COL = "id";
        String NAME_COL = "nazwa";
        String DESCRIPTION_COL = "opis";
        String PRICE_COL = "cena";
        String QUANTITY_COL = "ilosc";
        String ZDJECIE_COL = "zdjecie";

        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + DESCRIPTION_COL + " TEXT,"
                + PRICE_COL + " FLOAT,"
                + QUANTITY_COL + " INTEGER,"
                + ZDJECIE_COL + " TEXT)";

        db.execSQL(query);

        TABLE_NAME = "zamowienia";
        String ID_PC = "id_pc";
        String ID_AKCESORIA = "id_akcesoria";
        String DATA_COL = "data";
        String USER_COL = "user_id";
        String COMPANY_COL = "nazwa_firmy";

        String query2 = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ID_PC + " INTEGER, "
                + ID_AKCESORIA + " TEXT, "
                + DATA_COL + " DATETIME, "
                + PRICE_COL + " FLOAT, "
                + USER_COL + " INTEGER, "
                + COMPANY_COL + " TEXT)";

        db.execSQL(query2);

        TABLE_NAME = "users";
        String EMAIL_COL = "email";
        String PHONE_COL = "telefon";
        String LOGIN_COL = "login";
        String PASS_COL = "password";
        String IMIE_COL = "imie";
        String NAZWISKO_COL = "nazwisko";

        String query3 = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EMAIL_COL + " TEXT, "
                + PHONE_COL + " TEXT, "
                + LOGIN_COL + " TEXT, "
                + PASS_COL + " TEXT, "
                + IMIE_COL + " TEXT, "
                + NAZWISKO_COL + " TEXT)";

        db.execSQL(query3);

        TABLE_NAME = "akcesoria";
        String PURPOSE_COL = "przeznaczenie";
        String query4 = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PURPOSE_COL + " TEXT,"
                + NAME_COL + " TEXT, "
                + DESCRIPTION_COL + " TEXT, "
                + PRICE_COL + " FLOAT, "
                + QUANTITY_COL + " INTEGER, "
                + ZDJECIE_COL + " TEXT)";

        db.execSQL(query4);

        TABLE_NAME = "koszyk";
        String idProduktu = "id_produktu";
        String rodzaj = "rodzaj";
        String nrZestawu = "nrZestawu";
        String query5 = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + idProduktu + " INTEGER, "
                + rodzaj + " TEXT,"
                + nrZestawu + " INTEGER, "
                + USER_COL + " INTEGER)";
        db.execSQL(query5);

        for(int i = 0; i < nazwyZestawow.length; i++){
            String nazwa = nazwyZestawow[i];
            String opis = opisy[i];
            float cena = ceny[i];
            int ilosc = ilosci[i];

            //encode image to base64 string
//            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), images[i]);
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bm.compress(Bitmap.CompressFormat.JPEG, 20, baos);
//            byte[] bytes = baos.toByteArray();
//
//            String zdjecie = Base64.encodeToString(bytes, Base64.DEFAULT);
            String zdjecie = imageToBytesArray(images, i);

            String updateMagazyn = "INSERT INTO magazyn (" + NAME_COL + ", "
                    + DESCRIPTION_COL + ", "
                    + PRICE_COL + ", "
                    + QUANTITY_COL + ", "
                    + ZDJECIE_COL + ") VALUES (\""
                    + nazwa + "\" , \""
                    + opis + "\" , "
                    + cena + ", "
                    + ilosc + ", \""
                    + zdjecie + "\")";

            db.execSQL(updateMagazyn);

        }

        for(int i = 0; i < nazwyAkcesoriow.length; i++){
            String purpose = "";
            if(i < 2) purpose = "klawiatura";
            if(i >= 2 && i < 4 ) purpose = "myszka";
            if(i >= 4 && i < 6) purpose = "headset";
            String nazwa = nazwyAkcesoriow[i];
            String opis = opisyAkcesoriow[i];
            float cena = cenyAkcesoriow[i];
            int ilosc = ilosciAkcesoriow[i];

            String zdjecie = imageToBytesArray(imagesAkcesoriow, i);

            String updateAkcesoria = "INSERT INTO akcesoria (" + PURPOSE_COL + ", "
                    + NAME_COL + ", "
                    + DESCRIPTION_COL + ", "
                    + PRICE_COL + ", "
                    + QUANTITY_COL + ", "
                    + ZDJECIE_COL + ") VALUES (\""
                    + purpose + "\", \""
                    + nazwa + "\", \""
                    + opis + "\", "
                    + cena + ", "
                    + ilosc + ", \""
                    + zdjecie + "\")";

            db.execSQL(updateAkcesoria);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<Zestaw> readAllZestawy(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from magazyn";
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Zestaw> zestawy = new ArrayList<>();

        if(cursor.moveToFirst()){
            do {
                Bitmap bm = base64ToBitmap(cursor.getString(5));
                zestawy.add(new Zestaw(cursor.getInt(0),cursor.getString(1),
                        cursor.getString(2),
                        cursor.getFloat(3),
                        cursor.getInt(4),
                        cursor.getString(5), bm));
            }while(cursor.moveToNext());
        }

        return zestawy;
    }

    public ArrayList<Accessory> readAllAkcesoria(){
        ArrayList<Accessory> akcesoria = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM akcesoria";
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                Bitmap bm = base64ToBitmap(cursor.getString(6));
                //dodawać akcesoria do arraylisty es
                akcesoria.add(new Accessory(cursor.getInt(0),
                        cursor.getString(2),
                        cursor.getString(1),
                        cursor.getString(3),
                        cursor.getFloat(4),
                        cursor.getInt(5),
                        cursor.getString(6),
                        bm));
            }while(cursor.moveToNext());
        }

        return akcesoria;
    }

    public void addCartToDb(Cart cart, int userId){
        ArrayList<ArrayList<Integer>> pcsArray = cart.getPcIds();
        ArrayList<ArrayList<Integer>> accesoriesArray = cart.getAccesoryIds();
        int id = cart.getNumer();

        ArrayList<Zestaw> pcs = readAllZestawy();
        ArrayList<Accessory> accessories = readAllAkcesoria();

        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < pcsArray.get(0).size(); i++){
            String query = MessageFormat.format("INSERT INTO koszyk (id_produktu, rodzaj, nrZestawu, user_id) VALUES ({0}, \"{1}\", {2}, {3})", pcsArray.get(0).get(i), "pc", id, userId);
            db.execSQL(query);
        }

        for (int i = 0; i< accesoriesArray.get(0).size(); i++){
            String queryAcc = MessageFormat.format("INSERT INTO koszyk (id_produktu, rodzaj, nrZestawu, user_id) VALUES ({0}, \"{1}\", {2}, {3})", accesoriesArray.get(0).get(i), "akcesoria", id, userId);
            db.execSQL(queryAcc);
        }

    }

    public Cart readCartFromDb(int userId){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Integer> kolejka = new ArrayList<>();

        ArrayList<ArrayList<Integer>> cartPcs = new ArrayList<>();
        ArrayList<ArrayList<Integer>> cartAccs = new ArrayList<>();

        String q = "SELECT DISTINCT nrZestawu FROM koszyk";
        Cursor c = db.rawQuery(q, null);
        if(c.moveToFirst()){
            do{
                kolejka.add(c.getInt(0));
            }while(c.moveToNext());
        }

        for(int i = 0; i < kolejka.size(); i++){
            ArrayList<Integer> pcIds = new ArrayList<>();
            ArrayList<Integer> accIds = new ArrayList<>();

            int k = kolejka.get(i);
            String query = "SELECT * FROM koszyk WHERE rodzaj = 'pc' AND user_id = " + userId + " AND nrZestawu = " + k;
            Cursor cursor = db.rawQuery(query, null);

            if(cursor.moveToFirst()){
                do{
                    pcIds.add(cursor.getInt(1));
                }while(cursor.moveToNext());
            }

            String query2 = "SELECT * FROM koszyk WHERE rodzaj = 'akcesoria' AND user_id = " + userId + " AND nrZestawu = " + k;
            Cursor cursor2 = db.rawQuery(query2, null);

            if(cursor2.moveToFirst()){
                do{
                    accIds.add(cursor2.getInt(1));
                }while(cursor2.moveToNext());
            }
            cartPcs.add(pcIds);
            cartAccs.add(accIds);
        }

        return new Cart(cartPcs, cartAccs, kolejka);
    }

    public int getLastNrZestawuNumber(int user_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DISTINCT nrZestawu FROM koszyk WHERE user_id = " + user_id + " ORDER BY nrZestawu DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);
        int num = 0;
        if(cursor.moveToFirst()){
            num = cursor.getInt(0);
        }
        return num;
    }

    public void removeZestawFromCart(int nrZestawu){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM koszyk WHERE user_id = 0 AND nrZestawu = " + nrZestawu;
        db.execSQL(query);
    }

    private Bitmap base64ToBitmap(String base64string){
        byte[] imageBytes = Base64.decode(base64string, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    private String imageToBytesArray(int[] imageArray, int i){
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), imageArray[i]);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] bytes = baos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public void clearCartTable(int userId){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM koszyk WHERE user_id = " + userId;
        db.execSQL(query);
    }

    public void submitOrder(ArrayList<Integer> pcIds, ArrayList<Integer> accIds, Double orderTotal, String companyName, int userId){
        StringBuilder pcIdsString = new StringBuilder();
        StringBuilder accIdsString = new StringBuilder();
        for(int i = 0; i < pcIds.size(); i++){
            pcIdsString.append(pcIds.get(i)).append(";");
        }
        for(int i = 0; i < accIds.size(); i++){
            accIdsString.append(accIds.get(i)).append(";");
        }
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String dateString = dateFormat.format(date);

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "INSERT INTO zamowienia (id_pc, id_akcesoria, data, cena, user_id, nazwa_firmy) VALUES (\""
                + pcIds + "\", \""
                + accIds + "\", \""
                + dateString + "\", "
                + orderTotal + ", "
                + userId + ", \""
                + companyName + "\")";
        db.execSQL(query);
    }

    public ArrayList<Order> readOrderHistory(int userId){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM zamowienia WHERE user_id = " + userId + " ORDER BY data DESC";
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Order> orders = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                ArrayList<Integer> pcIds = new ArrayList<>();
                ArrayList<Integer> accIds = new ArrayList<>();

                String raw1 = cursor.getString(1);
                raw1 = raw1.substring(1);
                raw1 = raw1.substring(0, raw1.length()-1);

                String raw2 = cursor.getString(2);
                raw2 = raw2.substring(1);
                raw2 = raw2.substring(0, raw2.length()-1);

                String[] pcIdsString = raw1.split(",");
                String[] accIdsString = raw2.split(",");
                LocalDateTime date = LocalDateTime.parse(cursor.getString(3), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                Double price = cursor.getDouble(4);

                for(int i = 0; i < pcIdsString.length; i++){
                    pcIds.add(Integer.parseInt(pcIdsString[i].replaceAll(" ", "")));
                }
                for (int i = 0; i < accIdsString.length; i++){
                    accIds.add(Integer.parseInt(accIdsString[i].replaceAll(" ", "")));
                }

                int id = cursor.getInt(0);

                String companyName = cursor.getString(6);

                Order order = new Order(id, pcIds, accIds, date, price, companyName);
                orders.add(order);

            }while(cursor.moveToNext());
        }
        return orders;
    }

    public ArrayList<String> readLoginsFromDb(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> logins = new ArrayList<>();

        String query = "SELECT login FROM users";
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                String login = cursor.getString(0);
                logins.add(login);
            }while(cursor.moveToNext());
        }

        return logins;
    }

    public void createNewUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        String login = user.getLogin();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        String phone = user.getPhone();
        String hash = user.getHash();

        String query = "INSERT INTO users (email, telefon, login, password, imie, nazwisko) VALUES(\""
                + email + "\", \""
                + phone + "\", \""
                + login + "\", \""
                + hash + "\", \""
                + firstName + "\", \""
                + lastName + "\")";

        db.execSQL(query);
    }

    public boolean validateUser(String login, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT password FROM users WHERE login = \"" + login + "\"";
        Cursor cursor = db.rawQuery(query, null);

        User user = new User();

        if(cursor.moveToFirst()){
            String hash = cursor.getString(0);
            String decrypted = user.decrypt(hash);
            return password.equals(decrypted);
        }
        return false;
    }

    public int getUserId(String login){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT id from users where login = \"" + login + "\"";
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            int id = cursor.getInt(0);
            return id;
        }
        return 0;
    }

    public User getUserById(int userId){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM users WHERE id = " + userId;
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            String email = cursor.getString(1);
            String phone = cursor.getString(2);
            String login = cursor.getString(3);
            String imie = cursor.getString(5);
            String nazwisko = cursor.getString(6);
            User user = new User(login, imie, nazwisko, email, phone);
            return user;
        }
        return null;
    }

}
