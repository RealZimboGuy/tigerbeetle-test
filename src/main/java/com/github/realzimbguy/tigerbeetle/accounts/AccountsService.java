package com.github.realzimbguy.tigerbeetle.accounts;

import com.github.realzimbguy.tigerbeetle.StaticVariables;
import com.github.realzimbguy.tigerbeetle.accounts.model.*;
import com.github.realzimbguy.tigerbeetle.repo.*;
import com.github.realzimbguy.tigerbeetle.repo.entity.AccountEntity;
import com.github.realzimbguy.tigerbeetle.repo.entity.AccountFlagEntity;
import com.github.realzimbguy.tigerbeetle.services.TigerBeetleService;
import com.tigerbeetle.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class AccountsService {

	private static Logger             logger = LoggerFactory.getLogger(AccountsService.class);
	private final  TigerBeetleService tigerBeetleService;
	private final  AccountRepo        accountRepo;
	private final  LedgerRepo         ledgerRepo;
	private final  AccountCodesRepo   accountCodesRepo;
	private final  AccountFlagsRepo   accountFlagsRepo;
	private final  TransferCodesRepo  transferCodesRepo;

	public AccountsService(TigerBeetleService tigerBeetleService,
	                       AccountRepo accountRepo, LedgerRepo ledgerRepo, AccountCodesRepo accountCodesRepo, AccountFlagsRepo accountFlagsRepo,
	                       TransferCodesRepo transferCodesRepo) {

		this.tigerBeetleService = tigerBeetleService;
		this.accountRepo = accountRepo;
		this.ledgerRepo = ledgerRepo;
		this.accountCodesRepo = accountCodesRepo;
		this.accountFlagsRepo = accountFlagsRepo;
		this.transferCodesRepo = transferCodesRepo;
	}


	public StatementResponseDto getStatement(StatementRequestDto statementRequestDto) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

		//if timestamp max is zero we set it to a specific value to support joining as the double query will return values in the same order
		//timestamp in tigerbeetle is in microseconds
		if (statementRequestDto.getTimestampMax() == 0) {
			ZonedDateTime currentTime = ZonedDateTime.now();
			long nanoseconds = currentTime.toInstant().toEpochMilli() * 1_000_000 + currentTime.getNano();
			statementRequestDto.setTimestampMax(nanoseconds);
			logger.info("Setting timestampMax to {}", statementRequestDto.getTimestampMax());
		}

		StatementResponseDto responseDto = new StatementResponseDto();
		responseDto.setAccountId(statementRequestDto.getAccountId());
		responseDto.setTimestampMin(statementRequestDto.getTimestampMin());
		responseDto.setTimestampMax(statementRequestDto.getTimestampMax());
		responseDto.setStatementRecordDtoList(new ArrayList<>());

		TransferBatch transfers = new TransferBatch(1);
		AccountBalanceBatch accountBalanceBatch = new AccountBalanceBatch(1);
		AccountFilter filter = new AccountFilter();
		filter.setAccountId(statementRequestDto.getAccountId());
		filter.setTimestampMin(statementRequestDto.getTimestampMin()); // No filter by Timestamp.
		filter.setTimestampMax(statementRequestDto.getTimestampMax()); // No filter by Timestamp.
		filter.setLimit(statementRequestDto.getLimit()); // Limit to ten transfers at most.
		filter.setDebits(statementRequestDto.isDebits()); // Include transfer from the debit side.
		filter.setCredits(statementRequestDto.isCredits()); // Include transfer from the credit side.

		//we only support reversed as this allows you to get the correct last balance as at that time
		filter.setReversed(true); // Sort by timestamp in reverse-chronological order.

		try {
			transfers = tigerBeetleService.getAccountTransfers(filter);
			accountBalanceBatch = tigerBeetleService.getAccountHistory(filter);
			Map<Long, StatementRecordDto> statementMap = new HashMap<>();

			while (transfers.next()) {
				StatementRecordDto statementRecordDto = new StatementRecordDto();
				statementRecordDto.setTransferId(UInt128.asUUID(transfers.getId()));
				statementRecordDto.setTimestamp(transfers.getTimestamp());
				statementRecordDto.setFormattedTimestamp(simpleDateFormat.format(transfers.getTimestamp()));
				statementRecordDto.setCreditAccountId(UInt128.asBigInteger(transfers.getCreditAccountId()).longValue());
				statementRecordDto.setDebitAccountId(UInt128.asBigInteger(transfers.getDebitAccountId()).longValue());
				statementRecordDto.setAmount(transfers.getAmount());
				responseDto.getStatementRecordDtoList().add(statementRecordDto);
				statementMap.put(transfers.getTimestamp(), statementRecordDto);
			}

			responseDto.setStatementRecordCount(responseDto.getStatementRecordDtoList().size());

			while (accountBalanceBatch.next()) {
				StatementRecordDto statementRecordDto = statementMap.get(accountBalanceBatch.getTimestamp());
				if (statementRecordDto != null) {

					if (statementRecordDto.getCreditAccountId() == statementRequestDto.getAccountId()) {
						statementRecordDto.setCreditsPosted(accountBalanceBatch.getCreditsPosted());
					}
					if (statementRecordDto.getDebitAccountId() == statementRequestDto.getAccountId()) {
						statementRecordDto.setAmount(statementRecordDto.getAmount().multiply(BigInteger.valueOf(-1)));
						statementRecordDto.setDebitsPosted(accountBalanceBatch.getDebitsPosted());
					}

					statementRecordDto.setPostedBalance(accountBalanceBatch.getCreditsPosted().subtract(accountBalanceBatch.getDebitsPosted()));

					statementRecordDto.setCreditsPosted(accountBalanceBatch.getCreditsPosted());
					statementRecordDto.setDebitsPosted(accountBalanceBatch.getDebitsPosted());
				}
			}


			CompletableFuture<BigInteger> totalCreditsFuture = CompletableFuture.supplyAsync(() ->
					responseDto.getStatementRecordDtoList().stream()
							.filter(statementRecordDto -> statementRecordDto.getCreditsPosted() != null)
							.filter(statementRecordDto -> statementRecordDto.getCreditAccountId() == statementRequestDto.getAccountId())
							.map(record -> BigInteger.ONE)
							.reduce(BigInteger::add)
							.orElse(BigInteger.ZERO)
			);

			CompletableFuture<BigInteger> totalDebitsFuture = CompletableFuture.supplyAsync(() ->
					responseDto.getStatementRecordDtoList().stream()
							.filter(statementRecordDto -> statementRecordDto.getDebitsPosted() != null)
							.filter(statementRecordDto -> statementRecordDto.getDebitAccountId() == statementRequestDto.getAccountId())
							.map(record -> BigInteger.ONE)
							.reduce(BigInteger::add)
							.orElse(BigInteger.ZERO)
			);


			responseDto.setTotalDebitsCount(totalDebitsFuture.join());
			responseDto.setTotalCreditsCount(totalCreditsFuture.join());

			if (responseDto.getTotalDebitsCount() == null) {
				responseDto.setTotalDebitsCount(BigInteger.ZERO);
			}
			if (responseDto.getTotalCreditsCount() == null) {
				responseDto.setTotalCreditsCount(BigInteger.ZERO);
			}

			responseDto.setClosingBalance(BigInteger.ZERO);


				for (StatementRecordDto statementRecordDto : responseDto.getStatementRecordDtoList()) {
					//get the first non null
					if (statementRecordDto.getPostedBalance() != null) {
						responseDto.setClosingBalance(statementRecordDto.getPostedBalance());
						break;
					}
				}



			if (responseDto.getTotalDebitsCount() == null) {
				responseDto.setTotalDebitsCount(BigInteger.ZERO);
			}
			if (responseDto.getTotalCreditsCount() == null) {
				responseDto.setTotalCreditsCount(BigInteger.ZERO);
			}

			responseDto.setMessage("Success");

			return responseDto;

		} catch (ConcurrencyExceededException e) {
			logger.error("ConcurrencyExceededException", e);
			throw new RuntimeException(e);
		}

	}


	public TransferResponseDto getTransfer(UUID id) {


		IdBatch ids = new IdBatch(1);
		ids.add(UInt128.asBytes(id));
		TransferBatch transfers = null;
		try {
			transfers = tigerBeetleService.lookupTransfer(ids);
		} catch (ConcurrencyExceededException e) {
			throw new RuntimeException(e);
		}

		while (transfers.next()) {
			TransferDto transferDto = new TransferDto();
			transferDto.setId(UInt128.asUUID(transfers.getId()));
			transferDto.setDebitAccountId(UInt128.asBigInteger(transfers.getDebitAccountId()).longValue());
			transferDto.setCreditAccountId(UInt128.asBigInteger(transfers.getCreditAccountId()).longValue());
			transferDto.setTimestamp(transfers.getTimestamp());
			transferDto.setLedger(ledgerRepo.findById(transfers.getLedger()).get().getName());
			transferDto.setTransferCode(transferCodesRepo.findById(transfers.getCode()).get().getName());

			transferDto.setAmount(transfers.getAmount());

			int flags = transfers.getFlags();
			List<String> flagsList = new ArrayList<>();

			if ((flags & TransferFlags.NONE) != 0) {
				flagsList.add("NONE");
			}
			if ((flags & TransferFlags.LINKED) != 0) {
				flagsList.add("LINKED");
			}
			if ((flags & TransferFlags.PENDING) != 0) {
				flagsList.add("PENDING");
			}
			if ((flags & TransferFlags.POST_PENDING_TRANSFER) != 0) {
				flagsList.add("POST_PENDING_TRANSFER");
			}
			if ((flags & TransferFlags.VOID_PENDING_TRANSFER) != 0) {
				flagsList.add("VOID_PENDING_TRANSFER");
			}
			if ((flags & TransferFlags.BALANCING_DEBIT) != 0) {
				flagsList.add("BALANCING_DEBIT");
			}
			if ((flags & TransferFlags.BALANCING_CREDIT) != 0) {
				flagsList.add("BALANCING_CREDIT");
			}

			transferDto.setTransferFlagList(flagsList);
			return new TransferResponseDto(false, "Success", transferDto);

		}
		return new TransferResponseDto(true, "Transfer Not Found");

	}

	public AccountResponseDto getAccount(Long id) {

		IdBatch ids = new IdBatch(1);
		ids.add(id);
		AccountBatch accounts = null;
		try {
			accounts = tigerBeetleService.lookupAccounts(ids);
		} catch (ConcurrencyExceededException e) {
			throw new RuntimeException(e);
		}

		while (accounts.next()) {
			AccountDto accountDto = new AccountDto();
			accountDto.setAccountId(UInt128.asBigInteger(accounts.getId()).longValue());
			accountDto.setCustomerId(UInt128.asUUID(accounts.getUserData128()));
			accountDto.setLedger(ledgerRepo.findById(accounts.getLedger()).get().getName());
			accountDto.setAccountCode(accountCodesRepo.findById(accounts.getCode()).get().getName());
			accountDto.setDebitsPending(accounts.getDebitsPending());
			accountDto.setDebitsPosted(accounts.getDebitsPosted());
			accountDto.setCreditsPending(accounts.getCreditsPending());
			accountDto.setCreditsPosted(accounts.getCreditsPosted());

			int flags = accounts.getFlags();
			List<String> flagsList = new ArrayList<>();
			for (AccountFlagEntity accountFlag : accountFlagsRepo.findAll()) {
				if ((flags & accountFlag.getId()) != 0) {
					flagsList.add(accountFlag.getName());
				}
			}

			accountDto.setAccountFlagList(flagsList);


			AccountEntity accountEntity = accountRepo.findById(id).orElse(null);
			if (accountEntity == null) {
				logger.info("Tigerbeetle account found but not in local db, creating");
				accountEntity = new AccountEntity();
				accountEntity.setId(id);
				accountEntity.setName("Unknown");
				accountRepo.save(accountEntity);

			}


			accountDto.setAccountName(accountEntity.getName());

			return new AccountResponseDto(false, "Success", accountDto);
		}

		return new AccountResponseDto(true, "Account not found");
	}

	public TransferBatchResponseDto createTransfer(TransferBatchRequestDto transferBatchRequestDto) {


		TransferBatch transfers = new TransferBatch(transferBatchRequestDto.getTransferList().size());
		for (int i = 0; i < transferBatchRequestDto.getTransferList().size(); i++) {

			TransferDto transferDto = transferBatchRequestDto.getTransferList().get(i);

			transfers.add();
			transfers.setId(UInt128.asBytes(transferDto.getId()));
			transfers.setDebitAccountId(transferDto.getDebitAccountId());
			transfers.setCreditAccountId(transferDto.getCreditAccountId());
			transfers.setLedger(ledgerRepo.findByName(transferDto.getLedger()).getId());
			transfers.setCode(transferCodesRepo.findByName(transferDto.getTransferCode()).getId());
			transfers.setAmount(transferDto.getAmount());

			if (!transferDto.getTransferFlagList().contains(StaticVariables.TransferFlag.LINKED.getName())) {
				transferDto.getTransferFlagList().add(StaticVariables.TransferFlag.LINKED.getName());
			}

			//if the last item in the list remove the linked
			if (i == transferBatchRequestDto.getTransferList().size() - 1) {
				transferDto.getTransferFlagList().remove(StaticVariables.TransferFlag.LINKED.getName());
			}

			///bitwise and all the flags and add to the account flags

			int flags = 0;
			for (String flag : transferDto.getTransferFlagList()) {
				flags = flags | StaticVariables.TransferFlag.fromName(flag).getValue();
			}
			transfers.setFlags(flags);
		}

		CreateTransferResultBatch transferErrors = null;
		try {
			transferErrors = tigerBeetleService.createTransfers(transfers);

			while (transferErrors.next()) {
				logger.info("Transfer Batch response:{}", transferErrors.getResult().toString());
				if (transferErrors.getResult().value > 0) {
					return new TransferBatchResponseDto(true, String.format("Error creating transfer at %d: %s\n",
							transferErrors.getIndex(), transferErrors.getResult()));

				}

			}

			return new TransferBatchResponseDto(false, "Success");

		} catch (ConcurrencyExceededException e) {
			logger.error("ConcurrencyExceededException", e);
			throw new RuntimeException(e);
		}
	}


	public TransferResponseDto createTransfer(TransferDto transferDto) {

		TransferBatch transfers = new TransferBatch(1);
		transfers.add();
		transfers.setId(UInt128.asBytes(transferDto.getId()));
		transfers.setDebitAccountId(transferDto.getDebitAccountId());
		transfers.setCreditAccountId(transferDto.getCreditAccountId());
		transfers.setLedger(ledgerRepo.findByName(transferDto.getLedger()).getId());
		transfers.setCode(transferCodesRepo.findByName(transferDto.getTransferCode()).getId());
		transfers.setAmount(transferDto.getAmount());
		///bitwise and all the flags and add to the account flags
		int flags = 0;
		for (String flag : transferDto.getTransferFlagList()) {
			flags = flags | accountFlagsRepo.findByName(flag).getId();
		}
		transfers.setFlags(flags);
		CreateTransferResultBatch transferErrors = null;
		try {
			transferErrors = tigerBeetleService.createTransfers(transfers);

			while (transferErrors.next()) {
				if (transferErrors.getResult().value > 0) {
					return new TransferResponseDto(true, String.format("Error creating transfer at %d: %s\n",
							transferErrors.getIndex(), transferErrors.getResult()));

				}

			}

			return getTransfer(transferDto.getId());

		} catch (ConcurrencyExceededException e) {
			logger.error("ConcurrencyExceededException", e);
			throw new RuntimeException(e);
		}


	}

	public AccountResponseDto createAccount(AccountDto accountDto) {

		if (accountDto.getAccountName() == null || accountDto.getAccountName().isEmpty()) {
			return new AccountResponseDto(true, "Account name is required");
		}


		AccountBatch accounts = new AccountBatch(1);
		accounts.add();
		accounts.setId(accountDto.getAccountId());
		accounts.setUserData128(UInt128.asBytes(accountDto.getCustomerId()));
		accounts.setUserData64(ZonedDateTime.now().toEpochSecond());
		//todo find a use for the 32 bit user data
//        accounts.setUserData32();
		accounts.setLedger(ledgerRepo.findByName(accountDto.getLedger()).getId());
		accounts.setCode(accountCodesRepo.findByName(accountDto.getAccountCode()).getId());

		///bitwise and all the flags and add to the account flags
		int flags = 0;
		for (String flag : accountDto.getAccountFlagList()) {
			flags = flags | accountFlagsRepo.findByName(flag).getId();
		}
		accounts.setFlags(flags);


		CreateAccountResultBatch accountErrors = null;
		try {
			accountErrors = tigerBeetleService.createAccounts(accounts);

			while (accountErrors.next()) {
				if (accountErrors.getResult().value > 0) {
					return new AccountResponseDto(true, String.format("Error creating account at %d: %s\n",
							accountErrors.getIndex(), accountErrors.getResult()));

				}
			}
			//account created if we got no response
			AccountEntity accountEntity = new AccountEntity();
			accountEntity.setId(accountDto.getAccountId());
			accountEntity.setName(accountDto.getAccountName());
			accountRepo.save(accountEntity);

			return getAccount(accountDto.getAccountId());

		} catch (ConcurrencyExceededException e) {
			logger.error("ConcurrencyExceededException", e);
			return new AccountResponseDto(true, "Error trying to create account");
		}

	}

}
