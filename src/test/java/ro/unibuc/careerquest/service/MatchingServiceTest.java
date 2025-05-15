package ro.unibuc.careerquest.service;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ro.unibuc.careerquest.data.ApplicationEntity;
import ro.unibuc.careerquest.data.ApplicationRepository;
import ro.unibuc.careerquest.data.CVComponent;
import ro.unibuc.careerquest.data.CVEntity;
import ro.unibuc.careerquest.data.CVRepository;
import ro.unibuc.careerquest.data.JobEntity;
import ro.unibuc.careerquest.data.JobRepository;
import ro.unibuc.careerquest.data.UserEntity;
import ro.unibuc.careerquest.data.UserRepository;
import ro.unibuc.careerquest.dto.Application;
import ro.unibuc.careerquest.dto.Job;
import ro.unibuc.careerquest.dto.JobContent;
import ro.unibuc.careerquest.exception.AlreadyAppliedException;
import ro.unibuc.careerquest.exception.CVNotFoundException;
import ro.unibuc.careerquest.exception.EntityNotFoundException;
import ro.unibuc.careerquest.exception.JobNotFoundException;
import ro.unibuc.careerquest.exception.UserNotFoundException;
@ExtendWith(SpringExtension.class) 
public class MatchingServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock 
    private CVRepository cvRepository;

    @Mock 
    private JobRepository jobRepository;

    @InjectMocks
    private MatchingService matchingService = new MatchingService();


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void test_match() throws UserNotFoundException, JobNotFoundException, CVNotFoundException {
        //Arrange
        String jobId = "1";
        String cvId = "1";

        // if we do Senior Software Engineer score should be 0.66
        JobEntity job_entity1 = new JobEntity("1", "Software Engineer", "description", 
                                "Adobe", Arrays.asList("Java", "C"), Arrays.asList("SWE"),
                                Arrays.asList("Leadership", "Problem Solving"), 5000, "Romania");

        CVEntity cv_entity = new CVEntity("1", "bagsylina", "description", "achievements");
    
        
        cv_entity.addEducation(new CVComponent(LocalDate.of(2022,10,1), "education", "Bachelor of CS", "Unibuc"));
        cv_entity.addSkill("Java");
        cv_entity.addSkill("C");
        cv_entity.addTool("SWE");

        // daca job id e de exemplu 2 ce se intampla?
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job_entity1));

        when (cvRepository.findById(cvId)).thenReturn(Optional.of(cv_entity));

        // Act
        double score = matchingService.match(jobId, cvId);

        //Assert
        assertEquals(0.99, score);


    }

    //tests generated with chatGPT

    @Test
    void match_successSenior_withSkillsAndTools() throws Exception {
        String jobId = "job1";
        String cvId = "cv1";

        JobEntity job = new JobEntity();
        job.setId(jobId);
        job.setTitle("Senior Developer");
        job.setAbilities(List.of("Java", "Spring"));  // size = 2
        job.setDomains(List.of("Backend", "Microservices")); // size = 2

        CVEntity cv = new CVEntity(cvId, "user1");
        cv.addExperience(new CVComponent());
        cv.addExperience(new CVComponent());
        cv.addExperience(new CVComponent()); // 3 experiences → matchesExperience = 1
        cv.addSkill("Java");    // matches 1 of 2 abilities
        cv.addSkill("Python");  // no match
        cv.addTool("Backend");  // matches 1 of 2 domains
        cv.addTool("Frontend"); // no match

        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));
        when(cvRepository.findById(cvId)).thenReturn(Optional.of(cv));

        double score = matchingService.match(jobId, cvId);

        // Integer division mimic:
        double skillMatch = (1 / 2);  // integer division -> 0
        double toolMatch = (1 / 2);   // integer division -> 0
        double expected = 0.33 * 1 + 0.33 * skillMatch + 0.33 * toolMatch;

        assertEquals(expected, score, 0.0001);
    }

    @Test
    void match_successJunior_noToolsMatched() throws Exception {
        String jobId = "job2";
        String cvId = "cv2";

        JobEntity job = new JobEntity();
        job.setId(jobId);
        job.setTitle("Junior Analyst");
        job.setAbilities(List.of("Excel"));
        job.setDomains(List.of("Data"));

        CVEntity cv = new CVEntity(cvId, "user2");
        cv.addEducation(new CVComponent());
        cv.addSkill("Excel");
        cv.addTool("OtherTool");

        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));
        when(cvRepository.findById(cvId)).thenReturn(Optional.of(cv));

        double score = matchingService.match(jobId, cvId);

        double expected = 0.33 * 1 + 0.33 * 1 + 0.33 * 0;
        assertEquals(expected, score, 0.0001);
    }

    @Test
    void match_successIntern_withProjectsAndExtracurricular() throws Exception {
        String jobId = "job3";
        String cvId = "cv3";

        JobEntity job = new JobEntity();
        job.setId(jobId);
        job.setTitle("Intern Researcher");
        // To avoid division by zero in skillMatch/toolMatch, add at least 1 dummy ability/domain
        job.setAbilities(List.of("DummyAbility"));
        job.setDomains(List.of("DummyDomain"));

        CVEntity cv = new CVEntity(cvId, "user3");
        // No education:
        // According to logic: education.size() == 0 && projects.size() > 0 && extracurricular.size() > 0 → matchesExperience = 1
        cv.addProject(new CVComponent());
        cv.addExtracurricular(new CVComponent());

        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));
        when(cvRepository.findById(cvId)).thenReturn(Optional.of(cv));

        double score = matchingService.match(jobId, cvId);

        double skillMatch = 0.0 / 1;  // 0 because no skills in CV
        double toolMatch = 0.0 / 1;   // 0 because no tools in CV
        double expected = 0.33 * 1 + 0.33 * skillMatch + 0.33 * toolMatch;

        assertEquals(expected, score, 0.0001);
    }

    @Test
    void match_unknownPosition_defaultsMatchesExperience() throws Exception {
        String jobId = "job4";
        String cvId = "cv4";

        JobEntity job = new JobEntity();
        job.setId(jobId);
        job.setTitle("Manager Something");
        job.setAbilities(List.of("Skill1"));
        job.setDomains(List.of("Domain1"));

        CVEntity cv = new CVEntity(cvId, "user4");

        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));
        when(cvRepository.findById(cvId)).thenReturn(Optional.of(cv));

        double score = matchingService.match(jobId, cvId);

        double expected = 0.33 * 1 + 0.33 * 0 + 0.33 * 0;
        assertEquals(expected, score, 0.0001);
    }

    @Test
    void match_throwsJobNotFoundException() {
        String jobId = "missingJob";
        String cvId = "cv";

        when(jobRepository.findById(jobId)).thenReturn(Optional.empty());

        assertThrows(JobNotFoundException.class, () -> matchingService.match(jobId, cvId));
    }

    @Test
    void match_throwsCVNotFoundException() {
        String jobId = "job";
        String cvId = "missingCV";

        when(jobRepository.findById(jobId)).thenReturn(Optional.of(new JobEntity()));
        when(cvRepository.findById(cvId)).thenReturn(Optional.empty());

        assertThrows(CVNotFoundException.class, () -> matchingService.match(jobId, cvId));
    }

    /*@Test
    public void test_recommend() throws UserNotFoundException, JobNotFoundException, CVNotFoundException {
        String cvId = "1";
        JobEntity job_entity1 = new JobEntity("1", "Senior Software Engineer", "description", 
                                "Adobe", Arrays.asList("Java", "C"), Arrays.asList("SWE"),
                                Arrays.asList("Leadership", "Problem Solving"), 5000, "Romania");
    // @Test
    // public void test_recommend() throws UserNotFoundException, JobNotFoundException, CVNotFoundException {
    //     String cvId = "1";
    //     JobEntity job_entity1 = new JobEntity("1", "Senior Software Engineer", "description", 
    //                             "Adobe", Arrays.asList("Java", "C"), Arrays.asList("SWE"),
    //                             Arrays.asList("Leadership", "Problem Solving"), 5000, "Romania");
        
    //     JobEntity job_entity2 = new JobEntity("2", "QA Enginner", "description", 
    //     "Amazon", Arrays.asList("DevOps", "C", "Agile"), Arrays.asList("Testing"),
    //     Arrays.asList("Leadership", "Problem Solving"), 10000, "Romania");

    //     CVEntity cv_entity = new CVEntity("1", "bagsylina", "description", "achievements");

    //     cv_entity.addEducation(new CVComponent(LocalDate.of(2022,10,1), "education", "Bachelor of CS", "Unibuc"));
    //     cv_entity.addSkill("Java");
    //     cv_entity.addSkill("C");
    //     cv_entity.addTool("SWE");

    //     List<JobEntity> job_entities = Arrays.asList(job_entity1, job_entity2);

    //     when(cvRepository.findById(cvId)).thenReturn(Optional.of(cv_entity));
    //     when(jobRepository.findAll()).thenReturn(job_entities);

    //     // Act
    //     List<Job> ordered_jobs = matchingService.recommend(cvId);

        //Assert
        assertEquals("2", ordered_jobs.get(0).getId());
        assertEquals("1", ordered_jobs.get(1).getId());
    }*/
    
}
