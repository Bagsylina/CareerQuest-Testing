package ro.unibuc.careerquest.dto;

public class CVCreation {

    private String description;
    private String achievements;

    public CVCreation() {}

    public CVCreation(String description, String achievements) {
        this.description = description;
        this.achievements = achievements;
    }

    public String getDescription() {return description;}
    public String getAchievements() {return achievements;}
    public void setDescription(String description) {this.description = description;}
    public void setAchievements(String achievements) {this.achievements = achievements;}
}
