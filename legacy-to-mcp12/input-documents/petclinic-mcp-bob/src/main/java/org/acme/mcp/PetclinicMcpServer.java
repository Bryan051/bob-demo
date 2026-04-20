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

    @Tool(description = "List all pets registered in the petclinic. Takes no parameters. Returns a JSON array of Pet objects, each with id, name, birthDate, type, ownerId, and visits.")
    public ToolResponse listPets() {
        try {
            String json = objectMapper.writeValueAsString(petclinicRestClient.listPets());
            return ToolResponse.success(new TextContent(json));
        } catch (Exception e) {
            return ToolResponse.error("Failed to list pets: " + e.getMessage());
        }
    }

    @Tool(description = "Get a single pet by its ID. Accepts petId (integer). Returns the Pet object with id, name, birthDate, type, ownerId, and visits.")
    public ToolResponse getPet(
            @ToolArg(description = "The numeric ID of the pet to retrieve. Example: 1") Integer petId) {
        try {
            String json = objectMapper.writeValueAsString(petclinicRestClient.getPet(petId));
            return ToolResponse.success(new TextContent(json));
        } catch (Exception e) {
            return ToolResponse.error("Failed to get pet " + petId + ": " + e.getMessage());
        }
    }

    @Tool(description = "Get a pet belonging to a specific owner. Accepts ownerId (integer) and petId (integer). Returns the Pet object with id, name, birthDate, type, ownerId, and visits.")
    public ToolResponse getOwnersPet(
            @ToolArg(description = "The numeric ID of the owner. Example: 1") Integer ownerId,
            @ToolArg(description = "The numeric ID of the pet belonging to that owner. Example: 2") Integer petId) {
        try {
            String json = objectMapper.writeValueAsString(petclinicRestClient.getOwnersPet(ownerId, petId));
            return ToolResponse.success(new TextContent(json));
        } catch (Exception e) {
            return ToolResponse.error("Failed to get pet " + petId + " for owner " + ownerId + ": " + e.getMessage());
        }
    }

    @Tool(description = "List all pet types available in the petclinic. Takes no parameters. Returns a JSON array of PetType objects, each with id and name.")
    public ToolResponse listPetTypes() {
        try {
            String json = objectMapper.writeValueAsString(petclinicRestClient.listPetTypes());
            return ToolResponse.success(new TextContent(json));
        } catch (Exception e) {
            return ToolResponse.error("Failed to list pet types: " + e.getMessage());
        }
    }

    @Tool(description = "Get a single pet type by its ID. Accepts petTypeId (integer). Returns the PetType object with id and name.")
    public ToolResponse getPetType(
            @ToolArg(description = "The numeric ID of the pet type. Example: 1") Integer petTypeId) {
        try {
            String json = objectMapper.writeValueAsString(petclinicRestClient.getPetType(petTypeId));
            return ToolResponse.success(new TextContent(json));
        } catch (Exception e) {
            return ToolResponse.error("Failed to get pet type " + petTypeId + ": " + e.getMessage());
        }
    }
}
