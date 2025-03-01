package alexisa.exrate.service;

import alexisa.exrate.model.dto.ExchangeRateDto;
import alexisa.exrate.model.entity.ExchangeRate;
import alexisa.exrate.repository.ExchangeRateRepository;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {
    private final ExchangeRateRepository exchangeRateRepository;

    @Transactional
    public void saveExchangeRates(List<ExchangeRateDto> exchangeRates) {
        var entities = exchangeRates.stream()
                .map(ExchangeRateDto::toEntity)
                .toList();
        var ids = entities.stream().map(ExchangeRate::getId).toList();
        var existingIds = new HashSet<>(
                exchangeRateRepository.findAllById(ids).stream()
                        .map(ExchangeRate::getId)
                        .toList()
        );
        var notExisting = entities.stream()
                .filter(ent -> !existingIds.contains(ent.getId()))
                .toList();
        exchangeRateRepository.saveAll(notExisting);
    }

    public List<ExchangeRateDto> fetchExchangeRates(@Nullable String currency, @Nullable LocalDate fromDate) {
        return exchangeRateRepository.findByCurrencyAndDate(currency, fromDate).stream()
                .map(ExchangeRateDto::fromEntity)
                .toList();
    }

    public List<ExchangeRateDto> fetchLatest() {
        return exchangeRateRepository.findLatestExchangeRatesForEachCurrency().stream()
                .map(ExchangeRateDto::fromEntity)
                .toList();
    }
}
