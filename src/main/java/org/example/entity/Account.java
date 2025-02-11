package org.example.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "account_id")
    private int accountId;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_account"))
    private User user;

    private double balance;

    @Column(name = "is_unlocked")
    private Boolean isUnlocked;

    @OneToMany(mappedBy = "account")
    private List<BankTransaction> bankTransactions;

    public Account() {
    }

    public Account(User user, double balance, Boolean isUnlocked) {
        this.user = user;
        this.balance = balance;
        this.isUnlocked = isUnlocked;
    }

    @Override
    public String toString() {
        return "Account{" +
                "account_id=" + accountId +
                ", user=" + user.getEmail() +
                ", balance=" + balance +
                ", isUnlocked=" + isUnlocked +
                '}';
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Boolean getUnlocked() {
        return isUnlocked;
    }

    public void setUnlocked(Boolean unlocked) {
        isUnlocked = unlocked;
    }

    public List<BankTransaction> getTransactions() {
        return bankTransactions;
    }

    public void setTransactions(List<BankTransaction> bankTransactions) {
        this.bankTransactions = bankTransactions;
    }


}
