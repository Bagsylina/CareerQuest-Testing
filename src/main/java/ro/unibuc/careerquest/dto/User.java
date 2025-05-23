package ro.unibuc.careerquest.dto;

import java.time.Period;

import com.fasterxml.jackson.annotation.JsonFormat;

import ro.unibuc.careerquest.data.UserEntity;

import java.time.LocalDate;

import ro.unibuc.careerquest.data.UserEntity;

public class User {

    private String username;

    private String description;

    private String firstName;
    private String lastName;
    private String fullName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthdate;
    private int age;
    
    private String email;
    private String phone;

    public User() {}

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public User(String username, String description, String firstName, String lastName, LocalDate birthdate, String email, String phone) {
        this.username = username;
        this.description = description;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = firstName + " " + lastName;
        this.birthdate = birthdate;
        if(birthdate != null) {
            LocalDate currentDate = LocalDate.now();
            this.age = Period.between(birthdate, currentDate).getYears();
        }
        else
            this.age = 0;
        this.email = email;
        this.phone = phone;
    }

    public User(UserEntity user) {
        this.username = user.getUsername();
        this.description = user.getDescription();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.fullName = user.getFullName();
        this.birthdate = user.getBirthdate();
        if(birthdate != null) {
            LocalDate currentDate = LocalDate.now();
            this.age = Period.between(birthdate, currentDate).getYears();
        }
        else
            this.age = 0;
        this.email = user.getEmail();
        this.phone = user.getPhone();
    }

    public String getUsername() {return username;}
    
    public String getName() {return fullName;}
    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public void setFirstName(String firstName) {
        this.firstName = firstName;
        this.fullName = firstName + " " + lastName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
        this.fullName = firstName + " " + lastName;
    }

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public LocalDate getBirthdate() {return birthdate;}
    public int getAge() {return age;}
    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
        if(birthdate != null) {
            LocalDate currentDate = LocalDate.now();
            this.age = Period.between(birthdate, currentDate).getYears();
        }
        else
            this.age = 0;
    } 

    public String getEmail() {return email;}
    public String getPhone() {return phone;}
    public void setEmail(String email) {this.email = email;}
    public void setPhone(String phone) {this.phone = phone;}
}
