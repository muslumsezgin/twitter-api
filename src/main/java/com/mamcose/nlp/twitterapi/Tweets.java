package com.mamcose.nlp.twitterapi;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class Tweets {
    @Id
    private ObjectId _id;
    private long id;
    private String text;
    private long user;

    public Tweets() { }

    public Tweets(ObjectId _id, long id, String text, long user) {
        this._id = _id;
        this.id = id;
        this.text = text;
        this.user = user;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }
}
