package com.example;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
public class ImageJDBCTemplate implements ImageDAO {
    private JdbcTemplate jdbcTemplateObject;
    public void setDataSource(Connection connection) {
        this.jdbcTemplateObject = new JdbcTemplate(
            new SingleConnectionDataSource(connection, false)
        );
    }
    public void create(String name, byte[] data) {
        String SQL = "insert into Image (name, data) values (?, ?)";
        jdbcTemplateObject.update(SQL, name, data);
        System.out.println("Registro creado = " + name + " Data = " + data);
        return;
    }

    public void create(Image image) {
        // https://www.devglan.com/spring-jdbc/fetch-auto-generated-primary-key-value-after-insert-spring-jdbc
        String SQL = "insert into Image (name, data) values (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplateObject.update(
            new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps =
                        connection.prepareStatement(SQL, new String[] {"id"});
                    ps.setString(1, image.getName());
                    ps.setBytes(2, image.getData());
                    return ps;
                }
            },
            keyHolder);
        image.setId(keyHolder.getKey().intValue());
        System.out.println("Registro creado = " + image.getName() + " Data = " + image.getData());
        return;
    }

    public Image getImage(Integer id) {
        String SQL = "select * from Image where id = ?";
        Image image = jdbcTemplateObject.queryForObject(SQL, new Object[]{id}, new ImageMapper());
        return image;
    }

    public List<Image> listImages() {
        String SQL = "select * from Image";
        List <Image> images =
        jdbcTemplateObject.query(SQL, new ImageMapper());
        return images;
    }

    public void delete(Integer id) {
        String SQL = "delete from Image where id = ?";
        jdbcTemplateObject.update(SQL, id);
        System.out.println("Borrado ID = " + id );
        return;
    }
    
    public void update(Integer id, String name, byte[] data){
        String SQL = "update Image set data = ?, name = ? where id = ?";
        jdbcTemplateObject.update(SQL, data, name, id);
        System.out.println("Actualizado ID = " + id );
        return;
    }
}