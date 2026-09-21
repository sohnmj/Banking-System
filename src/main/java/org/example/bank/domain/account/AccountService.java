package org.example.bank.domain.account;

import lombok.RequiredArgsConstructor;
import org.example.bank.domain.user.User;
import org.example.bank.domain.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserService userService;
    @Transactional
    public AccountCreatedResponse createAccount(AccountCreatedRequest request){
        Account account = Account.createAccount(request.user(),
                request.accountNumber(), request.accountName(), request.balance());
        Account save = accountRepository.save(account);
        return new AccountCreatedResponse(save.getId(),save.getAccountNumber(), save.getAccountName());
    }
    //출금하기
    @Transactional
    public Boolean transferFund(String fromUsername, String fromAccountNumber,String toAccountNumber, BigDecimal fund){
        User fromUser=userService.getUser(fromUsername);
        Account fromAccount = accountRepository.findByAccountNumberAndUser_Id(fromAccountNumber, fromUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않거나 소유하지 않은 계좌입니다."));
        fromAccount.withdraw(fund);
        Account toAccount=accountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않은 계좌입니다."));
        toAccount.deposit(fund);
        return true;
    }



}
