package com.example;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
public class ImageMapper implements RowMapper<Image> {
    public Image mapRow(ResultSet rs, int rowNum)
    throws SQLException {
        Image image = new Image();
        image.setId(rs.getInt("id"));
        image.setName(rs.getString("name"));
        image.setImage(rs.getBytes("data"));
        return image;
    }
}