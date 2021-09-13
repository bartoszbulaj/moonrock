package pl.bartoszbulaj.moonrock.simulator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import pl.bartoszbulaj.moonrock.simulator.mapper.CandleMapper;
import pl.bartoszbulaj.moonrock.simulator.service.impl.SimulatorHistoryServiceImpl;

@Configuration
public class BeansConfiguration {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public SimulatorHistoryServiceImpl simulatorHistoryService(RestTemplate restTemplate, CandleMapper candleMapper) {
		return new SimulatorHistoryServiceImpl(restTemplate, candleMapper);
	}
}
