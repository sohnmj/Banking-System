package org.example.bank.domain.account;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.bank.domain.user.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true, length = 30)
    private String accountNumber;

    @Column(nullable = false, length = 30)
    private String accountName;

    @Column(nullable = false)
    private BigDecimal balance;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static Account createAccount(User user, String accountNumber,String accountName, BigDecimal initialBalance) {
        if (initialBalance == null || initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("초기 잔액은 0원 이상이어야 합니다.");
        }
        Account account = new Account();
        account.user = user;
        account.accountNumber = accountNumber;
        account.balance = initialBalance;
        account.status = AccountStatus.ACTIVE;
        account.createdAt = LocalDateTime.now();
        account.accountName=accountName;
        return account;
    }
    public void withdraw(BigDecimal amount) {
        validateActiveStatus();
        validateAmount(amount, "출금");

        if (this.balance.compareTo(amount) < 0) {
            throw new IllegalStateException("계좌 잔액이 부족합니다. 현재 잔액: " + this.balance + ", 요청 금액: " + amount);
        }

        this.balance = this.balance.subtract(amount);
    }
    public void deposit(BigDecimal amount) {
        validateActiveStatus();
        validateAmount(amount, "입금");

        this.balance = this.balance.add(amount);
    }
    private void validateActiveStatus() {
        if (this.status != AccountStatus.ACTIVE) {
            throw new IllegalStateException("활성 상태(ACTIVE)인 계좌에서만 거래가 가능합니다. 현재 상태: " + this.status);
        }
    }

    private void validateAmount(BigDecimal amount, String operationName) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(operationName + " 금액은 0원보다 커야 합니다.");
        }
    }
}
