package com.example;

import java.sql.Connection;
import java.util.List;


public interface ImageDAO {
    public void setDataSource(Connection connection);
    public void create(String name, byte[] data);
    public void create(Image user);
    public Image getImage(Integer id);
    public List<Image> listImages();
    public void delete(Integer id);
    public void update(Integer id, String name, byte[] data);
}