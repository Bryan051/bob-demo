package org.acme.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.acme.dto.Owner;
import org.acme.dto.Pet;
import org.acme.dto.PetType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@RegisterRestClient(configKey = "petclinic")
@Produces(MediaType.APPLICATION_JSON)
public interface PetclinicService {

    @GET
    @Path("/pettypes")
    List<PetType> listPetTypes();

    @GET
    @Path("/pettypes/{petTypeId}")
    PetType getPetType(@PathParam("petTypeId") int petTypeId);

    @GET
    @Path("/pets")
    List<Pet> listPets();

    @GET
    @Path("/pets/{petId}")
    Pet getPet(@PathParam("petId") int petId);

    @GET
    @Path("/owners/{ownerId}/pets/{petId}")
    Pet getOwnersPet(@PathParam("ownerId") int ownerId, @PathParam("petId") int petId);

    @GET
    @Path("/owners")
    List<Owner> listOwners(@QueryParam("lastName") String lastName);

    @GET
    @Path("/owners/{ownerId}")
    Owner getOwner(@PathParam("ownerId") int ownerId);
}
