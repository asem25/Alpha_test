package ru.semavin.alpha_test.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.semavin.alpha_test.dtos.ContactDTO;
import ru.semavin.alpha_test.models.Client;
import ru.semavin.alpha_test.models.Contact;
import ru.semavin.alpha_test.repositories.ContactRepository;
import ru.semavin.alpha_test.utils.ContactNotFoundException;

import java.util.List;

@Service
@Slf4j
public class ContactService {
    private final ContactRepository contactRepository;
    private final CoordinatorService coordinatorService;

    @Autowired
    public ContactService(ContactRepository contactRepository, CoordinatorService coordinatorService) {
        this.contactRepository = contactRepository;
        this.coordinatorService = coordinatorService;
    }

    public List<ContactDTO> getAllContacts() {
        log.info("Fetching all contacts");
        return contactRepository.findAll().stream()
                .map(CoordinatorService::convertContactToContactDTO)
                .toList();
    }

    public ContactDTO getContactById(Long id) {
        log.info("Fetching contact with ID: {}", id);
        return CoordinatorService.convertContactToContactDTO(contactRepository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException("Contact with ID: " + id + " not found")));
    }

    @Transactional
    public void createContact(ContactDTO contactDTO) {
        log.info("Creating contact: {}", contactDTO);

        Client client = coordinatorService.findClientById(contactDTO.getClient_id());
        Contact contact = CoordinatorService.convertContactDTOtoContact(contactDTO);

        contact.setClient(client);

        contactRepository.save(contact);

        log.info("Contact created: {}", contact);
    }

    public void updateContact(Long id, ContactDTO contactDTO) {
        log.info("Updating contact with ID: {}", id);
        coordinatorService.updateContact(id, contactDTO);
    }

    @Transactional
    public void deleteContact(Long id) {
        log.info("Deleting contact with ID: {}", id);

        if (!contactRepository.existsById(id)) {
            throw new ContactNotFoundException("Contact with ID: " + id + " not found");
        }

        contactRepository.deleteById(id);

        log.info("Contact with ID: {} successfully deleted", id);
    }
}

