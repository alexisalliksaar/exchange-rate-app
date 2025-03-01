package alexisa.exrate.service;

import alexisa.exrate.config.DataConfig;
import alexisa.exrate.model.dto.ExchangeRateDto;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;

@RequiredArgsConstructor
@Service
@Slf4j
public class ExchangeRatePopulatorService {
    private final ExchangeRateService exchangeRateService;
    private final ExchangeRateFetchingService exchangeRateFetchingService;
    private final DataConfig dataConfig;

    // TODO Some sort of error handling and retrying mechanism for data population

    @Setter
    private LocalDate lastSuccessfulDataDate = null;
    @Scheduled(cron = "${data.populator.dailyFetchCronPattern}", zone = "UTC")
    public void populateLatestData() {
        log.info("Starting latest data population cron");
        if (lastSuccessfulDataDate != null) {
            fetchAndSaveData(lastSuccessfulDataDate);
        } else {
            log.warn("lastSuccessfulDataDate is null, skipping data fetching");
        }
    }

    public void populateStartUpData() {
        log.info("Starting startup data population");
        var daysFetchedCount = dataConfig.getPopulator().getStartUpDaysFetched();
        fetchAndSaveData(LocalDate.now().minusDays(daysFetchedCount));
    }

    private void fetchAndSaveData(LocalDate fromDate) {
        try {
            var exchangeRates = exchangeRateFetchingService.fetchExchangeRates(fromDate);
            exchangeRateService.saveExchangeRates(exchangeRates);
            lastSuccessfulDataDate = exchangeRates.stream()
                    .max(Comparator.comparing(ExchangeRateDto::date))
                    .map(ExchangeRateDto::date)
                    .orElse(lastSuccessfulDataDate);
        } catch (Exception e) {
            log.error("Failed to fetch and save data", e);
        }
    }
}
