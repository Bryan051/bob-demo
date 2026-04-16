package org.acme;

import java.util.List;

import org.acme.model.Pet;
import org.acme.model.PetType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@Path("/api")
@RegisterRestClient(baseUri = "http://localhost:9966")
public interface PetclinicService {

    @GET
    @Path("/pets")
    List<Pet> listPets();

    @GET
    @Path("/pets/{petId}")
    Pet getPet(@PathParam("petId") int petId);

    @GET
    @Path("/pettypes")
    List<PetType> listPetTypes();
}
