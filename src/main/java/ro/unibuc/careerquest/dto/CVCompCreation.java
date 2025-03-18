package ro.unibuc.careerquest.dto;

import java.time.LocalDate;

public class CVCompCreation {

    private String field;

    private LocalDate startDate;
    private LocalDate endDate;

    private String title;
    private String institution;
    private String description;

    public CVCompCreation() {}
    
    public CVCompCreation(String field, LocalDate startDate, String title, String institution, String description) {
        this.field = field;
        this.startDate = startDate;
        this.title = title;
        this.institution = institution;
        this.description = description;
    }

    public CVCompCreation(String field, LocalDate startDate, LocalDate endDate, String title, String institution, String description) {
        this.field = field;
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.institution = institution;
        this.description = description;
    }

    public String getField() {return field;}

    public LocalDate getStartDate() {return startDate;}
    public LocalDate getEndDate() {return endDate;}
    public void setStartDate(LocalDate startDate) {this.startDate = startDate;}
    public void setEndDate(LocalDate endDate) {this.endDate = endDate;}

    public String getTitle() {return title;}
    public String getInstitution() {return institution;}
    public String getDescription() {return description;}
    public void setTitle(String title) {this.title = title;}
    public void setInstitution(String institution) {this.institution = institution;}
    public void setDescription(String description) {this.description = description;}
}
