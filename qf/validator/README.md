# SciVault-QF Reference Validator

Java reference validator for SciVault Question Format (SciVault-QF) 0.1.

## Requirements

- Java 11+
- Maven 3.6+

## Install location

Copy this directory to `scivault-software/qf/validator/`.

## Build

```bash
mvn clean package
```

This creates:

```text
target/scivault-qf-validator-0.1.0-all.jar
```

## Run all conformance tests

```bash
java -jar target/scivault-qf-validator-0.1.0-all.jar \
  conformance ../conformance/manifest.json \
  --schema ../schema/scivault-qf-0.1.schema.json
```

Target result:

```text
20/20 conformance tests passed.
```

## Validate one file

```bash
java -jar target/scivault-qf-validator-0.1.0-all.jar \
  validate ../conformance/valid/basic-multiple-choice.json \
  --schema ../schema/scivault-qf-0.1.schema.json
```

Add `--json` for machine-readable output.

## Expression safety

SciVault-QF v0.1 expressions are parsed as a restricted arithmetic grammar, not executed as Java, JavaScript, Python, shell code, or another general-purpose language. This implementation currently accepts numeric literals, variable names, parentheses, unary `+`/`-`, and `+ - * / % **`.
