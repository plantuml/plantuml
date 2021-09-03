# GitHub Releases

Creating a new [release][1] in GitHub is done as part of the [CI workflow][2]
but only when the workflow run is for a new git tag beginning with a `v`.

Tags [cannot][3] be part of a pull request, so you need to push directly to the `plantuml` repo, e.g.

	git tag -a v1.2021.1 -m "version 1.2021.1"
	git push origin v1.2021.1

The release will only happen if the username making the push is matched in the CI `Configure job` step.

# Releases Elsewhere 

PlantUML is released to other places, currently that happens outside of GitHub and is not documented here.

[1]: https://github.com/plantuml/plantuml/releases
[2]: https://github.com/plantuml/plantuml/actions/workflows/ci.yml
[3]: https://stackoverflow.com/questions/12278660/adding-tags-to-a-pull-request
