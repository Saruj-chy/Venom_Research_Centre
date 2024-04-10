package com.sd.spartan.vrc.model;

import java.lang.reflect.Field;

public class GroupNameModel {
    public String group_id, group_name, group_image ;

//    public Field[] getAllFields(){
//        return this.getClass().getDeclaredFields() ;
//    }

    public GroupNameModel() {
    }

    public GroupNameModel(String group_id, String group_name, String group_image) {
        this.group_id = group_id;
        this.group_name = group_name;
        this.group_image = group_image;
    }

    public GroupNameModel(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_id() {
        return group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public String getGroup_image() {
        return group_image;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public void setGroup_image(String group_image) {
        this.group_image = group_image;
    }

    @Override
    public String toString() {
        return "GroupNameModel{" +
                "group_id='" + group_id + '\'' +
                ", group_name='" + group_name + '\'' +
                ", group_image='" + group_image + '\'' +
                '}';
    }
}
