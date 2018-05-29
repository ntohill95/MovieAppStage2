package com.example.niamhtohill.movieappudacity;

public class ReviewObject {

    private String author;
    private String content;

    public ReviewObject(String author, String content){
        this.author =author;
        this.content=content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
