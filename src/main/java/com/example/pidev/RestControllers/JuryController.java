package com.example.pidev.RestControllers;

import com.example.pidev.DAO.Entities.Jury;
import com.example.pidev.Services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/DanceEscape/juries")
public class JuryController {

    private final ProjectService projectService;

    @Autowired
    public JuryController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Jury> getJuryById(@PathVariable Long id) {
        Jury jury = projectService.findJuryById(id);
        if (jury == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(jury, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Jury> createJury(@RequestBody Jury jury) {
        Jury createdJury = projectService.saveJury(jury);
        return new ResponseEntity<>(createdJury, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Jury> updateJury(@PathVariable Long id, @RequestBody Jury jury) {
        Jury existingJury = projectService.findJuryById(id);
        if (existingJury == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        jury.setId(id);
        Jury updatedJury = projectService.saveJury(jury);
        return new ResponseEntity<>(updatedJury, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJury(@PathVariable Long id) {
        projectService.deleteJuryById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /*@GetMapping("/categories/{categoryId}")
    public ResponseEntity<List<Jury>> getJuriesByCategoryId(@PathVariable Long categoryId) {
        List<Jury> juries = projectService.findJuriesByCategoryId(categoryId);
        return new ResponseEntity<>(juries, HttpStatus.OK);
    }*/
}