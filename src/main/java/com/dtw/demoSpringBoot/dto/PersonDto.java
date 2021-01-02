package com.dtw.demoSpringBoot.dto;

import java.util.ArrayList;
import java.util.List;

import com.dtw.demoSpringBoot.entity.Person;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonDto {

	@JsonProperty(access = Access.READ_ONLY)
	private Long id;
	private String name;
	private Integer age;
	private List<String> favorites;
	
	
	public PersonDto() {}
	public PersonDto(String name, Integer age, List<String> favorites) {
		this.name = name;
		this.age = age;
		this.favorites = favorites;
	}
	public PersonDto(Long id, String name, Integer age, List<String> favorites) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.favorites = favorites;
	}
	
	
	public Person toEntity() {
		return new Person(id, name, age, favorites);
	}
	
	public static List<Person> listToEntity(List<PersonDto> personDtoList) {
		List<Person> personList = new ArrayList<Person>();
		
		for(PersonDto personDto : personDtoList) {
			personList.add(personDto.toEntity());
		}
		
		return personList;
	}
}