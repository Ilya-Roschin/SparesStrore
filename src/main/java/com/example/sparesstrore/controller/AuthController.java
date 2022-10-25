package com.example.sparesstrore.controller;

import com.example.sparesstrore.entity.User;
import com.example.sparesstrore.model.Role;
import com.example.sparesstrore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/success")
    public String getSuccessPage() {
        return "success";
    }


    @GetMapping("/reg")
    public String getRegistrationPage() {
        return "reg";
    }

    @PostMapping("/reg")
    public String addUserToDB(@RequestParam String email, @RequestParam String password) {
        if(!isEmailUsed(email)) {
            userRepository.save(new User(email,new BCryptPasswordEncoder().encode(password), Role.USER));
            return "login";
        }
        return "reg";
    }


    private boolean isEmailUsed(String email) {
        boolean check = false;
        try {
            if(userRepository.findByEmail(email).isPresent()) {
                check = true;
            }
        } catch (Exception exception) {
            //email not founded/ null pointer catch
        }
        return check;
    }
}
