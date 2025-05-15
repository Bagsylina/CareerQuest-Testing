
package ro.unibuc.careerquest.controller;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Arrays;

import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ro.unibuc.careerquest.dto.Application;
import ro.unibuc.careerquest.dto.CV;
import ro.unibuc.careerquest.dto.Job;
import ro.unibuc.careerquest.dto.User;
import ro.unibuc.careerquest.service.ApplicationService;
import ro.unibuc.careerquest.service.JobsService;

class ApplicationControllerTest {

    @Mock
    private ApplicationService applicationService;

    @InjectMocks
    private ApplicationController applicationController;

    @Mock
    private JobsService jobsService;

    @InjectMocks
    private JobsController jobsController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(applicationController, jobsController).build();
    }

    @Test
    void test_getApplication() throws Exception {
        String username = "user1";
        String cvId = "1";
        String jobId = "1";
        String appId = "1";

        User user = new User(username, "user1@email.com");
        CV cv = new CV(cvId, username, "test", "test");
        Job job = new Job(jobId, "Title", "Description", "Employer", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 4000, "Bucharest");
        Application app = new Application(appId, job, user, cv);
        when(applicationService.getApplication(appId)).thenReturn(app);

        mockMvc.perform(get("/app/{id}", appId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(appId))
            .andExpect(jsonPath("$.user.username").value(username))
            .andExpect(jsonPath("$.user.email").value("user1@email.com"))
            .andExpect(jsonPath("$.cv.id").value(cvId))
            .andExpect(jsonPath("$.cv.userId").value(username))
            .andExpect(jsonPath("$.cv.description").value("test"))
            .andExpect(jsonPath("$.cv.achievements").value("test"))
            .andExpect(jsonPath("$.job.id").value(jobId))
            .andExpect(jsonPath("$.job.title").value("Title"))
            .andExpect(jsonPath("$.job.description").value("Description"))
            .andExpect(jsonPath("$.job.employer").value("Employer"))
            .andExpect(jsonPath("$.job.salary").value(4000))
            .andExpect(jsonPath("$.job.location").value("Bucharest"));
    }

    @Test
    void test_deleteApplication() throws Exception {
        String username = "user1";
        String cvId = "1";
        String jobId = "1";
        String appId = "1";

        User user = new User(username, "user1@email.com");
        CV cv = new CV(cvId, username, "test", "test");
        Job job = new Job(jobId, "Title", "Description", "Employer", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 4000, "Bucharest");
        Application app = new Application(appId, job, user, cv);
        when(jobsService.jobApply(jobId, cvId)).thenReturn(app);

        //first apply to job
        mockMvc.perform(post("/job-app/{id}?cvId={cvId}", jobId, cvId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(appId))
            .andExpect(jsonPath("$.user.username").value(username))
            .andExpect(jsonPath("$.user.email").value("user1@email.com"))
            .andExpect(jsonPath("$.cv.id").value(cvId))
            .andExpect(jsonPath("$.cv.userId").value(username))
            .andExpect(jsonPath("$.cv.description").value("test"))
            .andExpect(jsonPath("$.cv.achievements").value("test"))
            .andExpect(jsonPath("$.job.id").value(jobId))
            .andExpect(jsonPath("$.job.title").value("Title"))
            .andExpect(jsonPath("$.job.description").value("Description"))
            .andExpect(jsonPath("$.job.employer").value("Employer"))
            .andExpect(jsonPath("$.job.salary").value(4000))
            .andExpect(jsonPath("$.job.location").value("Bucharest"));

        //then delete the application
        mockMvc.perform(delete("/app/{id}", appId))
            .andExpect(status().isOk());

         //check application is deleted
         verify(applicationService, times(1)).deleteApplication(appId);
    }
}
