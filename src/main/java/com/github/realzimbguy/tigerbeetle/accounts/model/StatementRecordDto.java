package com.github.realzimbguy.tigerbeetle.accounts.model;

import java.math.BigInteger;
import java.util.UUID;

public class StatementRecordDto {


	private UUID       transferId;
	private long       timestamp;
	private String     formattedTimestamp;
	private long debitAccountId;
	private long creditAccountId;
	private BigInteger amount;
	private BigInteger creditsPosted;
	private BigInteger debitsPosted;
	private BigInteger postedBalance;

	public UUID getTransferId() {

		return transferId;
	}

	public void setTransferId(UUID transferId) {

		this.transferId = transferId;
	}

	public long getTimestamp() {

		return timestamp;
	}

	public void setTimestamp(long timestamp) {

		this.timestamp = timestamp;
	}

	public String getFormattedTimestamp() {

		return formattedTimestamp;
	}

	public void setFormattedTimestamp(String formattedTimestamp) {

		this.formattedTimestamp = formattedTimestamp;
	}

	public BigInteger getCreditsPosted() {

		return creditsPosted;
	}

	public void setCreditsPosted(BigInteger creditsPosted) {

		this.creditsPosted = creditsPosted;
	}

	public BigInteger getDebitsPosted() {

		return debitsPosted;
	}

	public void setDebitsPosted(BigInteger debitsPosted) {

		this.debitsPosted = debitsPosted;
	}

	public BigInteger getPostedBalance() {

		return postedBalance;
	}

	public void setPostedBalance(BigInteger postedBalance) {

		this.postedBalance = postedBalance;
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

	public BigInteger getAmount() {

		return amount;
	}

	public void setAmount(BigInteger amount) {

		this.amount = amount;
	}

	@Override
	public String toString() {

		return "StatementRecordDto{" +
				"transferId=" + transferId +
				", timestamp=" + timestamp +
				", formattedTimestamp='" + formattedTimestamp + '\'' +
				", debitAccountId=" + debitAccountId +
				", creditAccountId=" + creditAccountId +
				", amount=" + amount +
				", creditsPosted=" + creditsPosted +
				", debitsPosted=" + debitsPosted +
				", postedBalance=" + postedBalance +
				'}';
	}
}
