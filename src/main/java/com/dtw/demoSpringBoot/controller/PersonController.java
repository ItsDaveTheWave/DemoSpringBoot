package com.dtw.demoSpringBoot.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dtw.demoSpringBoot.dto.PersonDto;
import com.dtw.demoSpringBoot.entity.Person;
import com.dtw.demoSpringBoot.exceptions.EntityNotFoundException;
import com.dtw.demoSpringBoot.service.PersonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonpatch.JsonPatchException;

@RestController
@RequestMapping(value = "person")
public class PersonController {

	@Autowired
	private PersonService personService;
	
	@Autowired
	private ConversionService converter;
	
	
	@GetMapping
	public ResponseEntity<List<PersonDto>> getAll() {
		List<PersonDto> body = new ArrayList<>();
		List<Person> personList = personService.getAll();
		personList.forEach((person) -> {
			body.add(converter.convert(person, PersonDto.class));
		});
		return ResponseEntity.ok(body);
	}
	
	@PostMapping
	public ResponseEntity<PersonDto> create(@RequestBody @Valid PersonDto personDto) {
		Person person = converter.convert(personDto, Person.class);
		PersonDto body = converter.convert(personService.create(person), PersonDto.class);
		return new ResponseEntity<PersonDto>(body, HttpStatus.CREATED);
	}
	
	@PatchMapping(path = "{id}", consumes = "application/json-patch+json")
	public ResponseEntity<PersonDto> partialUpdate(@PathVariable("id") Long id, @RequestBody JsonNode mergePatchNode) 
			throws EntityNotFoundException, JsonProcessingException, JsonPatchException, IllegalArgumentException, IllegalAccessException {
		Person patchedPerson = personService.partialUpdate(id, mergePatchNode);
		return ResponseEntity.ok(converter.convert(patchedPerson, PersonDto.class));
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) 
			throws EntityNotFoundException {
		personService.delete(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}