package com.example.campuslostfound;

public class Item {

    public String id;
    public String name;
    public String location;
    public String description;
    public String type;
    public String image;
    public String status;

    public Item() {
        // Firestore needs empty constructor
    }
}