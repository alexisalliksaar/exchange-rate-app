package alexisa.exrate.service;

import alexisa.exrate.config.DataConfig;
import alexisa.exrate.exception.DataFetchingException;
import alexisa.exrate.model.dto.ExchangeRateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateFetchingService {
    private final RestClient restClient;
    private final DataConfig dataConfig;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * @param from date considered at the end of day
     * @return exchange rates from the end of day since {@code from}
     */
    public List<ExchangeRateDto> fetchExchangeRates(LocalDate from) {
        return fetchExchangeRates(Map.of("startPeriod", from.format(DATE_FORMATTER)));
    }

    public List<ExchangeRateDto> fetchExchangeRates(int lastNDays) {
        return fetchExchangeRates(Map.of("lastNObservations", String.valueOf(lastNDays)));
    }

    private List<ExchangeRateDto> fetchExchangeRates(Map<String, String> queryParams) {
        String urlWithParams = UriComponentsBuilder.fromUriString(dataConfig.getApi().getUrl())
                .queryParams(toMultiValueMap(queryParams))
                .toUriString();
        var result = Objects.requireNonNull(
                restClient.get().uri(urlWithParams).exchange((request, response) -> {
                    if (response.getStatusCode().is2xxSuccessful()) {
                        var csvData = response.getBody();
                        return parseExchangeRateCsv(csvData);
                    }

                    throw new DataFetchingException(String.format("Failed to fetch exchange rate data. Api responded with statusCode: %d", response.getStatusCode().value()));
                })
        );
        log.info("Fetched {} exchange rates", result.size());
        return result;
    }

    private MultiValueMap<String, String> toMultiValueMap(Map<String, String> queryParams) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        if (queryParams != null) {
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                multiValueMap.add(entry.getKey(), entry.getValue());
            }
        }
        return multiValueMap;
    }

    private List<ExchangeRateDto> parseExchangeRateCsv(InputStream csvData) throws IOException {
        try (Reader reader = new InputStreamReader(csvData)) {
            Iterable<CSVRecord> records = CSVFormat.RFC4180.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .get()
                    .parse(reader);
            List<ExchangeRateDto> result = new ArrayList<>();
            for (CSVRecord csvRecord : records) {
                String currencyStr = csvRecord.get("CURRENCY");
                String dateStr = csvRecord.get("TIME_PERIOD");
                String valueStr = csvRecord.get("OBS_VALUE");
                result.add(ExchangeRateDto.builder()
                        .currency(currencyStr)
                        .date(LocalDate.parse(dateStr, DATE_FORMATTER))
                        .exRateValue(Float.parseFloat(valueStr))
                        .build()
                );
            }
            return result;
        }
    }
}
