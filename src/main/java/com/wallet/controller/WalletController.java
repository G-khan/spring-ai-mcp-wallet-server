package com.wallet.controller;

import com.wallet.model.Wallet;
import com.wallet.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/wallets")
@Slf4j
public class WalletController {

    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public Wallet createWallet(@RequestBody Wallet wallet) {
        return walletService.createWallet(wallet);
    }

    @GetMapping
    public List<Wallet> getAllWallets() {
        return walletService.getAllWallets();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Wallet> getWalletById(@PathVariable Long id) {
        return walletService.getWalletById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Wallet> updateWallet(@PathVariable Long id, @RequestBody Wallet walletDetails) {
        Wallet updatedWallet = walletService.updateWallet(id, walletDetails);
        return ResponseEntity.ok(updatedWallet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWallet(@PathVariable Long id) {
        walletService.deleteWallet(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/expense")
    public ResponseEntity<Wallet> addExpense(@PathVariable Long id, @RequestParam BigDecimal amount) {
        Wallet updatedWallet = walletService.addExpense(id, amount);
        return ResponseEntity.ok(updatedWallet);
    }

    @PostMapping("/{id}/income")
    public ResponseEntity<Wallet> addIncome(@PathVariable Long id, @RequestParam BigDecimal amount) {
        Wallet updatedWallet = walletService.addIncome(id, amount);
        log.info("Rest Adding income of {} to wallet with ID: {}", amount, id);
        return ResponseEntity.ok(updatedWallet);
    }
} 