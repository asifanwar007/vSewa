package com.example.vsewa.NavigationButton.ui.dashboard;

public class DataModel {

    String name;
    String age;
    String uid;
    String address;
    String gender;
    String imageLink;

    public DataModel(String name, String age, String uid, String address, String gender, String imageLink) {
        this.name=name;
        this.age=age;
        this.uid=uid;
        this.address = address;
        this.gender = gender;
        this.imageLink = imageLink;

    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return this.age;
    }

    public String getUid(){
        return uid;
    }

    public  String getAddress(){
        return address;
    }

    public String getGender(){
        return gender;
    }

    public String getImageLink(){
        return imageLink;
    }

}