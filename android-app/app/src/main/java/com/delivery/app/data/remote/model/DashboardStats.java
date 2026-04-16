package com.delivery.app.data.remote.model;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class DashboardStats {
    @SerializedName("total")
    private int total;

    @SerializedName("parEtat")
    private Map<String, Integer> parEtat;

    @SerializedName("parLivreur")
    private Map<String, Integer> parLivreur;

    @SerializedName("parClient")
    private Map<String, Integer> parClient;

    @SerializedName("annulees")
    private int annulees;

    public DashboardStats() {}

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Map<String, Integer> getParEtat() {
        return parEtat;
    }

    public void setParEtat(Map<String, Integer> parEtat) {
        this.parEtat = parEtat;
    }

    public Map<String, Integer> getParLivreur() {
        return parLivreur;
    }

    public void setParLivreur(Map<String, Integer> parLivreur) {
        this.parLivreur = parLivreur;
    }

    public Map<String, Integer> getParClient() {
        return parClient;
    }

    public void setParClient(Map<String, Integer> parClient) {
        this.parClient = parClient;
    }

    public int getAnnulees() {
        return annulees;
    }

    public void setAnnulees(int annulees) {
        this.annulees = annulees;
    }

    public int getEncours() {
        return parEtat != null && parEtat.get("EC") != null ? parEtat.get("EC") : 0;
    }

    public int getLivrees() {
        return parEtat != null && parEtat.get("LI") != null ? parEtat.get("LI") : 0;
    }

    public int getEnAttente() {
        return parEtat != null && parEtat.get("AL") != null ? parEtat.get("AL") : 0;
    }
}