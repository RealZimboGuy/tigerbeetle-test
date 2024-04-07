package com.github.realzimbguy.tigerbeetle;

import com.github.realzimbguy.tigerbeetle.repo.entity.AccountEntity;

import java.util.List;

public class StaticVariables {


    public static enum AccountCode {
        _1000_BANK(1000,"1000_BANK"),
        _2000_SUSPENSE(2000, "2000_SUSPENSE"),
        _3000_INTERNAL(3000, "3000_INTERNAL"),
        _4000_MERCHANT(4000, "4000_MERCHANT"),
        _4001_MERCHANT_SUSPENSE(4001, "4001_MERCHANT_SUSPENSE"),
        _5000_CUSTOMER_CHECK(5000, "5000_CUSTOMER_CHECK"),
        _5001_CUSTOMER_CREDIT(5001, "5001_CUSTOMER_CREDIT"),
        _5002_CUSTOMER_SAVINGS(5002, "5002_CUSTOMER_SAVINGS");

        private final int value;
        private final String name;

        AccountCode(int value,String name) {
            this.value = value;
            this.name = name;
        }

        public static AccountCode fromValue(int code) {
            for (AccountCode accountCode : AccountCode.values()) {
                if (accountCode.value == code) {
                    return accountCode;
                }
            }
            return null;
        }

        public String getName() {

            return name;
        }

        public int getValue() {
            return value;
        }
    }
    public static enum AccountFlag {
        NONE(0),
        LINKED(1),
        DEBITS_MUST_NOT_EXCEED_CREDITS(2),
        CREDITS_MUST_NOT_EXCEED_DEBITS(4),
        HISTORY(8);

        private final int value;

        AccountFlag(int value) {
            this.value = value;
        }

        public static List<AccountFlag> fromValue(int flags) {
            //FLAGS IS an bit mask to identify the flags

            List<AccountFlag> accountFlags = new java.util.ArrayList<>();
            for (AccountFlag accountFlag : AccountFlag.values()) {
                if ((accountFlag.value & flags) == accountFlag.value) {
                    accountFlags.add(accountFlag);
                }
            }
            return accountFlags;
        }

        public int getValue() {
            return value;
        }
    }
    public static enum TransferFlag {

        NONE("NONE",0),
        LINKED("LINKED",1),
        PENDING("PENDING",2),
        POST_PENDING_TRANSFER("POST_PENDING_TRANSFER",4),
        VOID_PENDING_TRANSFER("VOID_PENDING_TRANSFER",8),
        BALANCING_DEBIT("BALANCING_DEBIT",16),
        BALANCING_CREDIT("BALANCING_CREDIT",16);

        private final int value;
        private final String name;

        TransferFlag(String name,int value) {
            this.value = value;
	        this.name = name;
        }

        public static TransferFlag fromName(String flag) {
            for (TransferFlag transferFlag : TransferFlag.values()) {
                if (transferFlag.name.equals(flag)) {
                    return transferFlag;
                }
            }
            return null;
        }

        public static List<TransferFlag> fromValue(int flags) {
            //FLAGS IS an bit mask to identify the flags

            List<TransferFlag> transferFlags = new java.util.ArrayList<>();
            for (TransferFlag transferFlag : TransferFlag.values()) {
                if ((transferFlag.value & flags) == transferFlag.value) {
                    transferFlags.add(transferFlag);
                }
            }
            return transferFlags;
        }

        public int getValue() {
            return value;
        }

        public String getName() {

            return name;
        }
    }

    public static enum Ledger {
        _710_CURRENCY_ZWL(710,"710_CURRENCY_ZAR"),
        _840_CURRENCY_USD(840,"840_CURRENCY_USD"),
        _932_CURRENCY_ZWL(932,"932_CURRENCY_ZWL");

        private final String name;
        private final int id;

        Ledger(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public static Ledger fromValue(int ledger) {
            for (Ledger l : Ledger.values()) {
                if (l.id == ledger) {
                    return l;
                }
            }
            return null;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }
    }
    public static enum TransferCode {
        _1001_CUSTOMER_TRANSFER(1001,"1001_CUSTOMER_TRANSFER"),
        _2001_GENERAL_TRANSFER(2001,"2001_GENERAL_TRANSFER");

        private final String name;
        private final int id;

        TransferCode(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public static Ledger fromValue(int ledger) {
            for (Ledger l : Ledger.values()) {
                if (l.id == ledger) {
                    return l;
                }
            }
            return null;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }
    }

}
