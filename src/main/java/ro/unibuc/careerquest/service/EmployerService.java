package main.java.ro.unibuc.careerquest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import main.java.ro.unibuc.careerquest.dto.Employer;
import ro.unibuc.careerquest.data.EmployerEntity;
import ro.unibuc.careerquest.data.EmployerRepository;

import ro.unibuc.careerquest.exception.EntityNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class EmployerService {

    @Autowired
    private EmployerRepository employerRepository;

    private final AtomicLong counter = new AtomicLong();
    private static final String helloTemplate = "Hello, %s!";
    private static final String informationTemplate = "%s : %s!";


    public Employer buildEmployerFromInfo(String name) throws EntityNotFoundException {
        EmployerEntity entity = employerRepository.findByName(name);
        if (entity == null) {
            throw new EntityNotFoundException(name);
        }
        return new Employer(Long.toString(counter.incrementAndGet()), String.format(informationTemplate, entity.getName(), entity.getCompany()));
    }

    public List<Employer> getAllEmployers() {
        List<EmployerEntity> entities = employerRepository.findAll();
        return entities.stream()
                .map(entity -> new Employer(entity.getId(), entity.getName()))
                .collect(Collectors.toList());
    }

    // public Employer getEmployerById(String id) throws EntityNotFoundException {
    //     Optional<EmployerEntity> optionalEntity = employerRepository.findById(id);
    //     EmployerEntity entity = optionalEntity.orElseThrow(() -> new EntityNotFoundException(id));
    //     return new Employer(entity.getId(), entity.getName());
    // }

    public Employer getEmployerById(String employerId) {
        // Căutăm employer-ul după ID
        EmployerEntity entity = employerRepository.findById(employerId)
                .orElseThrow(() -> new EntityNotFoundException("Employer not found"));
    
        // Returnăm un obiect Employer pe baza entității găsite
        return new Employer(entity.getId(), entity.getName(), entity.getEmail(), entity.getPhone(), entity.getCompany(),entity.getLastPaymentDate(),entity.isPremium());
    }
    

    public Employer saveEmployer(Employer employer) {
        EmployerEntity entity = new EmployerEntity();
        entity.setId(employer.getId());
        entity.setName(employer.getName());
        employerRepository.save(entity);
        return new Employer(entity.getId(), entity.getName());
    }

    public List<Employer> saveAll(List<Employer> employers) {
        List<EmployerEntity> entities = employers.stream()
                .map(emp -> {
                    EmployerEntity entity = new EmployerEntity();
                    entity.setId(emp.getId());
                    entity.setName(emp.getName());
                    return entity;
                })
                .collect(Collectors.toList());

        List<EmployerEntity> savedEntities = employerRepository.saveAll(entities);

        return savedEntities.stream()
                .map(entity -> new Employer(entity.getId(), entity.getName()))
                .collect(Collectors.toList());
    }

    public Employer updateEmployer(String id, Employer emp) throws EntityNotFoundException {
        EmployerEntity entity = employerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
        entity.setName(emp.getName());
        employerRepository.save(entity);
        return new Employer(entity.getId(), entity.getName());
    }
    public Employer updatePayment( String id, LocalDate lastPaymentDate , boolean premiu) throws EntityNotFoundException {
        EmployerEntity entity = employerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
        entity.setLastPaymentDate(lastPaymentDate);
        entity.setPremium(premiu);
        employerRepository.save(entity);
        return new Employer(entity.getId(), entity.getName());
    }

    public void deleteEmployer(String id) throws EntityNotFoundException {
        EmployerEntity entity = employerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
        employerRepository.delete(entity);
    }

    public void deleteAllEmplyers() {
        employerRepository.deleteAll();
    }
    
}
