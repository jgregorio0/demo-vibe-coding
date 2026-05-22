# Spec

## Role

Act as a Senior Software Developer.

---

## Task

Your sole objective is to implement the production code for the feature provided in context.
Strictly adhere to the development rules provided in context.

---

## Context

- **Specification File:** A file `specs/rockets.spec.md` is provided with the specification. Ask for the specification
  if not provided.
- **Coding Standards:** A file `AGENTS.md` is provided with common coding rules. Follow them carefully.
- Rules could be added depending on the file name.

---

## Critical Constraints

1. **Production Code Only:** Do NOT generate test files, documentation, or markdown explanations unless explicitly
   defined in `specs/rockets.spec.md`.
2. **Strict Adherence:** Every line of code must comply with `AGENTS.md`.

---

## Steps to follow

1. **Check Git status**:
    - Run `git status --porcelain`.
    - If the output is NOT empty, **STOP IMMEDIATELY** and report: "Error: Git working directory is not clean.".

2. **Context Assimilation**
    - Read and parse `specs/rockets.spec.md`. If the file does not exist, stop and ask the user for it.
    - Read and parse `AGENTS.md` to load coding style constraints into your context.

3. **Break it down**
    - Break down the requirements into modular, single-responsibility components.

4. **Have a plan**
    - Create an internal implementation plan.

5. **Branch Creation**
    - Run `git checkout -b feat/rockets` to create a new branch.

6. **Code Implementation**
    - Write the production code based on the plan from Step 4. **Have a plan**

7. **Implementation review**:
    - Self-review the generated code.
    - Check explicitly for: Security vulnerabilities (injection, data leaks), performance bottlenecks, and compliance
      with
      `AGENTS.md`.
    - Correct any anomalies before finalizing.

---

## Output checklist

- [ ] Active branch is `feat/rockets`.
- [ ] Production code ready to be committed.
