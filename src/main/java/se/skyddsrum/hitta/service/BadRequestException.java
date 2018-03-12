package se.skyddsrum.hitta.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public final class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = 7205127623073579743L;

	public BadRequestException(String message) {
		super(message);
	}
}
