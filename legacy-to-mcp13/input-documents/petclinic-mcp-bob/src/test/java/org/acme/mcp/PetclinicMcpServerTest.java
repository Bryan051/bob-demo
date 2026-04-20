package org.acme.mcp;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkiverse.mcp.server.ToolResponse;
import org.acme.client.PetclinicService;
import org.acme.dto.PetType;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class PetclinicMcpServerTest {

    @InjectMock
    @RestClient
    PetclinicService petclinicService;

    @Inject
    PetclinicMcpServer mcpServer;

    @Test
    void listPetTypes_returnsSuccessResponse() {
        PetType cat = new PetType();
        cat.setId(1);
        cat.setName("cat");
        when(petclinicService.listPetTypes()).thenReturn(List.of(cat));

        ToolResponse response = mcpServer.listPetTypes();

        assertNotNull(response);
        assertFalse(response.isError());
        assertTrue(response.content().stream()
                .anyMatch(c -> c.asText().contains("cat")));
    }

    @Test
    void getPet_returnsErrorOnException() {
        when(petclinicService.getPet(anyInt())).thenThrow(new RuntimeException("not found"));

        ToolResponse response = mcpServer.getPet(999);

        assertNotNull(response);
        assertTrue(response.isError());
    }
}
