// app/gestion/GRH/security/MongoConvertersConfig.java
package app.gestion.GRH.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.List;

@Configuration
public class MongoConvertersConfig {

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(List.of(
                new YearMonthWriteConverter(),
                new YearMonthReadConverter()
        ));
    }
}
