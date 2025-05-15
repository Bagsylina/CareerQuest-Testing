package ro.unibuc.careerquest;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

import ro.unibuc.careerquest.controller.ApplicationController;
import ro.unibuc.careerquest.controller.CVController;
import ro.unibuc.careerquest.controller.EmployerController;
import ro.unibuc.careerquest.controller.JobsController;
import ro.unibuc.careerquest.controller.MatchingController;
import ro.unibuc.careerquest.controller.UserController;

@SpringBootTest
@Tag("SmokeTest")
class SmokeTest {

    @Autowired
    private ApplicationController applicationController;

    @Autowired 
    private CVController cvController;

    @Autowired
    private EmployerController employerController;

    @Autowired
    private JobsController jobsController;

    @Autowired
    private MatchingController matchingController;

    @Autowired
    private UserController userController;

    @Container
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.20")
            .withExposedPorts(27017)
            .withSharding();
    
    @BeforeAll
    public static void setUp() {
        mongoDBContainer.start();
    }
        
    @AfterAll
    public static void tearDown() {
        mongoDBContainer.stop();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        final String MONGO_URL = "mongodb://host.docker.internal:";
        final String PORT = String.valueOf(mongoDBContainer.getMappedPort(27017));

        registry.add("mongodb.connection.url", () -> MONGO_URL + PORT);
    }

    @Test
    void contextLoads() {
        assertNotNull(applicationController);
        assertNotNull(cvController);
        assertNotNull(employerController);
        assertNotNull(jobsController);
        assertNotNull(matchingController);
        assertNotNull(userController);
    }
}
