package ro.unibuc.careerquest.data;

import java.util.ArrayList;
import java.util.List;

import ro.unibuc.careerquest.data.CVComponent;

import org.springframework.data.annotation.Id;

public class EmployerEntity {

    @Id
    private String id;
    private String name;
    private String email;
    private String phone;
    private String company;

    public EmployerEntity() {}

    public EmployerEntity(String id, String name, String email, String phone, String company) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.company = company;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

}


