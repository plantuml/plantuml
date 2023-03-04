# GitHub Releases

Creating a new [release][1] in GitHub is done as part of the [CI workflow][2]
but only when the workflow run is for a new git tag beginning with a `v`.

Tags [cannot][3] be part of a pull request, so you need to push directly to the `plantuml` repo, e.g.

	git tag -a v1.2021.1 -m "version 1.2021.1"
	git push origin v1.2021.1

The release will only happen if the username making the push is matched in the CI `Configure job` step.

# Docker Images

Docker images are released as [Github Packages][7] and to [Docker hub][8].

# Artifact Signing

The CI workflow will sign artifacts if the `ARTIFACT_SIGNING_KEY` [GitHub secret][4] is present.  This should be a
private GPG key as described [here][5].  The passphrase is stored in the `ARTIFACT_SIGNING_PASSPHRASE` secret.

Currently, the signature files are only published as part of the [snapshot][6] releases.
In future, they will be part of the versioned releases as well.

# Releases Elsewhere 

PlantUML is released to other places, currently that happens outside of GitHub and is not documented here.

[1]: https://github.com/plantuml/plantuml/releases
[2]: https://github.com/plantuml/plantuml/actions/workflows/ci.yml
[3]: https://stackoverflow.com/questions/12278660/adding-tags-to-a-pull-request
[4]: https://docs.github.com/en/actions/security-guides/encrypted-secrets
[5]: https://central.sonatype.org/publish/requirements/gpg/#generating-a-key-pair
[6]: https://github.com/plantuml/plantuml/releases/tag/snapshot
[7]: https://github.com/plantuml/plantuml/pkgs/container/plantuml
[8]: https://hub.docker.com/r/plantuml/plantuml
