package com.dtw.demoSpringBoot.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.dtw.demoSpringBoot.config.properties.PersonProperties.*;
import com.dtw.demoSpringBoot.dto.PersonDto;
import com.dtw.demoSpringBoot.entity.Person;
import com.dtw.demoSpringBoot.exceptions.EntityNotFoundException;
import com.dtw.demoSpringBoot.service.PersonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonpatch.JsonPatchException;

@RestController
@RequestMapping(value = "person")
@Validated
public class PersonController implements IController<PersonDto> {

	@Autowired
	private PersonService personService;
	
	@Autowired
	private ConversionService converter;
	
	
	@GetMapping
	public ResponseEntity<List<PersonDto>> getAll(
			@RequestParam(defaultValue = "0") @Min(0) Integer page, 
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) @Min(1) Integer pageSize, 
			@RequestParam(defaultValue = DEFAULT_SORT_BY) String sortBy) {
		
		List<PersonDto> body = new ArrayList<>();
		Page<Person> personPage = personService.getAll(PageRequest.of(page, pageSize, Sort.Direction.ASC, sortBy));
		personPage.forEach((person) -> {
			body.add(converter.convert(person, PersonDto.class));
		});
		return ResponseEntity.ok(body);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<PersonDto> getOne(@PathVariable Long id) 
			throws EntityNotFoundException {
		Person person = personService.getOne(id);
		return ResponseEntity.ok(converter.convert(person, PersonDto.class));
	}
	
	@PostMapping
	public ResponseEntity<PersonDto> create(@RequestBody @Valid PersonDto personDto) {
		Person person = converter.convert(personDto, Person.class);
		PersonDto body = converter.convert(personService.create(person), PersonDto.class);
		return new ResponseEntity<PersonDto>(body, HttpStatus.CREATED);
	}
	
	@PutMapping("{id}")
	public ResponseEntity<PersonDto> totalUpdate(@PathVariable Long id, @RequestBody @Valid PersonDto target) 
			throws EntityNotFoundException {
		return ResponseEntity.ok(converter.convert(personService.totalUpdate(id, target), PersonDto.class));
	}
	
	@PatchMapping(path = "{id}", consumes = "application/json-patch+json")
	public ResponseEntity<PersonDto> partialUpdate(@PathVariable Long id, @RequestBody JsonNode mergePatchNode) 
			throws EntityNotFoundException, JsonProcessingException, JsonPatchException, IllegalArgumentException, IllegalAccessException {
		Person patchedPerson = personService.partialUpdate(id, mergePatchNode);
		return ResponseEntity.ok(converter.convert(patchedPerson, PersonDto.class));
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) 
			throws EntityNotFoundException {
		personService.delete(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}