# Contributing to Paymob Java SDK

Thank you for your interest in contributing! This guide will help you get started.

## 📋 Table of Contents

- [Code of Conduct](#code-of-conduct)
- [How Can I Contribute?](#how-can-i-contribute)
- [Development Setup](#development-setup)
- [Branching & Commits](#branching--commits)
- [Code Style](#code-style)
- [Submitting a Pull Request](#submitting-a-pull-request)
- [CI/CD Workflows](#cicd-workflows)

---

## Code of Conduct

Be respectful, constructive, and inclusive. We're all here to build something useful for the community.

---

## How Can I Contribute?

### 🐛 Report a Bug

1. Check [existing issues](https://github.com/Yusuf-Hussien/Paymob-Java-SDK/issues) first.
2. Open a new issue using the **Bug Report** template.
3. Include: steps to reproduce, expected vs. actual behavior, SDK version, and Java version.

### 💡 Request a Feature

1. Open a new issue using the **Feature Request** template.
2. Describe the use case and proposed solution.

### 🔧 Submit a Fix or Feature

1. Fork the repo and create a branch from `main`.
2. Make your changes (see guidelines below).
3. Open a pull request.

---

## Development Setup

### Prerequisites

- **Java 17+** ([Download](https://adoptium.net/))
- **Maven 3.6+** ([Download](https://maven.apache.org/download.cgi))
- **Git**

### Build & Test

```bash
# Clone your fork
git clone https://github.com/<your-username>/Paymob-Java-SDK.git
cd Paymob-Java-SDK

# Build
mvn clean compile

# Run tests
mvn test

# Full build (compile + test + package)
mvn clean package
```

---

## Branching & Commits

### Branch Naming

| Type | Format | Example |
|------|--------|---------|
| Feature | `feature/<short-description>` | `feature/add-installments-api` |
| Bug fix | `fix/<short-description>` | `fix/hmac-validation-error` |
| Docs | `docs/<short-description>` | `docs/update-webhook-guide` |
| Refactor | `refactor/<short-description>` | `refactor/http-client-cleanup` |

### Commit Messages

We follow [Conventional Commits](https://www.conventionalcommits.org/):

```
<type>(<scope>): <description>

[optional body]
```

**Types:** `feat`, `fix`, `docs`, `refactor`, `test`, `chore`, `build`, `ci`

**Examples:**

```
feat(webhook): add card token webhook parser
fix(subscription): correct plan creation endpoint path
docs(readme): update quick start section
test(inquiry): add inquiry service unit tests
```

---

## Code Style

- Follow standard **Java conventions** (camelCase for methods/fields, PascalCase for classes).
- Use **4 spaces** for indentation (no tabs).
- Keep methods focused — prefer small, single-purpose methods.
- Add **Javadoc** for all public classes and methods.
- Use the **Builder pattern** for request objects with more than 2 parameters.
- All request/response objects should be **immutable** (final fields, no setters).
- Handle null values explicitly — don't rely on `NullPointerException`.

---

## Submitting a Pull Request

### Before You Submit

- [ ] Your code compiles without errors (`mvn clean compile`)
- [ ] All existing tests pass (`mvn test`)
- [ ] You've added tests for new functionality
- [ ] You've updated documentation if needed
- [ ] Your commits follow Conventional Commits format
- [ ] Your branch is up to date with `main`

### PR Process

1. **Open a PR** against the `main` branch.
2. Fill out the **PR template** completely.
3. Wait for CI checks to pass.
4. A maintainer will review your PR and may request changes.
5. Once approved, a maintainer will merge your PR.

### What Makes a Good PR?

- **Small and focused** — one feature or fix per PR.
- **Well-tested** — include unit tests for new code.
- **Well-documented** — update README or docs if applicable.
- **Clean history** — squash fixup commits before requesting review.

---

## CI/CD Workflows

We use GitHub Actions to automate our testing and release processes.

### Automated Checks (PRs)
When you open a Pull Request against `main` or `develop`, the following workflows are triggered:
- **CI**: Compiles the code and runs unit tests across JDK 17 and 21. 
- **Integration Tests**: These **do not** run automatically on PRs from forks because they require sensitive API credentials. Maintainers will run them manually or after merging.

### Push to Main Branch
When code is merged or pushed directly to the `main` branch, several workflows are triggered:
1.  **CI**: Full compilation and unit testing.
2.  **Integration Tests**: Runs the full suite of integration tests against the live Paymob Sandbox API.
3.  **Generate JavaDoc**: Automatically updates the SDK documentation hosted on [GitHub Pages](https://yusuf-hussien.github.io/Paymob-Java-SDK/).
4.  **Release**: If the version in `pom.xml` hasn't been published yet, this workflow will:
    - Publish the artifacts to **Maven Central**.
    - Create a new **GitHub Release** with the JAR and release notes.

> [!IMPORTANT]
> To release a new version, ensure the `RELEASE_VERSION` variable in the GitHub environment is updated before pushing to `main`.

---

## 📄 License

By contributing, you agree that your contributions will be licensed under the [MIT License](LICENSE).
