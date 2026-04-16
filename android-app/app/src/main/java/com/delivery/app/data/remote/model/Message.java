package com.delivery.app.data.remote.model;

import com.google.gson.annotations.SerializedName;

public class Message {
    public static final String TYPE_INFO = "INFO";
    public static final String TYPE_URGENCE = "URGENCE";

    @SerializedName("_id")
    private String id;

    @SerializedName("fromUser")
    private String fromUser;

    @SerializedName("toUser")
    private String toUser;

    @SerializedName("message")
    private String message;

    @SerializedName("type")
    private String type;

    @SerializedName("commandeId")
    private String commandeId;

    @SerializedName("clientContact")
    private String clientContact;

    @SerializedName("createdAt")
    private String createdAt;

    private User fromUserObj;
    private User toUserObj;

    public Message() {}

    public Message(String fromUser, String toUser, String message, String type) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.message = message;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCommandeId() {
        return commandeId;
    }

    public void setCommandeId(String commandeId) {
        this.commandeId = commandeId;
    }

    public String getClientContact() {
        return clientContact;
    }

    public void setClientContact(String clientContact) {
        this.clientContact = clientContact;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public User getFromUserObj() {
        return fromUserObj;
    }

    public void setFromUserObj(User fromUserObj) {
        this.fromUserObj = fromUserObj;
    }

    public User getToUserObj() {
        return toUserObj;
    }

    public void setToUserObj(User toUserObj) {
        this.toUserObj = toUserObj;
    }

    public boolean isUrgent() {
        return TYPE_URGENCE.equals(type);
    }
}