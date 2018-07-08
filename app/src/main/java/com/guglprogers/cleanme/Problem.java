package com.guglprogers.cleanme;

import android.graphics.drawable.Drawable;

import com.here.android.mpa.common.GeoCoordinate;

import java.util.Date;

public class Problem {
    private String description;
    private GeoCoordinate address;
    private int radius;
    private Drawable photo;
    private int type;
    private String street;
    private Date date;


    public boolean hasDate(){
        return date != null;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public boolean hasStreet(){
        return street != null;
    }

    public Problem(String description, GeoCoordinate address, int radius, Drawable photo, int type) {
        this.description = description;
        this.address = address;
        this.radius = radius;
        this.photo = photo;
        this.type = type;
    }

    public Problem(String description, GeoCoordinate address, int radius, Drawable photo, int type,String street) {
        this(description,address,radius,photo,type);
        street = street;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GeoCoordinate getAddress() {
        return address;
    }

    public void setAddress(GeoCoordinate address) {
        this.address = address;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public Drawable getPhoto() {
        return photo;
    }

    public void setPhoto(Drawable photo) {
        this.photo = photo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
