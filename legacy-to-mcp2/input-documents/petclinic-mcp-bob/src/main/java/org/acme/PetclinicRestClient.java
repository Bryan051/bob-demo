package org.acme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;
import java.util.Map;

@RegisterRestClient(configKey = "petclinic")
@Path("/api")
public interface PetclinicRestClient {

    @GET
    @Path("/pets")
    @Produces(MediaType.APPLICATION_JSON)
    List<Map<String, Object>> getAllPets();

    @GET
    @Path("/pets/{petId}")
    @Produces(MediaType.APPLICATION_JSON)
    Map<String, Object> getPetById(@PathParam("petId") int petId);
}
