package com.snacks.ordercommands.error;

public class OrderDoesNotExistException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public OrderDoesNotExistException(String message) {
		super(message);
	}
}