package org.acme.mcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolResponse;
import io.quarkiverse.mcp.server.TextContent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.client.PetclinicRestClient;
import org.acme.client.dto.Pet;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@ApplicationScoped
public class PetclinicMcpServer {

    @Inject
    @RestClient
    PetclinicRestClient petclinicRestClient;

    @Inject
    ObjectMapper objectMapper;

    @Tool(description = "List all pets registered in the petclinic system. "
            + "Returns a JSON array of Pet objects, each containing: "
            + "id (Integer), name (String), birthDate (LocalDate), "
            + "type (PetType with id and name), ownerId (Integer), "
            + "and visits (list of Visit objects). "
            + "Use this tool when you need an overview of all pets or when filtering by name or type on the client side.")
    public ToolResponse listPets() {
        try {
            List<Pet> pets = petclinicRestClient.listPets();
            String json = objectMapper.writeValueAsString(pets);
            return ToolResponse.success(new TextContent(json));
        } catch (Exception e) {
            return ToolResponse.error("Failed to list pets: " + e.getMessage());
        }
    }

    @Tool(description = "Retrieve a single pet by its unique numeric ID. "
            + "Parameter: petId (int) — the numeric identifier of the pet. "
            + "Returns a JSON object of type Pet containing: "
            + "id (Integer), name (String), birthDate (LocalDate), "
            + "type (PetType with id and name), ownerId (Integer), "
            + "and visits (list of Visit objects with id, petId, date, description). "
            + "Returns an error if the pet with the given ID does not exist.")
    public ToolResponse getPet(int petId) {
        try {
            Pet pet = petclinicRestClient.getPet(petId);
            String json = objectMapper.writeValueAsString(pet);
            return ToolResponse.success(new TextContent(json));
        } catch (Exception e) {
            return ToolResponse.error("Failed to get pet with id " + petId + ": " + e.getMessage());
        }
    }

    @Tool(description = "Retrieve a specific pet belonging to a specific owner, identified by both owner ID and pet ID. "
            + "Parameters: ownerId (int) — the numeric identifier of the owner; "
            + "petId (int) — the numeric identifier of the pet. "
            + "Returns a JSON object of type Pet containing: "
            + "id (Integer), name (String), birthDate (LocalDate), "
            + "type (PetType with id and name), ownerId (Integer), "
            + "and visits (list of Visit objects with id, petId, date, description). "
            + "Use this tool when you already know the owner context and want to scope the lookup to that owner's pets.")
    public ToolResponse getOwnersPet(int ownerId, int petId) {
        try {
            Pet pet = petclinicRestClient.getOwnersPet(ownerId, petId);
            String json = objectMapper.writeValueAsString(pet);
            return ToolResponse.success(new TextContent(json));
        } catch (Exception e) {
            return ToolResponse.error("Failed to get pet " + petId + " for owner " + ownerId + ": " + e.getMessage());
        }
    }
}
