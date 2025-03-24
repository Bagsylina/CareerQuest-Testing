package ro.unibuc.careerquest.service;

import ro.unibuc.careerquest.exception.UserNotFoundException;
import ro.unibuc.careerquest.data.UserRepository;
import ro.unibuc.careerquest.data.UserEntity;
import ro.unibuc.careerquest.exception.JobNotFoundException;
import ro.unibuc.careerquest.data.JobRepository;
import ro.unibuc.careerquest.data.JobEntity;
import ro.unibuc.careerquest.exception.CVNotFoundException;
import ro.unibuc.careerquest.data.CVRepository;
import ro.unibuc.careerquest.data.CVEntity;

import ro.unibuc.careerquest.dto.User;
import ro.unibuc.careerquest.dto.Job;

@ExtendWith(SpringExtension.class)
public class MatchingServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock 
    private CVRepository cvRepository;

    @Mock 
    private JobRepository jobDatabase;

    @InjectMocks
    private MatchingService matchingService = new MatchingService();


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    JobEntity job_entity1 = new JobEntity("1", "Senior Software Engineer", "description", 
                                "Adobe", Arrays.asList("Java", "C"), Arrays.asList("SWE"),
                                Arrays.asList("Leadership", "Problem Solving"), 5000, "Romania");

    @Test
    void test_match() throws UserNotFoundException, JobNotFoundException, CVNotFoundException {
        //Arrange
        String jobId = "1";
        String cvId = "1";
        String username = "bagsylina"
        
        CVEntity cv_entity = new CVEntity();

        UserEntity user_entity = new UserEntity();

        // daca job id e de exemplu 2 ce se intampla?
        when(jobDatabase.findById(jobId)).thenReturn(job_entity1);

        when (userRepository.findByUsername(username)).thenReturn(user_entity);

        when (cvRepository.findById(cvId)).thenReturn(cv_entity);

        // Act
        double score = matchingService.match(jobId, cvId);

        // Assert
        assertEquals(score, 0.99)

    }

    @Test
    void test_recommend() throws UserNotFoundException, JobNotFoundException, CVNotFoundException {
        String cvId = "1";

        CVEntity cv_entity = new CVEntity();

        List<JobEntity> job_entities = List.asArrays( job_entity1, new JobEntity());

        when (cvRepository.findById(cvId)).thenReturn(cv_entity);
        when (jobDatabase.findAll()).thenReturn(job_entities);

        // Act
        List<Job> ordered_jobs = matchingService.recommend(cvId);

        //Assert
        assertEquals("2", ordered_jobs[0].getId());
        assertEquals("1", ordered_jobs[1].getId());
    }
    
}
