package ro.unibuc.careerquest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import ro.unibuc.careerquest.service.ApplicationService;
import ro.unibuc.careerquest.dto.Application;
import ro.unibuc.careerquest.dto.Job;
import ro.unibuc.careerquest.dto.User;
import ro.unibuc.careerquest.exception.EntityNotFoundException;
import ro.unibuc.careerquest.exception.CVNotFoundException;
import ro.unibuc.careerquest.exception.UserNotFoundException;
import ro.unibuc.careerquest.exception.JobNotFoundException;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;
    
    @GetMapping("/applications/hello")
    @ResponseBody
    public String sayHello() {
        return "hello application controller";
    }
    
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

    //get all applications for a job
    @GetMapping("/app/job/{jobId}")
    @ResponseBody
    public List<Application> getApplicationsForJob(@PathVariable String jobId) throws JobNotFoundException, UserNotFoundException, CVNotFoundException {
        return applicationService.getApplicationsForJob(jobId);
    }

    // get all applications of a user
    @GetMapping("/app/user/{username}")
    @ResponseBody
    public List<Application> getApplicationsOfUser(@PathVariable String username) throws JobNotFoundException, UserNotFoundException, CVNotFoundException {
        return applicationService.getApplicationsOfUser(username);
    }

    @GetMapping("/app/match/{jobId}")
    @ResponseBody
    public double match(@PathVariable String jobId, @RequestParam String cvId) throws JobNotFoundException, UserNotFoundException, CVNotFoundException {
        return applicationService.match(jobId, cvId);
    }

    @GetMapping("/app/recommend/{cvId}")
    @ResponseBody
    public List<Job> match(@PathVariable String cvId) throws JobNotFoundException, UserNotFoundException, CVNotFoundException {
        return applicationService.recommend(cvId);
    }
}
