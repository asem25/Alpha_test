package ru.semavin.alpha_test.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.semavin.alpha_test.controllers.ClientController;
import ru.semavin.alpha_test.services.ClientService;

@Configuration
public class TestConfig {

    @Bean
    public ClientService clientService() {
        return Mockito.mock(ClientService.class);
    }

    @Bean
    public ClientController clientController(ClientService clientService) {
        return new ClientController(clientService);
    }
}
