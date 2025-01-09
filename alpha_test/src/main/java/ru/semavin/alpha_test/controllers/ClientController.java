package ru.semavin.alpha_test.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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




}
