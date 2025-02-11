package org.example.entity;

import jakarta.persistence.*;
import org.example.enums.TransactionType;

@Entity
@Table(name = "transaction")
public class BankTransaction {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private int transactionId;
    @ManyToOne
    @JoinColumn(name = "account_id", foreignKey = @ForeignKey(name = "fk_account_transaction"))
    private Account account;
    private double amount;
    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    public BankTransaction() {
    }

    public BankTransaction(Account account, double amount, TransactionType transactionType) {
        this.account = account;
        this.amount = amount;
        this.transactionType = transactionType;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", account=" + account.getAccountId() +
                ", amount=" + amount +
                ", transactionType=" + transactionType +
                '}';
    }
}
