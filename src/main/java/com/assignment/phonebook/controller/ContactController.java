package com.assignment.phonebook.controller;

import com.assignment.phonebook.persistance.dto.AuthenticationRequest;
import com.assignment.phonebook.services.implementations.ContactCRUDApplicationService;
import com.assignment.phonebook.services.implementations.JwtApplicationService;
import com.assignment.phonebook.services.models.Contact;
import com.assignment.phonebook.services.models.UserInfo;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/contacts")
@OpenAPIDefinition(info = @Info(title = "Contact API", version = "1.0", description = "Manage contacts and authentication"))
@Tag(name = "Contacts", description = "Endpoints for managing contacts and authentication")
public class ContactController {

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private ContactCRUDApplicationService contactService;

    @Autowired
    private JwtApplicationService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Operation(summary = "Get paginated list of contacts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved contacts"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping
    @PreAuthorize("hasAuthority('USER_ROLE')")
    public List<Contact> getContacts(
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10") @RequestParam(defaultValue = "10") int size) {
        logger.info("Fetching contacts - page: {}, size: {}", page, size);
        return contactService.getContacts(page, size);
    }

    @Operation(summary = "Get a contact by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contact found"),
            @ApiResponse(responseCode = "404", description = "Contact not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_ROLE')")
    public ResponseEntity<Contact> getContact(
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader,
            @Parameter(description = "ID of the contact", required = true) @PathVariable Long id) {
        logger.info("Fetching contact with ID: {}", id);
        Optional<Contact> contact = contactService.getContact(id);
        return contact.map(ResponseEntity::ok).orElseGet(() -> {
            logger.warn("Contact not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        });
    }

    @Operation(summary = "Create a new contact")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contact created"),
            @ApiResponse(responseCode = "400", description = "Invalid contact input")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('USER_ROLE')")
    public Contact addContact(
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Contact to add",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Contact.class)))
            @Valid @RequestBody Contact contact) {
        logger.info("Adding new contact: {}", contact);
        return contactService.addContact(contact);
    }

    @Operation(summary = "Update an existing contact by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contact updated"),
            @ApiResponse(responseCode = "404", description = "Contact not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_ROLE')")
    public ResponseEntity<Contact> updateContact(
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader,
            @Parameter(description = "ID of the contact to update", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated contact details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Contact.class)))
            @Valid @RequestBody Contact contactDetails) {
        logger.info("Updating contact with ID: {}", id);
        Contact updatedContact = contactService.updateContact(id, contactDetails);
        return ResponseEntity.ok(updatedContact);
    }

    @Operation(summary = "Delete a contact by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Contact deleted"),
            @ApiResponse(responseCode = "404", description = "Contact not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_ROLE')")
    public ResponseEntity<Void> deleteContact(
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader,
            @Parameter(description = "ID of the contact to delete", required = true) @PathVariable Long id) {
        logger.info("Deleting contact with ID: {}", id);
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search contacts by name or phone number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results returned")
    })
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('USER_ROLE')")
    public List<Contact> searchContacts(
            @RequestHeader(value = "Authorization", required = true) String authorizationHeader,
            @Parameter(description = "Search query", example = "John") @RequestParam String query) {
        logger.info("Searching contacts with query: {}", query);
        return contactService.searchContacts(query);
    }

    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered")
    })
    @PostMapping("/newUser")
    public String addNewUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "New user info",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserInfo.class)))
            @RequestBody UserInfo userInfo) {
        logger.info("Registering new user: {}", userInfo.getEmail());
        return jwtService.addUser(userInfo);
    }

    @Operation(summary = "Authenticate user and return JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "403", description = "Invalid credentials")
    })
    @PostMapping("/authenticate")
    public String authenticateAndGetToken(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Username and password",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AuthenticationRequest.class)))
            @RequestBody AuthenticationRequest authRequest) {
        logger.info("Authenticating user: {}", authRequest.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            logger.info("Authentication successful for user: {}", authRequest.getUsername());
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            logger.warn("Authentication failed for user: {}", authRequest.getUsername());
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }
}
