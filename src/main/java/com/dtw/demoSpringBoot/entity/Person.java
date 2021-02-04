package com.dtw.demoSpringBoot.entity;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
public class Person {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, insertable = false, updatable = false)
	private Long id;
	
	@Column(nullable = false)
	@NotBlank
	private String name;
	
	@Column
	private String surname;
	
	@Column(nullable = false)
	@NotNull
	@Positive
	private Integer age;

	@ElementCollection
	private List<String> favorites;
	
	
	private static PersonBuilder builder() {
		return new PersonBuilder();
	}
	
	public static PersonBuilder builder(String name, Integer age) {
		return builder().name(name).age(age);
	}
}