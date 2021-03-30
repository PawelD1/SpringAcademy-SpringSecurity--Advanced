package paweldziedzic.springsecurity.advanced.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import paweldziedzic.springsecurity.advanced.entity.AppUser;
import paweldziedzic.springsecurity.advanced.repo.VerificationTokenRepo;
import paweldziedzic.springsecurity.advanced.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MainController {


    private UserService userService;
    private VerificationTokenRepo verificationTokenRepo;

    @Autowired
    public MainController(UserService userService, VerificationTokenRepo verificationTokenRepo) {
        this.userService = userService;
        this.verificationTokenRepo = verificationTokenRepo;
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }

    @RequestMapping("/signup")
    public ModelAndView singup() {
        return  new ModelAndView("registration", "user", new AppUser());
    }

    @RequestMapping("/register")
    public ModelAndView register(AppUser user, HttpServletRequest request, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()){
            return new ModelAndView("redirect:/signup");
        }
        userService.addNewUser(user, request);
        return new ModelAndView("redirect:/login");
    }

    @RequestMapping("/verify-token")
    public ModelAndView register(@RequestParam String token) {
        userService.verifyToken(token);
        return new ModelAndView("redirect:/login");
    }

    @RequestMapping("/verify-token-new-admin")
    public ModelAndView verifyAdmin(@RequestParam String token){
        userService.verifyAdmin(token);
        return new ModelAndView("redirect:/login");
    }
}
