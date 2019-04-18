package com.hackathon.analysis.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.Date;

@Document(indexName = "twittercloud", type = "cloudcontent")
public class twitterCloud implements Serializable {

    // private int id;
    @Id
    private String _id;
    private String usernameTweet;
    private String text;
    private String url;
    private Date datetime;

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setUsernameTweet(String usernameTweet) {
        this.usernameTweet = usernameTweet;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }



    public String get_id() {
        return _id;
    }

    public String getUsernameTweet() {
        return usernameTweet;
    }

    public String getText() {
        return text;
    }

    public String getUrl() {
        return url;
    }

    public Date getDatetime() {
        return datetime;
    }


}

