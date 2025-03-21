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

import java.util.Optional;

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
}
