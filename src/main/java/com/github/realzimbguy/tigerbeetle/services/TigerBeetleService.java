package com.github.realzimbguy.tigerbeetle.services;

import com.tigerbeetle.*;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TigerBeetleService {

    Logger logger = LoggerFactory.getLogger(TigerBeetleService.class);
    String replicaAddress = System.getenv("localhost");
    byte[] clusterID = UInt128.asBytes(0);
    String[] replicaAddresses = new String[] {replicaAddress == null ? "3000" : replicaAddress};

    private Client client;

    public AccountBalanceBatch getAccountHistory(AccountFilter filter) throws ConcurrencyExceededException {

        return client.getAccountHistory(filter);
    }

    public TransferBatch getAccountTransfers(AccountFilter filter) throws ConcurrencyExceededException {

        return client.getAccountTransfers(filter);
    }

    @PostConstruct
    public void init() {
        logger.info("Startuo TigerBeetleService");
        client =  new Client(clusterID, replicaAddresses);
    }

    public CreateAccountResultBatch createAccounts(AccountBatch accounts) throws ConcurrencyExceededException {
        return client.createAccounts(accounts);
    }

    public CreateTransferResultBatch createTransfers(TransferBatch transfers) throws ConcurrencyExceededException {
        return client.createTransfers(transfers);
    }


    public AccountBatch lookupAccounts(IdBatch ids) throws ConcurrencyExceededException {
        return client.lookupAccounts(ids);
    }


    public TransferBatch lookupTransfer(IdBatch ids) throws ConcurrencyExceededException {
        return client.lookupTransfers(ids);
    }

}
