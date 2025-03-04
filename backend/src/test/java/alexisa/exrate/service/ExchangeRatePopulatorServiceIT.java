package alexisa.exrate.service;

import alexisa.exrate.repository.ExchangeRateRepository;
import alexisa.exrate.test.BaseIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ExchangeRatePopulatorServiceIT extends BaseIntegrationTest {
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Test
    void testPopulateLatestData() {
        Assertions.assertEquals(0, exchangeRateRepository.findAll().size());
    }
}
