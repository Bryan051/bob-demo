# AGENTS.md

This file provides guidance to Codex (Codex.ai/code) when working with code in this repository.

## Repository Purpose

This is **bob-demo**: a collection of self-contained IBM Bob (AI coding assistant) demo projects. Each demo lives in its own top-level folder and showcases a specific Bob capability. There is no single build system — each demo is independent.

## Demo Structure

Every demo follows a four-folder convention:

```
<demo-name>/
  README.md              # Step-by-step journey, decisions, lessons learned
  prompt-templates/      # IBM Bob prompts used in the demo
  input-documents/       # Source files Bob processes (Java apps, configs, etc.)
  optional-generated-content/  # Artifacts Bob produced
  screenshots/           # Visual evidence of Bob's responses
```

## Key Demos

- **`legacy-to-mcp/`** — Expose a legacy Java/Jakarta EE app as an MCP Server via a Quarkus sidecar, without modifying the original code. The app under `input-documents/petclinic-mcp-bob/` uses `./mvnw quarkus:dev` to start in dev mode with MCP enabled. Custom Bob mode defined in `.bob/`.
- **`pge-project/`** — Plan-Generate-Evaluate (PGE) agentic pipeline using the Anthropic SDK (`pge.py`). Runs Planner → Generator → Evaluator in a loop; reads prompts from `prompts/` and writes `workflows/plan.json`, `code.json`, `evaluation.json`.
- **`tekton-devops/`** — Generate Tekton CI pipelines. Uses `AGENTS.md` for project-wide pipeline overview and `SKILLS.md`-based skills for per-stage details (secrets, build, security, pipeline).
- **`bob-get-started/express-todo-api/`** — TypeScript/Express REST API: `npm run dev` (ts-node), `npm run build` (tsc), `npm test`.
- **`ansible-devops/`**, **`quarkus-ai-integration/`**, **`modernize-ejb-to-quarkus/`**, **`automated-architecture-taikai/`**, **`beads/`**, **`bob-modes/`**, **`getting-started-skills/`** — additional demos; see each folder's README.

## PGE Pipeline (`pge-project/pge.py`)

```bash
python pge-project/pge.py "<task description>" --max-iterations 3
```

Uses `Codex-opus-4-6` with prompt caching. Outputs JSON files to `pge-project/workflows/`. Prompts are in `pge-project/prompts/{planner,generator,evaluator}.md`.

The Codex agents `legacy-to-mcp-planner`, `legacy-to-mcp-generator`, and `legacy-to-mcp-evaluator` mirror this flow for the legacy-to-MCP demos.

## IBM Bob Concepts Used in Demos

- **Modes** — Custom Bob modes are defined in `.bob/` directories (e.g., `mcp.json`, rules). Modes scope which rules and MCP servers are active.
- **`AGENTS.md`** — Project-wide rules Bob reads automatically; used for pipeline/workflow overviews.
- **`SKILLS.md` / `SKILL.md`** — Task-specific knowledge files for specialized steps (e.g., Tekton build, security scanning).
- **Rules vs. Skills** — Rules are project/mode-specific; Skills are global and activated automatically.

## Contributing

Create a branch before starting a demo (so you can reset to `main` if needed). New demos should copy `demo-template/` and be registered in the root `README.md`. Commits must be signed (`git commit -S`).
