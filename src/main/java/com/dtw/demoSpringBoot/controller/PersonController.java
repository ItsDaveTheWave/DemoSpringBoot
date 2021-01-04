package com.dtw.demoSpringBoot.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dtw.demoSpringBoot.dto.PersonDto;
import com.dtw.demoSpringBoot.entity.Person;
import com.dtw.demoSpringBoot.exceptions.PersonNotFoundException;
import com.dtw.demoSpringBoot.service.PersonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;

@RestController
@RequestMapping(value = "person")
public class PersonController {

	@Autowired
	private PersonService personService;
	
	
	@GetMapping
	public List<PersonDto> getAll() {
		return Person.listToDto(personService.getAll());
	}
	
	@PostMapping
	public PersonDto create(@RequestBody @Valid PersonDto personDto) {
		return personService.create(personDto.toEntity()).toDto();
	}
	
	@PatchMapping(path = "{id}", consumes = "application/json-patch+json")
	public ResponseEntity<PersonDto> partialUpdate(@PathVariable("id") Long id, @RequestBody JsonPatch patch) {
		
		try {
			Person patchedPerson = personService.partialUpdate(id, patch);
			return ResponseEntity.ok(patchedPerson.toDto());
		} catch(JsonPatchException | JsonProcessingException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		} catch(PersonNotFoundException e1) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
}