package ro.unibuc.careerquest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ro.unibuc.careerquest.dto.CV;
import ro.unibuc.careerquest.dto.CVCreation;
import ro.unibuc.careerquest.dto.User;
import ro.unibuc.careerquest.dto.UserCreation;
import ro.unibuc.careerquest.data.ApplicationEntity;
import ro.unibuc.careerquest.exception.InvalidEmailException;
import ro.unibuc.careerquest.exception.UsernameTakenException;
import ro.unibuc.careerquest.exception.UserNotFoundException;
import ro.unibuc.careerquest.service.UserService;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class UserController {
    
    @Autowired
    private UserService userService;

    @GetMapping("/test")
    @ResponseBody
    public String getTest() {
        return "This is a test!";
    }

    @GetMapping("/users")
    @ResponseBody
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{name}")
    @ResponseBody
    public List<User> getAllUsersByName(@PathVariable String name) {
        return userService.getAllUsersByName(name);
    }

    @GetMapping("/user/{id}")
    @ResponseBody
    public User getUser(@PathVariable String id) throws UserNotFoundException {
        return userService.getUser(id);
    }

    @PostMapping("/user")
    @ResponseBody
    public User createUser(@RequestBody UserCreation user) throws UsernameTakenException, InvalidEmailException {
        return userService.createUser(user);
    }

    @PutMapping("/user-cred/{id}") 
    @ResponseBody
    public User updateCredentials(@PathVariable String id, @RequestBody UserCreation user) throws UserNotFoundException, InvalidEmailException {
        return userService.updateCredentials(id, user);
    }

    @PutMapping("/user/{id}")
    @ResponseBody
    public User updateUser(@PathVariable String id, @RequestBody User user) throws UserNotFoundException {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/user/{id}")
    @ResponseBody
    public void deleteUser(@PathVariable String id) throws UserNotFoundException {
        userService.deleteUser(id);
    }

    @GetMapping("/user-cvs/{id}")
    @ResponseBody
    public List<CV> getCVs(@PathVariable String id) throws UserNotFoundException {
        return userService.getCVs(id);
    }

    @PostMapping("/user-cvs/{id}")
    @ResponseBody
    public CV addCV(@PathVariable String id, @RequestBody CVCreation cv) throws UserNotFoundException {
        return userService.addCV(id, cv);
    }

    @GetMapping("/user-app/{id}")
    @ResponseBody
    public List<ApplicationEntity> getApplications(@PathVariable String id) {
        return userService.getApplications(id);
    }
}
