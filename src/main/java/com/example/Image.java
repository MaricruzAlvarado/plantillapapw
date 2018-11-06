package com.example;

import java.sql.Blob;

public class Image {
    private Integer id;
    private String name;
    private byte[] data;
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setImage(byte[] data) {
        this.data = data;
    }
    public byte[] getData() {
        return data;
    }    
}