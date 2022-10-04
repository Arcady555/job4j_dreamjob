package ru.job4j.dreamjob.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.dreamjob.service.UserService;
import ru.job4j.dreamjob.store.model.User;
import ru.job4j.dreamjob.utility.TakeUserUtility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@ThreadSafe
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String addUserGet(Model model, HttpSession session) {
        TakeUserUtility.getInstance(model, session);
        model.addAttribute("user1", new User(0, "Заполните поле", "Заполните поле"));
        return "addUser";
    }

    @PostMapping("/registration")
    public String addUserPost(@ModelAttribute User user) {
        Optional<User> regUser = userService.add(user);
        if (regUser.isEmpty()) {
            return "redirect:/fail";
        }
        return "redirect:/success";
    }

    @GetMapping("/success")
    public String success(Model model, HttpSession session) {
        TakeUserUtility.getInstance(model, session);
        return "printSuccess";
    }

    @GetMapping("/fail")
    public String fail(Model model, HttpSession session) {
        TakeUserUtility.getInstance(model, session);
        return "printFail";
    }

    @GetMapping("/loginPage")
    public String loginGet(Model model, @RequestParam(name = "fail",
            required = false) Boolean fail, HttpSession session) {
        TakeUserUtility.getInstance(model, session);
        model.addAttribute("fail", fail != null);
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(@ModelAttribute User user, HttpServletRequest req) {
        Optional<User> userDb = userService.findUserByEmailAndPwd(
                user.getEmail(), user.getPassword()
        );
        if (userDb.isEmpty()) {
            return "redirect:/loginPage?fail=true";
        }
        HttpSession session = req.getSession();
        session.setAttribute("user", userDb.get());
        return "redirect:/index";
    }
}