package org.acme.mcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.quarkiverse.mcp.server.ToolResponse;
import org.acme.client.PetclinicRestClient;
import org.acme.client.dto.Pet;
import org.acme.client.dto.PetType;
import org.acme.client.dto.Visit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetclinicMcpServerTest {

    @Mock
    PetclinicRestClient petclinicRestClient;

    @InjectMocks
    PetclinicMcpServer server;

    @BeforeEach
    void setUp() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        var field = PetclinicMcpServer.class.getDeclaredField("objectMapper");
        field.setAccessible(true);
        field.set(server, objectMapper);
    }

    private Pet makePet(int id, String name) {
        PetType type = new PetType();
        type.setId(1);
        type.setName("cat");
        Pet pet = new Pet();
        pet.setId(id);
        pet.setName(name);
        pet.setBirthDate(LocalDate.of(2020, 1, 1));
        pet.setType(type);
        pet.setOwnerId(1);
        pet.setVisits(List.of());
        return pet;
    }

    @Test
    void listPets_returnsNonNullResponse() {
        when(petclinicRestClient.listPets()).thenReturn(List.of(makePet(1, "Leo")));
        ToolResponse response = server.listPets();
        assertNotNull(response);
        assertTrue(response.isSuccess());
    }

    @Test
    void getPet_returnsNonNullResponse() {
        when(petclinicRestClient.getPet(1)).thenReturn(makePet(1, "Leo"));
        ToolResponse response = server.getPet(1);
        assertNotNull(response);
        assertTrue(response.isSuccess());
    }

    @Test
    void getOwnersPet_returnsNonNullResponse() {
        when(petclinicRestClient.getOwnersPet(1, 2)).thenReturn(makePet(2, "Basil"));
        ToolResponse response = server.getOwnersPet(1, 2);
        assertNotNull(response);
        assertTrue(response.isSuccess());
    }

    @Test
    void listPetTypes_returnsNonNullResponse() {
        PetType pt = new PetType();
        pt.setId(1);
        pt.setName("cat");
        when(petclinicRestClient.listPetTypes()).thenReturn(List.of(pt));
        ToolResponse response = server.listPetTypes();
        assertNotNull(response);
        assertTrue(response.isSuccess());
    }

    @Test
    void getPetType_returnsNonNullResponse() {
        PetType pt = new PetType();
        pt.setId(1);
        pt.setName("cat");
        when(petclinicRestClient.getPetType(1)).thenReturn(pt);
        ToolResponse response = server.getPetType(1);
        assertNotNull(response);
        assertTrue(response.isSuccess());
    }

    @Test
    void getPet_onException_returnsError() {
        when(petclinicRestClient.getPet(99)).thenThrow(new RuntimeException("not found"));
        ToolResponse response = server.getPet(99);
        assertNotNull(response);
        assertFalse(response.isSuccess());
    }

    @Test
    void listPets_responseContainsPetName() throws Exception {
        when(petclinicRestClient.listPets()).thenReturn(List.of(makePet(1, "Leo")));
        ToolResponse response = server.listPets();
        assertTrue(response.isSuccess());
        String content = response.content().get(0).asText().text();
        assertTrue(content.contains("Leo"));
    }
}
