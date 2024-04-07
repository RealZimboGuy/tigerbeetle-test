package com.github.realzimbguy.tigerbeetle.accounts.model;

import java.util.List;

public class TransferBatchRequestDto {


	private List<TransferDto> transferList;


	public List<TransferDto> getTransferList() {

		return transferList;
	}

	public void setTransferList(List<TransferDto> transferList) {

		this.transferList = transferList;
	}
}
