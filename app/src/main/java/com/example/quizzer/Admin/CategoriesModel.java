package com.example.quizzer.Admin;

import java.util.List;

public class CategoriesModel {
    public CategoriesModel() {
    }


    private String name;
    String key;
    private String url;

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

    public List<String> getSets() {
        return sets;
    }

    public void setSets(List<String> sets) {
        this.sets = sets;
    }

    public CategoriesModel(String name, String key, List<String> sets) {
        this.name = name;
        this.key = key;
        this.sets = sets;
    }

    private List<String> sets;
}