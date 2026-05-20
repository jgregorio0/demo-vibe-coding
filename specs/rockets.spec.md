# Rocket Management API Specification

## Problem Description
- As a **manager**, I want to **register and manage rockets in the fleet**, so that **I can schedule flights for bookings**.
- As a **user**, I want to **retrieve details of available rockets**, so that **I can match customer requirements to the correct rocket capabilities**.
- As a **admin**, I want to **enforce validation rules on rocket range and capacity**, so that **we prevent unsafe configurations and overbooking**.

## Solution Overview
- A Rocket Management service will be introduced to maintain a centralized registry of all rockets within the space booking system. This service will allow registering new rockets, updating their specifications, retrieving details of a single rocket, listing the entire fleet, and decommissioning (deleting) rockets. It will automatically validate all entries to ensure that each rocket has a unique name, a valid range classification, and a capacity within acceptable safety limits.

## Acceptance Criteria
- [ ] AC 1 - Register Rocket Success
  - **GIVEN** - A manager wants to register a new rocket.
  - **WHEN** - The manager submits a request with a unique name, a valid range, and a capacity between 1 and 10.
  - **THEN** - The system saves the rocket and returns its details including a generated ID.
- [ ] AC 2 - Duplicate Rocket Name
  - **GIVEN** - A rocket with name "Falcon" already exists in the system.
  - **WHEN** - A manager attempts to register a new rocket with the name "Falcon".
  - **THEN** - The system rejects the registration and returns a conflict error.
- [ ] AC 3 - Invalid Rocket Capacity
  - **GIVEN** - A manager wants to register or update a rocket.
  - **WHEN** - The manager submits a capacity value less than 1 or greater than 10.
  - **THEN** - The system rejects the request and returns a validation error.
- [ ] AC 4 - Invalid Rocket Range
  - **GIVEN** - A manager wants to register or update a rocket.
  - **WHEN** - The manager submits a range value that is not suborbital, orbital, or interplanetary.
  - **THEN** - The system rejects the request and returns a validation error.
- [ ] AC 5 - Retrieve Rocket Details Success
  - **GIVEN** - A rocket with a specific ID exists in the system.
  - **WHEN** - A user requests details of the rocket using that ID.
  - **THEN** - The system returns the rocket's name, range, and capacity.
- [ ] AC 6 - Retrieve Rocket Details Not Found
  - **GIVEN** - A user wants to retrieve rocket details.
  - **WHEN** - The user requests details of a rocket using an ID that does not exist.
  - **THEN** - The system returns a not found error.
- [ ] AC 7 - Update Rocket Success
  - **GIVEN** - A rocket with a specific ID exists in the system.
  - **WHEN** - A manager requests to update the rocket details with a unique name, a valid range, and a capacity between 1 and 10.
  - **THEN** - The system saves the changes and returns the updated details.
- [ ] AC 8 - Delete Rocket Success
  - **GIVEN** - A rocket with a specific ID exists in the system.
  - **WHEN** - A manager requests to decommission the rocket using its ID.
  - **THEN** - The system removes the rocket from the registry.
- [ ] AC 9 - List Rockets Success
  - **GIVEN** - There are registered rockets in the system.
  - **WHEN** - A user requests the list of all registered rockets.
  - **THEN** - The system returns a collection containing all rockets in the fleet.
