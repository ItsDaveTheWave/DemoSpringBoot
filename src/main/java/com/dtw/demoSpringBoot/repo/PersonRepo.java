package com.dtw.demoSpringBoot.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dtw.demoSpringBoot.entity.Person;

public interface PersonRepo extends JpaRepository<Person, Long>{

}