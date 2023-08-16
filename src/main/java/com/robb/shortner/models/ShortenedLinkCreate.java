package com.robb.shortner.models;

public class ShortenedLinkCreate {
    private String url;

    public ShortenedLinkCreate(String url) {
        this.url = url;
    }
    public ShortenedLinkCreate() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ShortenedLinkCreate [URL=" + url + "]";
    }
}
