package com.example.hulkstore.controller;

import com.example.hulkstore.domain.User;
import com.example.hulkstore.dto.UserDTO;
import com.example.hulkstore.exception.InvalidUserException;
import com.example.hulkstore.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.logging.Logger;

@Controller
public class UserController {
    private final UserService userService;
    private static final Logger logger = Logger.getLogger(UserController.class.getName());
    private static final String indexTemplate = "index";
    private static final String modalTemplate = "modal";
    private static final String errorTemplate = "error";
    private static final String loginTemplate = "login";
    private static final String logupTemplate = "logup";

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //The login form is displayed
    @GetMapping("/login")
    public String login() {
        return loginTemplate;
    }

    //The logup form is displayed
    @GetMapping("/logup")
    public String addUser() {
        return logupTemplate;
    }

    //A user is added, if this is not possible, an error is displayed
    @PostMapping("/users")
    public String addUser(User user, Model model) {
        try {
            logger.info(String.format("Adding user with email=%s", user.getEmail()));
            userService.add(user);
            logger.info(String.format("User with email=%s added", user.getEmail()));
            model.addAttribute("back", "login");
            model.addAttribute("message", "Congratulations, you have successfully registered");

            return modalTemplate;
        } catch (Exception e) {
            addAttributes(model, "logup", "The user trying to login already exists or is invalid", String.format("Could not add user with email=%s", user.getEmail()));

            return errorTemplate;
        }
    }

    //Authentication of a user is performed, if not possible, an error message is displayed
    @PostMapping("/authenticate")
    public String authenticate(UserDTO userDTO, Model model) {
        try {
            logger.info(String.format("Authenticating user with email=%s", userDTO.getEmail()));
            userService.authenticate(userDTO);
            logger.info(String.format("User with email=%s authenticated", userDTO.getEmail()));
            model.addAttribute("message", "Hi! " + userDTO.getEmail());
            model.addAttribute("messageButton", "Log out");
            /*In case of being the generic user, the list of products is shown so that you can add, modify or delete
             a product. And if you are a customer user, the available products are shown so you can buy*/
            if ("admin@gmail.com".equals(userDTO.getEmail())) {
                model.addAttribute("path", "products");
                model.addAttribute("menu", "See stock");
            } else {
                model.addAttribute("path", "stock");
                model.addAttribute("menu", "See products");
            }
            return indexTemplate;
        } catch (InvalidUserException e) {
            addAttributes(model, "login", "The user trying to login is invalid", String.format("Authentication error for email=%s", userDTO.getEmail()));

            return errorTemplate;
        }
    }

    private void addAttributes(Model model, String back, String message, String errorMessage) {
        logger.info(errorMessage);
        model.addAttribute("back", back);
        model.addAttribute("message", message);
    }
}