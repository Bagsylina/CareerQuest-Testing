package ro.unibuc.careerquest.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ro.unibuc.careerquest.data.CVComponent;
import ro.unibuc.careerquest.data.CVEntity;
import ro.unibuc.careerquest.data.CVRepository;
import ro.unibuc.careerquest.dto.CV;
import ro.unibuc.careerquest.dto.CVCompCreation;
import ro.unibuc.careerquest.dto.CVCreation;
import ro.unibuc.careerquest.exception.CVNotFoundException;
import ro.unibuc.careerquest.exception.IndexNotFoundException;
import ro.unibuc.careerquest.exception.FieldNotFoundException;

import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class CVServiceTest {
    
    @Mock
    private CVRepository cvRepository;

    @InjectMocks
    private CVService cvService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test_getCV() {
        //intialize cv in database
        String id = "1";
        String nonExistingId = "2";
        CVEntity cvEntity = new CVEntity(id, "user1", "description", "achievements");
        
        when(cvRepository.findById(id)).thenReturn(Optional.of(cvEntity));
        when(cvRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        
        //get cv
        CV cv = cvService.getCV(id);

        //verify cv
        assertNotNull(cv);
        assertEquals(id, cv.getId());
        assertEquals("user1", cv.getUserId());
        assertEquals("description", cv.getDescription());
        assertEquals("achievements", cv.getAchievements());

        //exception for nonexisting cv
        assertThrows(CVNotFoundException.class, () -> cvService.getCV(nonExistingId));
    }

    @Test
    void test_updateCV() {
        //initialize current cv and cv update
        String id = "1";
        String nonExistingId = "2";
        CVEntity cvEntity = new CVEntity(id, "user1", "description", "achievements");
        CVCreation cvData = new CVCreation("updated", null);

        when(cvRepository.findById(id)).thenReturn(Optional.of(cvEntity));
        when(cvRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        when(cvRepository.save(any(CVEntity.class))).thenReturn(new CVEntity(id, "user1", "updated", "achievements"));

        //update cv
        CV cv = cvService.updateCV(id, cvData);

        //verify update
        assertNotNull(cv);
        assertEquals(id, cv.getId());
        assertEquals("user1", cv.getUserId());
        assertEquals("updated", cv.getDescription());
        assertEquals("achievements", cv.getAchievements());

        //exception for nonexisting cv
        assertThrows(CVNotFoundException.class, () -> cvService.updateCV(nonExistingId, cvData));
    }

    @Test
    void test_addExperience() {
        //initialize current cv and cv update
        String id = "1";
        String nonExistingId = "2";
        CVEntity cvEntity = new CVEntity(id, "user1", "description", "achievements");
        CVEntity updatedCvEntity = new CVEntity(id, "user1", "description", "achievements");

        //initialize updates
        CVCompCreation experience = new CVCompCreation("experience", LocalDate.of(2025, 7, 1), "Software Developer", "Adobe", "Description");
        CVCompCreation invalid = new CVCompCreation("invalid", LocalDate.of(2022, 10, 1), "Education", "University", "Description");
        updatedCvEntity.addExperience(new CVComponent(LocalDate.of(2025, 7, 1), "Software Developer", "Adobe", "Description"));

        when(cvRepository.findById(id)).thenReturn(Optional.of(cvEntity));
        when(cvRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        when(cvRepository.save(any(CVEntity.class))).thenReturn(updatedCvEntity);

        //add experience
        CV cv = cvService.addExperience(id, experience);

        //verify update
        assertNotNull(cv);
        assertEquals(id, cv.getId());
        assertEquals("user1", cv.getUserId());
        assertEquals("description", cv.getDescription());
        assertEquals("achievements", cv.getAchievements());
        assertEquals(LocalDate.of(2025, 7, 1), cv.getExperience().get(0).getStartDate());
        assertEquals(true, cv.getExperience().get(0).getIsOngoing());
        assertEquals("Software Developer", cv.getExperience().get(0).getTitle());
        assertEquals("Adobe", cv.getExperience().get(0).getInstitution());
        assertEquals("Description", cv.getExperience().get(0).getDescription());

        //exception for invalid field and nonexisting cv
        assertThrows(FieldNotFoundException.class, () -> cvService.addExperience(id, invalid));
        assertThrows(CVNotFoundException.class, () -> cvService.addExperience(nonExistingId, experience));
    }

    @Test
    void test_addTag() {
        //initialize current cv and cv update
        String id = "1";
        String nonExistingId = "2";
        CVEntity cvEntity = new CVEntity(id, "user1", "description", "achievements");
        CVEntity updatedCvEntity = new CVEntity(id, "user1", "description", "achievements");
        updatedCvEntity.addSkill("Java");

        when(cvRepository.findById(id)).thenReturn(Optional.of(cvEntity));
        when(cvRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        when(cvRepository.save(any(CVEntity.class))).thenReturn(updatedCvEntity);

        //add tag
        CV cv = cvService.addTag(id, "skill", "Java");
        assertNotNull(cv);
        assertEquals(id, cv.getId());
        assertEquals("user1", cv.getUserId());
        assertEquals("description", cv.getDescription());
        assertEquals("achievements", cv.getAchievements());
        assertEquals("Java", cv.getSkills().get(0));

        //exception for invalid field and nonexisting cv
        assertThrows(FieldNotFoundException.class, () -> cvService.addTag(id, "invalid", "Invalid"));
        assertThrows(CVNotFoundException.class, () -> cvService.addTag(nonExistingId, "skill", "Java"));
    }

    @Test
    void test_removeExperience() {
        //initialize current cv and cv update
        String id = "1";
        String nonExistingId = "2";
        CVEntity cvEntity = new CVEntity(id, "user1", "description", "achievements");
        CVEntity updatedCvEntity = new CVEntity(id, "user1", "description", "achievements");
        cvEntity.addExperience(new CVComponent(LocalDate.of(2025, 7, 1), "Software Developer", "Adobe", "Description"));

        when(cvRepository.findById(id)).thenReturn(Optional.of(cvEntity));
        when(cvRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        when(cvRepository.save(any(CVEntity.class))).thenReturn(updatedCvEntity);

        //remove experience
        CV cv = cvService.removeExperience(id, "experience", 0);

        //verify update
        assertNotNull(cv);
        assertEquals(id, cv.getId());
        assertEquals("user1", cv.getUserId());
        assertEquals("description", cv.getDescription());
        assertEquals("achievements", cv.getAchievements());
        assert(cv.getEducation().isEmpty());

        //exception for invalid field, nonexisting cv and invalid index
        assertThrows(FieldNotFoundException.class, () -> cvService.removeExperience(id, "invalid", 0));
        assertThrows(CVNotFoundException.class, () -> cvService.removeExperience(nonExistingId, "experience", 0));
        assertThrows(IndexNotFoundException.class, () -> cvService.removeExperience(id, "education", 2));
    }

    @Test
    void test_removeTag() {
        //initialize current cv and cv update
        String id = "1";
        String nonExistingId = "2";
        CVEntity cvEntity = new CVEntity(id, "user1", "description", "achievements");
        CVEntity updatedCvEntity = new CVEntity(id, "user1", "description", "achievements");
        cvEntity.addSkill("Java");

        when(cvRepository.findById(id)).thenReturn(Optional.of(cvEntity));
        when(cvRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        when(cvRepository.save(any(CVEntity.class))).thenReturn(updatedCvEntity);

        //remove tag
        CV cv = cvService.removeTag(id, "skill", 0);

        //verify update
        assertNotNull(cv);
        assertEquals(id, cv.getId());
        assertEquals("user1", cv.getUserId());
        assertEquals("description", cv.getDescription());
        assertEquals("achievements", cv.getAchievements());
        assert(cv.getSkills().isEmpty());

        //exception for invalid field, nonexisting cv and invalid index
        assertThrows(FieldNotFoundException.class, () -> cvService.removeTag(id, "invalid", 0));
        assertThrows(CVNotFoundException.class, () -> cvService.removeTag(nonExistingId, "skill", 0));
        assertThrows(IndexNotFoundException.class, () -> cvService.removeTag(id, "language", 2));
    }

    @Test
    void test_deleteCV() {
        //intialize cv in database
        String id = "1";
        String nonExistingId = "2";
        CVEntity cvEntity = new CVEntity(id, "user1", "description", "achievements");
        
        when(cvRepository.findById(id)).thenReturn(Optional.of(cvEntity));
        when(cvRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        //delete cv
        cvService.deleteCV(id);
        verify(cvRepository, times(1)).delete(cvEntity);

        //exception for nonexisting cv
        assertThrows(CVNotFoundException.class, () -> cvService.getCV(nonExistingId));
    }
}
