package com.marketplace.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marketplace.models.Role;
import com.marketplace.models.User;
import com.marketplace.models.Wallet;
import com.marketplace.repository.UserRepository;
import com.marketplace.repository.WalletRepository;

@Service
public class AuthService {
    @Autowired private UserRepository userRepository;
    @Autowired private WalletRepository walletRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtService jwtService;
    @Autowired private AuthenticationManager authenticationManager;

    @Transactional
    public String register(String name, String email, String password, Role role) {
        User user = new User(name, email, passwordEncoder.encode(password), role);
        userRepository.save(user);

        Wallet wallet = new Wallet(user, 0.0);
        walletRepository.save(wallet);

        user.setWallet(wallet);
        userRepository.save(user);

        return jwtService.generateToken(user.getEmail());
    }

    public String login(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        return jwtService.generateToken(email);
    }
}