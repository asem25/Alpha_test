package ru.semavin.alpha_test.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.semavin.alpha_test.config.TestConfig;
import ru.semavin.alpha_test.dtos.ContactDTO;
import ru.semavin.alpha_test.services.ContactService;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContactController.class)
@ContextConfiguration(classes = TestConfig.class)
@Import(GlobalExceptionHandler.class)
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContactService contactService;

    @BeforeEach
    void setUp() {
        Mockito.reset(contactService);
    }

    @Test
    void testGetAllContacts_whenContactsExist() throws Exception {
        ContactDTO contact1 = new ContactDTO("+79991234567", "contact1@example.com", 1L);
        ContactDTO contact2 = new ContactDTO("+79991112233", "contact2@example.com", 2L);

        when(contactService.getAllContacts()).thenReturn(Arrays.asList(contact1, contact2));

        mockMvc.perform(get("/api/contacts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].phone").value("+79991234567"))
                .andExpect(jsonPath("$[0].email").value("contact1@example.com"))
                .andExpect(jsonPath("$[1].phone").value("+79991112233"))
                .andExpect(jsonPath("$[1].email").value("contact2@example.com"));
    }

    @Test
    void testGetAllContacts_whenNoContactsExist() throws Exception {
        when(contactService.getAllContacts()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/contacts"))
                .andExpect(status().isOk())
                .andExpect(content().string("List contact is empty"));
    }

    @Test
    void testGetContactById_whenContactExists() throws Exception {
        ContactDTO contact = new ContactDTO("+79991234567", "contact@example.com", 1L);
        when(contactService.getContactById(1L)).thenReturn(contact);

        mockMvc.perform(get("/api/contacts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value("+79991234567"))
                .andExpect(jsonPath("$.email").value("contact@example.com"));
    }

    @Test
    void testCreateContact_whenValidInput() throws Exception {
        String validContactJson = """
                    {
                        "phone": "+79991234567",
                        "email": "contact@example.com",
                        "client_id": 1
                    }
                """;

        mockMvc.perform(post("/api/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validContactJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Contact added"));

        verify(contactService, times(1)).createContact(any(ContactDTO.class));
    }

    @Test
    void testCreateContact_whenInvalidInput() throws Exception {
        String invalidContactJson = """
                    {
                        "phone": "invalid",
                        "email": "invalid",
                        "client_id": 1
                    }
                """;

        mockMvc.perform(post("/api/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidContactJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateContact_whenValidInput() throws Exception {
        String updatedContactJson = """
                    {
                        "phone": "+79991112233",
                        "email": "updated@example.com",
                        "client_id": 1
                    }
                """;

        mockMvc.perform(put("/api/contacts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedContactJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value("+79991112233"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));

        verify(contactService, times(1)).updateContact(eq(1L), any(ContactDTO.class));
    }

    @Test
    void testDeleteContact_whenContactExists() throws Exception {
        mockMvc.perform(delete("/api/contacts/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Contact with id: 1 deleted successfully"));

        verify(contactService, times(1)).deleteContact(1L);
    }

}
