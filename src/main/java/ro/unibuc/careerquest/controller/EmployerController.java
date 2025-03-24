package ro.unibuc.careerquest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ro.unibuc.careerquest.dto.Employer;
import ro.unibuc.careerquest.service.EmployerService;
//import ro.unibuc.hello.dto.Greeting;
import ro.unibuc.careerquest.exception.EntityNotFoundException;
//import ro.unibuc.careerquest.service.GreetingsService;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class EmployerController {
    @Autowired
    private EmployerService empsService;

    //list of all the emplyers
    @GetMapping("/employer")
    @ResponseBody
    public List<Employer> getAllEmployers() {
        return empsService.getAllEmployers();
    }

    //return one employer
    @GetMapping("/employer/{id}")
    @ResponseBody
    public Employer getEmployer(@PathVariable String id) throws EntityNotFoundException {
        return empsService.getEmployerById(id);
    }

    //add new employer
    @PostMapping("/employer")
    @ResponseBody
    public Employer createEmployer(@RequestBody Employer employer) {
        return empsService.saveEmployer(employer);
    }

    //update employer
    @PutMapping("/employer/{id}")
    @ResponseBody
    public Employer updateEmployer(@PathVariable String id, @RequestBody Employer employer) throws EntityNotFoundException {
        return empsService.updateEmployer(id, employer);
    }

    //delete employer
    @DeleteMapping("/employer/{id}")
    @ResponseBody
    public void deleteEmployer(@PathVariable String id) throws EntityNotFoundException {
        empsService.deleteEmployer(id);
    }

    //some employer is paying for premium
    @PutMapping("/employer/{id}/pay")
    @ResponseBody
    public Employer payForPremium(@PathVariable String id) {
        Employer employer = empsService.updatePayment(id,LocalDate.now(), true);
        return empsService.saveEmployer(employer);
    }

}
