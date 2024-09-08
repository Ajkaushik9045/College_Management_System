package com.example.myapplication;

public class Teacher {
    String name;
    String id;
    String pass;

    public Teacher(String name, String id, String pass) {
        this.name = name;
        this.id = id;

        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
        public String getPass() {
        return pass;
    }
}
