package com.example.ishitaroychowdhury.socialapp;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ishitaroychowdhury on 11/21/17.
 */

public class Posts implements Serializable {

    String trackid;
    String post,name,id;
    Date date;


    Posts(){

    }

    public String getId() {
        return id;
    }

    public void setId(String  id) {
        this.id = id;
    }

    public String getTrackid() {
        return trackid;
    }

    public void setTrackid(String trackid) {
        this.trackid = trackid;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Posts{" +
                "id=" + id +
                ", trackid=" + trackid +
                ", post='" + post + '\'' +
                ", name='" + name + '\'' +
                ", date=" + date +
                '}';
    }

    public Posts(String post, String name,Date date, String id) {
        this.post = post;
        this.name = name;
        this.date = date;
        this.id=id;
    }

}
