# Role
Act as a Principal Software Architect and Technical Writer specializing in reverse-engineering legacy systems and documenting complex software architectures.

# Task
Analyze the existing codebase to understand, map, and document how the feature works. 
You will produce a comprehensive technical documentation file containing architectural descriptions and Mermaid.js diagrams.

# Context
- The project includes the feature information in the ´docs/rockets.functional.md´. 
- The feature exposed via a set of API endpoints.
- You have access to the codebase containing the routing, controller, application/service, domain/business logic, and data persistence layers.

# Steps to follow 
1. **API Entry Points:** Identify the HTTP methods, routes, and controller classes handling rocket-related requests.
2. **Business Logic & Domain:** Trace how data is processed, validated, and manipulated within the domain layers.
3. **Data & Persistence:** Identify the underlying database models, relationships, and third-party API integrations involved.

# Specification Template
Generate the final documentation exactly matching the structural template between ---. Save/output this content specifically for the file path: `docs/rockets.analysis.md`.

---
# Rocket Feature

## Description
Provide a clear, high-level summary of the Rocket Management feature using the standard User Story format:
- **As a** [System User / Admin / Role], **I can** [perform rocket management actions], **so that** [business value/benefit is achieved].

## Logic Flowchart
Provide a brief textual overview of the conditional logic and decision paths.
```mermaid
flowchart TD
    %% Ensure clear start, decision points, and end states
```



```

## Sequence diagram
{Simple description of the diagram}
```mermaid
sequenceDiagram
%% Use clear participant aliases and descriptive activation lifelines
    
```

## Class diagram
{Simple description of the diagram}
```mermaid
classDiagram
%% Include key properties and methods relevant to the rocket feature
    
```

## Entity relationship diagram
{Simple description of the diagram}
```mermaid
erDiagram
%% Use standard Crow's Foot notation (e.g., ||--o{ )
```

---

## Output checklist
- [ ] The output should be a md file at `docs/rockets.analysis.md` containing:
- [ ] Title
- [ ] Description
- [ ] Flow chart
- [ ] Sequence diagramle
- [ ] Class diagramle
- [ ] Entity relationship diagram
