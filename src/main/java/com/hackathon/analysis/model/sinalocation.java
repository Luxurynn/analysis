package com.hackathon.analysis.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

@Document(indexName = "sinalocation", type = "location")
public class sinalocation implements Serializable {

   // private int id;
    @Id
    private String _id;
    private String city;
    private String gender;
    private String nick_name;
    private String province;
    private int fans_num;
    private int follow_num;
    private int tweets_num;

    public String get_id() {
        return _id;
    }

    public int getFans_num() {
        return fans_num;
    }

    public int getFollow_num() {
        return follow_num;
    }

    public int getTweets_num() {
        return tweets_num;
    }

    public String getNick_name() {
        return nick_name;
    }

    public String getCity() {
        return city;
    }

    public String getGender() {
        return gender;
    }

    public String getProvince() {
        return province;
    }
}
