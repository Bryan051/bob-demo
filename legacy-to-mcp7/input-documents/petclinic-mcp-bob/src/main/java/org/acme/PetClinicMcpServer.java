package org.acme;

import io.quarkiverse.mcp.server.TextContent;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import jakarta.inject.Singleton;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@Singleton
public class PetClinicMcpServer {

    @RestClient
    PetClinicService petClinicService;

    @Tool(description = "List all pets registered in the PetClinic application")
    public ToolResponse listPets() {
        List<Pet> pets = petClinicService.listPets();
        return ToolResponse.success(new TextContent(pets.toString()));
    }

    @Tool(description = "Get a single pet by its ID from the PetClinic application")
    public ToolResponse getPet(
            @ToolArg(description = "The numeric ID of the pet to retrieve") int petId) {
        Pet pet = petClinicService.getPet(petId);
        return ToolResponse.success(new TextContent(pet.toString()));
    }
}
