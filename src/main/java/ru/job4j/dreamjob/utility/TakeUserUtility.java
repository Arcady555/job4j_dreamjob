package ru.job4j.dreamjob.utility;

import org.springframework.ui.Model;
import ru.job4j.dreamjob.store.model.User;

import javax.servlet.http.HttpSession;

public final class TakeUserUtility {

    private TakeUserUtility(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        } else {
            user.setName(user.getEmail());
        }
        model.addAttribute("user", user);
    }

    public static TakeUserUtility getInstance(Model model, HttpSession session) {
        return new TakeUserUtility(model, session);
    }
}