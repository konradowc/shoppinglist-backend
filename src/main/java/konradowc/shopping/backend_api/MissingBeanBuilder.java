package konradowc.shopping.backend_api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class MissingBeanBuilder {

    // since this bean wasn't being found
	@Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }
}
