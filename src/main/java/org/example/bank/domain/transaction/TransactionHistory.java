package org.example.bank.domain.transaction;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.bank.domain.account.Account;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_histories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransactionHistory  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 출금 계좌
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_account_id", nullable = false)
    private Account fromAccount;

    // 입금 계좌
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_account_id", nullable = false)
    private Account toAccount;

    // 거래 금액
    @Column(nullable = false)
    private BigDecimal amount;

    // 거래 유형
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 송금 전용
    public static TransactionHistory createTransferHistory(Account fromAccount, Account toAccount, BigDecimal amount) {
        if (fromAccount == null || toAccount == null) {
            throw new IllegalArgumentException("출금 계좌와 입금 계좌는 필수입니다.");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("거래 금액은 0원보다 커야 합니다.");
        }

        TransactionHistory history = new TransactionHistory();
        history.fromAccount = fromAccount;
        history.toAccount = toAccount;
        history.amount = amount;
        history.type = TransactionType.TRANSFER;
        history.createdAt=LocalDateTime.now();
        return history;
    }
}