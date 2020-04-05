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


    public List<String> getSets() {
        return sets;
    }

    public void setSets(List<String> sets) {
        this.sets = sets;
    }


    public CategoryModel(String name, String key, List<String> sets) {
        this.name = name;
        this.key = key;
        this.sets = sets;
    }

    private List<String> sets;
}