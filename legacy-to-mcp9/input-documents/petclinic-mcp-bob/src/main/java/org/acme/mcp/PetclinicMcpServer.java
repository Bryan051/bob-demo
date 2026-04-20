package org.acme.mcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkiverse.mcp.server.TextContent;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.client.PetclinicRestClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class PetclinicMcpServer {

    @Inject
    @RestClient
    PetclinicRestClient petclinicRestClient;

    @Inject
    ObjectMapper objectMapper;

    @Tool(description = "List all pets registered in the petclinic")
    public ToolResponse listPets() {
        try {
            String json = objectMapper.writeValueAsString(petclinicRestClient.listPets());
            return ToolResponse.success(new TextContent(json));
        } catch (Exception e) {
            return ToolResponse.error("Failed to list pets: " + e.getMessage());
        }
    }

    @Tool(description = "Get a pet by its ID")
    public ToolResponse getPet(@ToolArg(description = "The pet ID") int petId) {
        try {
            String json = objectMapper.writeValueAsString(petclinicRestClient.getPet(petId));
            return ToolResponse.success(new TextContent(json));
        } catch (Exception e) {
            return ToolResponse.error("Failed to get pet: " + e.getMessage());
        }
    }

    @Tool(description = "Get a pet belonging to a specific owner")
    public ToolResponse getOwnersPet(
            @ToolArg(description = "The owner ID") int ownerId,
            @ToolArg(description = "The pet ID") int petId) {
        try {
            String json = objectMapper.writeValueAsString(petclinicRestClient.getOwnersPet(ownerId, petId));
            return ToolResponse.success(new TextContent(json));
        } catch (Exception e) {
            return ToolResponse.error("Failed to get owner's pet: " + e.getMessage());
        }
    }
}
