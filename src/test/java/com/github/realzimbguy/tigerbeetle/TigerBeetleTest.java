package com.github.realzimbguy.tigerbeetle;

import com.tigerbeetle.*;

public class TigerBeetleTest {


	public static void main(String[] args) throws ConcurrencyExceededException {


		String replicaAddress = System.getenv("localhost");
		byte[] clusterID = UInt128.asBytes(0);
		String[] replicaAddresses = new String[] {replicaAddress == null ? "3000" : replicaAddress};
		try (var client = new Client(clusterID, replicaAddresses)) {
			// Use client

//			AccountBatch accounts = new AccountBatch(1);
//			accounts.add();
//			accounts.setId(999);
//			accounts.setUserData128(UInt128.asBytes(java.util.UUID.randomUUID()));
//			accounts.setUserData64(1234567890);
//			accounts.setUserData32(42);
//			accounts.setLedger(932);
//			accounts.setCode(10);
//			accounts.setFlags(0);
//			accounts.setFlags( AccountFlags.HISTORY | AccountFlags.DEBITS_MUST_NOT_EXCEED_CREDITS);
////
////
//			CreateAccountResultBatch accountErrors = client.createAccounts(accounts);


			TransferBatch transfers = new TransferBatch(1);
//			transfers.add();
//			transfers.setId(UInt128.asBytes(java.util.UUID.randomUUID()));
//			transfers.setDebitAccountId(222222);
//			transfers.setCreditAccountId(999);
//			transfers.setAmount(21);
//			transfers.setUserData128(UInt128.asBytes(java.util.UUID.randomUUID()));
//			transfers.setUserData64(1234567890);
//			transfers.setUserData32(42);
//			transfers.setTimeout(0);
//			transfers.setLedger(932);
//			transfers.setCode(911);
//			transfers.setFlags(0);
//
//			CreateTransferResultBatch transferErrors = client.createTransfers(transfers);
//
//			System.out.println(transferErrors);

			int accountId = 2222;

			AccountFilter filter = new AccountFilter();
			filter.setAccountId(accountId);
			filter.setTimestampMin(0); // No filter by Timestamp.
			filter.setTimestampMax(0); // No filter by Timestamp.
			filter.setLimit(10); // Limit to ten transfers at most.
			filter.setDebits(true); // Include transfer from the debit side.
			filter.setCredits(true); // Include transfer from the credit side.
			filter.setReversed(true); // Sort by timestamp in reverse-chronological order.

			transfers = client.getAccountTransfers(filter);

			System.out.println("print transfers");
			while(transfers.next()){
				System.out.println("**");
				System.out.println("id:" + UInt128.asUUID(transfers.getId()));
				System.out.println("debit acc:" +UInt128.asBigInteger(transfers.getDebitAccountId()));
				System.out.println("credit acc:" +UInt128.asBigInteger(transfers.getCreditAccountId()));
				System.out.println("amt:" +transfers.getAmount());
				System.out.println("user data 128:" +UInt128.asBigInteger(transfers.getUserData128()));
				System.out.println("user data 64:" +transfers.getUserData64());
				System.out.println("user data 32:" +transfers.getUserData32());
				System.out.println("timeout :" +transfers.getTimeout());
				System.out.println("ledger :" +transfers.getLedger());
				System.out.println("code :" +transfers.getCode());
				System.out.println("flags :" +transfers.getFlags());
				System.out.println("timestamp :" +transfers.getTimestamp());
			}

			System.out.println("****\n\n***");

			filter = new AccountFilter();
			filter.setAccountId(accountId);
			filter.setTimestampMin(0); // No filter by Timestamp.
			filter.setTimestampMax(0); // No filter by Timestamp.
			filter.setLimit(10); // Limit to ten balances at most.
			filter.setDebits(true); // Include transfer from the debit side.
			filter.setCredits(true); // Include transfer from the credit side.
			filter.setReversed(false); // Sort by timestamp in reverse-chronological order.
			AccountBalanceBatch account_balances = client.getAccountHistory(filter);

			System.out.println("print account balances: " + accountId);

			while (account_balances.next()){
				System.out.println("timestamp: " + account_balances.getTimestamp());
				System.out.println("credits posted: " +account_balances.getCreditsPosted());
				System.out.println("debits posted: " +account_balances.getDebitsPosted());


			}

//			IdBatch ids = new IdBatch(1);
//			ids.add(999);
//			AccountBatch accounts = client.lookupAccounts(ids);
//
//			while (accounts.next()){
//				System.out.println(UInt128.asBigInteger(accounts.getId()));
//			}
//
//
//			System.out.println(account_balances);

		}

	}

}
