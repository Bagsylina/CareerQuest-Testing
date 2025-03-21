package ro.unibuc.careerquest.data;


public class JobContent {
    //private String id;
    private String title;
    private String description;
    private String employer; // !!
    private String[] abilities; // !! necessary abilities tags
    private String[] domains; // !! domain tags
    private String[] characteristics; // !! other characteristics tags
    private Integer salary;
    private String location;

    public JobContent() {}

    public JobContent(String title, String description, String employer, String[] abilities, String[] domains, String[] characteristics, Integer salary, String location) {
        //this.id = id;
        this.title = title;
        this.description = description;
        this.employer = employer;
        this.abilities = abilities; // here a copy??
        this.domains = domains;
        this.characteristics = characteristics;
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

    public String[] getAbilities() {
        return abilities;
    }

    public void setAbilities(String[] abilities) {
        this.abilities = abilities;
    }

    public String[] getDomains() {
        return domains;
    }

    public void setDomains(String[] domains) {
        this.domains = domains;
    }

    public String[] getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(String[] characteristics) {
        this.characteristics = characteristics;
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
