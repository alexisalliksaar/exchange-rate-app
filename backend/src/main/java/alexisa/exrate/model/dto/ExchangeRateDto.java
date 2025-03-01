package alexisa.exrate.model.dto;

import alexisa.exrate.model.entity.ExchangeRate;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ExchangeRateDto(
        String currency,
        LocalDate date,
        float exRateValue
) {
    public static ExchangeRateDto fromEntity(ExchangeRate entity) {
        return ExchangeRateDto.builder()
                .currency(entity.getCurrency())
                .date(entity.getDate())
                .exRateValue(entity.getExRateValue())
                .build();
    }

    public static ExchangeRate toEntity(ExchangeRateDto dto) {
        return ExchangeRate.builder()
                .currency(dto.currency())
                .date(dto.date())
                .exRateValue(dto.exRateValue())
                .build();
    }
}
