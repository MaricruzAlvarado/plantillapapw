package com.example; // Paquete

import java.net.URISyntaxException;
import java.sql.SQLException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    @GetMapping("/user")
    public String userForm(Model model) {
        model.addAttribute("user", new User());
        return "user";
    }

    @GetMapping("/user/{id}")
    public String userEditForm(Model model, @PathVariable(value="id") Integer id)
            throws URISyntaxException, SQLException {
        UserJDBCTemplate userTemplate = new UserJDBCTemplate();
        userTemplate.setDataSource(Main.getConnection());
        User user = userTemplate.getUser(id);
        model.addAttribute("user", user);
        return "userEdit";
    }

    @PostMapping("/user")
    public String userSubmit(@ModelAttribute User user) throws URISyntaxException, SQLException {
        UserJDBCTemplate userTemplate = new UserJDBCTemplate();
        userTemplate.setDataSource(Main.getConnection());
        userTemplate.create(user);
        return "newUser";
    }

    @PostMapping("/userEdit")
    public String userEditSubmit(@ModelAttribute User user) throws URISyntaxException, SQLException {
        UserJDBCTemplate userTemplate = new UserJDBCTemplate();
        userTemplate.setDataSource(Main.getConnection());
        userTemplate.update(user.getId(), user.getName(), user.getAge());
        return "newUser";
    }

}