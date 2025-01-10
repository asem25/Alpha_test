package ru.semavin.alpha_test.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.semavin.alpha_test.dtos.ClientDTO;
import ru.semavin.alpha_test.services.ClientService;

import java.util.*;


@RestController
@RequestMapping("/api/clients")
public class ClientController {
    private ClientService clientService;
    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<?> getAllClients() {
        List<ClientDTO> clients = clientService.getAllClients();

        return clients.isEmpty() ? ResponseEntity.ok("List clients is empty") : ResponseEntity.ok(clients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClientDTOById(id));
    }

    @PostMapping
    public ResponseEntity<?> createClient(@RequestBody @Valid ClientDTO clientDTO) {
        clientService.createClient(clientDTO);
        return ResponseEntity.ok("Client add");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClient(@PathVariable Long id, @RequestBody @Valid ClientDTO clientDTO) {
        clientService.updateClient(id, clientDTO);
        return ResponseEntity.ok("Client with id:" + id + " updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.ok("Client with id " + id + " deleted successfully");
    }
}
