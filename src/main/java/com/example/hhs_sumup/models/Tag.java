package com.example.hhs_sumup.models;

public class Tag {
    int tag_id;
    String t_naam;

    public Tag(int tag_id, String t_naam) {
        this.tag_id = tag_id;
        this.t_naam = t_naam;
    }

    public String getT_naam() {
        return t_naam;
    }
}
