package com.dtw.demoSpringBoot.utils;

import org.springframework.core.convert.converter.Converter;

import com.dtw.demoSpringBoot.dto.PersonDto;
import com.dtw.demoSpringBoot.entity.Person;

public class DtoToPersonConverter implements Converter<PersonDto, Person> {

	@Override
	public Person convert(PersonDto source) {
		
		String fullName[] = source.getName().split("_", 2);		
		if(fullName.length > 1) {
			return new Person(source.getId(), fullName[0], fullName[1], source.getAge(), source.getFavorites());
		}
		else {
			return Person.builder(fullName[0], source.getAge()).id(source.getId()).favorites(source.getFavorites()).build();
		}
	}
}