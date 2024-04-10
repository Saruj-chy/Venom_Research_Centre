package com.sd.spartan.vrc.model;

public class BuilderModel {
    private String food_id, food_name;
    private String fd_sts_id, fd_sts_name ;
    private String act_id, act_name ;
    private String exc_id, exc_name ;
    private String medi_id, medi_name ;
    private String shed_id, shed_name ;
    private String mem_id, mem_username, mem_phn_num, mem_password, mem_sts_id, sts_name ;
    private String log_id, snake_id, snake_name, log_msg, latitute, longitute, log_sts_id, log_name, log_entry_datetime ;
    private String food_weight, food_num, health_status,
            cleaning, venom_collection,  medi_details, remarks, reg_date, reg_time;
    private String age, weight_gm, head, body, tail, total, dorsal, ventral, anal, sub_caudal, notes, occ_date, occ_time;
    private String id, group_name, notice_sec_id, notice_sts_id, msg, date_time, hour;


    public BuilderModel(String food_id, String food_name, String fd_sts_id, String fd_sts_name, String act_id, String act_name, String exc_id, String exc_name, String medi_id, String medi_name, String shed_id, String shed_name, String mem_id, String mem_username, String mem_phn_num, String mem_password, String mem_sts_id, String sts_name, String log_id, String snake_id, String snake_name, String log_msg, String latitute, String longitute, String log_sts_id, String log_name, String log_entry_datetime, String food_weight, String food_num, String health_status, String cleaning, String venom_collection, String medi_details, String remarks, String reg_date, String reg_time, String age, String weight_gm, String head, String body, String tail, String total, String dorsal, String ventral, String anal, String sub_caudal, String notes, String occ_date, String occ_time, String id, String group_name, String notice_sec_id, String notice_sts_id, String msg, String date_time, String hour) {
        this.food_id = food_id;
        this.food_name = food_name;
        this.fd_sts_id = fd_sts_id;
        this.fd_sts_name = fd_sts_name;
        this.act_id = act_id;
        this.act_name = act_name;
        this.exc_id = exc_id;
        this.exc_name = exc_name;
        this.medi_id = medi_id;
        this.medi_name = medi_name;
        this.shed_id = shed_id;
        this.shed_name = shed_name;
        this.mem_id = mem_id;
        this.mem_username = mem_username;
        this.mem_phn_num = mem_phn_num;
        this.mem_password = mem_password;
        this.mem_sts_id = mem_sts_id;
        this.sts_name = sts_name;
        this.log_id = log_id;
        this.snake_id = snake_id;
        this.snake_name = snake_name;
        this.log_msg = log_msg;
        this.latitute = latitute;
        this.longitute = longitute;
        this.log_sts_id = log_sts_id;
        this.log_name = log_name;
        this.log_entry_datetime = log_entry_datetime;
        this.food_weight = food_weight;
        this.food_num = food_num;
        this.health_status = health_status;
        this.cleaning = cleaning;
        this.venom_collection = venom_collection;
        this.medi_details = medi_details;
        this.remarks = remarks;
        this.reg_date = reg_date;
        this.reg_time = reg_time;
        this.age = age;
        this.weight_gm = weight_gm;
        this.head = head;
        this.body = body;
        this.tail = tail;
        this.total = total;
        this.dorsal = dorsal;
        this.ventral = ventral;
        this.anal = anal;
        this.sub_caudal = sub_caudal;
        this.notes = notes;
        this.occ_date = occ_date;
        this.occ_time = occ_time;
        this.id = id;
        this.group_name = group_name;
        this.notice_sec_id = notice_sec_id;
        this.notice_sts_id = notice_sts_id;
        this.msg = msg;
        this.hour = hour;
        this.date_time = date_time;
    }

    public String getFood_name() {
        return food_name;
    }

    public String getFood_id() {
        return food_id;
    }

    public String getFd_sts_id() {
        return fd_sts_id;
    }

    public String getFd_sts_name() {
        return fd_sts_name;
    }

    public String getAct_id() {
        return act_id;
    }

    public String getAct_name() {
        return act_name;
    }

    public String getExc_id() {
        return exc_id;
    }

    public String getExc_name() {
        return exc_name;
    }

    public String getMedi_id() {
        return medi_id;
    }

    public String getMedi_name() {
        return medi_name;
    }

    public String getShed_id() {
        return shed_id;
    }

    public String getShed_name() {
        return shed_name;
    }

    public String getMem_id() {
        return mem_id;
    }

    public String getMem_username() {
        return mem_username;
    }

    public String getMem_phn_num() {
        return mem_phn_num;
    }

    public String getMem_password() {
        return mem_password;
    }

    public String getMem_sts_id() {
        return mem_sts_id;
    }

    public String getSts_name() {
        return sts_name;
    }

    public String getLog_id() {
        return log_id;
    }

    public String getSnake_id() {
        return snake_id;
    }

    public String getSnake_name() {
        return snake_name;
    }

    public String getLog_msg() {
        return log_msg;
    }

    public String getLatitute() {
        return latitute;
    }

    public String getLongitute() {
        return longitute;
    }

    public String getLog_sts_id() {
        return log_sts_id;
    }

    public String getLog_name() {
        return log_name;
    }

    public String getLog_entry_datetime() {
        return log_entry_datetime;
    }

    public String getFood_weight() {
        return food_weight;
    }

    public String getFood_num() {
        return food_num;
    }

    public String getHealth_status() {
        return health_status;
    }

    public String getCleaning() {
        return cleaning;
    }

    public String getVenom_collection() {
        return venom_collection;
    }

    public String getMedi_details() {
        return medi_details;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getReg_date() {
        return reg_date;
    }

    public String getReg_time() {
        return reg_time;
    }

    public String getAge() {
        return age;
    }

    public String getWeight_gm() {
        return weight_gm;
    }

    public String getHead() {
        return head;
    }

    public String getBody() {
        return body;
    }

    public String getTail() {
        return tail;
    }

    public String getTotal() {
        return total;
    }

    public String getDorsal() {
        return dorsal;
    }

    public String getVentral() {
        return ventral;
    }

    public String getAnal() {
        return anal;
    }

    public String getSub_caudal() {
        return sub_caudal;
    }

    public String getNotes() {
        return notes;
    }

    public String getOcc_date() {
        return occ_date;
    }

    public String getOcc_time() {
        return occ_time;
    }

    public String getId() {
        return id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public String getNotice_sec_id() {
        return notice_sec_id;
    }

    public String getNotice_sts_id() {
        return notice_sts_id;
    }

    public String getMsg() {
        return msg;
    }

    public String getDate_time() {
        return date_time;
    }

    public String getHour() {
        return hour;
    }

    @Override
    public String toString() {
        return "BuilderModel{" +
                "food_id='" + food_id + '\'' +
                ", food_name='" + food_name + '\'' +
                '}';
    }
}
