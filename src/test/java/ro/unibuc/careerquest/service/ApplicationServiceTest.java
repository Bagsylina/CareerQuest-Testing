package ro.unibuc.careerquest.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ro.unibuc.careerquest.data.ApplicationEntity;
import ro.unibuc.careerquest.data.ApplicationRepository;
import ro.unibuc.careerquest.data.CVEntity;
import ro.unibuc.careerquest.data.CVRepository;
import ro.unibuc.careerquest.data.JobEntity;
import ro.unibuc.careerquest.data.JobRepository;
import ro.unibuc.careerquest.data.UserEntity;
import ro.unibuc.careerquest.data.UserRepository;
import ro.unibuc.careerquest.dto.Application;
import ro.unibuc.careerquest.exception.CVNotFoundException;
import ro.unibuc.careerquest.exception.EntityNotFoundException;
import ro.unibuc.careerquest.exception.UserNotFoundException;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ApplicationServiceTest {
    
    @Mock
    private JobRepository jobRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CVRepository cvRepository;

    @InjectMocks
    private ApplicationService applicationService;

    @BeforeEach 
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test 
    void test_getApplication() {
        //user
        String username = "user1";
        String nonExistingUsername = "user2";
        UserEntity userEntity = new UserEntity(username, "Parola1@", "user1@email.com");

        when(userRepository.findById(username)).thenReturn(Optional.of(userEntity));
        when(userRepository.findById(nonExistingUsername)).thenReturn(Optional.empty());

        //cv
        String cvId = "1";
        String nonExistingCVId = "2";
        CVEntity cvEntity = new CVEntity(cvId, username, "description", "achievements");

        when(cvRepository.findById(cvId)).thenReturn(Optional.of(cvEntity));
        when(cvRepository.findById(nonExistingCVId)).thenReturn(Optional.empty());

        //job
        String jobId = "1";
        String nonExistingJobId = "2";
        JobEntity jobEntity = new JobEntity(jobId, "Fullstack Engineer", "Description", "Adobe", null, null, null, 12000, "Bucharest");

        when(jobRepository.findById(jobId)).thenReturn(Optional.of(jobEntity));
        when(jobRepository.findById(nonExistingJobId)).thenReturn(Optional.empty());

        //application
        String appId = "1";
        String nonExistingAppId = "2";
        ApplicationEntity applicationEntity = new ApplicationEntity(appId, jobId, username, cvId);
        ApplicationEntity wrongUser = new ApplicationEntity("3", jobId, nonExistingUsername, cvId);
        ApplicationEntity wrongCV = new ApplicationEntity("4", jobId, username, nonExistingCVId);
        ApplicationEntity wrongJob = new ApplicationEntity("5", nonExistingJobId, username, cvId);

        when(applicationRepository.findById(appId)).thenReturn(Optional.of(applicationEntity));
        when(applicationRepository.findById("3")).thenReturn(Optional.of(wrongUser));
        when(applicationRepository.findById("4")).thenReturn(Optional.of(wrongCV));
        when(applicationRepository.findById("5")).thenReturn(Optional.of(wrongJob));
        when(applicationRepository.findById(nonExistingAppId)).thenReturn(Optional.empty());

        //get application
        Application application = applicationService.getApplication(appId);

        //verify application
        assertNotNull(application);
        assertEquals(appId, application.getId());
        assertEquals(username, application.getUser().getUsername());
        assertEquals("user1@email.com", application.getUser().getEmail());
        assertEquals(cvId, application.getCV().getId());
        assertEquals(username, application.getCV().getUserId());
        assertEquals("description", application.getCV().getDescription());
        assertEquals("achievements", application.getCV().getAchievements());
        assertEquals(jobId, application.getJob().getId());
        assertEquals("Fullstack Engineer", application.getJob().getTitle());
        assertEquals("Description", application.getJob().getDescription());
        assertEquals("Adobe", application.getJob().getEmployer());
        assertEquals(12000, application.getJob().getSalary());
        assertEquals("Bucharest", application.getJob().getLocation());

        //exceptions for nonexisting components
        assertThrows(EntityNotFoundException.class, () -> applicationService.getApplication(nonExistingAppId));
        assertThrows(UserNotFoundException.class, () -> applicationService.getApplication("3"));
        assertThrows(CVNotFoundException.class, () -> applicationService.getApplication("4"));
        assertThrows(EntityNotFoundException.class, () -> applicationService.getApplication("5"));
    }

    @Test
    void test_deleteApplication() {
        //initialize application in database
        String appId = "1";
        String nonExistingAppId = "2";
        ApplicationEntity applicationEntity = new ApplicationEntity(appId, "1", "user1", "1");

        when(applicationRepository.findById(appId)).thenReturn(Optional.of(applicationEntity));
        when(applicationRepository.findById(nonExistingAppId)).thenReturn(Optional.empty());

        //delete application
        applicationService.deleteApplication(appId);
        verify(applicationRepository, times(1)).delete(applicationEntity);

        //exception for nonexisting application
        assertThrows(EntityNotFoundException.class, () -> applicationService.deleteApplication(nonExistingAppId));
    }
}
