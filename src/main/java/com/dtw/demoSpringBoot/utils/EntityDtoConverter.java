package com.dtw.demoSpringBoot.utils;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class EntityDtoConverter<Entity, Dto> {

	@Autowired
	private ModelMapper modelMapper;
	
	private final Class<Entity> entityType;
	private final Class<Dto> dtoType;
	
	
	public EntityDtoConverter(Class<Entity> entityType, Class<Dto> dtoType) {
		this.entityType = entityType;
		this.dtoType = dtoType;
	}
	
	
	public Dto toDto(Entity entity) {
		return modelMapper.map(entity, dtoType);
	}
	
	public Entity toEntity(Dto dto) {
		return modelMapper.map(dto, entityType);
	}
	
	public List<Dto> listToDto(List<Entity> entityList) {
		List<Dto> dtoList = new ArrayList<Dto>();
		
		for(Entity entity : entityList) {
			dtoList.add(toDto(entity));
		}
		
		return dtoList;
	}
	
	public List<Entity> listToEntity(List<Dto> dtoList) {
		List<Entity> entityList = new ArrayList<Entity>();
		
		for(Dto dto : dtoList) {
			entityList.add(toEntity(dto));
		}
		
		return entityList;
	}
}