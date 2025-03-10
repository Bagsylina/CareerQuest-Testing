package ro.unibuc.careerquest.data;

import org.springframework.data.annotation.Id;

import ro.unibuc.careerquest.dto.Job;

public class JobEntity {

    @Id
    private String id;

    private String title;
    private String description;
    private String company;
    private String employer; // !!
    private String[] abilities; // !! necessary abilities tags
    private String[] domains; // !! domain tags
    private String[] characteristics; // !! other characteristics tags
    private Integer salary;
    private String location;

    public JobEntity() {}

    public JobEntity(String id, String title, String description, String company, String employer, String[] abilities, String[] domains, String[] characteristics, Integer salary, String location) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.company = company;
        this.employer = employer;
        this.abilities = abilities; // here a copy??
        this.domains = domains;
        this.characteristics = characteristics;
        this.salary = salary;
        this.location = location;
    }

    public JobEntity(Job job) {
        this.id = job.getId();
        setContents(job);
    }

    public JobEntity(String id, Job job) {
        this.id = id;
        setContents(job);
    }

    public void setContents(Job job) {
        this.title = job.getTitle();
        this.description = job.getDescription();
        this.company = job.getCompany();
        this.employer = job.getEmployer();
        this.abilities = job.getAbilities();
        this.domains = job.getDomains();
        this.characteristics = job.getCharacteristics();
        this.salary = job.getSalary();
        this.location = job.getLocation();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    @Override
    public String toString() {
        return String.format(
                "Information[id='%s', title='%s', description='%s']",
                id, title, description);
    }
}
