package alexisa.exrate.controller;

import alexisa.exrate.model.dto.ExchangeRateDto;
import alexisa.exrate.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;

    /**
     *
     * @param currency currency that is fetched. If null all currencies will be returned
     * @param fromDate If specified, will fetch data from the end of day of {@code fromDate}
     */
    @GetMapping("/exchangeRates")
    public List<ExchangeRateDto> fetchExchangeRates(
            @RequestParam(value = "currency", required = false) String currency,
            @RequestParam(value = "fromDate", required = false) LocalDate fromDate
    ) {
        var upperCaseCurr = currency == null ? null : currency.toUpperCase();
        return exchangeRateService.fetchExchangeRates(upperCaseCurr, fromDate);
    }

    @GetMapping("/exchangeRates/latest")
    public List<ExchangeRateDto> fetchExchangeRates() {
        return exchangeRateService.fetchLatest();
    }
}
