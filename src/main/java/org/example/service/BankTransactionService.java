package org.example.service;

import org.example.entity.BankTransaction;
import org.example.enums.TransactionType;

import java.util.List;

public interface BankTransactionService {

    void processTransaction(int accountId, double amount, TransactionType transactionType);

    void transfer(int fromAccountId, int toAccountId, double amount);
    List<BankTransaction> getAllTransactions();
}
