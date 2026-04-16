package com.delivery.app.data.remote.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Commande {
    @SerializedName("_id")
    private String id;

    @SerializedName("clientId")
    private String clientId;

    @SerializedName("date")
    private String date;

    @SerializedName("etat")
    private String etat;

    @SerializedName("montant")
    private double montant;

    @SerializedName("articles")
    private List<CommandeArticle> articles;

    private Client client;

    public Commande() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public List<CommandeArticle> getArticles() {
        return articles;
    }

    public void setArticles(List<CommandeArticle> articles) {
        this.articles = articles;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public static class CommandeArticle {
        @SerializedName("articleId")
        private String articleId;

        @SerializedName("quantite")
        private int quantite;

        private Article article;

        public CommandeArticle() {}

        public String getArticleId() {
            return articleId;
        }

        public void setArticleId(String articleId) {
            this.articleId = articleId;
        }

        public int getQuantite() {
            return quantite;
        }

        public void setQuantite(int quantite) {
            this.quantite = quantite;
        }

        public Article getArticle() {
            return article;
        }

        public void setArticle(Article article) {
            this.article = article;
        }
    }
}