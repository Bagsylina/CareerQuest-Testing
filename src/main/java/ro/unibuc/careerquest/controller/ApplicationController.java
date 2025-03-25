package ro.unibuc.careerquest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ro.unibuc.careerquest.service.ApplicationService;
import ro.unibuc.careerquest.dto.Application;
import ro.unibuc.careerquest.exception.EntityNotFoundException;
import ro.unibuc.careerquest.exception.CVNotFoundException;
import ro.unibuc.careerquest.exception.UserNotFoundException;

import org.springframework.web.bind.annotation.*;

@Controller
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;
    
    @GetMapping("/app/{id}")
    @ResponseBody
    public Application getApplication(@PathVariable String id) throws EntityNotFoundException {
        return applicationService.getApplication(id);
    }

    @DeleteMapping("/app/{id}")
    @ResponseBody
    public void deleteApplication(@PathVariable String id) throws EntityNotFoundException, CVNotFoundException, UserNotFoundException {
        applicationService.deleteApplication(id);
    } 
}
