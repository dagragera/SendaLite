package unex.cume.mdai.SendaLite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    // Redirige a la lista de usuarios si alguien accede a /admin
    @GetMapping("/admin")
    public String admin() {
        return "redirect:/usuarios";
    }
}

