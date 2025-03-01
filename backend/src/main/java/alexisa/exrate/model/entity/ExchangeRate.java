package alexisa.exrate.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@IdClass(ExchangeRate.ExchangeRateId.class)
public class ExchangeRate {

    @Id
    private LocalDate date;

    @Id
    private String currency;

    private float exRateValue;

    public ExchangeRateId getId() {
        return ExchangeRateId.builder()
                .currency(getCurrency())
                .date(getDate())
                .build();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    @EqualsAndHashCode
    @Builder
    public static class ExchangeRateId implements Serializable {
        private LocalDate date;
        private String currency;
    }
}
