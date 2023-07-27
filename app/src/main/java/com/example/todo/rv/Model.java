package com.example.todo.rv;

public class Model {
    private String title, description;
    private int id;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public Model(String title, String description, int id) {
        this.title = title;
        this.description = description;
        this.id = id;
    }
}
