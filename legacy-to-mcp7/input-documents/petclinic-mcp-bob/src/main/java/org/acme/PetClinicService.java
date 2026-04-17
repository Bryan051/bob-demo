package org.acme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@Path("/api")
@RegisterRestClient(baseUri = "http://localhost:9966")
@Produces(MediaType.APPLICATION_JSON)
public interface PetClinicService {

    @GET
    @Path("/pets")
    List<Pet> listPets();

    @GET
    @Path("/pets/{petId}")
    Pet getPet(@PathParam("petId") int petId);
}
