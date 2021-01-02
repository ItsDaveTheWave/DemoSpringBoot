package com.dtw.demoSpringBoot.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.dtw.demoSpringBoot.dto.PersonDto;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Person {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, insertable = false, updatable = false)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private Integer age;

	@ElementCollection
	private List<String> favorites;
	
	
	public Person() {}
	public Person(String name, Integer age, List<String> favorites) {
		this.name = name;
		this.age = age;
		this.favorites = favorites;
	}
	public Person(Long id, String name, Integer age, List<String> favorites) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.favorites = favorites;
	}
	
	
	public PersonDto toDto() {
		return new PersonDto(id, name, age, favorites);
	}
	
	public static List<PersonDto> listToDto(List<Person> personList) {
		List<PersonDto> personDtoList = new ArrayList<PersonDto>();
		
		for(Person person : personList) {
			personDtoList.add(person.toDto());
		}
		
		return personDtoList;
	}
}