package com.dtw.demoSpringBoot.utils;
import org.springframework.core.convert.converter.Converter;

import com.dtw.demoSpringBoot.dto.PersonDto;
import com.dtw.demoSpringBoot.entity.Person;

public class PersonToDtoConverter implements Converter<Person, PersonDto> {

	@Override
	public PersonDto convert(Person source) {
		String fullName = source.getName();
		if (source.getSurname() != null && !source.getSurname().isEmpty()) {
			fullName += "_" + source.getSurname();
		}
		return new PersonDto(source.getId(), fullName, source.getAge(), source.getFavorites());
	}
}