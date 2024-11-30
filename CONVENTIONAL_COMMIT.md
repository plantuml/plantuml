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


### Gitmoji Usage

[Gitmoji](https://gitmoji.dev/) provides a visual and expressive way to enhance commit messages by including emojis that represent the intent of the changes. It can be used alongside the Conventional Commit specification to add more clarity and fun to commit history.

Each Gitmoji corresponds to a specific type of change:

| Emoji   | Description                          | Conventional Commit Type   |
|---------|--------------------------------------|----------------------------|
| ✨      | Introducing new features             | `feat`                     |
| 🐛      | Fixing a bug                         | `fix`                      |
| 📝      | Writing or updating documentation    | `docs`                     |
| 🎨      | Improving code structure/style       | `style`                    |
| ♻️      | Refactoring code                    | `refactor`                 |
| ⚡️      | Improving performance                | `perf`                     |
| ✅      | Adding or updating tests             | `test`                     |
| 🔧      | Changes to configuration files       | `chore`                    |
| 🚀      | Deployment-related changes           | `chore`                    |
| 🔒      | Fixing security issues               | `fix`                      |
| 🌱      | Adding or updating a seed file       | `chore`                    |
| 🔥      | Removing code or files               | `chore`                    |

#### Guidelines for Using Gitmoji
- Place the corresponding emoji at the beginning of the **description** in the commit message header.
- Ensure the emoji aligns with the type and purpose of the change.
- Examples:
  - `✨feat: add user authentication module`
  - `🐛fix: resolve issue with null pointer exception`
  - `🌱chore: add initial database seed script`
  - `📝docs: update README with installation instructions`
  - `🔥chore: remove deprecated API endpoints`
  
Using Gitmoji is optional but can make commit messages more engaging and informative.
