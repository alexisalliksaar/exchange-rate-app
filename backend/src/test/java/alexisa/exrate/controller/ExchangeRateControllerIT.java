package alexisa.exrate.controller;

import alexisa.exrate.model.dto.ExchangeRateDto;
import alexisa.exrate.model.entity.ExchangeRate;
import alexisa.exrate.repository.ExchangeRateRepository;
import alexisa.exrate.test.BaseIntegrationTest;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class ExchangeRateControllerIT extends BaseIntegrationTest {

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Test
    void getLatestTest() throws Exception {
        ExchangeRate entity = ExchangeRate.builder()
                .currency("USD")
                .exRateValue(2.0f)
                .date(LocalDate.of(2025, 1, 1))
                .build();
        exchangeRateRepository.save(entity);
        flushAndClear();

        var mvcResult = mockMvc.perform(get("/api/exchangeRates/latest")).andExpect(status().isOk()).andReturn();
        List<ExchangeRateDto> res = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertEquals(1, res.size());
        var dto = res.getFirst();
        Assertions.assertEquals(entity.getCurrency(), dto.currency());
        Assertions.assertEquals(entity.getExRateValue(), dto.exRateValue());
        Assertions.assertEquals(entity.getDate(), dto.date());

        ExchangeRate newEntity = ExchangeRate.builder()
                .currency("USD")
                .exRateValue(2.5f)
                .date(LocalDate.of(2025, 1, 2))
                .build();
        exchangeRateRepository.save(newEntity);
        flushAndClear();

        mvcResult = mockMvc.perform(get("/api/exchangeRates/latest")).andExpect(status().isOk()).andReturn();
        res = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertEquals(1, res.size());
        dto = res.getFirst();
        Assertions.assertEquals(newEntity.getCurrency(), dto.currency());
        Assertions.assertEquals(newEntity.getExRateValue(), dto.exRateValue());
    }


    @Test
    void getExchangeRatesTest() throws Exception {
        ExchangeRate entity = ExchangeRate.builder()
                .currency("USD")
                .exRateValue(2.0f)
                .date(LocalDate.of(2025, 1, 1))
                .build();
        ExchangeRate entity2 = ExchangeRate.builder()
                .currency("USD")
                .exRateValue(2.5f)
                .date(LocalDate.of(2025, 1, 2))
                .build();
        exchangeRateRepository.saveAll(List.of(entity, entity2));
        flushAndClear();

        var mvcResult = mockMvc.perform(get("/api/exchangeRates")).andExpect(status().isOk()).andReturn();
        List<ExchangeRateDto> res = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        Assertions.assertEquals(2, res.size());
        var dto = res.getFirst();
        Assertions.assertEquals(entity.getCurrency(), dto.currency());
        Assertions.assertEquals(entity.getExRateValue(), dto.exRateValue());
        Assertions.assertEquals(entity.getDate(), dto.date());

        var dto2 = res.get(1);
        Assertions.assertEquals(entity2.getCurrency(), dto2.currency());
        Assertions.assertEquals(entity2.getExRateValue(), dto2.exRateValue());
        Assertions.assertEquals(entity2.getDate(), dto2.date());
    }
}
