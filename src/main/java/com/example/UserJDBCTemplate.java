package com.example;
import java.util.List;
import java.sql.Connection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.
SingleConnectionDataSource;
public class UserJDBCTemplate implements UserDAO {
    private JdbcTemplate jdbcTemplateObject;
    public void setDataSource(Connection connection) {
        this.jdbcTemplateObject = new JdbcTemplate(
            new SingleConnectionDataSource(connection, false)
        );
    }
    public void create(String name, Integer age) {
        String SQL = "insert into User (name, age) values (?, ?)";
        jdbcTemplateObject.update(SQL, name, age);
        System.out.println("Registro creado = " + name + " Age = " + age);
        return;
    }

    public void create(User user) {
        String SQL = "insert into User (name, age) values (?, ?)";
        jdbcTemplateObject.update(SQL, user.getName(), user.getAge());
        System.out.println("Registro creado = " + user.getName() + " Age = " + user.getAge());
        return;
    }

    public User getUser(Integer id) {
        String SQL = "select * from User where id = ?";
        User user = jdbcTemplateObject.queryForObject(SQL, new Object[]{id}, new UserMapper());
        return user;
    }

    public List<User> listUsers() {
        String SQL = "select * from User";
        List <User> users =
        jdbcTemplateObject.query(SQL, new UserMapper());
        return users;
    }

    public void delete(Integer id) {
        String SQL = "delete from User where id = ?";
        jdbcTemplateObject.update(SQL, id);
        System.out.println("Borrado ID = " + id );
        return;
    }
    
    public void update(Integer id, String name, Integer age){
        String SQL = "update User set age = ?, name = ? where id = ?";
        jdbcTemplateObject.update(SQL, age, name, id);
        System.out.println("Actualizado ID = " + id );
        return;
    }
}