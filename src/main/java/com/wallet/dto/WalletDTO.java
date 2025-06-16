package com.wallet.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class WalletDTO {
    private String name;
    private BigDecimal balance;
} 