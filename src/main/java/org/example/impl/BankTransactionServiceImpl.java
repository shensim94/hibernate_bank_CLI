package org.example.impl;

import org.example.entity.Account;
import org.example.entity.BankTransaction;
import org.example.enums.TransactionType;
import org.example.factory.MySessionFactory;
import org.example.service.BankTransactionService;
import org.hibernate.*;
import org.hibernate.query.Query;

import java.util.List;

public class BankTransactionServiceImpl implements BankTransactionService {
    private SessionFactory sessionFactory;
    public BankTransactionServiceImpl() {
        this.sessionFactory = MySessionFactory.getSessionFactory();
    }

    @Override
    public void processTransaction(int accountId, double amount, TransactionType transactionType){
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction(); // START TRANSACTION
            TransactionHelper(session, accountId, amount, transactionType);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("transaction failed: " + e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public void transfer(int fromAccountId, int toAccountId, double amount) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            // start transaction;
            transaction = session.beginTransaction();

            // do business;
            TransactionHelper(session, fromAccountId, amount, TransactionType.WITHDRAWAL);
            TransactionHelper(session, toAccountId, amount, TransactionType.DEPOSIT);

            // commit transaction;
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("transfer failed: " + e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public List<BankTransaction> getAllTransactions() {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            // start transaction;
            transaction = session.beginTransaction();

            // do business;
            String hql = "SELECT t FROM BankTransaction t";
            Query<BankTransaction> query = session.createQuery(hql, BankTransaction.class);
            List<BankTransaction> transactions = query.getResultList();

            // end transaction;
            transaction.commit();
            return transactions;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("transaction failed: " + e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return null;
    }

    private void TransactionHelper(Session session, int accountId, double amount, TransactionType transactionType) {
        if (amount <= 0) {
            throw new RuntimeException("the deposit/withdrawal amount cannot be negative or zero");
        }
        Account managedAccount = session.get(Account.class, accountId, LockMode.PESSIMISTIC_WRITE); // select ... for update
        if (managedAccount == null)
            throw new RuntimeException("Account " + accountId + " not found!");
        if (!managedAccount.getUnlocked())
            throw new RuntimeException("Account " + accountId + " is not currently approved");
        if (transactionType == TransactionType.WITHDRAWAL && managedAccount.getBalance() < amount)
            throw new RuntimeException("Withdrawal failed: insufficient fund in account " + accountId);
        BankTransaction bankTransaction = new BankTransaction(managedAccount, amount, transactionType); //
        if (transactionType == TransactionType.DEPOSIT) {
            managedAccount.setBalance(managedAccount.getBalance() + amount);
        } else {
            managedAccount.setBalance(managedAccount.getBalance() - amount);
        }
        session.persist(bankTransaction);
    }
}
