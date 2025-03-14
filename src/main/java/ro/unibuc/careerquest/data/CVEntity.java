package ro.unibuc.careerquest.data;

import java.util.ArrayList;
import java.util.List;

import ro.unibuc.careerquest.data.CVComponent;

import org.springframework.data.annotation.Id;

public class CVEntity {

    @Id
    private String id;
    private String userId;

    private String description;
    private String achievements;
    
    private List<CVComponent> education = new ArrayList<>();
    private List<CVComponent> experience = new ArrayList<>();
    private List<CVComponent> extracurricular = new ArrayList<>();
    private List<CVComponent> projects = new ArrayList<>();

    private List<String> skills = new ArrayList<>();
    private List<String> tools = new ArrayList<>();
    private List<String> languages = new ArrayList<>();

    public CVEntity() {}

    public CVEntity(String id, String userId) {
        this.id = id;
        this.userId = userId;
    }

    public CVEntity(String id, String userId, String description, String achievements) {
        this.id = id;
        this.userId = userId;
        this.description = description;
        this.achievements = achievements;
    }

    public String getId() {return id;}
    public String getUserId() {return userId;}

    public String getDescription() {return description;}
    public String getAchievements() {return achievements;}
    public void setDescription(String description) {this.description = description;}
    public void setAchievements(String achievements) {this.achievements = achievements;}

    public List<CVComponent> getEducation() {return education;}
    public List<CVComponent> getExperience() {return experience;}
    public List<CVComponent> getExtracurricular() {return extracurricular;}
    public List<CVComponent> getProjects() {return projects;}
    public void addEducation(CVComponent education) {this.education.add(education);}
    public void addExperience(CVComponent experience) {this.experience.add(experience);}
    public void addExtracurricular(CVComponent extracurricular) {this.extracurricular.add(extracurricular);}
    public void addProject(CVComponent project){this.projects.add(project);}

    public List<String> getSkills() {return skills;}
    public List<String> getTools() {return tools;}
    public List<String> getLanguages() {return languages;}
    public void addSkill(String skill) {this.skills.add(skill);}
    public void addTool(String tool) {this.tools.add(tool);}
    public void addLanguage(String language) {this.languages.add(language);}
}
