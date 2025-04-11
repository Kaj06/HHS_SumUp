package com.example.hhs_sumup.models;

public class Student {
    private int student_id;
    private String s_naam;
    private String s_hhsemail;
    private String s_wachtwoord;
    private String s_st_id;

    public Student(int student_id, String s_naam, String s_hhsemail, String s_wachtwoord, String s_st_id) {
        this.student_id = student_id;
        this.s_naam = s_naam;
        this.s_hhsemail = s_hhsemail;
        this.s_wachtwoord = s_wachtwoord;
        this.s_st_id = s_st_id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getS_naam() {
        return s_naam;
    }

    public void setS_naam(String s_naam) {
        this.s_naam = s_naam;
    }

    public String getS_hhsemail() {
        return s_hhsemail;
    }

    public void setS_hhsemail(String s_hhsemail) {
        this.s_hhsemail = s_hhsemail;
    }

    public String getS_wachtwoord() {
        return s_wachtwoord;
    }

    public void setS_wachtwoord(String s_wachtwoord) {
        this.s_wachtwoord = s_wachtwoord;
    }

    public String getS_st_id() {
        return s_st_id;
    }

    public void setS_st_id(String s_st_id) {
        this.s_st_id = s_st_id;
    }
}
