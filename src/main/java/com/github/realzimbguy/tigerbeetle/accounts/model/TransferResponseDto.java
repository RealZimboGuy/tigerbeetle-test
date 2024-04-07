package com.github.realzimbguy.tigerbeetle.accounts.model;

public class TransferResponseDto {

    private boolean error;
    private String  message;

    private TransferDto transfer;

    public TransferResponseDto() {
    }

    public TransferResponseDto(boolean error, String message) {
        this.error = error;
        this.message = message;
    }

    public TransferResponseDto(boolean error, String message, TransferDto transfer) {
        this.error = error;
        this.message = message;
        this.transfer = transfer;
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

    public TransferDto getTransfer() {

        return transfer;
    }

    public void setTransfer(TransferDto transfer) {

        this.transfer = transfer;
    }
}
