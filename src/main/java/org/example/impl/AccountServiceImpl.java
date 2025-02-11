package org.example.impl;

import org.example.entity.Account;
import org.example.entity.User;
import org.example.factory.MySessionFactory;
import org.example.service.AccountService;
import org.hibernate.*;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AccountServiceImpl implements AccountService {
    private SessionFactory sessionFactory;
    public AccountServiceImpl() {
        this.sessionFactory = MySessionFactory.getSessionFactory();
    }

    @Override
    public Account getAccountById(int accountId) {

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction(); // START TRANSACTION
            Account account = session.get(Account.class, accountId);
            transaction.commit();
            return account;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("transaction failed: " + e.getMessage());
        }
        return null;
    }


    @Override
    public void createAccount(Account account) {

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {

            // start transaction
            transaction = session.beginTransaction();

            // business logic
            session.persist(account);
            transaction.commit();
            System.out.println("Account " + account.getAccountId() + " created!");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("transaction failed: " + e.getMessage());
        }
    }

    @Override
    public void approveAccount(int accountId, boolean approved) {

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {

            // start transaction
            transaction = session.beginTransaction();

            // business logic
            Account managedAccount = session.get(Account.class, accountId, LockMode.PESSIMISTIC_WRITE);
            managedAccount.setUnlocked(approved);

            // end transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("transaction failed: " + e.getMessage());
        }
    }

    @Override
    public List<Account> getAllUserAccounts(int userId) {

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction(); // START TRANSACTION
            User user = session.get(User.class, userId);
            if (user == null) {
                transaction.commit();
                throw new RuntimeException("User " + userId + " not found");
            }
            Hibernate.initialize(user.getAccounts());
            List<Account> accounts = user.getAccounts();
            transaction.commit();
            return accounts;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.out.println("transaction failed: " + e.getMessage());
        }
        return new ArrayList<>(); // return an empty list if try-block fails
    }

    @Override
    public List<Account> getAllAccounts() {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {

            // start transaction;
            transaction = session.beginTransaction();

            // do business;
            String hql = "SELECT a FROM Account a";
            Query<Account> query = session.createQuery(hql, Account.class);
            List<Account> accounts = query.getResultList();

            // end transaction;
            transaction.commit();
            return accounts;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("transaction failed: " + e.getMessage());
        }
        return new ArrayList<>(); // return an empty list if try-block fails
    }
}
