package org.example.bank.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User getUser(String username){
        return userRepository.findByUsername(username).orElseThrow(()->new IllegalArgumentException("사용자가 없습니다."));
    }
}
