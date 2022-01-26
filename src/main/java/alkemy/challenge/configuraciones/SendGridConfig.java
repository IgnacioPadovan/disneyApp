package alkemy.challenge.configuraciones;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class SendGridConfig {

    @Value("${sendgrid.api.key}")
    private String sendGridAPIKey;

    @Bean
    public SendGrid getSendGrid() {
        return new SendGrid(sendGridAPIKey);
    }
}
