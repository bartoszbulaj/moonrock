package pl.bartoszbulaj.moonrock.simulator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeansConfiguration {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	// TODO remove this comment
	// @Bean
	// public SimulatorHistoryServiceImpl simulatorHistoryService(RestTemplate
	// restTemplate, CandleMapper candleMapper) {
	// return new SimulatorHistoryServiceImpl(restTemplate, candleMapper);
	// }
}
