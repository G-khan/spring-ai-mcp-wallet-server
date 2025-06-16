package com.wallet.service;

import com.wallet.model.Wallet;
import com.wallet.repository.WalletRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletService {
    private static final Logger logger = LoggerFactory.getLogger(WalletService.class);
    private final WalletRepository walletRepository;

    @Tool(name = "create-wallet", description = "Creates a new wallet with the given details")
    @Transactional
    public Wallet createWallet(Wallet wallet) {
        logger.info("Creating new wallet with name: {}", wallet.getName());
        Wallet savedWallet = walletRepository.save(wallet);
        logger.info("Successfully created wallet with ID: {}", savedWallet.getId());
        return savedWallet;
    }

    @Tool(name = "get-all-wallets", description = "Retrieves a list of all wallets")
    @Transactional(readOnly = true)
    public List<Wallet> getAllWallets() {
        logger.debug("Retrieving all wallets");
        List<Wallet> wallets = walletRepository.findAll();
        logger.debug("Found {} wallets", wallets.size());
        return wallets;
    }

    @Tool(name = "get-wallet-by-id", description = "Retrieves a wallet by its ID")
    @Transactional(readOnly = true)
    public Optional<Wallet> getWalletById(Long id) {
        logger.debug("Retrieving wallet with ID: {}", id);
        Optional<Wallet> wallet = walletRepository.findById(id);
        if (wallet.isPresent()) {
            logger.debug("Found wallet with ID: {}", id);
        } else {
            logger.warn("Wallet not found with ID: {}", id);
        }
        return wallet;
    }

    @Tool(name = "update-wallet", description = "Updates an existing wallet with new details")
    @Transactional
    public Wallet updateWallet(Long id, Wallet walletDetails) {
        logger.info("Updating wallet with ID: {}", id);
        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found"));
        
        logger.debug("Updating wallet details - Old name: {}, New name: {}", wallet.getName(), walletDetails.getName());
        wallet.setName(walletDetails.getName());
        wallet.setBalance(walletDetails.getBalance());
        
        Wallet updatedWallet = walletRepository.save(wallet);
        logger.info("Successfully updated wallet with ID: {}", id);
        return updatedWallet;
    }

    @Tool(name = "delete-wallet", description = "Deletes a wallet by its ID")
    @Transactional
    public void deleteWallet(Long id) {
        logger.info("Deleting wallet with ID: {}", id);
        walletRepository.deleteById(id);
        logger.info("Successfully deleted wallet with ID: {}", id);
    }

    @Tool(name = "add-expense", description = "Adds an expense to a wallet, reducing its balance")
    @Transactional
    public Wallet addExpense(Long id, BigDecimal amount) {
        logger.info("Adding expense of {} to wallet with ID: {}", amount, id);
        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found"));
        
        if (wallet.getBalance().compareTo(amount) < 0) {
            logger.error("Insufficient balance for wallet ID: {}. Current balance: {}, Required amount: {}", 
                id, wallet.getBalance(), amount);
            throw new InsufficientBalanceException("Insufficient balance");
        }
        
        wallet.setBalance(wallet.getBalance().subtract(amount));
        Wallet updatedWallet = walletRepository.save(wallet);
        logger.info("Successfully added expense. New balance: {}", updatedWallet.getBalance());
        return updatedWallet;
    }

    @Tool(name = "add-income", description = "Adds income to a wallet, increasing its balance")
    @Transactional
    public Wallet addIncome(Long id, BigDecimal amount) {
        logger.info("Adding income of {} to wallet with ID: {}", amount, id);
        Wallet wallet = walletRepository.findById(id)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found"));
        
        wallet.setBalance(wallet.getBalance().add(amount));
        Wallet updatedWallet = walletRepository.save(wallet);
        logger.info("Successfully added income. New balance: {}", updatedWallet.getBalance());
        return updatedWallet;
    }
} 