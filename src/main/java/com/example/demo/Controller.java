package com.example.demo;

import com.example.demo.domain.BaseStatus;
import com.example.demo.domain.Course;
import com.example.demo.domain.Lesson;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.LessonRepository;
import com.example.demo.specHelper.BitEqual;
import net.kaczmarzyk.spring.data.jpa.domain.*;
import net.kaczmarzyk.spring.data.jpa.web.annotation.*;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Conjunction;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Join;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.JoinType;
import java.util.Calendar;
import java.util.Date;

@RestController
public class Controller {
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;

    public Controller(CourseRepository courseRepository, LessonRepository lessonRepository) {
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
    }

    @GetMapping("/init")
    public ResponseEntity<CoreResponseBody> init() {
        Calendar c = Calendar.getInstance();
        Date date = new Date();
        c.setTime(date);

        Calendar c1 = Calendar.getInstance();
        Date date1 = new Date();
        c1.setTime(date1);
        c1.add(Calendar.HOUR, 2);
        Course course = Course.builder()
                .label("test1")
                .status(BaseStatus.Active)
                .startDate(c.getTime())
                .endDate(c1.getTime())
                .build();

        courseRepository.save(course);

        c1.add(Calendar.HOUR, 4);
        Course course2 = Course.builder()
                .label("test2")
                .startDate(c.getTime())
                .status(BaseStatus.Inactive)
                .endDate(c1.getTime())
                .build();

        courseRepository.save(course2);

        Lesson lesson = new Lesson();
        lesson.setLabel("lesson 1");
        lesson.setStatus(BaseStatus.Active);
        lesson.setCourse(course);

        lessonRepository.save(lesson);

        Lesson lesson1 = new Lesson();
        lesson1.setLabel("lesson 2");
        lesson.setStatus(BaseStatus.Inactive);
        lesson1.setCourse(course);

        lessonRepository.save(lesson1);

        Lesson lesson2 = new Lesson();
        lesson2.setLabel("lesson 3");
        lesson.setStatus(BaseStatus.Deleted);
        lesson2.setCourse(course);

        lessonRepository.save(lesson2);


        return null;
    }

    @GetMapping("/courses")
    public ResponseEntity<CoreResponseBody> searchByLabel(
            @Spec(path = "label", spec = Like.class) Specification<Course> spec,
            Pageable pageable
    ) {
        Page<Course> courses = courseRepository.findAll(spec, pageable);
        return ResponseEntity.ok(CoreResponseBody.result(courses));
    }


    @GetMapping("/coursesStatus")
    public ResponseEntity<CoreResponseBody> searchWithDefaultStatusOverride2(
            @Spec(path = "status", spec = BitEqual.class, defaultVal = "!Deleted")
                    Specification<Course> spec,
            Pageable pageable
    ) {
        Page<Course> courses = courseRepository.findAll(spec, pageable);
        return ResponseEntity.ok(CoreResponseBody.result(courses));
    }

    @GetMapping("/searchDateRange")
    public ResponseEntity<CoreResponseBody> searchDateRange(
            @Spec(path = "startDate", spec = Between.class, params = {"startDateAfter", "startDateBefore"}) Specification<Course> spec,
            Pageable pageable
    ) {
        Page<Course> courses = courseRepository.findAll(spec, pageable);
        return ResponseEntity.ok(CoreResponseBody.result(courses));
    }


    @PostMapping("/joniSearch2")
    public ResponseEntity<CoreResponseBody> joniSearch2(
            @Join(path = "course", alias = "c", distinct = false)
            @Spec(path = "c.label", params = "label", spec = Like.class)
                    Specification<Lesson> spec,
            Pageable pageable
    ) {
        //Page<Lesson> lessons = ;
        return ResponseEntity.ok(CoreResponseBody.result(lessonRepository.findAll(spec, pageable)));
    }

    @GetMapping("/joniSearch")
    public ResponseEntity<CoreResponseBody> joniSearch(
            @Join(path = "lessons", alias = "l")
            @Spec(path = "l.label", params = "label", spec = Like.class)
                    Specification<Course> spec,
            Pageable pageable
    ) {
        Page<Course> lessons = courseRepository.findAll(spec, pageable);
        return ResponseEntity.ok(CoreResponseBody.result(lessons));
    }

    @GetMapping("/conditionsearch")
    public ResponseEntity<CoreResponseBody> conditionSearch(
            @Join(path = "lessons", alias = "l")
            @Or({
                    @Spec(path = "l.label", params = "lesson.label", spec = Like.class),
                    @Spec(path = "label", params = "label", spec = Like.class),
            }) Specification<Course> spec,
            Pageable pageable
    ) {
        Page<Course> lessons = courseRepository.findAll(spec, pageable);
        return ResponseEntity.ok(CoreResponseBody.result(lessons));
    }
}
