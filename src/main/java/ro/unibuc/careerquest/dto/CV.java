package ro.unibuc.careerquest.dto;

import java.util.ArrayList;
import java.util.List;

import ro.unibuc.careerquest.data.CVComponent;

public class CV {

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

    public CV() {}

    public CV(String id, String userId) {
        this.id = id;
        this.userId = userId;
    }

    public CV(String id, String userId, String description, String achievements) {
        this.id = id;
        this.userId = userId;
        this.description = description;
        this.achievements = achievements;
    }

    public CV(String id, String userId, String description, String achievements, List<CVComponent> education, List<CVComponent> experience,
            List<CVComponent> extracurricular, List<CVComponent> projects, List<String> skills, List<String> tools, List<String> languages) {
        this.id = id;
        this.userId = userId;
        this.description = description;
        this.achievements = achievements;
        this.education = new ArrayList<>(education);
        this.experience = new ArrayList<>(experience);
        this.extracurricular = new ArrayList<>(extracurricular);
        this.projects = new ArrayList<>(projects);
        this.skills = new ArrayList<>(skills);
        this.tools = new ArrayList<>(tools);
        this.languages = new ArrayList<>(languages);
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
