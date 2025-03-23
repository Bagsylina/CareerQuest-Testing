package ro.unibuc.careerquest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.unibuc.careerquest.data.JobEntity;
import ro.unibuc.careerquest.data.JobRepository;
import ro.unibuc.careerquest.dto.Job;
import ro.unibuc.careerquest.data.ApplicationEntity;
import ro.unibuc.careerquest.dto.Application;
import ro.unibuc.careerquest.data.ApplicationRepository;
import ro.unibuc.careerquest.data.UserRepository;
import ro.unibuc.careerquest.data.CVEntity;
import ro.unibuc.careerquest.data.CVRepository;
import ro.unibuc.careerquest.data.UserEntity;
import ro.unibuc.careerquest.dto.CV;
import ro.unibuc.careerquest.dto.User;
import ro.unibuc.careerquest.exception.EntityNotFoundException;
import ro.unibuc.careerquest.exception.CVNotFoundException;
import ro.unibuc.careerquest.exception.UserNotFoundException;
import ro.unibuc.careerquest.exception.JobNotFoundException;
import ro.unibuc.careerquest.service.CVService;
import ro.unibuc.careerquest.service.UserService;
import ro.unibuc.careerquest.service.JobsService;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Component
public class ApplicationService {
    
    @Autowired
    private JobRepository jobDatabase;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CVRepository cvRepository;

    private static final Logger logger = LoggerFactory.getLogger(ApplicationService.class);

