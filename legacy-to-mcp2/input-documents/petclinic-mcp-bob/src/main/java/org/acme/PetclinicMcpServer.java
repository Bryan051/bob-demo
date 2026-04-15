package org.acme;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkiverse.mcp.server.TextContent;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class PetclinicMcpServer {

    @RestClient
    PetclinicRestClient petclinicRestClient;

    @Inject
    ObjectMapper objectMapper;

    @Tool(name = "getAllPets", description = "Retrieve all pets from the PetClinic application")
    public ToolResponse getAllPets() {
        try {
            List<Map<String, Object>> pets = petclinicRestClient.getAllPets();
            String json = objectMapper.writeValueAsString(pets);
            return ToolResponse.success(new TextContent(json));
        } catch (Exception e) {
            return ToolResponse.success(new TextContent("Error retrieving pets: " + e.getMessage()));
        }
    }

    @Tool(name = "getPetById", description = "Retrieve a specific pet by ID from the PetClinic application")
    public ToolResponse getPetById(@ToolArg(name = "petId", description = "The ID of the pet to retrieve") int petId) {
        try {
            Map<String, Object> pet = petclinicRestClient.getPetById(petId);
            String json = objectMapper.writeValueAsString(pet);
            return ToolResponse.success(new TextContent(json));
        } catch (Exception e) {
            return ToolResponse.success(new TextContent("Error retrieving pet with id " + petId + ": " + e.getMessage()));
        }
    }
}
