---
name: MCP Generator Conventions
description: Key implementation patterns for legacy-to-MCP Quarkus sidecar generation tasks
type: project
---

MCP server class must be @ApplicationScoped; REST client field requires both @Inject and @RestClient annotations; ObjectMapper is @Inject only — never instantiated with new.

@Tool methods must have no throws declarations; all exceptions caught inside and returned as ToolResponse.error("...message..."). Successful results use ToolResponse.success(new TextContent(json)).

configKey in @RegisterRestClient must match the middle segment of quarkus.rest-client.<configKey>.url in application.properties. For petclinic demos the value is "petclinic".

**Why:** plan.json evaluation_criteria explicitly checks for these patterns; failures here cause evaluator rejection.

**How to apply:** Validate configKey alignment, no-throws @Tool methods, and @Inject/@RestClient dual annotation on every generator run.

Kubernetes sidecar: add new container entry under spec.template.spec.containers alongside (not replacing) the existing legacy container. Use containerPort matching plan.json sidecar_port. Add QUARKUS_REST_CLIENT_<CONFIGKEY_UPPER>_URL env var pointing to localhost:<legacy_port>/<context>. Do not add Service or Route unless constraints.external_mcp_exposure or targets.kubernetes.require_service_route is true.

Tests use @QuarkusTest + @InjectMock @RestClient on the REST client field; inject the MCP server class directly; mock REST client return values with Mockito when(). Include at least one error-path test (REST client throws) in addition to happy-path tests.
