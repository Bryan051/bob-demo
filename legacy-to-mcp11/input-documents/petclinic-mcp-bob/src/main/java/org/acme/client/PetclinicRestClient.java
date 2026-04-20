package org.acme.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.acme.client.dto.Pet;
import org.acme.client.dto.PetType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@RegisterRestClient(configKey = "petclinic")
@Produces(MediaType.APPLICATION_JSON)
public interface PetclinicRestClient {

    @GET
    @Path("/api/pets")
    List<Pet> listPets();

    @GET
    @Path("/api/pets/{petId}")
    Pet getPet(@PathParam("petId") int petId);

    @GET
    @Path("/api/owners/{ownerId}/pets/{petId}")
    Pet getOwnersPet(@PathParam("ownerId") int ownerId, @PathParam("petId") int petId);

    @GET
    @Path("/api/pettypes")
    List<PetType> listPetTypes();

    @GET
    @Path("/api/pettypes/{petTypeId}")
    PetType getPetType(@PathParam("petTypeId") int petTypeId);
}
