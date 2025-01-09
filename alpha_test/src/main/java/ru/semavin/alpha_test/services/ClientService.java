package ru.semavin.alpha_test.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.semavin.alpha_test.models.Client;
import ru.semavin.alpha_test.repositories.ClientRepository;
import ru.semavin.alpha_test.utils.ClientNotFoundException;

@Service
public class ClientService {
    private ClientRepository clientRepository;
    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
    public Client getClientById(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Client with id: " + clientId + " not found"));
    }
}
