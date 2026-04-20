## Skill: Develop a Rest Client using MicroProfile Rest Client spec  

### Purpose
Enable Bob to develop a Rest Client using MicroProfile Rest Client.

### Overview
Quarkus provides an extension that integrates with MicroProfile Rest Client to make queries to external REST services.

Your work is to add the dependency and create the Rest Client interface for the required endpoints. 

### Core Rules
1. Always use the Quarkus MCP server to list the available extensions.
2. Always use the Quarkus MCP server to add the `rest-client-jackson` extension.
3. Always develop a Rest Client interface annotated with `org.eclipse.microprofile.rest.client.inject.RegisterRestClient` annotation and Jakarta REST annotations.
4. Use the `configKey` attribute (not `baseUri`) to identify the client; the base URL is set via `quarkus.rest-client.<configKey>.url` in `application.properties`.
5. Always add `@Produces(MediaType.APPLICATION_JSON)` at the interface level.
6. End the interface with the name of the remote service (e.g. `PetclinicRestClient`).

### Example Resource

A Rest Client interface connecting using GET Http Method to `/book` and `/book/all` example:

```java
@Path("/book")
@RegisterRestClient(configKey = "books")
@Produces(MediaType.APPLICATION_JSON)
public interface BooksRestClient {

    @GET
    Book getBookById(@QueryParam("id") String id);
    
    @GET
    @Path("/all")
    Set<Book> getAll();
}
```

And to use the interface use the `org.eclipse.microprofile.rest.client.inject.RestClient` annotation:

```java
@RestClient
BooksService bookService;
```


### Notes
- Use Quarkus MCP Server to add dependencies.

### Validation Check
After registering the extension, Bob should:
- Verify that the Rest Client interface has all the requested endpoints