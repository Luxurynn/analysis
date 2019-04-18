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

