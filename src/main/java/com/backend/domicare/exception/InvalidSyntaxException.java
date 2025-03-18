package com.backend.domicare.exception;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvalidSyntaxException extends RuntimeException {

	private static final long serialVersionUID = 7721177071697621360L;

	private List<String> errors = new ArrayList<String>();

	public InvalidSyntaxException(String s) {
		super(s);
		this.errors.add(s);
	}
}