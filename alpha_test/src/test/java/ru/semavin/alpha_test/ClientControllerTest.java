package ru.semavin.alpha_test;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.semavin.alpha_test.config.TestConfig;
import ru.semavin.alpha_test.controllers.ClientController;
import ru.semavin.alpha_test.controllers.GlobalExceptionHandler;
import ru.semavin.alpha_test.services.ClientService;
import ru.semavin.alpha_test.utils.ClientNotFoundException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
@ContextConfiguration(classes = TestConfig.class)
@Import(GlobalExceptionHandler.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ClientController clientController;
    @Autowired
    private ClientService clientService;

    @Test
    void testGetAllClients_whenNoClientsExist() throws Exception {
        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(content().string("List clients is empty"));
    }

    @Test
    void testCreateClient_whenValidInput() throws Exception {
        String validClientJson = """
            {
                "name": "John",
                "lastName": "Doe",
                "contacts": [
                    {
                        "phone": "+79991234567",
                        "email": "john.doe@example.com"
                    }
                ]
            }
        """;

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validClientJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Client add"));
    }

    @Test
    void testCreateClient_whenInvalidInput() throws Exception {
        String invalidClientJson = """
        {
            "name": "",
            "lastName": "",
            "contacts": [
                {
                    "phone": "invalid",
                    "email": "invalid"
                }
            ]
        }
    """;

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidClientJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Name must not be blank"))
                .andExpect(jsonPath("$.lastName").value("Last name must not be blank"))
                .andExpect(jsonPath("$.contacts[0].phone").doesNotExist())
                .andExpect(jsonPath("$.contacts[0].email").doesNotExist());
    }


    @Test
    void testGetClientById_whenClientDoesNotExist() throws Exception {
        Mockito.when(clientService.getClientDTOById(10L))
                .thenThrow(new ClientNotFoundException("Client with id 10 not found"));

        mockMvc.perform(get("/api/clients/10"))
                .andExpect(status().is(400))
                .andExpect(content().string("Client with id 10 not found"));
    }

    @Test
    void testUpdateClient_whenValidInput() throws Exception {
        String updatedClientJson = """
            {
                "name": "Updated John",
                "lastName": "Doe",
                "contacts": [
                    {
                        "phone": "+79991234567",
                        "email": "updated.doe@example.com"
                    }
                ]
            }
        """;

        mockMvc.perform(put("/api/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedClientJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Client with id:1 updated"));
    }

    @Test
    void testDeleteClient_whenClientExists() throws Exception {
        mockMvc.perform(delete("/api/clients/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Client with id 1 deleted successfully"));
    }

    @Test
    void testDeleteClient_whenClientDoesNotExist() throws Exception {
        Mockito.doThrow(new ClientNotFoundException("Client with id 999 not found"))
                .when(clientService).deleteClient(999L);

        mockMvc.perform(delete("/api/clients/999"))
                .andExpect(status().is(400))
                .andExpect(content().string("Client with id 999 not found"));
    }
}
