package com.github.realzimbguy.tigerbeetle.accounts.model;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

public class TransferDto {

    private UUID id;
    private long debitAccountId;
    private long   creditAccountId;
    private String transferCode;
    private String ledger;
    private BigInteger amount;

    private UUID pendingId;

    private long timestamp;

    private List<String> transferFlagList;


    public UUID getId() {

        return id;
    }

    public void setId(UUID id) {

        this.id = id;
    }

    public long getDebitAccountId() {

        return debitAccountId;
    }

    public void setDebitAccountId(long debitAccountId) {

        this.debitAccountId = debitAccountId;
    }

    public long getCreditAccountId() {

        return creditAccountId;
    }

    public void setCreditAccountId(long creditAccountId) {

        this.creditAccountId = creditAccountId;
    }

    public String getTransferCode() {

        return transferCode;
    }

    public void setTransferCode(String transferCode) {

        this.transferCode = transferCode;
    }


    public String getLedger() {

        return ledger;
    }

    public void setLedger(String ledger) {

        this.ledger = ledger;
    }

    public BigInteger getAmount() {

        return amount;
    }

    public void setAmount(BigInteger amount) {

        this.amount = amount;
    }

    public List<String> getTransferFlagList() {

        return transferFlagList;
    }

    public void setTransferFlagList(List<String> transferFlagList) {

        this.transferFlagList = transferFlagList;
    }

    public UUID getPendingId() {

        return pendingId;
    }

    public void setPendingId(UUID pendingId) {

        this.pendingId = pendingId;
    }

    public long getTimestamp() {

        return timestamp;
    }

    public void setTimestamp(long timestamp) {

        this.timestamp = timestamp;
    }

    @Override
    public String toString() {

        return "TransferDto{" +
                "id=" + id +
                ", debitAccountId=" + debitAccountId +
                ", creditAccountId=" + creditAccountId +
                ", transferCode='" + transferCode + '\'' +
                ", ledger='" + ledger + '\'' +
                ", amount=" + amount +
                ", pendingId=" + pendingId +
                ", timestamp=" + timestamp +
                ", transferFlagList=" + transferFlagList +
                '}';
    }
}
