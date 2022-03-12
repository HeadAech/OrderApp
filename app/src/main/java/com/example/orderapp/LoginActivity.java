package com.example.orderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.Permission;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    Button loginBtn;
    EditText loginEditText;
    EditText passwordEditText;

    Button registerBtn;

    boolean loginValid, passwordValid;
    SharedPreferences prefs;

    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.SEND_SMS},
                1);

        prefs = this.getSharedPreferences("com.example.orderapp", Context.MODE_PRIVATE);

        checkIfLoggedIn();
        registerBtn = findViewById(R.id.registerBtnLogin);

        loginBtn = findViewById(R.id.loginBtn);
        loginEditText = findViewById(R.id.loginEditText);
        passwordEditText = findViewById(R.id.loginPasswordEditText);

        dbHandler = new DBHandler(this);

        registerBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        loginEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                loginValid = false;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String login = loginEditText.getText().toString();
                if(login.length() < 8){
                    String err = getResources().getString(R.string.login_error);
                    loginEditText.setError(err);
                    loginValid = false;
                }else{
                    loginEditText.setError(null);
                    loginValid = true;
                }
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                passwordValid = false;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = passwordEditText.getText().toString();
                if(!validatePassword(password)){
                    String err = getResources().getString(R.string.password_error);
                    passwordEditText.setError(err);
                    passwordValid = false;
                }else{
                    passwordEditText.setError(null);
                    passwordValid = true;
                }
            }
        });

        loginBtn.setOnClickListener(v -> {
            loginUser();
        });
    }



    private void loginUser(){
        if(!loginValid){
            loginEditText.setError(getResources().getString(R.string.login_error));
        }
        if(!passwordValid){
            passwordEditText.setError(getResources().getString(R.string.password_error));
        }

        if(!loginValid || !passwordValid){
            return;
        }

        boolean loggedIn = dbHandler.validateUser(loginEditText.getText().toString(), passwordEditText.getText().toString());

        if(loggedIn){
            prefs.edit().putBoolean("logged_in", true).apply();
            prefs.edit().putInt("user_id", dbHandler.getUserId(loginEditText.getText().toString())).apply();
            checkIfLoggedIn();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.login_error);
        builder.setMessage(R.string.incorrect_login_password);
        builder.show();
        loginEditText.setText("");
        passwordEditText.setText("");

    }

    private void checkIfLoggedIn(){
        boolean loggedIn = prefs.getBoolean("logged_in", false);
        Log.i("YYY", String.valueOf(loggedIn));
        if(loggedIn){
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        }
    }

    // digit + lowercase char + uppercase char + punctuation + symbol
    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public static boolean validatePassword(final String password) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    prefs.edit().putBoolean("sms_permission", true).apply();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, this.getResources().getString(R.string.sms_denied), Toast.LENGTH_SHORT).show();
                    prefs.edit().putBoolean("sms_permission", false).apply();
                }
                return;
            }
        }
    }
}