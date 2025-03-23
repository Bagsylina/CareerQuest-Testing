package ro.unibuc.careerquest.dto;
import java.util.List;
import java.util.ArrayList;

public class JobContent {
    //private String id;
    private String title;
    private String description;
    private String employer; 
    private List<String> abilities = new ArrayList<>(); 
    private List<String> domains = new ArrayList<>(); 
    private List<String> characteristics = new ArrayList<>(); 
    private Integer salary;
    private String location;

    public JobContent() {}

    public JobContent(String title, String description, String employer, List<String> abilities, List<String> domains, List<String> characteristics, Integer salary, String location) {
        //this.id = id;
        this.title = title;
        this.description = description;
        this.employer = employer;
        this.abilities = new ArrayList<>(abilities);
        this.domains = new ArrayList<>(domains);
        this.characteristics = new ArrayList<>(characteristics);
        this.salary = salary;
        this.location = location;
    }

    // public String getId() { return id;}
    // public void setId(String id) { this.id = id;}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public List<String> getAbilities() {
        return abilities;
    }

    public void setAbilities(List<String> abilities) {
        this.abilities = new ArrayList<>(abilities);
    }

    public List<String> getDomains() {
        return domains;
    }

    public void setDomains(List<String> domains) {
        this.domains = new ArrayList<>(domains);
    }

    public List<String> getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(List<String> characteristics) {
        this.characteristics = new ArrayList<>(characteristics);
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
