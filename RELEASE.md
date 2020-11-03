# Release process

## Update Dokka docs

1. Generate markdown files: `make update-docs`.

## Publish the artifacts

Release ticket checklist:

- [ ] Cut a release branch, e.g. `release-v0.1.0`.
- [ ] Update the `CHANGELOG.md` file with a summary of the milestone. 
- [ ] Update the version number in the root `README.md` (new version) and `Makefile` (next version) files.
- [ ] Remove `-SNAPSHOT` from the version number in the root `build.gradle.kts` file (e.g. `version = "0.1.0"`). This will be the release number in the POM file.
- [ ] Create a new release (e.g. tag `v0.1.0`) targeting the release branch with the content in `CHANGELOG.md`. This will trigger a GitHub action that will automatically upload the artifact to Bintray.
- [ ] Update the version number in the root `build.gradle.kts` file to the next SNAPSHOT value (e.g. `version = "0.2.0-SNAPSHOT"`).
- [ ] Merge the PR.
