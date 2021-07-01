package com.example.demo.controllers;

import com.example.demo.Repository.LessonRepository;
import com.example.demo.domain.Lesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class LessonController {

    @Autowired
    private LessonRepository _lessonRepository;

    @GetMapping(
            value = "/lesson/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Lesson getLessonById(@PathVariable long id){
        return this._lessonRepository.findById(id).get();
    }

    @DeleteMapping(
            value = "/lesson/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Lesson deleteLessonById(@PathVariable long id){
        final Lesson lesson = this.getLessonById(id);
        this._lessonRepository.deleteById(id);
        return lesson;
    }

    @PostMapping(
            value = "/lesson",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Lesson createLesson(@RequestBody Lesson lesson){
        return this._lessonRepository.save(lesson);
    }

    @PatchMapping(
            value = "/lesson/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Lesson createLesson(@PathVariable long id, @RequestBody Lesson lesson){
        final Lesson lessonToUpdate = this.getLessonById(id);
        lessonToUpdate.setDeliveredOn(lesson.getDeliveredOn());
        lessonToUpdate.setName(lesson.getName());
        return this._lessonRepository.save(lessonToUpdate);
    }

}
