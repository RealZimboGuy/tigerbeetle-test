package com.github.realzimbguy.tigerbeetle.accounts.model;

import java.math.BigInteger;
import java.util.List;

public class StatementResponseDto {

	private boolean error;
	private String  message;

	private long accountId;
	private long timestampMin;
	private long timestampMax;

	private BigInteger     totalCreditsCount;
	private     BigInteger totalDebitsCount;
	private     BigInteger closingBalance;

	private int                      statementRecordCount;
	private List<StatementRecordDto> statementRecordDtoList;

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

	public long getAccountId() {

		return accountId;
	}

	public void setAccountId(long accountId) {

		this.accountId = accountId;
	}

	public long getTimestampMin() {

		return timestampMin;
	}

	public void setTimestampMin(long timestampMin) {

		this.timestampMin = timestampMin;
	}

	public long getTimestampMax() {

		return timestampMax;
	}

	public void setTimestampMax(long timestampMax) {

		this.timestampMax = timestampMax;
	}

	public BigInteger getTotalCreditsCount() {

		return totalCreditsCount;
	}

	public void setTotalCreditsCount(BigInteger totalCreditsCount) {

		this.totalCreditsCount = totalCreditsCount;
	}

	public BigInteger getTotalDebitsCount() {

		return totalDebitsCount;
	}

	public void setTotalDebitsCount(BigInteger totalDebitsCount) {

		this.totalDebitsCount = totalDebitsCount;
	}

	public BigInteger getClosingBalance() {

		return closingBalance;
	}

	public void setClosingBalance(BigInteger closingBalance) {

		this.closingBalance = closingBalance;
	}

	public List<StatementRecordDto> getStatementRecordDtoList() {

		return statementRecordDtoList;
	}

	public void setStatementRecordDtoList(List<StatementRecordDto> statementRecordDtoList) {

		this.statementRecordDtoList = statementRecordDtoList;
	}

	public int getStatementRecordCount() {

		return statementRecordCount;
	}

	public void setStatementRecordCount(int statementRecordCount) {

		this.statementRecordCount = statementRecordCount;
	}
}
