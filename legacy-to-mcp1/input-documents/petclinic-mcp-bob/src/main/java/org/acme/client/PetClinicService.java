package org.acme.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.acme.model.Pet;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

/**
 * MicroProfile Rest Client interface for the Spring PetClinic REST API.
 * Only exposes read (GET) operations for pets.
 */
@Path("/api")
@RegisterRestClient(configKey = "petclinic-api")
@Produces(MediaType.APPLICATION_JSON)
public interface PetClinicService {

    /**
     * List all pets.
     * GET /api/pets
     */
    @GET
    @Path("/pets")
    List<Pet> listPets();

    /**
     * Get a pet by its ID.
     * GET /api/pets/{petId}
     */
    @GET
    @Path("/pets/{petId}")
    Pet getPet(@PathParam("petId") int petId);
}
