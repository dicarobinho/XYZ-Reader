package com.example.xyzreader.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "articles")
public class Article implements Serializable {

    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private int id;

    @ColumnInfo(name = "title")
    @SerializedName("title")
    private String title;

    @ColumnInfo(name = "author")
    @SerializedName("author")
    private String author;

    @ColumnInfo(name = "body")
    @SerializedName("body")
    private String body;

    @ColumnInfo(name = "thumb")
    @SerializedName("thumb")
    private String thumb;

    @ColumnInfo(name = "photo")
    @SerializedName("photo")
    private String photo;

    @ColumnInfo(name = "aspect_ratio")
    @SerializedName("aspect_ratio")
    private float aspectRatio;

    @ColumnInfo(name = "published_date")
    @SerializedName("published_date")
    private String publishedDate;

    @Ignore
    public Article() {
    }

    public Article(int id, String title, String author, String body, String thumb, String photo, float aspectRatio, String publishedDate) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.body = body;
        this.thumb = thumb;
        this.photo = photo;
        this.aspectRatio = aspectRatio;
        this.publishedDate = publishedDate;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getBody() {
        return body;
    }

    public String getThumb() {
        return thumb;
    }

    public String getPhoto() {
        return photo;
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }
}
