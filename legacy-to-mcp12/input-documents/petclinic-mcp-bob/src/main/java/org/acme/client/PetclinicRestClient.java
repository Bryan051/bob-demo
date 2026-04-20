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

@Path("/api")
@RegisterRestClient(configKey = "petclinic")
@Produces(MediaType.APPLICATION_JSON)
public interface PetclinicRestClient {

    @GET
    @Path("/pets")
    List<Pet> listPets();

    @GET
    @Path("/pets/{petId}")
    Pet getPet(@PathParam("petId") Integer petId);

    @GET
    @Path("/owners/{ownerId}/pets/{petId}")
    Pet getOwnersPet(@PathParam("ownerId") Integer ownerId, @PathParam("petId") Integer petId);

    @GET
    @Path("/pettypes")
    List<PetType> listPetTypes();

    @GET
    @Path("/pettypes/{petTypeId}")
    PetType getPetType(@PathParam("petTypeId") Integer petTypeId);
}
