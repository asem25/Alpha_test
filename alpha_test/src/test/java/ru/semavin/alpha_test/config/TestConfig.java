package ru.semavin.alpha_test.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.semavin.alpha_test.controllers.ClientController;
import ru.semavin.alpha_test.controllers.ContactController;
import ru.semavin.alpha_test.services.ClientService;
import ru.semavin.alpha_test.services.ContactService;

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
    @Bean
    public ContactService contactService() {
        return Mockito.mock(ContactService.class);
    }

    @Bean
    public ContactController contactController(ContactService contactService) {
        return new ContactController(contactService);
    }
}
