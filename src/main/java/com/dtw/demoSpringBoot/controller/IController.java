package com.dtw.demoSpringBoot.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.http.ResponseEntity;

import com.dtw.demoSpringBoot.exceptions.EntityNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonpatch.JsonPatchException;

public interface IController<Entity> {

	ResponseEntity<List<Entity>> getAll(@Min(0) Integer page, @Min(1) Integer pageSize, String sortBy);
	ResponseEntity<Entity> getOne(Long id) throws EntityNotFoundException;
	ResponseEntity<Entity> create(@Valid Entity entity);
	ResponseEntity<Entity> totalUpdate(Long id, @Valid Entity entity) throws EntityNotFoundException;
	ResponseEntity<Entity> partialUpdate(Long id, JsonNode mergePatchNode) 
			throws EntityNotFoundException, JsonProcessingException, JsonPatchException, IllegalArgumentException, IllegalAccessException;
	ResponseEntity<Void> delete(Long id) throws EntityNotFoundException;
}