package com.github.realzimbguy.tigerbeetle.accounts.model;

public class StatementRequestDto {

	private long accountId;
	private long timestampMin;
	private long timestampMax;
	private int limit;
	private boolean debits;
	private boolean credits;
	private boolean reversed;

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

	public int getLimit() {

		return limit;
	}

	public void setLimit(int limit) {

		this.limit = limit;
	}

	public boolean isDebits() {

		return debits;
	}

	public void setDebits(boolean debits) {

		this.debits = debits;
	}

	public boolean isCredits() {

		return credits;
	}

	public void setCredits(boolean credits) {

		this.credits = credits;
	}

	public boolean isReversed() {

		return reversed;
	}

	public void setReversed(boolean reversed) {

		this.reversed = reversed;
	}
}
