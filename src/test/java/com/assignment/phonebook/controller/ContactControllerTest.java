//package com.assignment.phonebook.controller;
//
//import com.assignment.phonebook.persistance.dto.AuthenticationRequest;
//import com.assignment.phonebook.services.implementations.ContactCRUDApplicationService;
//import com.assignment.phonebook.services.implementations.JwtApplicationService;
//import com.assignment.phonebook.services.models.Contact;
//import com.assignment.phonebook.services.models.UserInfo;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.context.annotation.Bean;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Arrays;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(ContactController.class)
//@AutoConfigureMockMvc(addFilters = false) // <-- disable security filters for test
//public class ContactControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ContactCRUDApplicationService contactService;
//
//    @MockBean
//    private JwtApplicationService jwtService;
//    @MockBean
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    public void testGetContacts() throws Exception {
//        Contact contact = new Contact();
//        contact.setId(1L);
//        contact.setFirstName("John");
//
//        Mockito.when(contactService.getContacts(0, 10)).thenReturn(Arrays.asList(contact));
//
//        mockMvc.perform(get("/api/v1/contacts")
//                        .param("page", "0")
//                        .param("size", "10")
//                        .header("Authorization", "Bearer test-token"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].firstName").value("John"));
//    }
//
//    @Test
//    public void testGetContactById() throws Exception {
//        Contact contact = new Contact();
//        contact.setId(1L);
//        contact.setFirstName("John");
//
//        Mockito.when(contactService.getContact(1L)).thenReturn(Optional.of(contact));
//
//        mockMvc.perform(get("/api/v1/contacts/1")
//                        .header("Authorization", "Bearer test-token"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.firstName").value("John"));
//    }
//
//    @Test
//    public void testAddContact() throws Exception {
//        Contact contact = new Contact();
//        contact.setFirstName("John");
//
//        Mockito.when(contactService.addContact(any(Contact.class))).thenReturn(contact);
//
//        mockMvc.perform(post("/api/v1/contacts")
//                        .header("Authorization", "Bearer test-token")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(contact)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.firstName").value("John"));
//    }
//
//    @Test
//    public void testUpdateContact() throws Exception {
//        Contact updatedContact = new Contact();
//        updatedContact.setFirstName("Jane");
//
//        Mockito.when(contactService.updateContact(eq(1L), any(Contact.class))).thenReturn(updatedContact);
//
//        mockMvc.perform(put("/api/v1/contacts/1")
//                        .header("Authorization", "Bearer test-token")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updatedContact)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.firstName").value("Jane"));
//    }
//
//    @Test
//    public void testDeleteContact() throws Exception {
//        Mockito.doNothing().when(contactService).deleteContact(1L);
//
//        mockMvc.perform(delete("/api/v1/contacts/1")
//                        .header("Authorization", "Bearer test-token"))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    public void testSearchContacts() throws Exception {
//        Contact contact = new Contact();
//        contact.setFirstName("John");
//
//        Mockito.when(contactService.searchContacts("John")).thenReturn(Arrays.asList(contact));
//
//        mockMvc.perform(get("/api/v1/contacts/search")
//                        .param("query", "John")
//                        .header("Authorization", "Bearer test-token"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].firstName").value("John"));
//    }
//
//    @Test
//    public void testAddNewUser() throws Exception {
//        UserInfo userInfo = new UserInfo();
//        userInfo.setEmail("test@example.com");
//
//        Mockito.when(jwtService.addUser(any(UserInfo.class))).thenReturn("User Registered");
//
//        mockMvc.perform(post("/api/v1/contacts/newUser")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(userInfo)))
//                .andExpect(status().isOk())
//                .andExpect(content().string("User Registered"));
//    }
//
//    @Test
//    public void testAuthenticateAndGetToken() throws Exception {
//        AuthenticationRequest request = new AuthenticationRequest();
//        request.setUsername("testuser");
//        request.setPassword("password");
//
//        Authentication authentication = Mockito.mock(Authentication.class);
//        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
//        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
//        Mockito.when(jwtService.generateToken("testuser")).thenReturn("fake-jwt-token");
//
//        mockMvc.perform(post("/api/v1/contacts/authenticate")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(content().string("fake-jwt-token"));
//    }
//}
