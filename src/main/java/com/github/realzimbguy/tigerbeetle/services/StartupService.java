package com.github.realzimbguy.tigerbeetle.services;

import com.github.realzimbguy.tigerbeetle.StaticVariables;
import com.github.realzimbguy.tigerbeetle.repo.AccountCodesRepo;
import com.github.realzimbguy.tigerbeetle.repo.AccountFlagsRepo;
import com.github.realzimbguy.tigerbeetle.repo.LedgerRepo;
import com.github.realzimbguy.tigerbeetle.repo.TransferCodesRepo;
import com.github.realzimbguy.tigerbeetle.repo.entity.AccountCodeEntity;
import com.github.realzimbguy.tigerbeetle.repo.entity.AccountFlagEntity;
import com.github.realzimbguy.tigerbeetle.repo.entity.LedgerEntity;
import com.github.realzimbguy.tigerbeetle.repo.entity.TransferCodeEntity;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class StartupService {

    Logger logger = LoggerFactory.getLogger(StartupService.class);

    private final LedgerRepo ledgerRepo;
    private final AccountFlagsRepo accountFlagsRepo;
    private final AccountCodesRepo accountCodesRepo;

    private final TransferCodesRepo transferCodesRepo;

    public StartupService(LedgerRepo ledgerRepo, AccountFlagsRepo accountFlagsRepo, AccountCodesRepo accountCodesRepo, TransferCodesRepo transferCodesRepo) {
        this.ledgerRepo = ledgerRepo;
        this.accountFlagsRepo = accountFlagsRepo;
	    this.accountCodesRepo = accountCodesRepo;
	    this.transferCodesRepo = transferCodesRepo;
    }

    @PostConstruct
    public void init() {
       logger.info("StartupService");

         for (StaticVariables.Ledger defaultLedger : StaticVariables.Ledger.values()) {
             Optional<LedgerEntity> ledgerResult = ledgerRepo.findById(defaultLedger.getId());
             if (ledgerResult.isEmpty()) {
                 LedgerEntity ledger = new LedgerEntity();
                 ledger.setId(defaultLedger.getId());
                 ledger.setName(defaultLedger.getName());
                 ledgerRepo.save(ledger);
                 logger.info("Ledger created:{}", ledger.getName());
             }
         }

        for (StaticVariables.AccountFlag accountFlag : StaticVariables.AccountFlag.values()) {
            Optional<AccountFlagEntity> accountFlagsResult = accountFlagsRepo.findById(accountFlag.getValue());
            if (accountFlagsResult.isEmpty()) {
                AccountFlagEntity accountFlagEntity = new AccountFlagEntity();
                accountFlagEntity.setId(accountFlag.getValue());
                accountFlagEntity.setName(accountFlag.name());
                accountFlagsRepo.save(accountFlagEntity);
                logger.info("AccountFlags created:{}", accountFlag.name());
            }

        }
        for (StaticVariables.AccountCode accountCode : StaticVariables.AccountCode.values()) {
            Optional<AccountCodeEntity> accountFlagsResult = accountCodesRepo.findById(accountCode.getValue());
            if (accountFlagsResult.isEmpty()) {
                AccountCodeEntity accountCodeEntity = new AccountCodeEntity();
                accountCodeEntity.setId(accountCode.getValue());
                accountCodeEntity.setName(accountCode.getName());
                accountCodesRepo.save(accountCodeEntity);
                logger.info("AccountCode created:{}", accountCode.name());
            }

        }
        for (StaticVariables.TransferCode transferCode : StaticVariables.TransferCode.values()) {
         Optional<TransferCodeEntity> transferCodeResult = transferCodesRepo.findById(transferCode.getId());
            if (transferCodeResult.isEmpty()) {
                TransferCodeEntity transferCodeEntity = new TransferCodeEntity();
                transferCodeEntity.setId(transferCode.getId());
                transferCodeEntity.setName(transferCode.getName());
                transferCodesRepo.save(transferCodeEntity);
                logger.info("TransferCode created:{}", transferCode.name());
            }
        }



    }


}
