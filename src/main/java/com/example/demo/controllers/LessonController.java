package com.example.demo.controllers;

import com.example.demo.Repository.LessonRepository;
import com.example.demo.domain.Lesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

    @GetMapping(
            value = "/lesson/find/{title}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Lesson getLessonByTitle(@PathVariable String title){
        return this._lessonRepository.findByName(title);
    }

    @GetMapping(
            value = "/lesson/between",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Lesson> getLessonByDateRange(
            @RequestParam(value = "startDate") String startDateString,
            @RequestParam(value = "endDate") String endDateString
            ){

        final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        final LocalDate startDate = LocalDate.parse(startDateString, dateFormatter);
        final LocalDate endDate = LocalDate.parse(endDateString, dateFormatter);

        return this._lessonRepository.findByDeliveredOnBetween(startDate, endDate);
    }

    @GetMapping(
            value = {"/lesson", "/lesson/"},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Lesson> getLessons(){
        final List<Lesson> lessons = new ArrayList<Lesson>();
        this._lessonRepository.findAll().forEach( lessons::add );
        return lessons;
    }

}
