package com.dtw.demoSpringBoot.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dtw.demoSpringBoot.dto.PersonDto;
import com.dtw.demoSpringBoot.entity.Person;
import com.dtw.demoSpringBoot.exceptions.EntityNotFoundException;
import com.dtw.demoSpringBoot.repo.PersonRepo;
import com.dtw.demoSpringBoot.utils.EntityDtoConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;

@Service
public class PersonService extends EntityDtoConverter<Person, PersonDto>{

	@Autowired
	private PersonRepo personRepo;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	
	public PersonService() {
		super(Person.class, PersonDto.class);
	}
	
	
	public List<Person> getAll() {		
		return personRepo.findAll();
	}
	
	public Person create(Person person) {
		return personRepo.save(person);
	}

	public Person partialUpdate(Long id, JsonPatch patch)
			throws JsonPatchException, JsonProcessingException, EntityNotFoundException {
		Person person = personRepo.findById(id).orElseThrow(() -> new EntityNotFoundException(Person.class, id));
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
}