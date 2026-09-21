package org.example.bank.domain.account;

import org.example.bank.domain.user.User;

import java.math.BigDecimal;

public record AccountCreatedRequest(User user, String accountNumber,String accountName, BigDecimal balance){

}
