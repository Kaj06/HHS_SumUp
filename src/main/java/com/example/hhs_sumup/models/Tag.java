package com.example.hhs_sumup.models;

public class Tag {
    int tag_id;
    String t_naam;

    public Tag(int tag_id, String t_naam) {
        this.tag_id = tag_id;
        this.t_naam = t_naam;
    }

    public Tag() {
    }

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    public String getT_naam() {
        return t_naam;
    }

    public void setT_naam(String t_naam) {
        this.t_naam = t_naam;
    }
}
