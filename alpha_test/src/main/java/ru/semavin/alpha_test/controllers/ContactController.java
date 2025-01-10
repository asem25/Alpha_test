package ru.semavin.alpha_test.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.semavin.alpha_test.dtos.ContactDTO;
import ru.semavin.alpha_test.services.ContactService;

import java.util.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Контроллер контактов", description = "Управление контактами")
@RestController
@RequestMapping("/api/contacts")
public class ContactController {
    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @Operation(summary = "Получить все контакты", description = "Получить список всех контактов.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContactDTO.class))),
            @ApiResponse(responseCode = "204", description = "Список контактов пуст")
    })
    @GetMapping
    public ResponseEntity<?> getAllContacts() {
        List<ContactDTO> contactDTOS = contactService.getAllContacts();
        return contactDTOS.isEmpty() ? ResponseEntity.ok("List contact is empty") : ResponseEntity.ok(contactDTOS);
    }

    @Operation(summary = "Получить контакт по ID", description = "Получить контакт по его идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContactDTO.class))),
            @ApiResponse(responseCode = "400", description = "Контакт не найден")
    })
    @GetMapping("/{id}")
    public ContactDTO getContactById(@PathVariable Long id) {
        return contactService.getContactById(id);
    }

    @Operation(summary = "Создать новый контакт", description = "Добавить новый контакт в систему.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Контакт успешно создан")
    })
    @PostMapping
    public ResponseEntity<?> createContact(@RequestBody @Valid ContactDTO contact) {
        contactService.createContact(contact);
        return ResponseEntity.ok("Contact added");
    }

    @Operation(summary = "Обновить существующий контакт", description = "Обновить информацию о существующем контакте по его ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Контакт успешно обновлен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContactDTO.class))),
            @ApiResponse(responseCode = "400", description = "Контакт не найден")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateContact(@PathVariable Long id, @RequestBody @Valid ContactDTO contact) {
        contactService.updateContact(id, contact);
        return ResponseEntity.ok(contact);
    }

    @Operation(summary = "Удалить контакт", description = "Удалить контакт по его ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Контакт успешно удален"),
            @ApiResponse(responseCode = "400", description = "Контакт не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return ResponseEntity.ok("Contact with id: " + id + " deleted successfully");
    }
}
