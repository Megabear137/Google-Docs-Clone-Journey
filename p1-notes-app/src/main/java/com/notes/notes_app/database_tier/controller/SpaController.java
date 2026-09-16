package com.notes.notes_app.database_tier.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {

    @GetMapping({"/login", "/register", "/notes", "/notes/**"})
    public String forward() {
        return "forward:/index.html";
    }

}
