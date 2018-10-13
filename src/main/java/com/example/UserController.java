package com.example; // Paquete

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {
    @GetMapping("/users")
    public String users(Model model) throws URISyntaxException, SQLException {
        UserJDBCTemplate userTemplate = new UserJDBCTemplate();
        userTemplate.setDataSource(Main.getConnection());
        List<User> users = userTemplate.listUsers();
        model.addAttribute("users", users);
        model.addAttribute("user", new User());
        
        return "users";
    }
    
    @GetMapping("/user/{id}")
    public String userId(Model model, @PathVariable(value="id") final Integer id) throws URISyntaxException, SQLException {
        UserJDBCTemplate userTemplate = new UserJDBCTemplate();
        userTemplate.setDataSource(Main.getConnection());
        User user = userTemplate.getUser(id);
        model.addAttribute("user", user);
        
        return "user";
    }

    // UserController.java
    @GetMapping("/setUser")
    public String userForm(Model model) {
        model.addAttribute("user", new User());
        return "setUser";
    }

    @PostMapping("/editUser")
    public String editUserForm(@ModelAttribute User user, RedirectAttributes redir) {
        redir.addFlashAttribute("user", user);
        return "editUser";
    }

    @PostMapping("/user")
    public String userSubmit(@ModelAttribute User user, RedirectAttributes redir) throws URISyntaxException, SQLException {
    
        UserJDBCTemplate userTemplate = new UserJDBCTemplate();
        userTemplate.setDataSource(Main.getConnection());
        userTemplate.create(user);

        redir.addFlashAttribute("user", user);
        return "user";
    }

    @PostMapping("/updateUser")
    public String userEditSubmit(@ModelAttribute User user) throws URISyntaxException, SQLException {
        UserJDBCTemplate userTemplate = new UserJDBCTemplate();
        userTemplate.setDataSource(Main.getConnection());
        userTemplate.update(
            user.getId(), 
            user.getName(), 
            user.getAge()
        );
        
        return "redirect:users";
    }

}