package ro.unibuc.careerquest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.cucumber.java.lu.a.as;
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
