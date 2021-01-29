package de.frauenhofer.iosb.eskavi.controller;

import de.frauenhofer.iosb.eskavi.model.user.ImmutableUser;
import de.frauenhofer.iosb.eskavi.model.user.UserLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Collection;

@RestController
@RequestMapping("users")
public class UserManagmentController {

    @GetMapping("/register")
    public void register(@ModelAttribute("email") String email,@ModelAttribute("password") String password) {

    }

    @GetMapping("/login")
    public String login(@ModelAttribute("email") String email,@ModelAttribute("password") String password) {
        return null;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ImmutableUser getUser(@RequestHeader String jwtToken) {
        return null;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void deleteUser(@RequestHeader String jwtToken) {

    }

    @GetMapping("/security_question")
    public String geSecurityQuestion(@RequestHeader String jwtToken) {
        return null;
    }

    @PostMapping("/reset_password")
    public void resetPassword(@ModelAttribute String answer, @ModelAttribute String newPassword){

    }

    @PostMapping("/set_password")
    public void setPassword(@ModelAttribute String oldPassword, @ModelAttribute String newPassword){

    }

    @PostMapping("/level")
    public void setUserLevel(@ModelAttribute("email") String email,@ModelAttribute("email") UserLevel userLevel) {

    }

    @GetMapping("/levels")
    public Collection<String> getUserLevels() {
        return null;
    }
}
