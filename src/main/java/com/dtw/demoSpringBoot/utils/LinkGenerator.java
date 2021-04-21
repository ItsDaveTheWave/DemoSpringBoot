package com.dtw.demoSpringBoot.utils;

import com.dtw.demoSpringBoot.controller.IController;
import com.dtw.demoSpringBoot.dto.AbstractDto;
import com.dtw.demoSpringBoot.entity.AbstractEntity;
import com.dtw.demoSpringBoot.exceptions.EntityNotFoundException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public class LinkGenerator<Entity extends AbstractEntity, Dto extends AbstractDto> {
	
	private Dto dto;
	private Class<? extends IController<Dto>> controllerClass;
	private List<Entity> idOrderedList;
	private Integer listPosition;
	
	public LinkGenerator(Dto dto, Class<? extends IController<Dto>> controllerClass, JpaRepository<Entity, Long> repo) {
		this.dto = dto;
		this.controllerClass = controllerClass;
		
		idOrderedList = repo.findAll(Sort.by("id").ascending());
		List<Long> idList = new ArrayList<>();
		for(Entity entity : idOrderedList) {
			idList.add(entity.getId());
		}
		listPosition = idList.indexOf(dto.getId());
	}
	
	public LinkGenerator<Entity, Dto> self() throws EntityNotFoundException {
		dto.add(linkTo(methodOn(controllerClass).getOne(dto.getId())).withSelfRel());
		return this;
	}
	
	public LinkGenerator<Entity, Dto> all() throws EntityNotFoundException {
		dto.add(linkTo(methodOn(controllerClass).getAll(null, null, null)).withRel("all"));
		return this;
	}
	
	public LinkGenerator<Entity, Dto> first() throws EntityNotFoundException {
		if(!isFirst()) {
			dto.add(linkTo(methodOn(controllerClass).getOne(idOrderedList.get(0).getId())).withRel("first"));
		}
		return this;
	}
	
	public LinkGenerator<Entity, Dto> last() throws EntityNotFoundException {
		if(!isLast()) {
			dto.add(linkTo(methodOn(controllerClass).getOne(idOrderedList.get(idOrderedList.size() - 1).getId())).withRel("last"));
		}
		return this;
	}
	
	public LinkGenerator<Entity, Dto> previous() throws EntityNotFoundException {
		if(!isFirst()) {
			dto.add(linkTo(methodOn(controllerClass).getOne(idOrderedList.get(listPosition - 1).getId())).withRel("previous"));
		}
		return this;
	}
	
	public LinkGenerator<Entity, Dto> next() throws EntityNotFoundException {
		if(!isLast()) {
			dto.add(linkTo(methodOn(controllerClass).getOne(idOrderedList.get(listPosition + 1).getId())).withRel("next"));
		}
		return this;
	}
	
	public Dto build() {
		return dto;
	}
	
	private boolean isFirst() {		
		if(idOrderedList.get(0).getId() == dto.getId()) {
			return true;
		}
		return false;
	}
	
	private boolean isLast() {
		if(idOrderedList.get(idOrderedList.size() - 1).getId() == dto.getId()) {
			return true;
		}
		return false;
	}
}