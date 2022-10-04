package ru.job4j.dreamjob.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.dreamjob.utility.TakeUserUtility;

import javax.servlet.http.HttpSession;

@Controller
public class IndexControl {

    @GetMapping("/index")
    public String indexGet(Model model, HttpSession session) {
        TakeUserUtility.getInstance(model, session);
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/loginPage";
    }
}