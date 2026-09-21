package org.example.bank.domain.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    //소유자 검증 계좌 조회
    Optional<Account> findByAccountNumberAndUser_Id(String AccountNumber, Long UserId);
    //단순 계좌 조회
    Optional<Account> findByAccountNumber(String AccountNumber);
}
