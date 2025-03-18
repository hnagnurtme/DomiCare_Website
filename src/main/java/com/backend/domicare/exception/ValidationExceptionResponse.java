package com.backend.domicare.exception;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ValidationExceptionResponse extends ApiExceptionResponse{
	private List<String> details;
}
