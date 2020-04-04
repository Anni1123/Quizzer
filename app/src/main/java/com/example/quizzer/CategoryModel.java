package com.example.quizzer;

import java.util.List;

public class CategoryModel {
    public CategoryModel() {
    }


    private String name;
    String key;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getSets() {
        return sets;
    }

    public void setSets(List<String> sets) {
        this.sets = sets;
    }

    private String url;

    public CategoryModel(String name, String key, String url, List<String> sets) {
        this.name = name;
        this.key = key;
        this.url = url;
        this.sets = sets;
    }

    private List<String> sets;
}