package ru.semavin.alpha_test.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.semavin.alpha_test.dtos.ClientDTO;
import ru.semavin.alpha_test.services.ClientService;

import java.util.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Клиенты", description = "Управление клиентами")
@RestController
@RequestMapping("/api/clients")
public class ClientController {
    private ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Operation(summary = "Получить всех клиентов", description = "Возвращает список всех клиентов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка клиентов",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClientDTO.class))),
            @ApiResponse(responseCode = "204", description = "Список клиентов пуст")
    })
    @GetMapping
    public ResponseEntity<?> getAllClients() {
        List<ClientDTO> clients = clientService.getAllClients();

        return clients.isEmpty() ? ResponseEntity.ok("List clients is empty") : ResponseEntity.ok(clients);
    }

    @Operation(summary = "Получить клиента по ID", description = "Возвращает данные клиента по его идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение клиента",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClientDTO.class))),
            @ApiResponse(responseCode = "400", description = "Клиент не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClientDTOById(id));
    }

    @Operation(summary = "Создать клиента", description = "Добавляет нового клиента в систему")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Клиент успешно создан")
    })
    @PostMapping
    public ResponseEntity<?> createClient(@RequestBody @Valid ClientDTO clientDTO) {
        clientService.createClient(clientDTO);
        return ResponseEntity.ok("Client add");
    }

    @Operation(summary = "Обновить данные клиента", description = "Обновляет информацию о клиенте по его идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Клиент успешно обновлен")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateClient(@PathVariable Long id, @RequestBody @Valid ClientDTO clientDTO) {
        clientService.updateClient(id, clientDTO);
        return ResponseEntity.ok("Client with id:" + id + " updated");
    }

    @Operation(summary = "Удалить клиента", description = "Удаляет клиента по его идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Клиент успешно удален")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.ok("Client with id " + id + " deleted successfully");
    }
}
