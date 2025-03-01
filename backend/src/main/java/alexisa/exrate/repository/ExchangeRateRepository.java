package alexisa.exrate.repository;

import alexisa.exrate.model.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, ExchangeRate.ExchangeRateId> {

    @Query("""
            SELECT e FROM ExchangeRate e WHERE
            (:currency IS NULL OR e.currency = :currency)
            AND (:date IS NULL OR e.date >= :date)
            """)
    List<ExchangeRate> findByCurrencyAndDate(@Param("currency") String currency, @Param("date") LocalDate date);

    @Query("SELECT e FROM ExchangeRate e WHERE e.date IN (SELECT MAX(e2.date) FROM ExchangeRate e2 WHERE e2.currency = e.currency GROUP BY e2.currency)")
    List<ExchangeRate> findLatestExchangeRatesForEachCurrency();

}