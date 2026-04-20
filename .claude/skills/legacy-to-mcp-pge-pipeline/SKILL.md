---
name: legacy-to-mcp-pge-pipeline
description: PGE (Plan-Generate-Evaluate) pipeline orchestration for legacy-to-MCP modernization. Use this skill when asked to run or orchestrate the legacy-to-MCP pipeline.
---

# Legacy-to-MCP PGE Pipeline

## Overview

This pipeline modernizes a legacy Java app into an MCP sidecar using three sub-agents in a loop:

```
Planner → Generator → Evaluator → [FAIL → Generator again] → SUCCESS
```

State is persisted between iterations via JSON files in `workflows/`.

---

## State Files

| File | Written by | Read by |
|------|-----------|---------|
| `workflows/plan.json` | planner | generator, evaluator |
| `workflows/code.json` | generator | evaluator |
| `workflows/evaluation.json` | evaluator | orchestrator (you) |

---

## Sub-Agents

| Agent | Description |
|-------|-------------|
| `legacy-to-mcp-planner` | Reads the target project, writes `workflows/plan.json` |
| `legacy-to-mcp-generator` | Reads `plan.json`, implements the sidecar, writes `workflows/code.json` |
| `legacy-to-mcp-evaluator` | Validates output against `plan.json`, writes `workflows/evaluation.json` |

---

## Orchestration Steps

### Step 1 — Gather context, then run Planner (once)

**Before invoking the planner, you (orchestrator) must gather:**
1. Read each `.bob/rules-**/*.md` under the project root (Glob + Read)
2. Read the task description file (`**/prompt.txt`)
3. Fetch the OpenAPI spec URL from prompt.txt (WebFetch) — extract all GET endpoints and DTO schemas including nested types (e.g. Visit)
4. Read `pom.xml` — note existing dependencies
5. Read `src/main/resources/application.properties` — note existing properties
6. Read `src/main/kubernetes/*.yaml` — note existing Deployment structure

**Then invoke `legacy-to-mcp-planner`** with all gathered context embedded directly in the prompt:
```
Write <demo>/workflows/plan.json using the following context:

Rules: <rules content>
Task: <prompt.txt content>
OpenAPI endpoints: <extracted GET endpoints and DTO schemas>
pom.xml dependencies: <relevant deps>
application.properties: <current content>
Kubernetes manifest: <summary of existing Deployment>
Project root: <demo>/input-documents/<project-name>
Base package: <package>
configKey: <key>
```
Wait for: `plan.json saved to workflows/plan.json`

### Step 2 — Run Generator
Invoke `legacy-to-mcp-generator`:
```
Task: Read workflows/plan.json and implement the MCP sidecar. Write workflows/code.json.
```
Wait for: `code.json saved to workflows/code.json`

### Step 3 — Run Evaluator
Invoke `legacy-to-mcp-evaluator`:
```
Task: Validate the generated output against workflows/plan.json. Write workflows/evaluation.json.
```
Wait for: `evaluation.json saved to workflows/evaluation.json`

### Step 4 — Check Result
Read `workflows/evaluation.json` and check `status`:

- **SUCCESS** → pipeline complete. Report results to the user.
- **FAIL** → read `suggestion` field, pass it to generator as fix instructions, go back to Step 2.

---

## Iteration Rules

- Default max iterations: **3** (1 planner + up to 3 generator/evaluator cycles)
- Planner runs **only once** per pipeline execution; do not re-run on generator retry
- On retry, pass the evaluator's `suggestion` field verbatim to the generator:
  ```
  Fix the following issues and rewrite workflows/code.json:
  <evaluation.suggestion>
  ```
- If max iterations are reached without SUCCESS, report the final `evaluation.json` to the user and stop

---

## Invocation Example

When the user says "run the legacy-to-MCP pipeline on `<path>`":

1. Check if `workflows/plan.json` already exists — if so, ask whether to reuse it or re-plan
2. Run Planner → Generator → Evaluator loop as described above
3. After SUCCESS, summarize: changed files, added tests, any warnings from `evaluation.json`
