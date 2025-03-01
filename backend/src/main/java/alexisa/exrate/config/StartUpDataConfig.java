package alexisa.exrate.config;

import alexisa.exrate.service.ExchangeRatePopulatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class StartUpDataConfig {
    private final DataConfig dataConfig;
    private final ExchangeRatePopulatorService exchangeRatePopulatorService;

    @EventListener(ApplicationReadyEvent.class)
    public void populateData() {
        if (dataConfig.getPopulator().isPopulateOnStartup()) {
            exchangeRatePopulatorService.populateStartUpData();
        } else {
            log.info("Skipping startup data population");
        }
    }
}
