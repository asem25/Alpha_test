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
    private final ClientService clientService;
    @Autowired
    public ContactService(ContactRepository contactRepository, ClientService clientService) {
        this.contactRepository = contactRepository;
        this.clientService = clientService;
    }

    public List<ContactDTO> getAllContacts() {
        return contactRepository.findAll().stream()
                .map(this::convertContactToContactDTO)
                .toList();
    }
    public ContactDTO getContactById(Long id) {
        return convertContactToContactDTO(contactRepository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException("Contact with this id: " + id + " not found")));
    }
    @Transactional
    public void createContact(ContactDTO contactDTO) {
        log.info("Create contact : " + contactDTO);
        contactRepository.save(convertContactDTOtoContact(contactDTO));
    }
    @Transactional
    public void updateContact(Long id, ContactDTO contactDTO) {

        Contact contactFromContactRepository = contactRepository.findById(id)
                .orElseThrow(() -> new ContactNotFoundException("Contact with this id: " + id + "not found"));

        Client client = clientService.getClientById(contactDTO.getClient_id());



        contactFromContactRepository.setClient(client);
        contactFromContactRepository.setEmail(contactDTO.getEmail());
        contactFromContactRepository.setPhone(contactDTO.getPhone());

        contactRepository.save(contactFromContactRepository);
        log.info("Contact with id: " + id + "update");
    }
    @Transactional
    public void deleteContact(Long id) {
        if (!contactRepository.existsById(id)) {

            throw new ContactNotFoundException("Contact with this id: " + id + " not found");
        }

        contactRepository.deleteById(id);
        log.info("Contact with id: " + id + " delete");
    }


    private Contact convertContactDTOtoContact(ContactDTO contactDTO){
        return Contact.builder()
                .phone(contactDTO.getPhone())
                .email(contactDTO.getEmail())
                .client(clientService.getClientById(contactDTO.getClient_id()))
                .build();
    }
    private ContactDTO convertContactToContactDTO(Contact contact){
        return ContactDTO.builder()
                .client_id(contact.getClient().getClientId())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .build();
    }
}
