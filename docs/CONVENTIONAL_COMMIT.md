# Conventional Commit

**IMPORTANT:** *This is a draft.*

The [Conventional Commits specification](https://www.conventionalcommits.org/en/v1.0.0/#summary) is a lightweight convention for commit messages. It provides a simple set of rules for creating an explicit, meaningful commit history.

---

## Commit Message Format

Each commit message consists of the following elements:

- **Header** (mandatory): Includes an optional **scope** and a **description**.
- **Optional URL list**: Links to related GitHub issues, forums, or documents.
- **Optional body**: Provides additional context or details about the change.
- **Optional footer(s)**: For metadata like breaking changes or issues being closed.

### Format
```plaintext
<emoji>[optional scope]: <description>
[optional URL list]
[optional body]
[optional footer(s)]
```

### Header
The **header** is mandatory. The **scope** within the header is optional.

### Optional URL List
Use this section to document relevant links, such as GitHub issues or forum discussions related to the commit.

---

## Scope
The **scope** provides additional context about the change. It is optional but recommended for clarity, especially in projects with multiple modules or components.

---

## Subject
The **subject** is a succinct description of the change and follows these guidelines:

- Use the **imperative, present tense** (e.g., "fix", "add", "remove").
- Avoid capitalizing the first letter.
- Do not end with a period (.).

---

## Gitmoji Usage

[Gitmoji](https://gitmoji.dev/) enhances commit messages by adding emojis to represent the intent of the changes. It can be used alongside the Conventional Commits specification to make commit history more visual and expressive.

### Gitmoji Reference Table

| Emoji   | Description                                |
|---------|--------------------------------------------|
| ✨      | Introducing new features                   |
| 🐛      | Fixing a bug                               |
| 📝      | Writing or updating documentation          |
| 🎨      | Improving code structure/style             |
| ♻️      | Refactoring code                           |
| ⚡️      | Improving performance                      |
| ✅      | Adding or updating tests                   |
| 🔧      | Changes to configuration files             |
| 🚀      | Deployment-related changes                 |
| 🔒      | Fixing security issues                     |
| 🌱      | Adding or updating a seed file             |
| 🔥      | Removing code or files                    |
| 🚧      | Work in progress (WIP)                    |
| 📦️      | Add or update compiled files or packages  |
| ⚗️      | Perform experiments                       |
| 🎉      | Publish an official release               |
| 👷      | Add or update CI build system              |
| 📸      | Snapshot or preview release                |
| 🐾      | Small, incremental changes or tweaks       |
| 🖼️      | Enhance visual representation              |
| 💡      | Suggesting or implementing ideas          |
| 💄      | Add or update the UI and style files      |
| 💥      | Introduce breaking changes                |
| 📊      | Add or update logs, metrics, and traces   |
| 🍵      | Work about TeaVM                          |


#### Guidelines for Using Gitmoji
- Place the corresponding emoji at the beginning of the **description** in the commit message header.
- Ensure the emoji aligns with the purpose of the change.
- Add a space immediately after the emoji.

---

### Examples
- `✨ add user authentication module`
- `🐛 fix login form validation bug`
- `📝 update README with setup instructions`

