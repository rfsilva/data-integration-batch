package br.com.rodrigo.dataintegration.batch.config;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Configuration
public class SSOConfig {

    @Value("${service.token-path}")
    private String tokenPath;

    @Value("${service.data-integration.host}")
    private String host;

    @Value("${service.data-integration.client-id}")
    private String clientId;

    @Value("${service.data-integration.client-secret}")
    private String clientSecret;
}
