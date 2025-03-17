package main.java.ro.unibuc.careerquest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import main.java.ro.unibuc.careerquest.dto.Employer;
import main.java.ro.unibuc.careerquest.service.EmployerService;
//import ro.unibuc.hello.dto.Greeting;
import ro.unibuc.careerquest.exception.EntityNotFoundException;
//import ro.unibuc.careerquest.service.GreetingsService;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller

public class EmployerController {
    @Autowired
    private EmployerService empsService;

    @GetMapping("/employer")
    @ResponseBody
    public List<Employer> getAllEmployers() {
        return empsService.getAllEmployers();
    }

    @GetMapping("/employer/{id}")
    @ResponseBody
    public Employer getEmployer(@PathVariable String id) throws EntityNotFoundException {
        return empsService.getEmployerById(id);
    }

    @GetMapping("/info")
    @ResponseBody
    public Employer buildEmployerInfo(@RequestParam(name="title", required=false, defaultValue="Overview") String title) throws EntityNotFoundException {
        return empsService.buildEmployerFromInfo(title);
    }

    @PostMapping("/employer")
    @ResponseBody
    public Employer createEmployer(@RequestBody Employer employer) {
        return empsService.saveEmployer(employer);
    }

    @PutMapping("/employer/{id}")
    @ResponseBody
    public Employer updateEmployer(@PathVariable String id, @RequestBody Employer employer) throws EntityNotFoundException {
        return empsService.updateEmployer(id, employer);
    }

    @DeleteMapping("/employer/{id}")
    @ResponseBody
    public void deleteEmployer(@PathVariable String id) throws EntityNotFoundException {
        empsService.deleteEmployer(id);
    }
}
