package alexisa.exrate.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {
    private final DataConfig dataConfig;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .build();
    }
}
