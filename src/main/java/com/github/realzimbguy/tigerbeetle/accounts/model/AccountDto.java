package com.github.realzimbguy.tigerbeetle.accounts.model;

import com.github.realzimbguy.tigerbeetle.StaticVariables;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

public class AccountDto {

    private long accountId;
    private UUID customerId;
    private String       accountCode;

    private String accountName;
    private String            ledger;
    private List<String> accountFlagList;

    private BigInteger debitsPosted;
    private BigInteger debitsPending;
    private BigInteger creditsPosted;
    private BigInteger creditsPending;



    public long getAccountId() {

        return accountId;
    }

    public void setAccountId(long accountId) {

        this.accountId = accountId;
    }

    public UUID getCustomerId() {

        return customerId;
    }

    public void setCustomerId(UUID customerId) {

        this.customerId = customerId;
    }

    public String getAccountCode() {

        return accountCode;
    }

    public void setAccountCode(String accountCode) {

        this.accountCode = accountCode;
    }

    public String getLedger() {

        return ledger;
    }

    public void setLedger(String ledger) {

        this.ledger = ledger;
    }

    public List<String> getAccountFlagList() {

        return accountFlagList;
    }

    public void setAccountFlagList(List<String> accountFlagList) {

        this.accountFlagList = accountFlagList;
    }

    public String getAccountName() {

        return accountName;
    }

    public void setAccountName(String accountName) {

        this.accountName = accountName;
    }

    public BigInteger getDebitsPosted() {

        return debitsPosted;
    }

    public void setDebitsPosted(BigInteger debitsPosted) {

        this.debitsPosted = debitsPosted;
    }

    public BigInteger getDebitsPending() {

        return debitsPending;
    }

    public void setDebitsPending(BigInteger debitsPending) {

        this.debitsPending = debitsPending;
    }

    public BigInteger getCreditsPosted() {

        return creditsPosted;
    }

    public void setCreditsPosted(BigInteger creditsPosted) {

        this.creditsPosted = creditsPosted;
    }

    public BigInteger getCreditsPending() {

        return creditsPending;
    }

    public void setCreditsPending(BigInteger creditsPending) {

        this.creditsPending = creditsPending;
    }

    @Override
    public String toString() {

        return "AccountDto{" +
                "accountId=" + accountId +
                ", customerId=" + customerId +
                ", accountCode='" + accountCode + '\'' +
                ", accountName='" + accountName + '\'' +
                ", ledger='" + ledger + '\'' +
                ", accountFlagList=" + accountFlagList +
                ", debitsPosted=" + debitsPosted +
                ", debitsPending=" + debitsPending +
                ", creditsPosted=" + creditsPosted +
                ", creditsPending=" + creditsPending +
                '}';
    }
}
