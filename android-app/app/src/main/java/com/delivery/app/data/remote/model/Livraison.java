package com.delivery.app.data.remote.model;

import com.google.gson.annotations.SerializedName;

public class Livraison {
    @SerializedName("_id")
    private String id;

    @SerializedName("commandeId")
    private String commandeId;

    @SerializedName("livreurId")
    private String livreurId;

    @SerializedName("dateLivraison")
    private String dateLivraison;

    @SerializedName("modePaiement")
    private String modePaiement;

    @SerializedName("etat")
    private String etat;

    @SerializedName("remarque")
    private String remarque;

    @SerializedName("localisation")
    private Localisation localisation;

    private Commande commande;
    private User livreur;
    private int syncStatus;
    private long lastModified;

    public static class Localisation {
        @SerializedName("ville")
        private String ville;

        @SerializedName("latitude")
        private double latitude;

        @SerializedName("longitude")
        private double longitude;

        public Localisation() {}

        public Localisation(String ville, double latitude, double longitude) {
            this.ville = ville;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public String getVille() {
            return ville;
        }

        public void setVille(String ville) {
            this.ville = ville;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }

    public Livraison() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommandeId() {
        return commandeId;
    }

    public void setCommandeId(String commandeId) {
        this.commandeId = commandeId;
    }

    public String getLivreurId() {
        return livreurId;
    }

    public void setLivreurId(String livreurId) {
        this.livreurId = livreurId;
    }

    public String getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(String dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public String getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public Localisation getLocalisation() {
        return localisation;
    }

    public void setLocalisation(Localisation localisation) {
        this.localisation = localisation;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public User getLivreur() {
        return livreur;
    }

    public void setLivreur(User livreur) {
        this.livreur = livreur;
    }

    public int getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(int syncStatus) {
        this.syncStatus = syncStatus;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public String getClientName() {
        return commande != null && commande.getClient() != null 
            ? commande.getClient().getFullName() 
            : "";
    }

    public String getClientPhone() {
        return commande != null && commande.getClient() != null 
            ? commande.getClient().getTelephone() 
            : "";
    }

    public String getClientAddress() {
        return commande != null && commande.getClient() != null 
            ? commande.getClient().getFullAddress() 
            : "";
    }

    public String getClientCity() {
        return commande != null && commande.getClient() != null 
            ? commande.getClient().getVille() 
            : "";
    }

    public double getMontant() {
        return commande != null ? commande.getMontant() : 0;
    }

    public String getOrderNumber() {
        return commandeId != null ? commandeId.substring(commandeId.length() - 6) : "";
    }
}