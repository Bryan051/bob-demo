package org.acme;

import java.util.List;

import org.acme.model.Pet;
import org.acme.model.PetType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkiverse.mcp.server.TextContent;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import jakarta.inject.Inject;

public class PetTools {

    @RestClient
    PetclinicService petclinicService;

    @Inject
    ObjectMapper objectMapper;

    @Tool(description = "List all pets from the Petclinic")
    public ToolResponse listPets() {
        List<Pet> pets = petclinicService.listPets();
        return ToolResponse.success(new TextContent(toJson(pets)));
    }

    @Tool(description = "Get a pet by its ID")
    public ToolResponse getPet(@ToolArg(description = "The ID of the pet") int petId) {
        Pet pet = petclinicService.getPet(petId);
        return ToolResponse.success(new TextContent(toJson(pet)));
    }

    @Tool(description = "List all available pet types")
    public ToolResponse listPetTypes() {
        List<PetType> petTypes = petclinicService.listPetTypes();
        return ToolResponse.success(new TextContent(toJson(petTypes)));
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return obj.toString();
        }
    }
}
