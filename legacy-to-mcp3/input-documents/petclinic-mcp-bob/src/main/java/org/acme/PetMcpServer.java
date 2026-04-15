package org.acme;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.quarkiverse.mcp.server.TextContent;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@ApplicationScoped
public class PetMcpServer {

    @Inject
    @RestClient
    PetRestClient petRestClient;

    private final ObjectMapper objectMapper;

    public PetMcpServer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Tool(description = "List all pets")
    public ToolResponse listPets() {
        try {
            List<Pet> pets = petRestClient.listPets();
            String json = objectMapper.writeValueAsString(pets);
            return ToolResponse.success(new TextContent(json));
        } catch (Exception e) {
            return ToolResponse.success(new TextContent("{\"error\": \"" + e.getMessage() + "\"}"));
        }
    }

    @Tool(description = "Get a pet by ID")
    public ToolResponse getPetById(
            @ToolArg(name = "petId", description = "The ID of the pet") int petId) {
        try {
            Pet pet = petRestClient.getPetById(petId);
            String json = objectMapper.writeValueAsString(pet);
            return ToolResponse.success(new TextContent(json));
        } catch (Exception e) {
            return ToolResponse.success(new TextContent("{\"error\": \"" + e.getMessage() + "\"}"));
        }
    }
}
