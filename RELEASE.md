# Release process

## Update Dokka docs

1. Generate markdown files: `make update-docs`.

## Publish the artifacts

1. Cut a release branch, e.g. `release-v0.1.0`.
1. Update the version number in the root `build.gradle.kts` file (e.g. `version = "0.1.0"`). This will be the release number in the POM file.
1. Create a new release (e.g. tag `v0.1.0`) targeting the release branch. This will trigger a GitHub action that will automatically upload the artifact to Bintray.
