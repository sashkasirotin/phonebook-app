package com.assignment.phonebook.persistance.implemantations;

import com.assignment.phonebook.services.models.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ContactRepoApplicationService extends JpaRepository<Contact, Long> {
    List<Contact> findByFirstNameContainingOrLastNameContainingOrPhoneNumberContaining(String firstName, String lastName, String phoneNumber);

}
