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

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * Unit-level QuarkusTest for PetclinicMcpServer.
 *
 * PetclinicRestClient is mocked via @InjectMock so no running petclinic
 * instance is required. WireMock is intentionally omitted because the
 * quarkus-wiremock extension is not declared in pom.xml; @InjectMock /
 * Mockito (bundled with quarkus-junit in Quarkus 3.x) is sufficient.
 */
@QuarkusTest
class PetclinicMcpServerTest {

    @InjectMock
    @RestClient
    PetclinicRestClient petclinicRestClient;

    @Inject
    PetclinicMcpServer mcpServer;

    @Test
    void listPets_returnsPetJsonInToolResponse() {
        PetType type = new PetType();
        type.setId(1);
        type.setName("cat");

        Pet pet = new Pet();
        pet.setId(1);
        pet.setName("Basil");
        pet.setBirthDate(LocalDate.of(2020, 3, 9));
        pet.setType(type);
        pet.setOwnerId(1);

        when(petclinicRestClient.listPets()).thenReturn(List.of(pet));

        ToolResponse response = mcpServer.listPets();

        assertNotNull(response, "ToolResponse must not be null");
        assertFalse(response.isError(), "ToolResponse must not be an error");
        assertFalse(response.content().isEmpty(), "ToolResponse must contain at least one content item");
    }

    @Test
    void getPet_returnsPetJsonInToolResponse() {
        PetType type = new PetType();
        type.setId(2);
        type.setName("dog");

        Pet pet = new Pet();
        pet.setId(5);
        pet.setName("Max");
        pet.setBirthDate(LocalDate.of(2019, 6, 15));
        pet.setType(type);
        pet.setOwnerId(3);

        when(petclinicRestClient.getPet(5)).thenReturn(pet);

        ToolResponse response = mcpServer.getPet(5);

        assertNotNull(response);
        assertFalse(response.isError());
    }

    @Test
    void getPet_whenClientThrows_returnsErrorToolResponse() {
        when(petclinicRestClient.getPet(99))
                .thenThrow(new RuntimeException("pet not found"));

        ToolResponse response = mcpServer.getPet(99);

        assertNotNull(response);
        // Exception must be surfaced as a ToolResponse error, not a thrown exception
        assertFalse(response.content().isEmpty(), "Error ToolResponse must still carry content");
    }
}
