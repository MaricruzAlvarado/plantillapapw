package com.example;
import java.sql.Connection;
import java.util.List;

public interface UserDAO {
    public void setDataSource(Connection connection);
    public void create(String name, Integer age);
    public User getUser(Integer id);
    public List<User> listUsers();
    public void delete(Integer id);
    public void update(Integer id, Integer age);
}