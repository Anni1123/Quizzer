package com.example.quizzer.Admin;

public class CategoriesModel {
    public CategoriesModel() {
    }

    public CategoriesModel(String name, String url, int sets,String key) {
        this.name = name;
        this.key=key;
        this.url = url;
        this.sets = sets;
    }

    private String name;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    private String url;
    private int sets;
}