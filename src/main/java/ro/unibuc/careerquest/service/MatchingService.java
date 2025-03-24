package ro.unibuc.careerquest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.unibuc.careerquest.data.ApplicationRepository;
import ro.unibuc.careerquest.data.CVEntity;
import ro.unibuc.careerquest.data.CVRepository;
import ro.unibuc.careerquest.data.JobEntity;
import ro.unibuc.careerquest.data.JobRepository;
import ro.unibuc.careerquest.data.UserEntity;
import ro.unibuc.careerquest.data.UserRepository;
import ro.unibuc.careerquest.dto.Job;
import ro.unibuc.careerquest.exception.CVNotFoundException;
import ro.unibuc.careerquest.exception.JobNotFoundException;
import ro.unibuc.careerquest.exception.UserNotFoundException;

import java.util.*;

@Component
public class MatchingService {

    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private CVRepository cvRepository;

    public double match(String jobId, String cvId) throws UserNotFoundException, JobNotFoundException, CVNotFoundException {
        
        Optional<JobEntity> optionalJob = jobRepository.findById(jobId);
        JobEntity job = optionalJob.orElseThrow(() -> new JobNotFoundException(jobId));

        Optional<CVEntity> optionalCV = cvRepository.findById(cvId);
        CVEntity cv = optionalCV.orElseThrow(() -> new CVNotFoundException(cvId)); // aici ar trb sa avem

        // String username = cv.getUserId();
        // UserEntity user = userRepository.findByUsername(username);
        //UserEntity user = optionalUser.orElseThrow(() -> new UserNotFoundException(String.valueOf(username)));

        List<String> user_skills = new ArrayList<>(cv.getSkills());
        List<String> job_skills = new ArrayList<>(job.getAbilities());

        List<String> user_tools = new ArrayList<>(cv.getTools());
        List<String> job_tools = new ArrayList<>(job.getDomains());

        Integer noSkills = 0;
        Integer noTools = 0;


        for (String skill: user_skills) {
            if (job_skills.contains(skill)) {
                noSkills++;
            }
        }
        double skillMatch = noSkills / job_skills.size();

        for (String tool: user_tools) {
            if (job_tools.contains(tool)) {
                noTools++;
            }
        }
        double toolMatch = noTools / job_tools.size();

        String typeOfPosition = job.getTitle().split("\\s+")[0];
        Integer matchesExperience = 0;

        switch(typeOfPosition){
            case "Senior":
                if (cv.getExperience().size() >= 3)
                    matchesExperience = 1;
                break;
            case "Junior":
                if (cv.getEducation().size() > 0)
                    matchesExperience = 1;
                break;
            case "Intern":
                if (cv.getEducation().size() == 0 && cv.getProjects().size() > 0 && cv.getExtracurricular().size() > 0)
                    matchesExperience = 1;
                break;
            default:
                matchesExperience = 1;
                break;
        }
        
        
        double score = 0.33 * matchesExperience + 0.33 * skillMatch + 0.33 * toolMatch;

        return score;
    }

    public List<Job> recommend(String cvId) throws UserNotFoundException, JobNotFoundException, CVNotFoundException {
        Optional<CVEntity> optionalCV = cvRepository.findById(cvId);
        CVEntity cv = optionalCV.orElseThrow(() -> new CVNotFoundException(cvId)); 

        List<JobEntity> allJobs = jobRepository.findAll();

        List<Job> realJobs = allJobs.stream()
                                      .map(job -> new Job(job))
                                      .collect(Collectors.toList());

       realJobs.sort(Comparator.comparing(job -> match(job.getId(), cvId)));

        return realJobs;
        
    }
}
