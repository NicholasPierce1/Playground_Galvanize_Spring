package com.example.demo.Repository;

import com.example.demo.domain.Lesson;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LessonRepository extends CrudRepository<Lesson, Long> {

    public Lesson findByName(String name);

    //@Query(value = "Select l from Lesson l where delivered_on >= ?1 AND delivered_on <= ?2")
    public List<Lesson> findByDeliveredOnBetween(LocalDate start, LocalDate end);

    @Query(value = "select * " +
            "from Lesson " +
            "where " +
            "delivered_on >= to_date('?1-?2-?3', 'yyyy-MM-dd') " +
            "AND " +
            "delivered_on <= to_date('?4-?5-?6', 'yyyy-MM-dd')" +
            ";", nativeQuery = true)
    public List<Lesson> findByDeliveredOnBetweenDates(
            String startYear, int startMonth, int startDay,
            int endYear, int endMonth, int endDay
    );

}
