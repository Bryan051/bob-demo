package org.acme.mcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkiverse.mcp.server.TextContent;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.client.PetclinicService;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class PetclinicMcpServer {

    @Inject
    @RestClient
    PetclinicService petclinicService;

    @Inject
    ObjectMapper objectMapper;

    @Tool(description = "Lists all pet types available in the petclinic system. Takes no parameters. Returns a JSON array of PetType objects each with id and name fields.")
    public ToolResponse listPetTypes() {
        try {
            String json = objectMapper.writeValueAsString(petclinicService.listPetTypes());
            return ToolResponse.success(new TextContent(json));
        } catch (Exception e) {
            return ToolResponse.error("Failed to list pet types: " + e.getMessage());
        }
    }

    @Tool(description = "Retrieves a single pet type by its numeric ID. Parameter: petTypeId (int) — the ID of the pet type to look up, e.g. 1. Returns a single PetType object with id and name.")
    public ToolResponse getPetType(
            @ToolArg(description = "The numeric ID of the pet type to retrieve. Example: 1") int petTypeId) {
        try {
            String json = objectMapper.writeValueAsString(petclinicService.getPetType(petTypeId));
            return ToolResponse.success(new TextContent(json));
        } catch (Exception e) {
            return ToolResponse.error("Failed to get pet type with id " + petTypeId + ": " + e.getMessage());
        }
    }

    @Tool(description = "Lists all pets registered in the petclinic system. Takes no parameters. Returns a JSON array of Pet objects including id, name, birthDate, type, ownerId, and visits.")
    public ToolResponse listPets() {
        try {
            String json = objectMapper.writeValueAsString(petclinicService.listPets());
            return ToolResponse.success(new TextContent(json));
        } catch (Exception e) {
            return ToolResponse.error("Failed to list pets: " + e.getMessage());
        }
    }

    @Tool(description = "Retrieves a single pet by its numeric ID. Parameter: petId (int) — the ID of the pet, e.g. 3. Returns a Pet object with id, name, birthDate, type, ownerId, and visits.")
    public ToolResponse getPet(
            @ToolArg(description = "The numeric ID of the pet to retrieve. Example: 3") int petId) {
        try {
            String json = objectMapper.writeValueAsString(petclinicService.getPet(petId));
            return ToolResponse.success(new TextContent(json));
        } catch (Exception e) {
            return ToolResponse.error("Failed to get pet with id " + petId + ": " + e.getMessage());
        }
    }

    @Tool(description = "Retrieves a specific pet belonging to a specific owner. Parameters: ownerId (int) — the owner's ID, e.g. 1; petId (int) — the pet's ID, e.g. 2. Returns a Pet object.")
    public ToolResponse getOwnersPet(
            @ToolArg(description = "The numeric ID of the owner who owns the pet. Example: 1") int ownerId,
            @ToolArg(description = "The numeric ID of the pet belonging to the owner. Example: 2") int petId) {
        try {
            String json = objectMapper.writeValueAsString(petclinicService.getOwnersPet(ownerId, petId));
            return ToolResponse.success(new TextContent(json));
        } catch (Exception e) {
            return ToolResponse.error("Failed to get pet " + petId + " for owner " + ownerId + ": " + e.getMessage());
        }
    }

    @Tool(description = "Lists owners in the petclinic system, optionally filtered by last name. Parameter: lastName (string, optional) — partial or full last name filter, e.g. 'Smith'. Returns a JSON array of Owner objects.")
    public ToolResponse listOwners(
            @ToolArg(description = "Optional last name to filter owners. Pass empty string to return all owners. Example: 'Smith'") String lastName) {
        try {
            String filter = (lastName != null && !lastName.isBlank()) ? lastName : null;
            String json = objectMapper.writeValueAsString(petclinicService.listOwners(filter));
            return ToolResponse.success(new TextContent(json));
        } catch (Exception e) {
            return ToolResponse.error("Failed to list owners: " + e.getMessage());
        }
    }

    @Tool(description = "Retrieves a single owner by their numeric ID. Parameter: ownerId (int) — the ID of the owner, e.g. 1. Returns an Owner object with id, firstName, lastName, address, city, telephone, and pets.")
    public ToolResponse getOwner(
            @ToolArg(description = "The numeric ID of the owner to retrieve. Example: 1") int ownerId) {
        try {
            String json = objectMapper.writeValueAsString(petclinicService.getOwner(ownerId));
            return ToolResponse.success(new TextContent(json));
        } catch (Exception e) {
            return ToolResponse.error("Failed to get owner with id " + ownerId + ": " + e.getMessage());
        }
    }
}
