package com.wallet.config;

import com.wallet.service.WalletService;
import io.modelcontextprotocol.server.McpServer;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class McpServerConfig {

    @Bean
    public List<ToolCallback> walletTools(
            WalletService walletService) {
        return List.of(
            ToolCallbacks.from( walletService)
        );
    }
} 