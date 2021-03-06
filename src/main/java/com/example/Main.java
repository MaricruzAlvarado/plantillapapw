/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.sql.DataSource;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.UserJDBCTemplate;

@Controller
@SpringBootApplication
public class Main {

  @Value("${spring.datasource.url}")
  private String dbUrl;

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args);

    try (Connection connection = getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate(
        "CREATE TABLE IF NOT EXISTS User (" +
          "ID INT NOT NULL AUTO_INCREMENT," +
          "NAME VARCHAR(20) NOT NULL," +
          "AGE INT NOT NULL," +
          "PRIMARY KEY (ID)" +
        ");"
      );
    } catch (Exception e) {
      System.out.print("Super error");
    }
    
    UserJDBCTemplate userTemplate = new UserJDBCTemplate();
    userTemplate.setDataSource(getConnection());

    // userTemplate.create("Alberto", 30);
    // userTemplate.create("Anthony", 22);
    // userTemplate.create("Ricky", 20);

    List<User> users = userTemplate.listUsers();
    
    for (User u : users) {
      System.out.print("ID : " + u.getId());
      System.out.print(", Nombre : " + u.getName());
      System.out.println(", Edad : " + u.getAge());
    }
    
    // userTemplate.update(1, "Alberto", 31);

    // User user = userTemplate.getUser(1);
    
    // System.out.print("ID : " + user.getId() );
    // System.out.print(", Name : " + user.getName());
    // System.out.println(", Age : " + user.getAge());
  }

  @RequestMapping("/")
  String index() {
    return "index";
  }

  @RequestMapping("/fabrizio")
  String catCiclismo() {
    return "catCiclismo";
  }

  @RequestMapping("/db")
  String db(Map<String, Object> model) {
    try (Connection connection = getConnection()) {
      Statement stmt = connection.createStatement();

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
      
      stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
      
      ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

      ArrayList<String> output = new ArrayList<String>();
      while (rs.next()) {
        output.add("Entraste a las: " + rs.getTimestamp("tick"));
      }

      model.put("records", output);
      return "db";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @Bean
  public DataSource dataSource() throws SQLException {
    if (dbUrl == null || dbUrl.isEmpty()) {
      return new HikariDataSource();
    } else {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(dbUrl);
      return new HikariDataSource(config);
    }
  }

  public static Connection getConnection() throws URISyntaxException, SQLException {
      URI jdbUri = new URI(System.getenv("JAWSDB_URL"));

      String username = jdbUri.getUserInfo().split(":")[0];
      String password = jdbUri.getUserInfo().split(":")[1];
      String port = String.valueOf(jdbUri.getPort());
      String jdbUrl = "jdbc:mysql://" + jdbUri.getHost() + ":" + port + jdbUri.getPath();

      return DriverManager.getConnection(jdbUrl, username, password);
  }

}
