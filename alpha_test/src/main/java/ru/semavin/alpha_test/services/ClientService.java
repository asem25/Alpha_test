package ru.semavin.alpha_test.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.semavin.alpha_test.dtos.ClientDTO;
import ru.semavin.alpha_test.models.Client;
import ru.semavin.alpha_test.models.Contact;
import ru.semavin.alpha_test.repositories.ClientRepository;
import ru.semavin.alpha_test.utils.ClientNotFoundException;

import java.util.List;

@Service
@Slf4j
public class ClientService {
    private final ClientRepository clientRepository;
    private final CoordinatorService coordinatorService;

    @Autowired
    public ClientService(ClientRepository clientRepository, CoordinatorService coordinatorService) {
        this.clientRepository = clientRepository;
        this.coordinatorService = coordinatorService;
    }

    public List<ClientDTO> getAllClients() {
        log.info("Fetching all clients");
        return clientRepository.findAll().stream()
                .map(CoordinatorService::convertClientToDTO)
                .toList();
    }

    public ClientDTO getClientDTOById(Long clientId) {
        log.info("Fetching client with ID: {}", clientId);
        return CoordinatorService.convertClientToDTO(getClientById(clientId));
    }

    public void updateClient(Long id, ClientDTO clientDTO) {
        log.info("Updating client with ID: {}", id);
        coordinatorService.updateClient(id, clientDTO);
    }

    public void deleteClient(Long id) {
        log.info("Deleting client with ID: {}", id);

        if (!clientRepository.existsById(id)) {
            throw new ClientNotFoundException("Client with ID: " + id + " not found");
        }

        clientRepository.deleteById(id);

        log.info("Client with ID: {} successfully deleted", id);
    }

    @Transactional
    public void createClient(ClientDTO clientDTO) {
        log.info("Creating client: {}", clientDTO);

        Client client = CoordinatorService.convertDTOToClient(clientDTO);

        List<Contact> contacts = clientDTO.getContacts().stream()
                .map(contactDTO -> {
                    Contact contact = CoordinatorService.convertContactDTOtoContact(contactDTO);
                    contact.setClient(client);
                    return contact;
                }).toList();

        client.setContacts(contacts);

        clientRepository.save(client);

        log.info("Client created: {}", client);
    }

    protected Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client with ID: " + id + " not found"));
    }
}

