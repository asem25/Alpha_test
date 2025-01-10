package ru.semavin.alpha_test.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.semavin.alpha_test.dtos.ClientDTO;
import ru.semavin.alpha_test.dtos.ContactDTO;
import ru.semavin.alpha_test.models.Client;
import ru.semavin.alpha_test.models.Contact;
import ru.semavin.alpha_test.repositories.ClientRepository;
import ru.semavin.alpha_test.repositories.ContactRepository;
import ru.semavin.alpha_test.utils.ClientNotFoundException;
import ru.semavin.alpha_test.utils.ContactNotFoundException;

import java.util.stream.Collectors;
import java.util.*;

@Service
@Slf4j
public class CoordinatorService {

    private final ClientRepository clientRepository;
    private final ContactRepository contactRepository;

    public CoordinatorService(ClientRepository clientRepository, ContactRepository contactRepository) {
        this.clientRepository = clientRepository;
        this.contactRepository = contactRepository;
    }

    @Transactional
    public void updateContact(Long id, ContactDTO contactDTO) {
        Contact contactFromRepository = findContactById(id);
        Client client = findClientById(contactDTO.getClient_id());

        contactFromRepository.setClient(client);
        contactFromRepository.setEmail(contactDTO.getEmail());
        contactFromRepository.setPhone(contactDTO.getPhone());

        contactRepository.save(contactFromRepository);
        log.info("Contact with ID {} updated", id);
    }

    @Transactional
    public void updateClient(Long id, ClientDTO clientDTO) {
        Client clientFromRepository = findClientById(id);

        clientFromRepository.setName(clientDTO.getName());
        clientFromRepository.setLastName(clientDTO.getLastName());

        List<Contact> updatedContacts = clientDTO.getContacts().stream()
                .map(contactDTO -> findContactByPhoneOrCreate(id, contactDTO))
                .collect(Collectors.toList());

        clientFromRepository.getContacts().clear();
        clientFromRepository.getContacts().addAll(updatedContacts);


        clientRepository.save(clientFromRepository);
        log.info("Client with ID {} updated", id);
    }
    @Transactional
    public Contact findContactByPhoneOrCreate(Long id, ContactDTO contactDTO) {
        return contactRepository.findByPhone(contactDTO.getPhone())
                .orElseGet(() -> Contact.builder()
                        .client(findClientById(id))
                        .phone(contactDTO.getPhone())
                        .email(contactDTO.getEmail())
                        .build());
    }

    private Contact findContactById(Long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException("Contact with ID: " + id + " not found"));
    }

    public Client findClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client with ID: " + id + " not found"));
    }

    public static Contact convertContactDTOtoContact(ContactDTO contactDTO) {
        return Contact.builder()
                .phone(contactDTO.getPhone())
                .email(contactDTO.getEmail())
                .build(); // Client связывается отдельно
    }

    public static ContactDTO convertContactToContactDTO(Contact contact) {
        return ContactDTO.builder()
                .client_id(contact.getClient().getClientId())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .build();
    }

    public static Client convertDTOToClient(ClientDTO clientDTO) {
        return Client.builder()
                .name(clientDTO.getName())
                .lastName(clientDTO.getLastName())
                .build(); // Контакты связываются отдельно
    }

    public static ClientDTO convertClientToDTO(Client client) {
        return ClientDTO.builder()
                .name(client.getName())
                .lastName(client.getLastName())
                .contacts(client.getContacts().stream()
                        .map(CoordinatorService::convertContactToContactDTO)
                        .toList())
                .build();
    }
}
