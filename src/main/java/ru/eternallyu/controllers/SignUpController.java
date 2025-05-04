package ru.eternallyu.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.eternallyu.dto.RegistrationUserDto;
import ru.eternallyu.exception.UserAuthorizationException;
import ru.eternallyu.service.UserService;

@Controller
@RequiredArgsConstructor
public class SignUpController {

    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(SignUpController.class);

    @GetMapping("/registration")
    public String signUpGet(Model model) {
        model.addAttribute("user", new RegistrationUserDto());
        return "sign-up";
    }

    @PostMapping("/registration")
    public String signUpPost(@ModelAttribute("user") @Valid RegistrationUserDto registrationUserDto,
                             BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            logger.warn("Errors while signUp: {}", bindingResult.getAllErrors());
            return "sign-up";
        }

        if (!registrationUserDto.isPasswordsMatch()) {
            logger.info("Passwords do not match");
            bindingResult.rejectValue("repeatPassword", "error.user", "Passwords do not match.");
            return "sign-up";
        }

        userService.createUser(registrationUserDto);

        return "redirect:/login";
    }

}
