package org.acme.mcp;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkiverse.mcp.server.ToolResponse;
import jakarta.inject.Inject;
import org.acme.client.PetclinicRestClient;
import org.acme.client.dto.Pet;
import org.acme.client.dto.PetType;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
        PetType petType = new PetType();
        petType.setId(1);
        petType.setName("cat");

        Pet pet = new Pet();
        pet.setId(1);
        pet.setName("Leo");
        pet.setBirthDate(LocalDate.of(2020, 1, 1));
        pet.setType(petType);
        pet.setOwnerId(1);
        pet.setVisits(List.of());

        when(petclinicRestClient.listPets()).thenReturn(List.of(pet));

        ToolResponse response = petclinicMcpServer.listPets();

        assertNotNull(response);
        assertFalse(response.isError());
    }

    @Test
    void getPet_returnNonNullToolResponse() {
        PetType petType = new PetType();
        petType.setId(2);
        petType.setName("dog");

        Pet pet = new Pet();
        pet.setId(7);
        pet.setName("Basil");
        pet.setBirthDate(LocalDate.of(2019, 3, 15));
        pet.setType(petType);
        pet.setOwnerId(3);
        pet.setVisits(List.of());

        when(petclinicRestClient.getPet(7)).thenReturn(pet);

        ToolResponse response = petclinicMcpServer.getPet(7);

        assertNotNull(response);
        assertFalse(response.isError());
    }

    @Test
    void getPet_restClientThrows_returnErrorToolResponse() {
        when(petclinicRestClient.getPet(999))
                .thenThrow(new RuntimeException("Not found"));

        ToolResponse response = petclinicMcpServer.getPet(999);

        assertNotNull(response);
        assertFalse(!response.isError(), "Expected an error ToolResponse when REST client throws");
    }
}
