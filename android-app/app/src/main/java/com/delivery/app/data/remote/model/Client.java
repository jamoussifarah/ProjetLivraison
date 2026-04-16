package com.delivery.app.data.remote.model;

import com.google.gson.annotations.SerializedName;

public class Client {
    @SerializedName("_id")
    private String id;

    @SerializedName("nom")
    private String nom;

    @SerializedName("prenom")
    private String prenom;

    @SerializedName("adresse")
    private String adresse;

    @SerializedName("ville")
    private String ville;

    @SerializedName("code_postal")
    private String codePostal;

    @SerializedName("telephone")
    private String telephone;

    @SerializedName("email")
    private String email;

    public Client() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return prenom + " " + nom;
    }

    public String getFullAddress() {
        return adresse + ", " + ville + " " + (codePostal != null ? codePostal : "");
    }
}