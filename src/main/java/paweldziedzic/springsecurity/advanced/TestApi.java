package paweldziedzic.springsecurity.advanced;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class TestApi {

    @GetMapping("/forAll")
    public String forAll() {
        return "forAll";
    }

    @GetMapping("/forUser")
    public String forUser(Principal principal) {
        return "Hello user: " + principal.getName() ;
    }

    @GetMapping("/forAdmin")
    public String forAdmin(Principal principal) {
        return "Hello admin: " + principal.getName() ;
    }
}
