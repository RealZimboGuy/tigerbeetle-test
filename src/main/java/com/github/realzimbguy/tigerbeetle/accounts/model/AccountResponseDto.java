package com.github.realzimbguy.tigerbeetle.accounts.model;

public class AccountResponseDto {

    private boolean error;
    private String  message;
    private AccountDto account;

    public AccountResponseDto() {
    }

    public AccountResponseDto(boolean error, String message) {
        this.error = error;
        this.message = message;
    }

    public AccountResponseDto(boolean error, String message, AccountDto account) {
        this.error = error;
        this.message = message;
        this.account = account;
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

    public AccountDto getAccount() {
        return account;
    }

    public void setAccount(AccountDto account) {
        this.account = account;
    }
}
