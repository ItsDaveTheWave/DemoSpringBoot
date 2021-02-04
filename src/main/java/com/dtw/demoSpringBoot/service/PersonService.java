package com.dtw.demoSpringBoot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import com.dtw.demoSpringBoot.dto.PersonDto;
import com.dtw.demoSpringBoot.entity.Person;
import com.dtw.demoSpringBoot.exceptions.EntityNotFoundException;
import com.dtw.demoSpringBoot.repo.PersonRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
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
	private ConversionService converter;
	
	
	public List<Person> getAll() {		
		return personRepo.findAll();
	}
	
	public Person create(Person person) {
		return personRepo.save(person);
	}

	public Person partialUpdate(Long id, JsonPatch patch)
			throws JsonPatchException, JsonProcessingException, EntityNotFoundException {
		Person person = personRepo.findById(id).orElseThrow(() -> new EntityNotFoundException(Person.class, id));
		PersonDto personDtoPatched = applyPatch(patch, converter.convert(person, PersonDto.class));
		personDtoPatched.setId(id);
		
		return personRepo.save(converter.convert(personDtoPatched, Person.class));
	}
	
	public void delete(Long id) 
			throws EntityNotFoundException {
		Person person = personRepo.findById(id).orElseThrow(() -> new EntityNotFoundException(Person.class, id));
		personRepo.delete(person);
	}

	private PersonDto applyPatch(JsonPatch patch, PersonDto target)
			throws JsonPatchException, JsonProcessingException {
		JsonNode patched = patch.apply(objectMapper.convertValue(target, JsonNode.class));		
		return objectMapper.treeToValue(patched, PersonDto.class);
	}
}