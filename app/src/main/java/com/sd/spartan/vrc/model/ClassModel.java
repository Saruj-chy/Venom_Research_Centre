package com.sd.spartan.vrc.model;

public class ClassModel {
    String id, name ;


    public ClassModel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public ClassModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ClassModel setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "ClassModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
