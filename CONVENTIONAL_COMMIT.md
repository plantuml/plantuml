# Conventional Commit

IMPORTANT: *This is a draft*

The [Conventional Commits specification](https://www.conventionalcommits.org/en/v1.0.0/#summary) is a lightweight 
convention on top of commit messages. It provides an easy set of rules for creating an explicit commit history.


### Commit Message Format
Each commit message consists of a **header**, an **optional URL list** , an **optional body** and an **optional 
footer**. 
The header has a 
special format that includes a **type**, a **scope** and a **subject**:

```
<type>[optional scope]: <description>
[optional URL list]
[optional body]
[optional footer(s)]
```

The **header** is mandatory and the **scope** of the header is optional.

The **optional URL list** contains some links to github issue or to the forum and documents which issues
or features is concerned by this commit.


### Type
Must be one of the following:

* **feat**: A new feature
* **fix**: A bug fix
* **docs**: Documentation only changes
* **style**: Changes that do not affect the meaning of the code (white-space, formatting, missing
  semi-colons, etc)
* **refactor**: A code change that neither fixes a bug nor adds a feature
* **perf**: A code change that improves performance
* **test**: Adding missing or correcting existing test
* **chore**: Changes to the build process or auxiliary tools and libraries such as documentation
  generation

### Scope

### Subject
The subject contains succinct description of the change:

* use the imperative, present tense: "change" not "changed" nor "changes"
* don't capitalize first letter
* no dot (.) at the end

