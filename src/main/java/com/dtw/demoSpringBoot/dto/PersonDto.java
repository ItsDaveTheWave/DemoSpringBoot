package com.dtw.demoSpringBoot.dto;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.dtw.demoSpringBoot.entity.Person;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
public class PersonDto {

	@JsonProperty(access = Access.READ_ONLY)
	private Long id;
	
	@NotBlank
	private String name;
	
	@NotNull
	private Integer age;
	
	private List<String> favorites;
	
	
	
	private static PersonDtoBuilder builder() {
		return new PersonDtoBuilder();
	}
	
	public static PersonDtoBuilder builder(String name, Integer age) {
		return builder().name(name).age(age);
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