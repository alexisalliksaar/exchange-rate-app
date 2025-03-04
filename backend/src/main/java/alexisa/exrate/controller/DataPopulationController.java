package alexisa.exrate.controller;

import alexisa.exrate.service.ExchangeRatePopulatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pop")
@RequiredArgsConstructor
@Profile("dev")
public class DataPopulationController {
    private final ExchangeRatePopulatorService exchangeRatePopulatorService;

    @GetMapping("/start")
    public void populateStartUpData() {
        exchangeRatePopulatorService.populateStartUpData();
    }

    @GetMapping("/latest")
    public void populateLatestData() {
        exchangeRatePopulatorService.populateLatestData();
    }

}
