package ru.semavin.alpha_test.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.semavin.alpha_test.models.Contact;
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
}
