package com.example.bwurger;

import java.io.Serializable;
import java.util.ArrayList;

public class Burger implements Serializable {
    public String id;
    private String name;
    public String roti, daging, pelengkap, saus;
    private ArrayList<String> ingredients;
    private int imageResId; // ID resource untuk gambar burger

    public Burger() { } // Diperlukan untuk Firebase

    public Burger(String id, String name, ArrayList<String> ingredients, int imageResId) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public int getImageResId() {
        return imageResId; // Getter untuk ID resource gambar
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {return id;}

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}