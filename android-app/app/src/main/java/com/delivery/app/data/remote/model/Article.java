package com.delivery.app.data.remote.model;

import com.google.gson.annotations.SerializedName;

public class Article {
    @SerializedName("_id")
    private String id;

    @SerializedName("refart")
    private String refArt;

    @SerializedName("designation")
    private String designation;

    @SerializedName("prix")
    private double prix;

    @SerializedName("categorie")
    private String categorie;

    @SerializedName("qtestk")
    private int qteStock;

    public Article() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRefArt() {
        return refArt;
    }

    public void setRefArt(String refArt) {
        this.refArt = refArt;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public int getQteStock() {
        return qteStock;
    }

    public void setQteStock(int qteStock) {
        this.qteStock = qteStock;
    }
}