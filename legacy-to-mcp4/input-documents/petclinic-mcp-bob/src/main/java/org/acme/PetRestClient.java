package org.acme;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import java.util.List;

@Path("/api/pets")
@RegisterRestClient(configKey = "petclinic")
public interface PetRestClient {

    @GET
    List<Pet> getPets();

    @GET
    @Path("/{petId}")
    Pet getPetById(@PathParam("petId") int petId);
}
