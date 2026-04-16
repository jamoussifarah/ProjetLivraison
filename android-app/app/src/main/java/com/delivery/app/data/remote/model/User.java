package com.delivery.app.data.remote.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("_id")
    private String id;

    @SerializedName("nom")
    private String nom;

    @SerializedName("prenom")
    private String prenom;

    @SerializedName("login")
    private String login;

    @SerializedName("password")
    private String password;

    @SerializedName("role")
    private String role;

    @SerializedName("telephone")
    private String telephone;

    @SerializedName("ville")
    private String ville;

    public User() {}

    public User(String id, String nom, String prenom, String login, String role, String telephone, String ville) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.login = login;
        this.role = role;
        this.telephone = telephone;
        this.ville = ville;
    }

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getFullName() {
        return prenom + " " + nom;
    }
}