package cse.java2.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WholeController {
    @GetMapping({"/", "/index"})
    public String answerAllQuestion(Model model) {
        model.addAttribute("message", "HelloWorld");
        return "index";
    }
}
