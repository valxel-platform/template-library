# Library template (Java 21)

The golden path for shared Java libraries, for example in `vrixel-shared-services`. Libraries
have no runtime: they publish versioned packages that products adopt in their own change sets.

## What you get

| Area | Choice | Why |
| --- | --- | --- |
| Build | Gradle 9.8 wrapper, `java-library`, version catalog, sources and Javadoc jars | Consumers get docs and sources in their IDE |
| Tests | JUnit 6, AssertJ, JaCoCo line coverage of at least 90% | The floor the platform requires (VX-11); shared code is reused widely |
| API hygiene | Small public surface (`api` vs `implementation`), Javadoc linting | Fewer accidental breaking changes |
| Releases | Tag `vX.Y.Z` → tests, publish to GitHub Packages, GitHub release with notes | Semantic versioning; immutable published versions |
| Supply chain | Actions pinned by SHA, wrapper validation, dependency graph submission and review, Dependabot | Reviewed, known dependencies |

## Checks

| Check | Required | Read by the Vrixel SDLC as |
| --- | --- | --- |
| `unit` (`./gradlew check javadoc`) | yes | the `unit` suite, counts and coverage from `unit / report` |
| `CodeQL` | yes | the `sast` suite |
| `dependency-review` | no | the `dependencies` suite reads GitHub's dependency diff |

## Develop and release

```bash
./gradlew check                         # tests, coverage, Javadoc
git tag v1.2.0 && git push origin v1.2.0   # publishes 1.2.0
```

Never break a minor or patch release. Deprecate first, remove in the next major.
