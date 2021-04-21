package com.dtw.demoSpringBoot.dto;

import org.springframework.hateoas.RepresentationModel;

public abstract class AbstractDto extends RepresentationModel<AbstractDto>{

	protected Long id;

	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}
}