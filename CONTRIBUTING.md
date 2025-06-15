# Contributing to PlantUML



## I Have a Question

> If you want to ask a question, we assume that you have read the available [Documentation](https://plantuml.com).

Before you ask a question, it is best to search for
existing issues [on github](https://github.com/plantuml/plantuml/issues) 
or on [the online forum](https://forum.plantuml.net)
that might help you.

In case you have found a suitable issue and still need clarification, you can write your question in this issue.
It is also advisable to search the internet for answers first.

If you then still feel the need to ask a question and need clarification, we recommend the following:

- Open an issue [on github](https://github.com/plantuml/plantuml/issues/new) or on the
  [the online forum](https://forum.plantuml.net/ask).
- Provide as much context as you can about what you're running into.
- Provide a short example that shows the issue.
- Provide which version you are using and if you are using PlantUML through a plugin.

We will then take care of the issue as soon as possible.


## I Want To Contribute


### Reporting Bugs

#### Before Submitting a Bug Report

A good bug report shouldn't leave others needing to chase you up for more information. Therefore, we ask you to investigate carefully, collect information and describe the issue in detail in your report. Please complete the following steps in advance to help us fix any potential bug as fast as possible.

- Make sure that you are using the latest version.
- Also make sure to search the internet (including Stack Overflow) to see if users outside of the GitHub community have discussed the issue.


#### How Do I Submit a Good Bug Report?

We use GitHub issues to track bugs and errors. If you run into an issue with the project:

Before you ask a question, it is best to search for
existing issues [on github](https://github.com/plantuml/plantuml/issues) 
or on [the online forum](https://forum.plantuml.net)


- Open an issue [on github](https://github.com/plantuml/plantuml/issues) 
or on [the online forum](https://forum.plantuml.net).
- Explain the behavior you would expect and the actual behavior.
- Please provide as much context as possible and describe the *reproduction steps* that someone else can follow to recreate the issue on their own. This usually includes your code.
- For good bug reports you should isolate the problem and create a reduced test case.


### Suggesting Enhancements

This section guides you through submitting an enhancement suggestion for
PlantUML, **including completely new features and minor improvements to existing functionality**. Following
these guidelines will help maintainers and the community to understand your suggestion and find related suggestions.

#### Before Submitting an Enhancement

- Make sure that you are using the latest version.
- Read the [documentation](https://plantuml.com) carefully and find out if the functionality is already covered, maybe by an individual configuration.
- Perform some search [on github issue](https://github.com/plantuml/plantuml/issues) or  [on the online forum](https://forum.plantuml.net) to see if the enhancement has already been suggested. If it has, add a comment to the existing issue instead of opening a new one.
- Find out whether your idea fits with the scope and aims of the project. It's up to you to make a strong case to convince the project's developers of the merits of this feature. Keep in mind that we want features that will be useful to the majority of our users and not just a small subset. If you're just targeting a minority of users, consider writing an add-on/plugin library.

#### How Do I Submit a Good Enhancement Suggestion?

- Use a **clear and descriptive title** for the issue to identify the suggestion.
- Provide a **step-by-step description of the suggested enhancement** in as many details as possible.
- **Describe the current behavior** and **explain which behavior you expected to see instead** and why. At this point you can also tell which alternatives do not work for you.
- You may want to **include some existing diagram examples** that would help to understand the suggestion.
- **Explain why this enhancement would be useful** to most PlantUML users. You may also want to point out the other projects that solved it better and which could serve as inspiration.


### Your First Code Contribution

#### Good First Issues
  Start with issues labeled:
  - [good first issue](https://github.com/plantuml/plantuml/issues?q=is%3Aissue%20state%3Aopen%20label%3A%22good%20first%20issue%22)
  - [documentation](https://github.com/plantuml/plantuml/issues?q=is%3Aissue%20state%3Aopen%20label%3Adocumentation)

#### Environment Setup
1. **Prerequisites:**
  - [Java Development Kit (JDK)](https://jdk.java.net/) - version 8 or newer
  - [Gradle](https://gradle.org/install/) - version 7.0 or newer
  - [Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git) - to clone the repository and manage the version control

2. **Fork PlantUML repository** - [Fork](https://github.com/plantuml/plantuml/fork)

3. **Clone your repository to IDE:**

```sh
git clone https://github.com/{your-username}/plantuml
```

4. **Navigate to the project root directory:**

```sh
cd plantuml
```

#### Building the Project - [Full Guide](https://github.com/plantuml/plantuml/blob/master/BUILDING.md)

To build the project, run the following command from the project root directory:

```sh
gradle build
```

This command will build the project and create the necessary output files in the `build` directory.

#### Running Tests - [Full Guide](https://github.com/plantuml/plantuml/blob/master/docs/TESTING.md)

To run the tests included with the project, use the following command:

```sh
gradle test
```

#### Making Changes

**1. Create feature branch:**

```sh
git checkout -b add/issue-description
```

**2. Implement changes following code structure:**
    
  **Example:**
  - Diagram logic: src/main/java/net/sourceforge/plantuml
  - Syntax parsing: src/main/java/net/sourceforge/plantuml/syntax
  - etc

**3. Add tests for new features in:**
  - src/test/java/test/

**4. Commit and push changes**:
```sh
git commit -am "✨ add new feature" (It is necessary to follow the commit messages styleguide)
git push
```

#### Submitting PR
  **Open PR against plantuml:master**
  > PR should contain: 
  > 1) A title that briefly describes the essence of the changes; 
  > 2) A description that can explain in detail the essence of the changes.

### Improving The Documentation
#### What can be improved
  - Updating outdated sections
  - Enhancing explanations and examples
  - Adding new usage examples
  - Improving visual style and Markdown formatting
  - Adding links to relevant resourses
  - Adding more detailed information about the project's architecture and inner workings
#### How to improve it
**1. Fork PlantUML repository** - [Fork](https://github.com/plantuml/plantuml/fork)

**2. Clone your repository to IDE:**
```sh
git clone https://github.com/{your-username}/plantuml
```

**3. Navigate to the project root directory:**
```sh
cd plantuml
```
**4. Create a feature branch for documentation changes:**
```sh
git checkout -b {docname}-docs-change
```

**5. Make changes in docs**

**6. Commit and push changes:**
```sh
git commit -am "📝 add new doc section in {docname}" (It is necessary to follow the commit messages styleguide)
git push
```

**7. Open PR against plantuml:master**
  > PR should contain: 
  > 1) A title that briefly describes the essence of the changes; 
  > 2) A description that can explain in detail the essence of the changes.


### Improving non regression testing
<!-- TODO
-->

## Styleguides
### Commit Messages - [Full Guide](https://github.com/plantuml/plantuml/blob/master/CONVENTIONAL_COMMIT.md)

#### Format
```plaintext
<emoji>[optional scope]: <description>
[optional URL list]
[optional body]
[optional footer(s)]
```

- Use the **imperative, present tense** (e.g., "fix", "add", "remove").
- Avoid capitalizing the first letter.
- Do not end with a period (.).

---

#### Gitmoji Usage

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

#### Guidelines for Using Gitmoji
- Place the corresponding emoji at the beginning of the **description** in the commit message header.
- Ensure the emoji aligns with the purpose of the change.
- Add a space immediately after the emoji.