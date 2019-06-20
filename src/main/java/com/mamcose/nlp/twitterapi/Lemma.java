package com.mamcose.nlp.twitterapi;

public class Lemma {
    private String text;

    public Lemma() {
    }

    public Lemma(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
