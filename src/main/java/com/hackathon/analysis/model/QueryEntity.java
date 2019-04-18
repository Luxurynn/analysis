package com.hackathon.analysis.model;

/**
 * Created by syy on 18/04/2019.
 */
public class QueryEntity {
    private String query;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return "Query: " + query;
    }
}
