package com.example.demo.repository;


import com.example.demo.domain.Course;
import com.example.demo.domain.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends PagingAndSortingRepository<Lesson, Long>, JpaSpecificationExecutor<Lesson> {

}
