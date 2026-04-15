package org.acme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@Path("/api")
@RegisterRestClient(configKey = "petclinic")
@Produces(MediaType.APPLICATION_JSON)
public interface PetRestClient {

    @GET
    @Path("/pets")
    List<Pet> listPets();

    @GET
    @Path("/pets/{petId}")
    Pet getPetById(@PathParam("petId") int petId);
}
