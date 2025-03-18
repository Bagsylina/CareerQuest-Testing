package ro.unibuc.careerquest.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.unibuc.careerquest.dto.CVCompCreation;
import ro.unibuc.careerquest.data.CVComponent;
import ro.unibuc.careerquest.data.CVEntity;
import ro.unibuc.careerquest.data.CVRepository;
import ro.unibuc.careerquest.dto.CV;
import ro.unibuc.careerquest.dto.CVCreation;
import ro.unibuc.careerquest.exception.CVNotFoundException;
import ro.unibuc.careerquest.exception.FieldNotFoundException;
import ro.unibuc.careerquest.exception.IndexNotFoundException;

@Component
public class CVService {
    
    @Autowired
    private CVRepository cvRepository;

    public CV updateCV(String id, CVCreation cvData) throws CVNotFoundException {
        Optional<CVEntity> optionalCV = cvRepository.findById(id);
        CVEntity cv = optionalCV.orElseThrow(() -> new CVNotFoundException(id));

        if(cvData.getDescription() != null)
            cv.setDescription(cvData.getDescription());
        if(cvData.getAchievements() != null)
            cv.setAchievements(cvData.getAchievements());

        cvRepository.save(cv);

        return new CV(cv.getId(), cv.getUserId(), cv.getDescription(), cv.getAchievements(), cv.getEducation(), cv.getExperience(),
                    cv.getExtracurricular(), cv.getProjects(), cv.getSkills(), cv.getTools(), cv.getLanguages());
    }

    public CV addExperience(String id, CVCompCreation expData) throws CVNotFoundException, FieldNotFoundException {
        Optional<CVEntity> optionalCV = cvRepository.findById(id);
        CVEntity cv = optionalCV.orElseThrow(() -> new CVNotFoundException(id));

        CVComponent experience;

        if(expData.getEndDate() != null)
            experience = new CVComponent(expData.getStartDate(), expData.getEndDate(), expData.getTitle(), expData.getInstitution(), expData.getDescription());
        else
            experience = new CVComponent(expData.getStartDate(), expData.getTitle(), expData.getInstitution(), expData.getDescription());

        switch(expData.getField()) {
            case "education": 
                cv.addEducation(experience);
                break;
            case "experience":
                cv.addExperience(experience);
                break;
            case "extracurricular":
                cv.addExtracurricular(experience);
                break;
            case "project":
                cv.addProject(experience);
                break;
            default:
                throw new FieldNotFoundException(expData.getField());
        }

        cvRepository.save(cv);

        return new CV(cv.getId(), cv.getUserId(), cv.getDescription(), cv.getAchievements(), cv.getEducation(), cv.getExperience(),
                    cv.getExtracurricular(), cv.getProjects(), cv.getSkills(), cv.getTools(), cv.getLanguages());
    }

    public CV addTag(String id, String field, String tag) throws CVNotFoundException, FieldNotFoundException {
        Optional<CVEntity> optionalCV = cvRepository.findById(id);
        CVEntity cv = optionalCV.orElseThrow(() -> new CVNotFoundException(id));

        switch(field) {
            case "skill": 
                cv.addSkill(tag);
                break;
            case "tool":
                cv.addTool(tag);
                break;
            case "language":
                cv.addLanguage(tag);
                break;
            default:
                throw new FieldNotFoundException(field);
        }

        cvRepository.save(cv);

        return new CV(cv.getId(), cv.getUserId(), cv.getDescription(), cv.getAchievements(), cv.getEducation(), cv.getExperience(),
                    cv.getExtracurricular(), cv.getProjects(), cv.getSkills(), cv.getTools(), cv.getLanguages());
    }

    public CV removeExperience(String id, String field, int i) throws CVNotFoundException, FieldNotFoundException, IndexNotFoundException {
        Optional<CVEntity> optionalCV = cvRepository.findById(id);
        CVEntity cv = optionalCV.orElseThrow(() -> new CVNotFoundException(id));

        switch(field) {
            case "education": 
                List<CVComponent> education = cv.getEducation();
                if(education.size() <= i)
                    throw new IndexNotFoundException(Integer.toString(i));
                education.remove(i);
                break;
            case "experience":
                List<CVComponent> experience = cv.getExperience();
                if(experience.size() <= i)
                    throw new IndexNotFoundException(Integer.toString(i));
                experience.remove(i);
                break;
            case "extracurricular":
                List<CVComponent> extracurricular = cv.getExtracurricular();
                if(extracurricular.size() <= i)
                    throw new IndexNotFoundException(Integer.toString(i));
                extracurricular.remove(i);
                break;
            case "project":
                List<CVComponent> projects = cv.getProjects();
                if(projects.size() <= i)
                    throw new IndexNotFoundException(Integer.toString(i));
                projects.remove(i);
                break;
            default:
                throw new FieldNotFoundException(field);
        }

        cvRepository.save(cv);

        return new CV(cv.getId(), cv.getUserId(), cv.getDescription(), cv.getAchievements(), cv.getEducation(), cv.getExperience(),
                    cv.getExtracurricular(), cv.getProjects(), cv.getSkills(), cv.getTools(), cv.getLanguages());
    }

    public CV removeTag(String id, String field, int i) throws CVNotFoundException, FieldNotFoundException, IndexNotFoundException {
        Optional<CVEntity> optionalCV = cvRepository.findById(id);
        CVEntity cv = optionalCV.orElseThrow(() -> new CVNotFoundException(id));

        switch(field) {
            case "skill": 
                List<String> skills = cv.getSkills();
                if(skills.size() <= i)
                    throw new IndexNotFoundException(Integer.toString(i));
                skills.remove(i);
                break;
            case "tool":
                List<String> tools = cv.getTools();
                if(tools.size() <= i)
                    throw new IndexNotFoundException(Integer.toString(i));
                tools.remove(i);
                break;
            case "language":
                List<String> languages = cv.getLanguages();
                if(languages.size() <= i)
                    throw new IndexNotFoundException(Integer.toString(i));
                languages.remove(i);
                break;
            default:
                throw new FieldNotFoundException(field);
        }

        cvRepository.save(cv);

        return new CV(cv.getId(), cv.getUserId(), cv.getDescription(), cv.getAchievements(), cv.getEducation(), cv.getExperience(),
                    cv.getExtracurricular(), cv.getProjects(), cv.getSkills(), cv.getTools(), cv.getLanguages());
    }

    public void deleteCV(String id) throws CVNotFoundException {
        Optional<CVEntity> optionalCV = cvRepository.findById(id);
        CVEntity cv = optionalCV.orElseThrow(() -> new CVNotFoundException(id));

        cvRepository.delete(cv);
    }
}
