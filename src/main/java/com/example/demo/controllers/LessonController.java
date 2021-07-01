package com.example.demo.controllers;

import com.example.demo.Repository.LessonRepository;
import com.example.demo.domain.Lesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LessonController {

    @Autowired
    private LessonRepository _lessonRepository;

    @GetMapping("/lesson/{id}")
    public Lesson getLessonById(@PathVariable long id){
        return this._lessonRepository.findById(id).get();
    }

    @DeleteMapping("/lesson/{id}")
    public Lesson deleteLessonById(@PathVariable long id){
        final Lesson lesson = this.getLessonById(id);
        this._lessonRepository.deleteById(id);
        return lesson;
    }

}
