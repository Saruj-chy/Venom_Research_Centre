package com.sd.spartan.vrc.model;

public class SnakeNameListModel {
    public String snake_id, group_id, snake_name, group_name, date_of_collection, entry_datetime, group_image, health_status, reg_date ;

    public SnakeNameListModel(String snake_id, String group_id, String snake_name, String group_name, String date_of_collection, String entry_datetime, String group_image, String health_status, String reg_date) {
        this.snake_id = snake_id;
        this.group_id = group_id;
        this.snake_name = snake_name;
        this.group_name = group_name;
        this.date_of_collection = date_of_collection;
        this.entry_datetime = entry_datetime;
        this.group_image = group_image;
        this.health_status = health_status;
        this.reg_date = reg_date;
    }

    public String getSnake_id() {
        return snake_id;
    }

    public String getSnake_name() {
        return snake_name;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public String getEntry_datetime() {
        return entry_datetime;
    }

    public String getGroup_image() {
        return group_image;
    }

    public void setSnake_id(String snake_id) {
        this.snake_id = snake_id;
    }

    public void setSnake_name(String snake_name) {
        this.snake_name = snake_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public void setEntry_datetime(String entry_datetime) {
        this.entry_datetime = entry_datetime;
    }

    public void setGroup_image(String group_image) {
        this.group_image = group_image;
    }

    public String getDate_of_collection() {
        return date_of_collection;
    }

    public String getHealth_status() {
        return health_status;
    }

    public String getReg_date() {
        return reg_date;
    }
}
