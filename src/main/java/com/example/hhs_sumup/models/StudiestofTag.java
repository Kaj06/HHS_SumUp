package com.example.hhs_sumup.models;

public class StudiestofTag {
    private int ss_id;
    private int tag_id;

    public StudiestofTag(int ss_id, int tag_id) {
        this.ss_id = ss_id;
        this.tag_id = tag_id;
    }

    public StudiestofTag() {
    }

    public int getSs_id() {
        return ss_id;
    }

    public void setSs_id(int ss_id) {
        this.ss_id = ss_id;
    }

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }
}
