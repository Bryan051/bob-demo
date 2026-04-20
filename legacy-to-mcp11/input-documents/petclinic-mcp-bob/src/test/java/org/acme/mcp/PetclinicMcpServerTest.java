package org.acme.mcp;

import io.quarkiverse.mcp.server.ToolResponse;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.acme.client.PetclinicRestClient;
import org.acme.client.dto.Pet;
import org.acme.client.dto.PetType;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@QuarkusTest
class PetclinicMcpServerTest {

    @InjectMock
    @RestClient
    PetclinicRestClient petclinicRestClient;

    @Inject
    PetclinicMcpServer petclinicMcpServer;

    @Test
    void listPets_returnNonNullToolResponse() {
        Pet pet = new Pet();
        pet.setId(1);
        pet.setName("Buddy");
        when(petclinicRestClient.listPets()).thenReturn(List.of(pet));

        ToolResponse response = petclinicMcpServer.listPets();

        assertNotNull(response);
        assertFalse(response.isError());
    }

    @Test
    void getPet_returnNonNullToolResponse() {
        Pet pet = new Pet();
        pet.setId(1);
        pet.setName("Buddy");
        when(petclinicRestClient.getPet(1)).thenReturn(pet);

        ToolResponse response = petclinicMcpServer.getPet(1);

        assertNotNull(response);
        assertFalse(response.isError());
    }

    @Test
    void getOwnersPet_returnNonNullToolResponse() {
        Pet pet = new Pet();
        pet.setId(2);
        pet.setName("Whiskers");
        pet.setOwnerId(5);
        when(petclinicRestClient.getOwnersPet(5, 2)).thenReturn(pet);

        ToolResponse response = petclinicMcpServer.getOwnersPet(5, 2);

        assertNotNull(response);
        assertFalse(response.isError());
    }

    @Test
    void listPetTypes_returnNonNullToolResponse() {
        PetType petType = new PetType();
        petType.setId(1);
        petType.setName("cat");
        when(petclinicRestClient.listPetTypes()).thenReturn(List.of(petType));

        ToolResponse response = petclinicMcpServer.listPetTypes();

        assertNotNull(response);
        assertFalse(response.isError());
    }

    @Test
    void getPetType_returnNonNullToolResponse() {
        PetType petType = new PetType();
        petType.setId(1);
        petType.setName("cat");
        when(petclinicRestClient.getPetType(1)).thenReturn(petType);

        ToolResponse response = petclinicMcpServer.getPetType(1);

        assertNotNull(response);
        assertFalse(response.isError());
    }

    @Test
    void listPets_restClientThrows_returnsErrorResponse() {
        when(petclinicRestClient.listPets()).thenThrow(new RuntimeException("Connection refused"));

        ToolResponse response = petclinicMcpServer.listPets();

        assertNotNull(response);
        assert response.isError();
    }

    @Test
    void getPet_restClientThrows_returnsErrorResponse() {
        when(petclinicRestClient.getPet(99)).thenThrow(new RuntimeException("Pet not found"));

        ToolResponse response = petclinicMcpServer.getPet(99);

        assertNotNull(response);
        assert response.isError();
    }
}
