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

    //Tests generated using chatGPT

    @Test
    void addExperience_educationField_addsEducation() throws CVNotFoundException, FieldNotFoundException {
        String cvId = "1";
        CVEntity cvEntity = new CVEntity(cvId, "user1");
        CVCompCreation compCreation = new CVCompCreation("education", LocalDate.of(2019, 1, 1), LocalDate.of(2020, 1, 1), "Degree", "Uni", "Desc");

        when(cvRepository.findById(cvId)).thenReturn(Optional.of(cvEntity));
        when(cvRepository.save(any(CVEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CV result = cvService.addExperience(cvId, compCreation);

        assertEquals(cvId, result.getId());
        assertEquals(1, cvEntity.getEducation().size());
        assertEquals("Degree", cvEntity.getEducation().get(0).getTitle());
    }

    @Test
    void addExperience_experienceField_addsExperience() throws CVNotFoundException, FieldNotFoundException {
        String cvId = "2";
        CVEntity cvEntity = new CVEntity(cvId, "user2");
        CVCompCreation compCreation = new CVCompCreation("experience", LocalDate.of(2018, 5, 1), null, "Job", "Company", "Desc");

        when(cvRepository.findById(cvId)).thenReturn(Optional.of(cvEntity));
        when(cvRepository.save(any(CVEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CV result = cvService.addExperience(cvId, compCreation);

        assertEquals(cvId, result.getId());
        assertEquals(1, cvEntity.getExperience().size());
        assertEquals("Job", cvEntity.getExperience().get(0).getTitle());
        assertTrue(cvEntity.getExperience().get(0).getIsOngoing());
    }

    @Test
    void addExperience_extracurricularField_addsExtracurricular() throws CVNotFoundException, FieldNotFoundException {
        String cvId = "3";
        CVEntity cvEntity = new CVEntity(cvId, "user3");
        CVCompCreation compCreation = new CVCompCreation("extracurricular", LocalDate.of(2020, 3, 1), LocalDate.of(2021, 3, 1), "Club", "Organization", "Desc");

        when(cvRepository.findById(cvId)).thenReturn(Optional.of(cvEntity));
        when(cvRepository.save(any(CVEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CV result = cvService.addExperience(cvId, compCreation);

        assertEquals(cvId, result.getId());
        assertEquals(1, cvEntity.getExtracurricular().size());
        assertEquals("Club", cvEntity.getExtracurricular().get(0).getTitle());
    }

    @Test
    void addExperience_projectField_addsProject() throws CVNotFoundException, FieldNotFoundException {
        String cvId = "4";
        CVEntity cvEntity = new CVEntity(cvId, "user4");
        CVCompCreation compCreation = new CVCompCreation("project", LocalDate.of(2021, 6, 1), null, "ProjectX", "CompanyX", "Desc");

        when(cvRepository.findById(cvId)).thenReturn(Optional.of(cvEntity));
        when(cvRepository.save(any(CVEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CV result = cvService.addExperience(cvId, compCreation);

        assertEquals(cvId, result.getId());
        assertEquals(1, cvEntity.getProjects().size());
        assertEquals("ProjectX", cvEntity.getProjects().get(0).getTitle());
        assertTrue(cvEntity.getProjects().get(0).getIsOngoing());
    }

    @Test
    void addExperience_invalidField_throwsFieldNotFoundException() {
        String cvId = "5";
        CVEntity cvEntity = new CVEntity(cvId, "user5");
        CVCompCreation compCreation = new CVCompCreation("invalidField", LocalDate.of(2020, 1, 1), "Title", "Institution", "Desc");

        when(cvRepository.findById(cvId)).thenReturn(Optional.of(cvEntity));

        assertThrows(FieldNotFoundException.class, () -> {
            cvService.addExperience(cvId, compCreation);
        });
    }

    @Test
    void addExperience_cvNotFound_throwsCVNotFoundException() {
        String cvId = "nonexistent";
        CVCompCreation compCreation = new CVCompCreation("experience", LocalDate.of(2020, 1, 1), "Title", "Institution", "Desc");

        when(cvRepository.findById(cvId)).thenReturn(Optional.empty());

        assertThrows(CVNotFoundException.class, () -> {
            cvService.addExperience(cvId, compCreation);
        });
    }

    @Test
    void addTag_skillField_addsSkill() throws CVNotFoundException, FieldNotFoundException {
        String cvId = "10";
        CVEntity cvEntity = new CVEntity(cvId, "user10");
        String tag = "Java";

        when(cvRepository.findById(cvId)).thenReturn(Optional.of(cvEntity));
        when(cvRepository.save(any(CVEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CV result = cvService.addTag(cvId, "skill", tag);

        assertEquals(cvId, result.getId());
        assertEquals(1, cvEntity.getSkills().size());
        assertEquals(tag, cvEntity.getSkills().get(0));
    }

    @Test
    void addTag_toolField_addsTool() throws CVNotFoundException, FieldNotFoundException {
        String cvId = "11";
        CVEntity cvEntity = new CVEntity(cvId, "user11");
        String tag = "Git";

        when(cvRepository.findById(cvId)).thenReturn(Optional.of(cvEntity));
        when(cvRepository.save(any(CVEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CV result = cvService.addTag(cvId, "tool", tag);

        assertEquals(cvId, result.getId());
        assertEquals(1, cvEntity.getTools().size());
        assertEquals(tag, cvEntity.getTools().get(0));
    }

    @Test
    void addTag_languageField_addsLanguage() throws CVNotFoundException, FieldNotFoundException {
        String cvId = "12";
        CVEntity cvEntity = new CVEntity(cvId, "user12");
        String tag = "English";

        when(cvRepository.findById(cvId)).thenReturn(Optional.of(cvEntity));
        when(cvRepository.save(any(CVEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CV result = cvService.addTag(cvId, "language", tag);

        assertEquals(cvId, result.getId());
        assertEquals(1, cvEntity.getLanguages().size());
        assertEquals(tag, cvEntity.getLanguages().get(0));
    }

    @Test
    void addTag_invalidField_throwsFieldNotFoundException() {
        String cvId = "13";
        CVEntity cvEntity = new CVEntity(cvId, "user13");

        when(cvRepository.findById(cvId)).thenReturn(Optional.of(cvEntity));

        assertThrows(FieldNotFoundException.class, () -> {
            cvService.addTag(cvId, "invalidField", "tag");
        });
    }

    @Test
    void addTag_cvNotFound_throwsCVNotFoundException() {
        String cvId = "nonexistent";

        when(cvRepository.findById(cvId)).thenReturn(Optional.empty());

        assertThrows(CVNotFoundException.class, () -> {
            cvService.addTag(cvId, "skill", "Java");
        });
    }

    @Test
    void removeExperience_education_removesItem() throws Exception {
        String cvId = "1";
        CVEntity cvEntity = new CVEntity(cvId, "user1");
        CVComponent comp = new CVComponent(LocalDate.now(), "Title", "Inst", "Desc");
        cvEntity.addEducation(comp);

        when(cvRepository.findById(cvId)).thenReturn(Optional.of(cvEntity));
        when(cvRepository.save(any(CVEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CV result = cvService.removeExperience(cvId, "education", 0);

        assertEquals(cvId, result.getId());
        assertTrue(cvEntity.getEducation().isEmpty());
    }

    @Test
    void removeExperience_experience_removesItem() throws Exception {
        String cvId = "2";
        CVEntity cvEntity = new CVEntity(cvId, "user2");
        CVComponent comp = new CVComponent(LocalDate.now(), "Title", "Inst", "Desc");
        cvEntity.addExperience(comp);

        when(cvRepository.findById(cvId)).thenReturn(Optional.of(cvEntity));
        when(cvRepository.save(any(CVEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CV result = cvService.removeExperience(cvId, "experience", 0);

        assertEquals(cvId, result.getId());
        assertTrue(cvEntity.getExperience().isEmpty());
    }

    @Test
    void removeExperience_extracurricular_removesItem() throws Exception {
        String cvId = "3";
        CVEntity cvEntity = new CVEntity(cvId, "user3");
        CVComponent comp = new CVComponent(LocalDate.now(), "Title", "Inst", "Desc");
        cvEntity.addExtracurricular(comp);

        when(cvRepository.findById(cvId)).thenReturn(Optional.of(cvEntity));
        when(cvRepository.save(any(CVEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CV result = cvService.removeExperience(cvId, "extracurricular", 0);

        assertEquals(cvId, result.getId());
        assertTrue(cvEntity.getExtracurricular().isEmpty());
    }

    @Test
    void removeExperience_project_removesItem() throws Exception {
        String cvId = "4";
        CVEntity cvEntity = new CVEntity(cvId, "user4");
        CVComponent comp = new CVComponent(LocalDate.now(), "Title", "Inst", "Desc");
        cvEntity.addProject(comp);

        when(cvRepository.findById(cvId)).thenReturn(Optional.of(cvEntity));
        when(cvRepository.save(any(CVEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CV result = cvService.removeExperience(cvId, "project", 0);

        assertEquals(cvId, result.getId());
        assertTrue(cvEntity.getProjects().isEmpty());
    }

    @Test
    void removeExperience_invalidField_throwsFieldNotFoundException() {
        String cvId = "5";
        CVEntity cvEntity = new CVEntity(cvId, "user5");

        when(cvRepository.findById(cvId)).thenReturn(Optional.of(cvEntity));

        assertThrows(FieldNotFoundException.class, () -> {
            cvService.removeExperience(cvId, "invalidField", 0);
        });
    }

    @Test
    void removeExperience_indexOutOfBounds_throwsIndexNotFoundException() {
        String cvId = "6";
        CVEntity cvEntity = new CVEntity(cvId, "user6");
        // no education added, so index 0 invalid

        when(cvRepository.findById(cvId)).thenReturn(Optional.of(cvEntity));

        assertThrows(IndexNotFoundException.class, () -> {
            cvService.removeExperience(cvId, "education", 0);
        });
    }

    @Test
    void removeExperience_cvNotFound_throwsCVNotFoundException() {
        String cvId = "missing";

        when(cvRepository.findById(cvId)).thenReturn(Optional.empty());

        assertThrows(CVNotFoundException.class, () -> {
            cvService.removeExperience(cvId, "education", 0);
        });
    }


    @Test
    void removeTag_skill_removesItem() throws Exception {
        String cvId = "10";
        CVEntity cvEntity = new CVEntity(cvId, "user10");
        cvEntity.addSkill("Java");

        when(cvRepository.findById(cvId)).thenReturn(Optional.of(cvEntity));
        when(cvRepository.save(any(CVEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CV result = cvService.removeTag(cvId, "skill", 0);

        assertEquals(cvId, result.getId());
        assertTrue(cvEntity.getSkills().isEmpty());
    }

    @Test
    void removeTag_tool_removesItem() throws Exception {
        String cvId = "11";
        CVEntity cvEntity = new CVEntity(cvId, "user11");
        cvEntity.addTool("Git");

        when(cvRepository.findById(cvId)).thenReturn(Optional.of(cvEntity));
        when(cvRepository.save(any(CVEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CV result = cvService.removeTag(cvId, "tool", 0);

        assertEquals(cvId, result.getId());
        assertTrue(cvEntity.getTools().isEmpty());
    }

    @Test
    void removeTag_language_removesItem() throws Exception {
        String cvId = "12";
        CVEntity cvEntity = new CVEntity(cvId, "user12");
        cvEntity.addLanguage("English");

        when(cvRepository.findById(cvId)).thenReturn(Optional.of(cvEntity));
        when(cvRepository.save(any(CVEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CV result = cvService.removeTag(cvId, "language", 0);

        assertEquals(cvId, result.getId());
        assertTrue(cvEntity.getLanguages().isEmpty());
    }

    @Test
    void removeTag_invalidField_throwsFieldNotFoundException() {
        String cvId = "13";
        CVEntity cvEntity = new CVEntity(cvId, "user13");

        when(cvRepository.findById(cvId)).thenReturn(Optional.of(cvEntity));

        assertThrows(FieldNotFoundException.class, () -> {
            cvService.removeTag(cvId, "invalidField", 0);
        });
    }

    @Test
    void removeTag_indexOutOfBounds_throwsIndexNotFoundException() {
        String cvId = "14";
        CVEntity cvEntity = new CVEntity(cvId, "user14");
        // no skills, so index 0 invalid

        when(cvRepository.findById(cvId)).thenReturn(Optional.of(cvEntity));

        assertThrows(IndexNotFoundException.class, () -> {
            cvService.removeTag(cvId, "skill", 0);
        });
    }

    @Test
    void removeTag_cvNotFound_throwsCVNotFoundException() {
        String cvId = "missing";

        when(cvRepository.findById(cvId)).thenReturn(Optional.empty());

        assertThrows(CVNotFoundException.class, () -> {
            cvService.removeTag(cvId, "skill", 0);
        });
    }
}
