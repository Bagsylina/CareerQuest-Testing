package ro.unibuc.careerquest.controller;

import java.text.FieldPosition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

//import ro.unibuc.careerquest.dto.CVCompCreation;
import ro.unibuc.careerquest.data.CVComponent;
import ro.unibuc.careerquest.dto.CV;
import ro.unibuc.careerquest.dto.CVCreation;
import ro.unibuc.careerquest.exception.CVNotFoundException;
import ro.unibuc.careerquest.exception.IndexNotFoundException;
import ro.unibuc.careerquest.exception.FieldNotFoundException;
import ro.unibuc.careerquest.service.CVService;

import org.springframework.web.bind.annotation.*;

@Controller
public class CVController {

    @Autowired
    private CVService cvService;

    @PutMapping("/cv/{id}")
    @ResponseBody
    public CV updateCV(@PathVariable String id, @RequestBody CVCreation cv) throws CVNotFoundException {
        return cvService.updateCV(id, cv);
    }

    // @PutMapping("/cv-add-exp/{id}")
    // @ResponseBody 
    // public CV addExperience(@PathVariable String id, @RequestBody CVCompCreation experience) throws CVNotFoundException, FieldNotFoundException {
    //     return cvService.addExperience(id, experience);
    // }

    @PutMapping("/cv-add-tag/{id}")
    @ResponseBody
    public CV addTag(@PathVariable String id, @RequestParam String field, @RequestParam String tag) throws CVNotFoundException, FieldNotFoundException {
        return cvService.addTag(id, field, tag);
    }

    @PutMapping("/cv-rmv-exp/{id}")
    @ResponseBody
    public CV removeExperience(@PathVariable String id, @RequestParam String field, @RequestParam int i) throws CVNotFoundException, FieldNotFoundException, IndexNotFoundException {
        return cvService.removeExperience(id, field, i);
    }

    @PutMapping("/cv-rmv-tag/{id}")
    @ResponseBody
    public CV removeTag(@PathVariable String id, @RequestParam String field, @RequestParam int i) throws CVNotFoundException, FieldNotFoundException, IndexNotFoundException {
        return cvService.removeTag(id, field, i);
    }
    
    @DeleteMapping("/cv/{id}")
    @ResponseBody void deleteCV(@PathVariable String id) throws CVNotFoundException {
        cvService.deleteCV(id);
    }
}
