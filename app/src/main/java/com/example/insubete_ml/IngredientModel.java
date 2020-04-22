package com.example.insubete_ml;

import java.util.HashMap;

public class IngredientModel {
    private String name;
    private String quantity;
    private String choQuantity;
    private String gi;
    private String giByQuantity;
    private String servingSize;

    public String getName() {
        return name;
    }

    public void setName(String editTextValue) {
        this.name = editTextValue;
    }

    public String getChoQuantity() {
        return choQuantity;
    }

    public void setChoQuantity(String newChoQuantity) {
        this.choQuantity = newChoQuantity;
    }

    public String getGi() {
        return gi;
    }

    public void setGi(String newGi) {
        this.gi = newGi;
    }

    public String getGiByQuantity() {
        return giByQuantity;
    }

    public void setGiByQuantity(String newGiByQuantity) {
        this.giByQuantity = newGiByQuantity;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String editTextValue) {
        this.quantity = editTextValue;
    }

    public String getServingSize() {
        return servingSize;
    }

    public void setServingSize(String newServingSizeValue) {
        this.servingSize = newServingSizeValue;
    }



}