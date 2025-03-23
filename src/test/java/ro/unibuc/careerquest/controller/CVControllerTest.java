package ro.unibuc.careerquest.controller;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jakarta.servlet.ServletException;
import ro.unibuc.careerquest.data.CVComponent;
import ro.unibuc.careerquest.dto.CV;
import ro.unibuc.careerquest.dto.CVCompCreation;
import ro.unibuc.careerquest.dto.CVCreation;
import ro.unibuc.careerquest.exception.FieldNotFoundException;
import ro.unibuc.careerquest.service.CVService;
import ro.unibuc.careerquest.service.UserService;

public class CVControllerTest {

    @Mock
    UserService userService;

    @Mock
    CVService cvService;

    @InjectMocks
    private UserController userController;
    
    @InjectMocks
    CVController cvController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cvController, userController).build();
    }

    @Test
    void test_getCV() throws Exception {
        String id = "1";
        CV cv = new CV(id, "user1", "test", "test");
        when(cvService.getCV(id)).thenReturn(cv);

        mockMvc.perform(get("/cv/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.userId").value("user1"))
            .andExpect(jsonPath("$.description").value("test"))
            .andExpect(jsonPath("$.achievements").value("test"));
    }

    @Test
    void test_updateCV() throws Exception {
        String id = "1";
        CV cv = new CV(id, "user1", "updated", "test");
        when(cvService.updateCV(eq(id), any(CVCreation.class))).thenReturn(cv);

        mockMvc.perform(put("/cv/{id}", id)
            .content("{\"description\": \"updated\"}")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.userId").value("user1"))
            .andExpect(jsonPath("$.description").value("updated"))
            .andExpect(jsonPath("$.achievements").value("test"));
    }

    @Test
    void test_addExperience() throws Exception {
        String id = "1";
        CV cv = new CV(id, "user1", "test", "test", 
        Arrays.asList(new CVComponent(LocalDate.of(2022, 10, 1), "Bachelors of Computer Science", "University of Bucharest", "test")), 
        null, null, null, null, null, null);
        when(cvService.addExperience(eq(id), any(CVCompCreation.class))).thenReturn(cv);

        //Mock behavior for invalid field
        doThrow(new FieldNotFoundException("field"))
            .when(cvService).addExperience(eq(id), argThat(c -> {
                String field = c.getField();
                return !field.equals("education") &&
                    !field.equals("experience") &&
                    !field.equals("extracurricular") &&
                    !field.equals("project");
            }));

        //invalid field
        assertThrows(ServletException.class,
        () -> mockMvc.perform(put("/cv-add-exp/{id}", id)
            .content("{\"field\": \"something\"}")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof FieldNotFoundException))
        );

        mockMvc.perform(put("/cv-add-exp/{id}", id)
            .content("{\"field\": \"education\", \"startDate\": \"2022-10-01\", \"title\": \"Bachelors of Computer Science\", \"institution\": \"University of Bucharest\", \"description\": \"test\"}")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.userId").value("user1"))
            .andExpect(jsonPath("$.description").value("test"))
            .andExpect(jsonPath("$.achievements").value("test"))
            .andExpect(jsonPath("$.education[0].startDate").value("2022-10-01"))
            .andExpect(jsonPath("$.education[0].isOngoing").value(true))
            .andExpect(jsonPath("$.education[0].title").value("Bachelors of Computer Science"))
            .andExpect(jsonPath("$.education[0].institution").value("University of Bucharest"))
            .andExpect(jsonPath("$.education[0].description").value("test"));
    }

    @Test
    void test_addTag() throws Exception {
        String id = "1";
        CV cv = new CV(id, "user1", "test", "test", null, null, null, null, null, null, Arrays.asList("english"));
        when(cvService.addTag(eq(id), any(String.class), any(String.class))).thenReturn(cv);

        //Mock behavior for invalid field
        doThrow(new FieldNotFoundException("field"))
            .when(cvService).addTag(eq(id), argThat(field -> {
                return !field.equals("skill") &&
                    !field.equals("tool") &&
                    !field.equals("language");
            }), any(String.class));

        //invalid field
        assertThrows(ServletException.class,
        () -> mockMvc.perform(put("/cv-add-tag/{id}?field={f1}&tag={t1}", id, "something", "english"))
            .andExpect(status().isInternalServerError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof FieldNotFoundException))
        );

        mockMvc.perform(put("/cv-add-tag/{id}?field={f1}&tag={t1}", id, "language", "english"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.userId").value("user1"))
            .andExpect(jsonPath("$.description").value("test"))
            .andExpect(jsonPath("$.achievements").value("test"))
            .andExpect(jsonPath("$.languages[0]").value("english"));
    }

    @Test
    void test_removeExperience() throws Exception {
        String id = "1";
        CV cv = new CV(id, "user1", "test", "test", 
        Arrays.asList(new CVComponent(LocalDate.of(2022, 10, 1), "Bachelors of Computer Science", "University of Bucharest", "test")), 
        null, null, null, null, null, null);
        CV cvEmpty = new CV(id, "user1", "test", "test");
        when(cvService.addExperience(eq(id), any(CVCompCreation.class))).thenReturn(cv);
        when(cvService.removeExperience(eq(id), any(String.class), anyInt())).thenReturn(cvEmpty);

        //Mock behavior for invalid field
        doThrow(new FieldNotFoundException("field"))
            .when(cvService).addExperience(eq(id), argThat(c -> {
                String field = c.getField();
                return !field.equals("education") &&
                    !field.equals("experience") &&
                    !field.equals("extracurricular") &&
                    !field.equals("project");
            }));

        //invalid field
        assertThrows(ServletException.class,
        () -> mockMvc.perform(put("/cv-add-exp/{id}", id)
            .content("{\"field\": \"something\"}")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof FieldNotFoundException))
        );

        //add experience
        mockMvc.perform(put("/cv-add-exp/{id}", id)
            .content("{\"field\": \"education\", \"startDate\": \"2022-10-01\", \"title\": \"Bachelors of Computer Science\", \"institution\": \"University of Bucharest\", \"description\": \"test\"}")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.userId").value("user1"))
            .andExpect(jsonPath("$.description").value("test"))
            .andExpect(jsonPath("$.achievements").value("test"))
            .andExpect(jsonPath("$.education[0].startDate").value("2022-10-01"))
            .andExpect(jsonPath("$.education[0].isOngoing").value(true))
            .andExpect(jsonPath("$.education[0].title").value("Bachelors of Computer Science"))
            .andExpect(jsonPath("$.education[0].institution").value("University of Bucharest"))
            .andExpect(jsonPath("$.education[0].description").value("test"));

        //remove experience
        mockMvc.perform(put("/cv-rmv-exp/{id}?field={f1}&i={i1}", id, "education", 0))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.userId").value("user1"))
            .andExpect(jsonPath("$.description").value("test"))
            .andExpect(jsonPath("$.achievements").value("test"))
            .andExpect(jsonPath("$.education").isEmpty());
    }

    @Test
    void test_removeTag() throws Exception {
        String id = "1";
        CV cv = new CV(id, "user1", "test", "test", null, null, null, null, null, null, Arrays.asList("english"));
        CV cvEmpty = new CV(id, "user1", "test", "test");
        when(cvService.addTag(eq(id), any(String.class), any(String.class))).thenReturn(cv);
        when(cvService.removeTag(eq(id), any(String.class), anyInt())).thenReturn(cvEmpty);

        //Mock behavior for invalid field
        doThrow(new FieldNotFoundException("field"))
            .when(cvService).addTag(eq(id), argThat(field -> {
                return !field.equals("skill") &&
                    !field.equals("tool") &&
                    !field.equals("language");
            }), any(String.class));

        //invalid field
        assertThrows(ServletException.class,
        () -> mockMvc.perform(put("/cv-add-tag/{id}?field={f1}&tag={t1}", id, "something", "english"))
            .andExpect(status().isInternalServerError())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof FieldNotFoundException))
        );

        //add tag
        mockMvc.perform(put("/cv-add-tag/{id}?field={f1}&tag={t1}", id, "language", "english"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.userId").value("user1"))
            .andExpect(jsonPath("$.description").value("test"))
            .andExpect(jsonPath("$.achievements").value("test"))
            .andExpect(jsonPath("$.languages[0]").value("english"));

        //remove tag
        mockMvc.perform(put("/cv-rmv-tag/{id}?field={f1}&i={i1}", id, "language", 0))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.userId").value("user1"))
            .andExpect(jsonPath("$.description").value("test"))
            .andExpect(jsonPath("$.achievements").value("test"))
            .andExpect(jsonPath("$.languages").isEmpty());
    }

    @Test 
    void test_deleteCV() throws Exception {
        String username = "bagsylina";
        CV cv = new CV("1", username, "descriere", "ceva");
        when(userService.addCV(eq(username), any(CVCreation.class))).thenReturn(cv);

        //first create CV
        mockMvc.perform(post("/user-cvs/{id}", username)
            .content("{\"description\": \"descriere\", \"achievements\": \"ceva\"}")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("1"))
            .andExpect(jsonPath("$.userId").value(username))
            .andExpect(jsonPath("$.description").value("descriere"))
            .andExpect(jsonPath("$.achievements").value("ceva"));

        //then delete the cv
        mockMvc.perform(delete("/cv/{id}", "1"))
            .andExpect(status().isOk());

         //check cv is deleted
         verify(cvService, times(1)).deleteCV("1");
    }
}
