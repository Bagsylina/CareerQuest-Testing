package ro.unibuc.careerquest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ro.unibuc.careerquest.dto.Job;
import ro.unibuc.careerquest.exception.CVNotFoundException;
import ro.unibuc.careerquest.exception.JobNotFoundException;
import ro.unibuc.careerquest.exception.UserNotFoundException;
import ro.unibuc.careerquest.service.MatchingService;

@Controller
public class MatchingController {
    @Autowired
    private MatchingService matchingService;

    @GetMapping("/app/match/{jobId}")
    @ResponseBody
    public double match(@PathVariable String jobId, @RequestParam String cvId) throws JobNotFoundException, UserNotFoundException, CVNotFoundException {
        return matchingService.match(jobId, cvId);
    }

    @GetMapping("/app/recommend/{cvId}")
    @ResponseBody
    public List<Job> match(@PathVariable String cvId) throws JobNotFoundException, UserNotFoundException, CVNotFoundException {
        return matchingService.recommend(cvId);
    }
}
