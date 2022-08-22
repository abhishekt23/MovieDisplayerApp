package com.example.projectfinalapp;

import java.io.Serializable;


public class MovieInfo implements Serializable{
    private String image;
    private String name;
    private String description;
    private String releaseDate;
    private double vote;
    private String language;
    private double popularity;
    private int count;


    public MovieInfo(String image, String name, String description, String releaseDate, double vote, String language, double popularity, int count) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.vote = vote;
        this.language = language;
        this.popularity = popularity;
        this.count = count;
    }

    public String getImage() {
        return image;
    }

    public String getName(){
        return name;
    }


    public String getDescription(){
        return description;
    }

    public String getReleaseDate(){
        return releaseDate;
    }

    public double getVote(){
        return vote;
    }
    public double getPopularity(){
        return popularity;
    }
    public String getLanguage(){
        return language;
    }
    public int getCount(){
        return count;
    }
}