    public Application getApplication(String id) throws EntityNotFoundException, CVNotFoundException, UserNotFoundException {
        Optional<ApplicationEntity> optionalApp = applicationRepository.findById(id);
        ApplicationEntity app = optionalApp.orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));

        String jobId = app.getJobId();
        JobEntity job = jobDatabase.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(jobId)));

        String cvId = app.getCVId();
        Optional<CVEntity> optionalCV = cvRepository.findById(cvId);
        CVEntity cv = optionalCV.orElseThrow(() -> new CVNotFoundException(cvId));

        String username = app.getUsername();
        Optional<UserEntity> optionalUser = userRepository.findById(username);
        UserEntity user = optionalUser.orElseThrow(() -> new UserNotFoundException(username));

        Job jobData = new Job(job);
        CV cvData = new CV(cv.getId(), cv.getUserId(), cv.getDescription(), cv.getAchievements(), cv.getEducation(), cv.getExperience(),
                cv.getExtracurricular(), cv.getProjects(), cv.getSkills(), cv.getTools(), cv.getLanguages());
        User userData = new User(user.getUsername(), user.getDescription(), user.getFirstName(), user.getLastName(),
                user.getBirthdate(), user.getEmail(), user.getPhone());

        Application fullApp = new Application(app.getId(), jobData, userData, cvData);

        return fullApp;
    }

    public void deleteApplication(String id) throws EntityNotFoundException {
        Optional<ApplicationEntity> optionalApp = applicationRepository.findById(id);
        ApplicationEntity app = optionalApp.orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));

        applicationRepository.delete(app);
    }

    public List<Application> getApplicationsForJob(String jobId) throws JobNotFoundException, UserNotFoundException, CVNotFoundException {
        // Optional<JobEntity> optionalJob = jobDatabase.findById(jobId);
        // JobEntity job = optionalJob.orElseThrow(() -> new JobNotFoundException(String.valueOf(jobId)));

        List<ApplicationEntity> apps = applicationRepository.findByJobId(jobId);

        List<Application> new_apps= apps.stream()
                                            .map(app -> {
                                                //String jobId = app.getJobId();
                                                JobEntity job = jobDatabase.findById(jobId)
                                                        .orElseThrow(() -> new EntityNotFoundException(String.valueOf(jobId)));

                                                String cvId = app.getCVId();
                                                Optional<CVEntity> optionalCV = cvRepository.findById(cvId);
                                                CVEntity cv = optionalCV.orElseThrow(() -> new CVNotFoundException(cvId));

                                                String username = app.getUsername();
                                                Optional<UserEntity> optionalUser = userRepository.findById(username);
                                                UserEntity user = optionalUser.orElseThrow(() -> new UserNotFoundException(username));

                                                Application newApp =  new Application(
                                                                        app.getId(), 
                                                                        new Job(job),
                                                                        new User(user), 
                                                                        new CV(cv)  // Use an instance of CVService here
                                                                    );  
                                                return newApp;                                                
                                            })
                                            .collect(Collectors.toList());
        return new_apps;

    }

    public List<Application> getApplicationsOfUser(String username) throws JobNotFoundException, UserNotFoundException, CVNotFoundException {
        // Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        // UserEntity user_e = optionalUser.orElseThrow(() -> new UserNotFoundException(String.valueOf(username)));

        List<ApplicationEntity> apps = applicationRepository.findByUsername(username);
        
        List<Application> new_apps= apps.stream()
                                            .map(app -> {
                                                String jobId = app.getJobId();
                                                JobEntity job = jobDatabase.findById(jobId)
                                                        .orElseThrow(() -> new EntityNotFoundException(String.valueOf(jobId)));

                                                String cvId = app.getCVId();
                                                Optional<CVEntity> optionalCV = cvRepository.findById(cvId);
                                                CVEntity cv = optionalCV.orElseThrow(() -> new CVNotFoundException(cvId));

                                                //String username = app.getUsername();
                                                Optional<UserEntity> optionalUser = userRepository.findById(username);
                                                UserEntity user = optionalUser.orElseThrow(() -> new UserNotFoundException(username));

                                                Application newApp =  new Application(
                                                                        app.getId(), 
                                                                        new Job(job),
                                                                        new User(user), 
                                                                        new CV(cv)  // Use an instance of CVService here
                                                                    );  
                                                return newApp;                                                
                                            })
                                            .collect(Collectors.toList());
        return new_apps;
        
    }

    public double match(String jobId, String cvId) throws UserNotFoundException, JobNotFoundException, CVNotFoundException {
        
        Optional<JobEntity> optionalJob = jobDatabase.findById(jobId);
        JobEntity job = optionalJob.orElseThrow(() -> new JobNotFoundException(jobId));

        Optional<CVEntity> optionalCV = cvRepository.findById(cvId);
        CVEntity cv = optionalCV.orElseThrow(() -> new CVNotFoundException(cvId)); // aici ar trb sa avem

        String username = cv.getUserId();
        UserEntity user = userRepository.findByUsername(username);
        //UserEntity user = optionalUser.orElseThrow(() -> new UserNotFoundException(String.valueOf(username)));

        List<String> user_skills = new ArrayList<>(cv.getSkills());
        List<String> job_skills = new ArrayList<>(job.getAbilities());
        logger.info("User skills: {}", user_skills);
        logger.info("Job required skills: {}", job_skills);

        List<String> user_tools = new ArrayList<>(cv.getTools());
        List<String> job_tools = new ArrayList<>(job.getDomains());
        logger.info("User tools: {}", user_tools);
        logger.info("Job required tools: {}", job_tools);

        Integer noSkills = 0;
        Integer noTools = 0;


        for (String skill: user_skills) {
            if (job_skills.contains(skill)) {
                noSkills++;
            }
        }
        double skillMatch = noSkills / job_skills.size();
        logger.info("No skills: {}", noSkills);
        logger.info("Job skills: {}", job_skills.size());
        logger.info("Skill match: {}", skillMatch);

        for (String tool: user_tools) {
            if (job_tools.contains(tool)) {
                noTools++;
            }
        }
        double toolMatch = noTools / job_tools.size();

        String typeOfPosition = job.getTitle().split("\\s+")[0];
        Integer matchesExperience = 0;
        logger.info("Position type: {}", typeOfPosition);
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
        logger.info("Experience match: {}", matchesExperience);
        
        double score = 0.33 * matchesExperience + 0.33 * skillMatch + 0.33 * toolMatch;

        return score;
    }

    public List<Job> recommend(String cvId) throws UserNotFoundException, JobNotFoundException, CVNotFoundException {
        Optional<CVEntity> optionalCV = cvRepository.findById(cvId);
        CVEntity cv = optionalCV.orElseThrow(() -> new CVNotFoundException(cvId)); 

        List<JobEntity> allJobs = jobDatabase.findAll();

        List<Job> realJobs = allJobs.stream()
                                      .map(job -> new Job(job))
                                      .collect(Collectors.toList());

       realJobs.sort(Comparator.comparing(job -> match(job.getId(), cvId)));

        return realJobs;
        
    }
}
