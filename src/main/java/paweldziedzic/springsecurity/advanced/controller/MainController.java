package paweldziedzic.springsecurity.advanced.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import paweldziedzic.springsecurity.advanced.entity.AppUser;
import paweldziedzic.springsecurity.advanced.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class MainController {


    private UserService userService;

    public MainController(UserService userService) {
        this.userService = userService;
    }

//    @RequestMapping("/login")
//    public String login() {
//        return "loginOld";
//    }
    @RequestMapping(value="/login", method=RequestMethod.POST)
    public String login(@RequestBody AppUser appUser) {
        String username= appUser.getUsername();
        return userService.signInUser(username);
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
    public ModelAndView register(AppUser user, HttpServletRequest request, Model model) {
        boolean isAdmin = user.isAdmin();
        model.addAttribute("isAdmin", isAdmin);
        userService.addNewUser(user, request);
        return new ModelAndView("redirect:/login");
    }

    @RequestMapping("/verify-token")
    public ModelAndView register(@RequestParam String token) {
        userService.verifyToken(token);
        return new ModelAndView("redirect:/login");
    }
}
