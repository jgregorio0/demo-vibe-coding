# Spec
## Role
Act as a software analyst.

## Task
Generate a specification for a Rocket Management API that can be used to manage rockets in a space booking system.

Do not implement any code. Only generate the specification.

## Context

- An API endpoint to manage rockets in the Bookings application.
- Each rocket has a 
  - name
  - range (an enum with values: suborbital, orbital, interplanetary)
  - capacity (an integer between 1 and 10)


Ask for any additional context if needed.

## Specification Template
Follow this template for writing the specification file specs/rockets.spec.md.

```markdown
# Rocket Management API Specification

## Problem Description
- As a **{role}**, I want to **{action}**, so that **{benefit}**

## Solution Overview
- {Simple explanation of the solution. Do not include technical details.}

## Acceptance Criteria
- Use the following format.
```markdown
[ ] AC {count} - {AC title}
  - **GIVEN** - Describe the context or precondition.
  - **WHEN** - Describe the action or event.
  - **THEN** - Describe the expected result or outcome.
```
## Steps to follow
1. Define the problem. Clearly outline the problem with up to 3 user stories.
2. Outline the solution. Simplest approach for application, logic and infrastructure.
3. Set Acceptance Criteria. Up to 9 acceptance criteria in EARS format.

## Output checklist
- [ ] The output should be a md file at `specs/rockets.spec.md`
- [ ] The specificaion with:
  - Title
  - Problem Description
  - Solution Overview
  - Acceptance Criteria
