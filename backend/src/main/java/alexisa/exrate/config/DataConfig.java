package alexisa.exrate.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "data")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Configuration
public class DataConfig {

    private ApiConfig api;
    private PopulatorConfig populator;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class ApiConfig {
        private String url;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class PopulatorConfig {
        private boolean populateOnStartup = true;
        private int startUpDaysFetched;
        private String dailyFetchCronPattern;
    }
}
