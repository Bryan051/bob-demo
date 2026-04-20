package org.acme.mcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkiverse.mcp.server.TextContent;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.client.PetclinicRestClient;
import org.acme.client.dto.Pet;
import org.acme.client.dto.PetType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@ApplicationScoped
public class PetclinicMcpServer {

    @Inject
    @RestClient
    PetclinicRestClient petclinicRestClient;

    @Inject
    ObjectMapper objectMapper;

    @Tool(description = "List all pets in the petclinic")
    public ToolResponse listPets() {
        try {
            List<Pet> pets = petclinicRestClient.listPets();
            String json = objectMapper.writeValueAsString(pets);
            return ToolResponse.success(new TextContent(json));
        } catch (Exception e) {
            return ToolResponse.error("Failed to list pets: " + e.getMessage());
        }
    }

    @Tool(description = "Get a pet by its ID")
    public ToolResponse getPet(int petId) {
        try {
            Pet pet = petclinicRestClient.getPet(petId);
            String json = objectMapper.writeValueAsString(pet);
            return ToolResponse.success(new TextContent(json));
        } catch (Exception e) {
            return ToolResponse.error("Failed to get pet with id " + petId + ": " + e.getMessage());
        }
    }

    @Tool(description = "Get a pet belonging to a specific owner")
    public ToolResponse getOwnersPet(int ownerId, int petId) {
        try {
            Pet pet = petclinicRestClient.getOwnersPet(ownerId, petId);
            String json = objectMapper.writeValueAsString(pet);
            return ToolResponse.success(new TextContent(json));
        } catch (Exception e) {
            return ToolResponse.error("Failed to get pet " + petId + " for owner " + ownerId + ": " + e.getMessage());
        }
    }

    @Tool(description = "List all pet types in the petclinic")
    public ToolResponse listPetTypes() {
        try {
            List<PetType> petTypes = petclinicRestClient.listPetTypes();
            String json = objectMapper.writeValueAsString(petTypes);
            return ToolResponse.success(new TextContent(json));
        } catch (Exception e) {
            return ToolResponse.error("Failed to list pet types: " + e.getMessage());
        }
    }

    @Tool(description = "Get a pet type by its ID")
    public ToolResponse getPetType(int petTypeId) {
        try {
            PetType petType = petclinicRestClient.getPetType(petTypeId);
            String json = objectMapper.writeValueAsString(petType);
            return ToolResponse.success(new TextContent(json));
        } catch (Exception e) {
            return ToolResponse.error("Failed to get pet type with id " + petTypeId + ": " + e.getMessage());
        }
    }
}
