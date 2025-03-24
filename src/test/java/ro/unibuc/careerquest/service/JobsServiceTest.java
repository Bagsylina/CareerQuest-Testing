package ro.unibuc.careerquest.service;

@ExtendWith(SpringExtension.class)
public class JobsServiceTest {
    @Mock
    private JobRepository jobDatabase;

    @Mock 
    private ApplicationRepository applicationRepository;

    @Mock 
    private UserRepository userRepository;

    @Mock 
    private CVRepository cvRepository;

    @InjectMocks
    private JobsService jobsService = new JobsService();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    
}
