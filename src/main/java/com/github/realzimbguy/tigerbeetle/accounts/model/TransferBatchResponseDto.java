package com.github.realzimbguy.tigerbeetle.accounts.model;

import java.util.List;

public class TransferBatchResponseDto {


	private boolean error;
	private String  message;


	public TransferBatchResponseDto() {

	}

	public TransferBatchResponseDto(boolean error, String message) {

		this.error = error;
		this.message = message;
	}

	public boolean isError() {

		return error;
	}

	public void setError(boolean error) {

		this.error = error;
	}

	public String getMessage() {

		return message;
	}

	public void setMessage(String message) {

		this.message = message;
	}

	@Override
	public String toString() {

		return "TransferBatchResponseDto{" +
				"error=" + error +
				", message='" + message + '\'' +
				'}';
	}
}
