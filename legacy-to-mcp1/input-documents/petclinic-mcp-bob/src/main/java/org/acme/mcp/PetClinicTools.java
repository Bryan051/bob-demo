package org.acme.mcp;

import io.quarkiverse.mcp.server.TextContent;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import jakarta.inject.Inject;
import org.acme.client.PetClinicService;
import org.acme.model.Pet;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

/**
 * MCP Server tools that expose Spring PetClinic REST API read operations for pets.
 */
public class PetClinicTools {

    @Inject
    @RestClient
    PetClinicService petClinicService;

    @Tool(description = "List all pets registered in the PetClinic system. Returns an array of pets with their ID, name, birth date, type, owner ID, and visit history.")
    public ToolResponse listPets() {
        try {
            List<Pet> pets = petClinicService.listPets();
            StringBuilder sb = new StringBuilder();
            sb.append("Pets in PetClinic (").append(pets.size()).append(" total):\n\n");
            for (Pet pet : pets) {
                appendPetInfo(sb, pet);
                sb.append("\n");
            }
            return ToolResponse.success(new TextContent(sb.toString()));
        } catch (Exception e) {
            return ToolResponse.error("Failed to list pets: " + e.getMessage());
        }
    }

    @Tool(description = "Get a specific pet by its numeric ID. Returns the pet's details including ID, name, birth date, type, owner ID, and visit history.")
    public ToolResponse getPet(
            @ToolArg(description = "The numeric ID of the pet to retrieve (e.g. 1, 2, 3)") int petId) {
        try {
            Pet pet = petClinicService.getPet(petId);
            StringBuilder sb = new StringBuilder();
            sb.append("Pet details:\n\n");
            appendPetInfo(sb, pet);
            return ToolResponse.success(new TextContent(sb.toString()));
        } catch (Exception e) {
            return ToolResponse.error("Failed to get pet with ID " + petId + ": " + e.getMessage());
        }
    }

    private void appendPetInfo(StringBuilder sb, Pet pet) {
        sb.append("ID: ").append(pet.getId()).append("\n");
        sb.append("Name: ").append(pet.getName()).append("\n");
        sb.append("Birth Date: ").append(pet.getBirthDate()).append("\n");
        if (pet.getType() != null) {
            sb.append("Type: ").append(pet.getType().getName())
              .append(" (ID: ").append(pet.getType().getId()).append(")\n");
        }
        sb.append("Owner ID: ").append(pet.getOwnerId()).append("\n");
        if (pet.getVisits() != null && !pet.getVisits().isEmpty()) {
            sb.append("Visits (").append(pet.getVisits().size()).append("):\n");
            for (var visit : pet.getVisits()) {
                sb.append("  - Visit ID: ").append(visit.getId())
                  .append(", Date: ").append(visit.getDate())
                  .append(", Description: ").append(visit.getDescription()).append("\n");
            }
        } else {
            sb.append("Visits: none\n");
        }
    }
}
