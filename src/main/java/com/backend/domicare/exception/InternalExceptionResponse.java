package com.backend.domicare.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InternalExceptionResponse extends ApiExceptionResponse {

	private String detail;

	public InternalExceptionResponse(int error_code, String message, String detail) {
		super(error_code, message);
		this.detail = detail;
	}

}
