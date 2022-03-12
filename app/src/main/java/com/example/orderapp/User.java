package com.example.orderapp;

import java.util.Base64;

public class User {
    private int id;
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String hash;

    public User(){}

    public User(String login, String firstName, String lastName, String email, String phone){
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public User(String login, String firstName, String lastName, String email, String phone, String password){
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.hash = encrypt(password);
    }

    public String encrypt(String password){
        String b64encoded = Base64.getEncoder().encodeToString(password.getBytes());

        // Reverse the string
        String reverse = new StringBuffer(b64encoded).reverse().toString();

        StringBuilder tmp = new StringBuilder();
        final int OFFSET = 4;
        for (int i = 0; i < reverse.length(); i++) {
            tmp.append((char)(reverse.charAt(i) + OFFSET));
        }
        return tmp.toString();
    }

    public String decrypt(String hash){
        StringBuilder tmp = new StringBuilder();
        final int OFFSET = 4;
        for (int i = 0; i < hash.length(); i++) {
            tmp.append((char)(hash.charAt(i) - OFFSET));
        }

        String reversed = new StringBuffer(tmp.toString()).reverse().toString();
        return new String(Base64.getDecoder().decode(reversed));
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", hash='" + hash + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getHash() {
        return hash;
    }

    public String getPhone() {
        return phone;
    }
}
