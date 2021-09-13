package pl.bartoszbulaj.moonrock.simulator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import pl.bartoszbulaj.moonrock.simulator.mapper.CandleMapper;
import pl.bartoszbulaj.moonrock.simulator.service.impl.HistoryServiceImpl;

@Configuration
public class BeansConfiguration {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public HistoryServiceImpl historyService(RestTemplate restTemplate, CandleMapper candleMapper) {
		return new HistoryServiceImpl(restTemplate, candleMapper);
	}
}
