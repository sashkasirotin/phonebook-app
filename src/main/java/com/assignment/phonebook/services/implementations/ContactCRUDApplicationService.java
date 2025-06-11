package com.assignment.phonebook.services.implementations;

import com.assignment.phonebook.persistance.implemantations.ContactRepoApplicationService;
import com.assignment.phonebook.services.models.Contact;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactCRUDApplicationService {

    private static final Logger logger = LoggerFactory.getLogger(ContactCRUDApplicationService.class);

    @Autowired
    private ContactRepoApplicationService contactRepoApplicationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Contact> getContacts(int page, int size) {
        try {
            logger.info("Fetching contacts page={}, size={}", page, size);
            return contactRepoApplicationService.findAll(PageRequest.of(page, size)).getContent();
        } catch (Exception e) {
            logger.error("Failed to fetch contacts", e);
            throw new RuntimeException("Unable to fetch contacts", e);
        }
    }

    public Optional<Contact> getContact(Long id) {
        try {
            logger.info("Fetching contact with id={}", id);
            return contactRepoApplicationService.findById(id);
        } catch (Exception e) {
            logger.error("Error retrieving contact with id={}", id, e);
            throw new RuntimeException("Failed to retrieve contact", e);
        }
    }

    public Contact addContact(Contact contact) {
        try {
            logger.info("Attempting to add new contact: {}", contact);
            if (contact.getId() != null && getContact(contact.getId()).isPresent()) {
                logger.warn("Attempt to add a contact with existing id={}", contact.getId());
                throw new IllegalStateException("Contact already exists with id=" + contact.getId());
            }
            contact.setId(null);
            Contact saved = contactRepoApplicationService.save(contact);
            logger.info("Successfully added contact with id={}", saved.getId());
            return saved;
        } catch (Exception e) {
            logger.error("Failed to add contact: {}", contact, e);
            throw new RuntimeException("Unable to add contact", e);
        }
    }

    @Transactional
    public Contact updateContact(Long id, Contact updatedContact) {
        try {
            logger.info("Updating contact with id={}", id);
            Contact existing = contactRepoApplicationService.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Contact not found with id={}", id);
                        return new EntityNotFoundException("Contact not found with id=" + id);
                    });

            existing.setFirstName(updatedContact.getFirstName());
            existing.setLastName(updatedContact.getLastName());
            existing.setPhoneNumber(updatedContact.getPhoneNumber());
            existing.setAddress(updatedContact.getAddress());

            Contact saved = contactRepoApplicationService.save(existing);
            logger.info("Successfully updated contact with id={}", id);
            return saved;
        } catch (Exception e) {
            logger.error("Failed to update contact with id={}", id, e);
            throw new RuntimeException("Unable to update contact", e);
        }
    }

    public void deleteContact(Long id) {
        try {
            logger.info("Deleting contact with id={}", id);
            contactRepoApplicationService.deleteById(id);
            logger.info("Successfully deleted contact with id={}", id);
        } catch (Exception e) {
            logger.error("Failed to delete contact with id={}", id, e);
            throw new RuntimeException("Unable to delete contact", e);
        }
    }

    public List<Contact> searchContacts(String query) {
        try {
            logger.info("Searching contacts with query='{}'", query);
            return contactRepoApplicationService.findByFirstNameContainingOrLastNameContainingOrPhoneNumberContaining(
                    query, query, query);
        } catch (Exception e) {
            logger.error("Failed to search contacts with query='{}'", query, e);
            throw new RuntimeException("Unable to search contacts", e);
        }
    }
}
