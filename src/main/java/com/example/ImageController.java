package com.example; // Paquete

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ImageController {
    @GetMapping("/images")
    public String images(Model model) throws URISyntaxException, SQLException {
        // https://stackoverflow.com/questions/32045271/how-to-pass-thobject-values-from-html-to-controller
        ImageJDBCTemplate imageTemplate = new ImageJDBCTemplate();
        imageTemplate.setDataSource(Main.getConnection());
        List<Image> images = imageTemplate.listImages();
        model.addAttribute("images", images);
        model.addAttribute("image", new Image());
        
        return "images";
    }
    
    @GetMapping("/image/{id}")
    public void imageId(HttpServletResponse response, @PathVariable(value="id") final Integer id) throws URISyntaxException, SQLException, IOException {
        ImageJDBCTemplate imageTemplate = new ImageJDBCTemplate();
        imageTemplate.setDataSource(Main.getConnection());
        Image image = imageTemplate.getImage(id);

        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
        response.getOutputStream().write(image.getData());


        response.getOutputStream().close();
    }

    // ImageController.java
    @GetMapping("/setImage")
    public String imageForm(Model model) {
        model.addAttribute("image", new Image());
        return "setImage";
    }

    @PostMapping("/editImage")
    public String editImageForm(@ModelAttribute Image image, RedirectAttributes redir) {
        // https://stackoverflow.com/questions/24301114/passing-model-attribute-during-redirect-in-spring-mvc-and-avoiding-the-same-in-u
        redir.addFlashAttribute("image", image);
        return "editImage";
    }

    @PostMapping("/image")
    public String imageSubmit(@RequestParam("name") String name, @RequestParam("data") MultipartFile data, @ModelAttribute Image image, RedirectAttributes redir) throws URISyntaxException, SQLException, IOException {
        image.setName(name);
        image.setImage(data.getBytes());

        ImageJDBCTemplate imageTemplate = new ImageJDBCTemplate();
        imageTemplate.setDataSource(Main.getConnection());
        imageTemplate.create(image);

        redir.addFlashAttribute("image", image);

        return "image";
    }

    @PostMapping("/updateImage")
    public String imageEditSubmit(@ModelAttribute Image image) throws URISyntaxException, SQLException {
        ImageJDBCTemplate imageTemplate = new ImageJDBCTemplate();
        imageTemplate.setDataSource(Main.getConnection());
        imageTemplate.update(
            image.getId(), 
            image.getName(), 
            image.getData()
        );
        
        return "redirect:images";
    }

}