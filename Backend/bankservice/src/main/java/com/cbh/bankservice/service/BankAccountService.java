package com.cbh.bankservice.service;

import java.util.List;
import com.cbh.bankservice.model.BankAccount;
import com.cbh.bankservice.requestdto.AddBankAccountRequestDto;
import com.cbh.bankservice.requestdto.UpdateBalanceRequestDto;

/**
 * Service interface for managing bank accounts.
 * Defines methods for adding bank accounts, checking balances, updating balances,
 * and retrieving all bank accounts associated with a user ID.
 */
public interface BankAccountService {

    /**
     * Adds a new bank account.
     *
     * @param addBankAccountRequestDto The request DTO containing details for adding a new bank account.
     * @return The added BankAccount entity.
     */
    BankAccount add(AddBankAccountRequestDto addBankAccountRequestDto);

    /**
     * Retrieves the balance of a bank account.
     *
     * @param checkBalanceRequestDto The request DTO containing details for checking the balance.
     * @return The balance value.
     */
    double checkBalance(String userId, long accountNumber);

    /**
     * Updates the balance of a bank account.
     *
     * @param updateBalanceRequestDto The request DTO containing details for updating the balance.
     * @return The updated balance value.
     */
    double updateBalance(UpdateBalanceRequestDto updateBalanceRequestDto);

    /**
     * Retrieves all bank accounts associated with a user ID.
     *
     * @param userId The user ID for which bank accounts are to be retrieved.
     * @return List of BankAccount entities.
     */
    List<BankAccount> getBankAccounts(String userId);
}
