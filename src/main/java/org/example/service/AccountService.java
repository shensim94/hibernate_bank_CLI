package org.example.service;

import org.example.entity.Account;
import org.example.entity.User;

import java.util.List;

public interface AccountService {

    Account getAccountById(int accountId);
    void createAccount(Account account);

    void approveAccount(int accountId, boolean approved);

    List<Account> getAllUserAccounts(int userId);

    List<Account> getAllAccounts();
}
