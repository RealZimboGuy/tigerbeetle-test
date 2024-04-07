package com.github.realzimbguy.tigerbeetle.accounts;

import com.github.realzimbguy.tigerbeetle.accounts.model.*;
import com.tigerbeetle.TransferBatch;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("accounts")
public class AccountsController {

    private final AccountsService accountsService;

    public AccountsController(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @PostMapping
    public AccountResponseDto createAccount(@RequestBody AccountDto accountDto) {
        return accountsService.createAccount(accountDto);
    }

    @PostMapping("transfer")
    public TransferResponseDto createTransferSingle(@RequestBody TransferDto transferDto) {

        return accountsService.createTransfer(transferDto);
    }
    @PostMapping("transferBatch")
    public TransferBatchResponseDto createTransferBatch(@RequestBody TransferBatchRequestDto transferBatchRequestDto) {

        return accountsService.createTransfer(transferBatchRequestDto);
    }

    @PostMapping("statement")
    public StatementResponseDto getStatement(@RequestBody StatementRequestDto statementRequestDto) {

        return accountsService.getStatement(statementRequestDto);
    }

    @GetMapping("transfer/{id}")
    public TransferResponseDto getTransfer(@PathVariable UUID id) {

        return accountsService.getTransfer(id);
    }

    @GetMapping("{id}")
    public AccountResponseDto getAccount(@PathVariable Long id) {
        return accountsService.getAccount(id);
    }

}
