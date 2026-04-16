package org.acme;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkiverse.mcp.server.TextContent;
import io.quarkiverse.mcp.server.Tool;
import io.quarkiverse.mcp.server.ToolArg;
import io.quarkiverse.mcp.server.ToolResponse;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class PetMcpServer {

    @RestClient
    PetRestClient restClient;

    private final ObjectMapper mapper = new ObjectMapper();

    @Tool(description = "List all pets in the clinic")
    public ToolResponse listPets() throws Exception {
        return ToolResponse.success(new TextContent(mapper.writeValueAsString(restClient.getPets())));
    }

    @Tool(description = "Get a pet by its ID")
    public ToolResponse getPetById(@ToolArg(description = "Pet ID") int petId) throws Exception {
        return ToolResponse.success(new TextContent(mapper.writeValueAsString(restClient.getPetById(petId))));
    }
}
