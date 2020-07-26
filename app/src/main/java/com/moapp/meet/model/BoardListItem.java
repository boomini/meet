package com.moapp.meet.model;

public class BoardListItem {
    public String Id;
    public String Title;
    public String Detail;
    public long Date;
    public String Writer;
    public String Location;
    public String Time;
    public BoardListItem() {

    }

    public BoardListItem(String id,String title, String name, long date, String detail, String location, String time){
        Id = id;
        Title=title;
        Date=date;
        Writer=name;
        Detail=detail;
        Location=location;
        Time=time;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }

    public long getDate() {
        return Date;
    }

    public void setDate(long date) {
        Date = date;
    }

    public String getWriter() {
        return Writer;
    }

    public void setWriter(String writer) {
        Writer = writer;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}

