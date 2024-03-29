package com.dtw.demoSpringBoot.exceptions;

public class EntityNotFoundException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public EntityNotFoundException(Class<?> clazz) {
		super(clazz.getSimpleName() + " not found");
	}
	
	public EntityNotFoundException(Class<?> clazz, Long id) {
		super(clazz.getSimpleName() + " with id [" + id + "] not found");
	}
}