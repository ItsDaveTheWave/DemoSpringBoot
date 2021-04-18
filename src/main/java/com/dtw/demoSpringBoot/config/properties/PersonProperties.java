package com.dtw.demoSpringBoot.config.properties;

public class PersonProperties {

	public static final long MAX_AGE = 125L;
	public static final String NAME_SURNAME_REGEX = "^[A-Za-z ]{2,20}(?:_[A-Za-z ]{2,40})?$";
	public static final String DEFAULT_PAGE_SIZE = "10";
	public static final String DEFAULT_SORT_BY = "id";
}