package com.example.orderapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText loginEditText, firstNameEditText, lastNameEditText, emailEditText, phoneEditText, passwordEditText, repeatPasswordEditText;

    Button registerBtn;

    boolean loginValid, firstNameValid, lastNameValid, emailValid, phoneValid, passwordValid, repeatPasswordValid;

    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHandler = new DBHandler(this);

        loginEditText = findViewById(R.id.registerLoginEditText);
        firstNameEditText = findViewById(R.id.registerImieEditText);
        lastNameEditText = findViewById(R.id.registerNazwiskoEditText);
        emailEditText = findViewById(R.id.registerEmailEditText);
        passwordEditText = findViewById(R.id.registerPasswordEditText);
        repeatPasswordEditText = findViewById(R.id.registerRepeatPasswordEditText);
        phoneEditText = findViewById(R.id.registerPhoneEditText);

        registerBtn = findViewById(R.id.registerBtn);

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

        firstNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                firstNameValid = false;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String firstName = firstNameEditText.getText().toString();
                if(firstName.length() < 1){
                    String err = getResources().getString(R.string.imie_error);
                    firstNameEditText.setError(err);
                    firstNameValid = false;
                }else{
                    firstNameEditText.setError(null);
                    firstNameValid = true;
                }
            }
        });

        lastNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lastNameValid = false;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String lastName = lastNameEditText.getText().toString();
                if(lastName.length() < 1){
                    String err = getResources().getString(R.string.nazwisko_error);
                    lastNameEditText.setError(err);
                    lastNameValid = false;
                }else{
                    lastNameEditText.setError(null);
                    lastNameValid = true;
                }
            }
        });

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                emailValid = false;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String email = emailEditText.getText().toString();
                if(!validateEmail(email)){
                    String err = getResources().getString(R.string.email_error);
                    emailEditText.setError(err);
                    emailValid = false;
                }else{
                    emailEditText.setError(null);
                    emailValid = true;
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

        repeatPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                repeatPasswordValid = false;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = passwordEditText.getText().toString();
                String repeatedPassword = repeatPasswordEditText.getText().toString();
                if(!repeatedPassword.equals(password)){
                    String err = getResources().getString(R.string.repeat_password_error);
                    repeatPasswordEditText.setError(err);
                    repeatPasswordValid = false;
                }else{
                    repeatPasswordEditText.setError(null);
                    repeatPasswordValid = true;
                }
            }
        });

        phoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                phoneValid = false;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = phoneEditText.getText().toString();
                String err = getResources().getString(R.string.phone_error);
                if(phone.length() != 9){
                    phoneEditText.setError(err);
                    phoneValid = false;
                }else{
                    phoneEditText.setError(null);
                    phoneValid = true;
                }
            }
        });

        registerBtn.setOnClickListener(v -> {
            if(!loginValid){
                loginEditText.setError(getResources().getString(R.string.login_error));
            }
            if(!firstNameValid){
                firstNameEditText.setError(getResources().getString(R.string.imie_error));
            }
            if(!lastNameValid){
                lastNameEditText.setError(getResources().getString(R.string.nazwisko_error));
            }
            if(!emailValid){
                emailEditText.setError(getResources().getString(R.string.email_error));
            }
            if(!phoneValid){
                phoneEditText.setError(getResources().getString(R.string.phone_error));
            }
            if(!passwordValid){
                passwordEditText.setError(getResources().getString(R.string.password_error));
            }
            if(!repeatPasswordValid){
                repeatPasswordEditText.setError(getResources().getString(R.string.repeat_password_error));
            }

            if(!loginValid || !firstNameValid || !lastNameValid || !emailValid || !phoneValid || !passwordValid || !repeatPasswordValid){
                return;
            }

            Log.i("YYY", String.valueOf(loginNotAvailable(loginEditText.getText().toString())));

            if(loginNotAvailable(loginEditText.getText().toString())){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.login_taken);
                builder.show();
                return;
            }
            registerNewUser();

        });
    }

    private void registerNewUser() {
        String login = loginEditText.getText().toString();
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String phone = phoneEditText.getText().toString();

        User user = new User(login, firstName, lastName, email, phone, password);
        Log.i("YYY", user.encrypt(password));
        Log.i("YYY", user.decrypt(user.encrypt(password)));

        dbHandler.createNewUser(user);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.registered_successful);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    private boolean loginNotAvailable(String login){
        ArrayList<String> logins = dbHandler.readLoginsFromDb();
        return logins.stream().anyMatch(login::equals);
    }

    // digit + lowercase char + uppercase char + punctuation + symbol
    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public static boolean validatePassword(final String password) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}