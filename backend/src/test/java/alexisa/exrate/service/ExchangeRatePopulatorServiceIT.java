package alexisa.exrate.service;

import alexisa.exrate.config.DataConfig;
import alexisa.exrate.repository.ExchangeRateRepository;
import alexisa.exrate.test.BaseIntegrationTest;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

class ExchangeRatePopulatorServiceIT extends BaseIntegrationTest {
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;
    @Autowired
    private DataConfig dataConfig;
    @Autowired
    private ExchangeRatePopulatorService exchangeRatePopulatorService;

    @RegisterExtension
    private static final WireMockExtension dataApiMock = WireMockExtension.newInstance()
            .options(WireMockConfiguration.wireMockConfig()
                    .port(8000)
            ).build();

    @Test
    void testPopulateLatestData() {
        Assertions.assertEquals(0, exchangeRateRepository.findAll().size());
        var fromDate = LocalDate.of(2025, 1, 1);
        exchangeRatePopulatorService.setLastSuccessfulDataDate(fromDate);
        mockData(fromDate, DATA_API_RESPONSE);

        exchangeRatePopulatorService.populateLatestData();

        Assertions.assertEquals(1, exchangeRateRepository.findAll().size());
    }

    @Test
    void testConsecutivePopulateLatestData() {
        Assertions.assertEquals(0, exchangeRateRepository.findAll().size());
        var fromDate = LocalDate.of(2025, 1, 1);
        exchangeRatePopulatorService.setLastSuccessfulDataDate(fromDate);
        mockData(fromDate, DATA_API_RESPONSE);

        exchangeRatePopulatorService.populateLatestData();

        Assertions.assertEquals(1, exchangeRateRepository.findAll().size());

        var lastSuccessfulFetchDate = LocalDate.of(2025, 1, 2);
        mockData(lastSuccessfulFetchDate, DATA_API_RESPONSE_LATER);

        exchangeRatePopulatorService.populateLatestData();

        Assertions.assertEquals(2, exchangeRateRepository.findAll().size());
    }

    @Test
    void testPopulateStartUpData() {
        Assertions.assertEquals(0, exchangeRateRepository.findAll().size());
        var fromDate = LocalDate.now().minusDays(dataConfig.getPopulator().getStartUpDaysFetched());
        mockData(fromDate, DATA_API_RESPONSE);

        exchangeRatePopulatorService.populateStartUpData();

        Assertions.assertEquals(1, exchangeRateRepository.findAll().size());
    }

    public static final String DATA_API_RESPONSE = "exRateDataApiResponse.csv";
    public static final String DATA_API_RESPONSE_LATER = "exRateDataApiResponseLater.csv";


    static void mockData(LocalDate periodFrom, String fileName) {
        dataApiMock.stubFor(WireMock.get(String.format(
                        "/api?startPeriod=%s", DateTimeFormatter.ISO_LOCAL_DATE.format(periodFrom)
                )).willReturn(WireMock.aResponse()
                        .withBody(loadResourceAsString("fixtures/" + fileName))
                )
        );
    }

    static String loadResourceAsString(String resourcePath) {
        try (var in = ClassLoader.getSystemResourceAsStream(resourcePath)) {
            return IOUtils.toString(Objects.requireNonNull(in), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
