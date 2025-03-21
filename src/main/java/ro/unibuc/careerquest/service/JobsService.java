package ro.unibuc.careerquest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.unibuc.careerquest.data.JobContent;
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
import ro.unibuc.careerquest.exception.UsernameTakenException;
import ro.unibuc.careerquest.exception.AlreadyAppliedException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class JobsService {

    @Autowired
    private JobRepository jobDatabase;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CVRepository cvRepository;

    private final AtomicLong counter = new AtomicLong();
    private final AtomicLong appCounter = new AtomicLong();
    private static final String helloTemplate = "Hello, %s!";
    private static final String informationTemplate = "%s : %s!";


    public List<Job> getAllJobs() {
        List<JobEntity> entities = jobDatabase.findAll();
        return entities.stream()
                .map(entity -> new Job(entity))
                .collect(Collectors.toList());
    }

    public Job getJob(String id) throws EntityNotFoundException {
        Optional<JobEntity> optionalEntity = jobDatabase.findById(id);
        JobEntity entity = optionalEntity.orElseThrow(() -> new EntityNotFoundException(id));
        return new Job(entity); // implemented constructor for ease
    }

    public Job createJob(JobContent job) {
        JobEntity entity = new JobEntity(Long.toString(counter.incrementAndGet()), job); // implemented constructor for ease

        jobDatabase.save(entity);
        return new Job(entity); // implemented constructor for ease
    }

    
    public Job updateJob(String id, JobContent job) throws EntityNotFoundException {
        JobEntity entity = jobDatabase.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
        entity.setContents(job);
        jobDatabase.save(entity);
        return new Job(entity);
    }

    public void deleteJob(String id) throws EntityNotFoundException {
        JobEntity entity = jobDatabase.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
        jobDatabase.delete(entity);
    }

    public void deleteAllJobs() {
        jobDatabase.deleteAll();
    }

    public List<ApplicationEntity> getApplications(String jobId) {
        List<ApplicationEntity> applications = applicationRepository.findByJobId(jobId);
        return applications;
    }

    public Application jobApply(String jobId, String cvId) throws EntityNotFoundException, CVNotFoundException, UserNotFoundException, AlreadyAppliedException {
        //get job
        JobEntity job = jobDatabase.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(jobId)));

        //get cv
        Optional<CVEntity> optionalCV = cvRepository.findById(cvId);
        CVEntity cv = optionalCV.orElseThrow(() -> new CVNotFoundException(cvId));

        //verify that user has not already applied to job
        String username = cv.getUserId();
        Optional<ApplicationEntity> optionalApp = applicationRepository.findByJobIdAndUsername(jobId, username);
        optionalApp.ifPresent(app -> {throw new AlreadyAppliedException(app.getUsername());});

        //get user
        Optional<UserEntity> optionalUser = userRepository.findById(username);
        UserEntity user = optionalUser.orElseThrow(() -> new UserNotFoundException(username));

        //save in mongo
        ApplicationEntity app = new ApplicationEntity(Long.toString(appCounter.incrementAndGet()), jobId, cv.getUserId(), cvId);
        applicationRepository.save(app);

        //create app dto that contains all information about job cv and user
        Job jobData = new Job(job);
        CV cvData = new CV(cv.getId(), cv.getUserId(), cv.getDescription(), cv.getAchievements(), cv.getEducation(), cv.getExperience(),
                cv.getExtracurricular(), cv.getProjects(), cv.getSkills(), cv.getTools(), cv.getLanguages());
        User userData = new User(user.getUsername(), user.getDescription(), user.getFirstName(), user.getLastName(),
                user.getBirthdate(), user.getEmail(), user.getPhone());

        Application fullApp = new Application(app.getId(), jobData, userData, cvData);

        return fullApp;
    }
}

// public Job buildJob( String title, String description, String company, String employer, String[] abilities, String[] domains, String[] characteristics, Integer salary, String location) {
    //     return new Job(Long.toString(counter.incrementAndGet()), title, description, company, employer, abilities, domains, characteristics, salary, location);
    // }

    // public Job buildJobFromTitle(String title) throws EntityNotFoundException {
    //     JobEntity entity = jobDatabase.findByTitle(title);
    //     if (entity == null) {
    //         throw new EntityNotFoundException(title);
    //     }
    //     return new Job(Long.toString(counter.incrementAndGet()), entity); // implemented constructor for ease
    // }

// public List<Job> saveAll(List<Job> jobs) {
    //     List<JobEntity> entities = jobs.stream()
    //             .map(job -> {
    //                 JobEntity entity = new JobEntity(job);
    //                 return entity;
    //             })
    //             .collect(Collectors.toList());

    //     List<JobEntity> savedEntities = jobDatabase.saveAll(entities);

    //     return savedEntities.stream()
    //             .map(entity -> new Job(entity))
    //             .collect(Collectors.toList());
    // }

    /// ???