package com.example.makechatsapp.Model;

public class Room {
    String id;
    String name;
    String details;
    String createdAt;

    public Room() {
    }

    public Room(String name, String details, String createdAt) {
        this.name = name;
        this.details = details;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
