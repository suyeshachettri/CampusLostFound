package com.example.campuslostfound;

public class Item {
    String id;
    String name;
    String location;
    String description;
    String type;
    String image;
    String status;

    public Item() {
    }

    public Item(String id, String name, String location, String description, String type, String image, String status) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.description = description;
        this.type = type;
        this.image = image;
        this.status = status;
    }
}