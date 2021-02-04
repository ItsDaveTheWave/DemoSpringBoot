package com.dtw.demoSpringBoot.dto;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
public class PersonDto {

	@JsonProperty(access = Access.READ_ONLY)
	private Long id;
	
	//full-name, name and surname separated by '_', surname is optional
	@NotBlank	
	private String name;
	
	@NotNull
	@Positive
	private Integer age;
	
	private List<String> favorites;
	
	
	private static PersonDtoBuilder builder() {
		return new PersonDtoBuilder();
	}
	
	public static PersonDtoBuilder builder(String name, Integer age) {
		return builder().name(name).age(age);
	}
}