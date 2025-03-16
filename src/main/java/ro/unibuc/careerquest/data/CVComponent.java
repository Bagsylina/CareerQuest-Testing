package ro.unibuc.careerquest.data;

import java.time.LocalDate;

public class CVComponent {

    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isOngoing;

    private String title;
    private String institution;
    private String description;

    public CVComponent() {}
    
    public CVComponent(LocalDate startDate, String title, String institution, String description) {
        this.startDate = startDate;
        this.isOngoing = true;
        this.title = title;
        this.institution = institution;
        this.description = description;
    }

    public CVComponent(LocalDate startDate, LocalDate endDate, String title, String institution, String description) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.isOngoing = false;
        this.title = title;
        this.institution = institution;
        this.description = description;
    }

    public LocalDate getStartDate() {return startDate;}
    public LocalDate getEndDate() {return endDate;}
    public boolean getIsOngoing() {return isOngoing;}
    public void setStartDate(LocalDate startDate) {this.startDate = startDate;}
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        this.isOngoing = false;
    }

    public String getTitle() {return title;}
    public String getInstitution() {return institution;}
    public String getDescription() {return description;}
    public void setTitle(String title) {this.title = title;}
    public void setInstitution(String institution) {this.institution = institution;}
    public void setDescription(String description) {this.description = description;}
}
