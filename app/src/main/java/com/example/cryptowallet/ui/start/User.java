package com.example.cryptowallet.ui.start;

import androidx.annotation.NonNull;

import com.google.firebase.database.PropertyName;

import java.util.HashMap;

public class User {
    private String email;
    private String address;
    private String phone;
    private String name;
    private String surname;
    private float btc;

    private float eth;

    private float trp;

    private float balance;

    public User() {
    }

    public User(String email, String address, String phone, String name, String surname) {
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.name = name;
        this.surname = surname;
        balance = 0;
        btc = 0f;
        eth = 0f;
        trp = 0f;


    }
    public User(User user) {
        this.email = user.email;
        this.address = user.address;
        this.phone = user.phone;
        this.name = user.name;
        this.surname = user.surname;
        this.balance = user.balance;
        this.btc = user.btc;
        this.eth = user.eth;
        this.trp = user.trp;

    }

    public void setPortfolio(String name, float countChange) {
        if (name == "Bitcoin"){
            btc += countChange;
        }
        if (name == "Ethereum"){
            eth += countChange;
        }
        if (name == "Ripple") {
            trp += countChange;
        }
    }

    public void doContract(String name, float countChange) {
        if (name == "Bitcoin"){
            btc -= countChange;
        }
        if (name == "Ethereum"){
            eth -= countChange;
        }
        if (name == "Ripple") {
            trp -= countChange;
        }
    }
    // Getters and setters for the fields
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }


    public void addToBalance(float add) {
        this.balance += add;
    }

    public void setBalanceToZero(float add){
        this.balance = 0;
    }

    public float getBalance(){
        return this.balance;
    }

    public float getBtc(){
        return this.btc;
    }

    public float getEth(){
        return this.eth;
    }
    public float getTrp(){
        return this.trp;
    }

}