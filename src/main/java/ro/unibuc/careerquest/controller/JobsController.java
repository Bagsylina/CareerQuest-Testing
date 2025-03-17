package ro.unibuc.careerquest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ro.unibuc.careerquest.data.JobContent;
import ro.unibuc.careerquest.data.JobEntity;
import ro.unibuc.careerquest.dto.Job;
import ro.unibuc.careerquest.exception.EntityNotFoundException;
import ro.unibuc.careerquest.service.JobsService;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class JobsController {
    @Autowired
    private JobsService jobsService;

    @GetMapping("/hello")
    @ResponseBody
    public String sayHello() {
        return "hello";
    }

    @GetMapping("/job")
    @ResponseBody
    public List<JobEntity> getAllJobs() {
        return jobsService.getAllJobs();
    }

    @GetMapping("/job/{id}")
    @ResponseBody
    public Job getJob(@PathVariable String id) throws EntityNotFoundException {
        return jobsService.getJob(id);
    }

    
    @PostMapping("/job")
    @ResponseBody
    public Job createJob(@RequestBody JobContent job,String employerId) {
        return jobsService.createJob(job,employerId);
    }

    @PutMapping("/job/{id}")
    @ResponseBody
    public Job updateJob(@PathVariable String id, @RequestBody JobContent job) throws EntityNotFoundException {
        return jobsService.updateJob(id, job);
    }

    // add tags!!!!!!!!!!!!!!!!!!!!!!!

    @DeleteMapping("/job/{id}")
    @ResponseBody
    public void deleteJob(@PathVariable String id) throws EntityNotFoundException {
        jobsService.deleteJob(id);
    }

    @GetMapping("/jobs/employer/{employerId}")
    @ResponseBody
    public List<JobEntity> getJobsByEmployer(@PathVariable String employerId) {
        return jobsService.getJobsByEmployer(employerId);
    }

}


// here it's get!!
    // @GetMapping("/info")
    // @ResponseBody
    // public Job buildJobFromTitle(@RequestParam(name="title", required=false, defaultValue="Overview") String title) throws EntityNotFoundException {
    //     return jobsService.buildJobFromTitle(title);
    // }

    // @PostMapping("/job/build")
    // @ResponseBody
    // public Job buildJob(@RequestParam(name="title", required=true) String title,
    //                     @RequestParam(name="description", required=false) String description,
    //                     @RequestParam(name="company", required=false) String company,
    //                     @RequestParam(name="employer", required=false) String employer,
    //                     @RequestParam(name="salary", required=false) Integer salary,
    //                     @RequestParam(name="location", required=false) String location,
    //                     @RequestParam(name="abilities", required=false) String[] abilities,
    //                     @RequestParam(name="domains", required=false) String[] domains,
    //                     @RequestParam(name="characteristics", required=false) String[] characteristics
    //                    ) {             
    //     return jobsService.buildJob(title, description, company, employer, abilities, domains, characteristics, salary, location);
    // }
