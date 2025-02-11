package org.example;
import org.example.entity.Account;
import org.example.entity.BankTransaction;
import org.example.entity.User;
import org.example.enums.Role;
import org.example.enums.TransactionType;
import org.example.impl.AccountServiceImpl;
import org.example.impl.BankTransactionServiceImpl;
import org.example.impl.UserServiceImpl;
import org.example.service.AccountService;
import org.example.service.BankTransactionService;
import org.example.service.UserService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws InterruptedException {

        UserService userService = new UserServiceImpl();
        AccountService accountService = new AccountServiceImpl();
        BankTransactionService bankTransactionService = new BankTransactionServiceImpl();
        Scanner scanner = new Scanner(System.in);
        String employeeSecret = "secret";


        User activeUser = null;
        boolean flag = true;

        while (flag) {
            if (activeUser == null) {
                System.out.println("Press 1 to register");
                System.out.println("Press 2 to log in");
                System.out.println("Press 3 to exit");
                Integer userInput = scanner.nextInt();
                scanner.nextLine();
                switch (userInput) {
                    case 1:
                        System.out.println("Registering");
                        System.out.print("please enter your name:");
                        String name = scanner.nextLine();
                        System.out.print("please enter your email:");
                        String email = scanner.next();
                        System.out.print("please enter your password:");
                        String password = scanner.next();
                        System.out.print("are you an employee(y/n)?:");
                        boolean isEmployee = scanner.next().equalsIgnoreCase("y");
                        if (isEmployee) {
                            System.out.print("Please enter the employee_secret (hint: 'secret'):");
                            if(!scanner.next().equals(employeeSecret)) {
                                System.out.println("the secret was wrong, please try again");
                                continue;
                            }
                            userService.register(new User(name, email, password, Role.EMPLOYEE));
                        } else {
                            userService.register(new User(name, email, password, Role.CUSTOMER));
                        }
                    case 2:
                        System.out.println("Login in");
                        System.out.print("please enter your email:");
                        String loginEmail = scanner.next();
                        System.out.print("please enter your password:");
                        String loginPassword = scanner.next();
                        activeUser = userService.authenticate(loginEmail, loginPassword);
                        break;
                    case 3:
                        System.out.println("Bye!");
                        flag = false;
                        break;
                    default:
                        System.out.println("Invalid selection");
                        break;
                }
            } else {

                System.out.println("Welcome! "+activeUser.getName());

                // Customer Options
                if (activeUser.getRole() == Role.CUSTOMER) {
                    System.out.println("Selection your options");
                    System.out.println("1. View my available accounts");
                    System.out.println("2, Apply for a new account");
                    System.out.println("3. Deposit to an account");
                    System.out.println("4. Withdrawal from an account");
                    System.out.println("5. Transfer money to another account");
                    System.out.println("6. log out");
                    System.out.print("Enter your selection:");

                    int customerInput = scanner.nextInt();
                    switch (customerInput) {
                        case 1:
                            List<Account> userAccounts = accountService.getAllUserAccounts(activeUser.getUser_id());
                            userAccounts.forEach(System.out::println);
                            break;
                        case 2:
                            System.out.println("Applying for a new account...");
                            System.out.println("Starting balance below threshold($3000) will need manual approval");
                            System.out.print("Enter your starting balance:");
                            double startingBalance = scanner.nextDouble();
                            boolean isAutoApproved = startingBalance >= 3000;
                            accountService.createAccount(new Account(activeUser, startingBalance, isAutoApproved));
                            break;
                        case 3:
                            System.out.println("Making a deposit...");
                            System.out.print("Enter account id:");
                            int accountId1 = scanner.nextInt();
                            System.out.print("Enter amount($):");
                            double depositAmount = scanner.nextDouble();
                            bankTransactionService.processTransaction(accountId1, depositAmount, TransactionType.DEPOSIT);
                            break;
                        case 4:
                            System.out.println("Making a withdrawal...");
                            System.out.print("Enter account id:");
                            int accountId2 = scanner.nextInt();
                            System.out.print("Enter amount($):");
                            double withdrawalAmount = scanner.nextDouble();
                            bankTransactionService.processTransaction(accountId2, withdrawalAmount, TransactionType.WITHDRAWAL);
                            break;
                        case 5:
                            System.out.println("Making a transfer...");
                            System.out.print("Enter account id you with to transfer from:");
                            int fromId = scanner.nextInt();
                            System.out.print("Enter account id you with to transfer to:");
                            int toId = scanner.nextInt();
                            System.out.print("Enter amount($):");
                            double transferAmount = scanner.nextDouble();
                            bankTransactionService.transfer(fromId, toId, transferAmount);
                            break;
                        case 6:
                            System.out.println("Login out");
                            activeUser = null;
                            break;
                        default:
                            System.out.println("Invalid selection");
                            break;
                    }
                }
                // Employee options
                if (activeUser != null && activeUser.getRole() == Role.EMPLOYEE) {
                    System.out.println("Selection your options");
                    System.out.println("1, View all accounts by customer id");
                    System.out.println("2. approve account");
                    System.out.println("3. reject account");
                    System.out.println("4. View all transactions");
                    System.out.println("5. View all accounts");
                    System.out.println("6. log out");
                    System.out.print("Enter your selection:");

                    int employeeInput = scanner.nextInt();
                    switch (employeeInput) {
                        case 1:
                            System.out.println("Viewing accounts...");
                            System.out.print("Enter customer user id:");
                            accountService.getAllUserAccounts(scanner.nextInt()).forEach(System.out::println);
                            break;
                        case 2:
                            System.out.println("Approving account...");
                            System.out.print("Enter account id:");
                            accountService.approveAccount(scanner.nextInt(), true);
                            break;
                        case 3:
                            System.out.println("Rejecting account...");
                            System.out.print("Enter account id:");
                            accountService.approveAccount(scanner.nextInt(), false);
                            break;
                        case 4:
                            bankTransactionService.getAllTransactions().forEach(System.out::println);
                            break;
                        case 5:
                            accountService.getAllAccounts().forEach(System.out::println);
                            break;
                        case 6:
                            System.out.println("Login out");
                            activeUser = null;
                            break;
                        default:
                            System.out.println("Invalid selection");
                            break;
                    }
                }
            }
        }

    }
}
