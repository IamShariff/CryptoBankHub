package com.cbh.bankservice.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cbh.bankservice.model.BankAccount;
import com.cbh.bankservice.requestdto.AddBankAccountRequestDto;
import com.cbh.bankservice.requestdto.UpdateBalanceRequestDto;
import com.cbh.bankservice.service.BankAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

/**
 * Controller class for managing bank accounts.
 * Provides endpoints for adding bank accounts, checking balances, updating balances,
 * and retrieving all bank accounts associated with a user ID.
 */
@RestController
@RequestMapping("/bankaccount")
@RequiredArgsConstructor
@ApiResponse(description = "Operations related to bank account")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    /**
     * Adds a new bank account.
     *
     * @param addBankAccountRequestDto The request body containing details for adding a new bank account.
     * @return ResponseEntity with the added BankAccount entity.
     */
    @PostMapping()
    @Operation(tags = "Add a bank account")
    public ResponseEntity<BankAccount> add(@RequestBody AddBankAccountRequestDto addBankAccountRequestDto) {
        return ResponseEntity.ok(bankAccountService.add(addBankAccountRequestDto));
    }

    /**
     * Retrieves the balance of a bank account.
     *
     * @param checkBalanceRequestDto The request body containing details for checking the balance.
     * @return ResponseEntity with the balance value.
     */
    @GetMapping("/balance/userId/{userId}/accountNumber/{accountNumber}")
    @Operation(tags = "Get balance")
    public ResponseEntity<Double> balance(@PathVariable String userId, @PathVariable long accountNumber) {
        return ResponseEntity.ok(bankAccountService.checkBalance(userId,accountNumber));
    }

    /**
     * Updates the balance of a bank account.
     *
     * @param updateBalanceRequestDto The request body containing details for updating the balance.
     * @return ResponseEntity with the updated balance value.
     */
    @PatchMapping("/balance")
    @Operation(tags = "Update balance")
    public ResponseEntity<Double> updateBalance(@RequestBody UpdateBalanceRequestDto updateBalanceRequestDto) {
        return ResponseEntity.ok(bankAccountService.updateBalance(updateBalanceRequestDto));
    }

    /**
     * Retrieves all bank accounts associated with a user ID.
     *
     * @param userId The user ID for which bank accounts are to be retrieved.
     * @return ResponseEntity with the list of BankAccount entities.
     */
    @GetMapping("/{userId}")
    @Operation(tags = "Get all Bank accounts of user id.")
    public ResponseEntity<List<BankAccount>> bankAccounts(@PathVariable String userId) {
        return ResponseEntity.ok(bankAccountService.getBankAccounts(userId));
    }
}

