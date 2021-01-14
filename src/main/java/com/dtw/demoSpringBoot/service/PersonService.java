package com.dtw.demoSpringBoot.service;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dtw.demoSpringBoot.dto.PersonDto;
import com.dtw.demoSpringBoot.entity.Person;
import com.dtw.demoSpringBoot.exceptions.PersonNotFoundException;
import com.dtw.demoSpringBoot.repo.PersonRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;

@Service
public class PersonService {

	@Autowired
	private PersonRepo personRepo;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private ModelMapper modelMapper;
	
	
	public List<Person> getAll() {
		return personRepo.findAll();
	}
	
	public Person create(Person person) {
		return personRepo.save(person);
	}

	public Person partialUpdate(Long id, JsonPatch patch)
			throws JsonPatchException, JsonProcessingException, PersonNotFoundException {
		Person person = personRepo.findById(id).orElseThrow(PersonNotFoundException::new);
		Person personPatched = applyPatch(patch, toDto(person));
		personPatched.setId(id);
		personRepo.save(personPatched);
		return personPatched;
	}

	private Person applyPatch(JsonPatch patch, PersonDto target)
			throws JsonPatchException, JsonProcessingException {
		JsonNode patched = patch.apply(objectMapper.convertValue(target, JsonNode.class));

		//TODO: validate patched object like you would with @Valid
		
		return toEntity(objectMapper.disable(MapperFeature.USE_ANNOTATIONS).treeToValue(patched, PersonDto.class));
	}
	
	public PersonDto toDto(Person person) {
		return modelMapper.map(person, PersonDto.class);
	}
	
	public Person toEntity(PersonDto personDto) {
		return modelMapper.map(personDto, Person.class);
	}
	
	public List<PersonDto> listToDto(List<Person> personList) {
		List<PersonDto> personDtoList = new ArrayList<PersonDto>();
		
		for(Person person : personList) {
			personDtoList.add(toDto(person));
		}
		
		return personDtoList;
	}
	
	public List<Person> listToEntity(List<PersonDto> personDtoList) {
		List<Person> personList = new ArrayList<Person>();
		
		for(PersonDto personDto : personDtoList) {
			personList.add(toEntity(personDto));
		}
		
		return personList;
	}
}