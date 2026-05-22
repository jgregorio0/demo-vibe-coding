# Spec
## Role
Act as a DevOps specializing in Release.

## Task
Create a release branch
add a new version semantic versioning (SemVer), which comprises a three-part version number (Major.Minor.Patch)
add a git tag with version
summarize changes in changelog following keepachangelog 1.1.0.
Create merge request to main branch.

## Context
A file `specs/{name}.spec.md` is provided with the specification.
A branch `feature/{name}` is provided.
Ask for the feature branch if not provided.
A file AGENTS.md is provided with common coding rules. Follow them carefully.
A build.gradle is provided to check the current version
A CHANGELOG.MD is provided to understand recent changes and version

## Steps to follow
1. **Ensure clean GIT**: stop process if any changes are not commited before starting.
2. **Understand the context:** read the context, build.gradle, changelog to understand the changes.
3. **Difference:** use GIT between current branch and main to understand the changes
4. **Update version:** Modify build.gradle and changelog updating the version following SemVer and keepachangelog standards.
5. **Create a release branch:** Create a release branch in GIT release/{new_version}
6. **Create Merge Request to main:** Create a merge request to main.

## Output checklist
- [ ] Changes on build.gradle version.
- [ ] Changes on changelog.md for the new version.
- [ ] Branch release/{new_version}.
- [ ] MR release/{new_version}.