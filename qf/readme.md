# SciVault-QF

**SciVault-QF (SciVault Question Format)** is an open, machine-readable format for storing educational questions and question banks.

SciVault-QF is part of the SciVault Software project and is designed to provide a common question format that can be shared among SciVault applications and other compatible educational software.

The current specification version is **SciVault-QF 0.1**.

## Goals

SciVault-QF is designed to be:

- open and implementation-independent;
- human-readable;
- machine-validated;
- suitable for both simple and advanced educational questions;
- capable of representing scientific and mathematical content;
- usable by assessment, peer-instruction, and question-bank software;
- extensible without requiring proprietary software.

The format uses JSON encoded as UTF-8.

## Features

SciVault-QF 0.1 supports:

- multiple-choice questions;
- multiple-select questions;
- true/false questions;
- short-answer questions;
- fill-in-the-blank questions;
- numerical questions;
- LaTeX mathematical and scientific notation;
- question difficulty on a 0–10 scale;
- tags and learning objectives;
- explanations;
- dynamic numerical variables;
- calculated answers;
- answer tolerances and units;
- media resources;
- extension data for non-core features.

## Directory Structure

The SciVault-QF portion of the repository is organized as follows:

```text
qf/
├── README.md
│
├── schema/
│   └── scivault-qf-0.1.schema.json
│
├── conformance/
│   ├── manifest.json
│   ├── valid/
│   ├── valid-with-warnings/
│   └── invalid/
│
└── validator/
    ├── pom.xml
    ├── README.md
    └── src/
```

### `schema/`

Contains the machine-readable JSON Schema for SciVault-QF.

The schema validates the structural requirements of a SciVault-QF document.

### `conformance/`

Contains known-good and known-bad SciVault-QF files used to test implementations.

The conformance suite currently contains **20 test fixtures** divided into:

- valid files;
- valid files that should produce warnings;
- invalid files that should produce errors.

`manifest.json` defines the expected result for every fixture.

### `validator/`

Contains the Java reference validator.

The reference validator performs both:

1. JSON Schema validation; and
2. semantic validation that cannot conveniently be expressed by JSON Schema alone.

Examples of semantic checks include duplicate question IDs, undefined variables, missing media resources, invalid answer references, and unsafe expressions.

## Basic Document Structure

A SciVault-QF file contains document metadata and an array of questions.

For example:

```json
{
  "format": "SciVault-QF",
  "format_version": "0.1",

  "metadata": {
    "id": "org.example.physics.mechanics",
    "title": "Introductory Mechanics",
    "author": "Example Author",
    "license": "CC-BY-4.0",
    "version": "1.0",
    "language": "en-US",
    "tags": [
      "physics",
      "mechanics"
    ]
  },

  "questions": [
    {
      "id": "q001",
      "type": "multiple_choice",

      "question": "What is the SI unit of force?",

      "choices": [
        {
          "id": "A",
          "text": "Joule"
        },
        {
          "id": "B",
          "text": "Newton"
        },
        {
          "id": "C",
          "text": "Watt"
        }
      ],

      "answer": "B",

      "explanation": "The newton is the SI derived unit of force.",

      "difficulty": 1,

      "tags": [
        "physics",
        "units"
      ],

      "objectives": [
        "Identify the SI unit of force."
      ]
    }
  ]
}
```

## Validation

A conforming SciVault-QF implementation should perform both structural and semantic validation.

The official reference validator is located in:

```text
qf/validator/
```

### Requirements

The reference validator requires:

- Java 11 or later;
- Maven 3.6 or later.

### Build the Validator

From `qf/validator/`:

```bash
mvn clean package
```

This produces a self-contained executable JAR:

```text
target/scivault-qf-validator-0.1.0-all.jar
```

### Validate a Question File

From `qf/validator/`:

```bash
java -jar target/scivault-qf-validator-0.1.0-all.jar \
  validate ../conformance/valid/basic-multiple-choice.json \
  --schema ../schema/scivault-qf-0.1.schema.json
```

A valid file will produce:

```text
VALID
```

Warnings may also be reported for valid files.

Invalid files produce one or more error findings and a nonzero process exit status.

### Machine-Readable Results

Validation results can be returned as JSON:

```bash
java -jar target/scivault-qf-validator-0.1.0-all.jar \
  validate example.json \
  --schema ../schema/scivault-qf-0.1.schema.json \
  --json
```

## Conformance Testing

The complete SciVault-QF 0.1 conformance suite can be run with:

```bash
java -jar target/scivault-qf-validator-0.1.0-all.jar \
  conformance ../conformance/manifest.json \
  --schema ../schema/scivault-qf-0.1.schema.json
```

The SciVault-QF 0.1 reference implementation currently passes:

```text
20/20 conformance tests passed.
```

Other implementations of SciVault-QF should be able to use the same conformance suite.

## Validation Findings

Validation findings have a severity and stable code.

For example:

```json
{
  "severity": "error",
  "code": "ANSWER_UNKNOWN_CHOICE"
}
```

Current validation codes include examples such as:

```text
SCHEMA_VALIDATION_ERROR
DUPLICATE_QUESTION_ID
DUPLICATE_CHOICE_ID
ANSWER_UNKNOWN_CHOICE
INVALID_VARIABLE_RANGE
UNDEFINED_VARIABLE
UNUSED_VARIABLE
MISSING_BLANK
MISSING_MEDIA
INVALID_TRUE_FALSE_CHOICES
INVALID_EXPRESSION
MISSING_EXPLANATION
MISSING_OBJECTIVES
MISSING_SET_ID
```

`error` findings make a document invalid.

`warning` findings identify potential authoring problems but do not make an otherwise conforming document invalid.

## Dynamic Expressions

SciVault-QF supports calculated answers for dynamic numerical questions.

For example:

```json
"answer": {
  "expression": "v * t",
  "tolerance": 0.01,
  "unit": "m"
}
```

Expressions are **not executable program code**.

A SciVault-QF implementation must not pass expressions directly to a shell, JavaScript interpreter, Python interpreter, Java compiler, or other general-purpose execution environment.

The SciVault-QF 0.1 reference validator currently recognizes a deliberately restricted expression syntax containing:

- numeric literals;
- variable names;
- parentheses;
- `+`;
- `-`;
- `*`;
- `/`;
- `%`;
- `**`;
- unary `+` and `-`.

The expression language may be expanded and more formally specified in future versions.

## Media

Questions may reference media resources such as images.

Local media paths are resolved relative to the directory containing the SciVault-QF JSON file.

For example:

```json
"media": [
  {
    "id": "diagram-1",
    "type": "image",
    "src": "media/diagram.svg",
    "alt": "A physics diagram."
  }
]
```

A validator should report a missing local resource as `MISSING_MEDIA`.

## Extensions

SciVault-QF core objects are intentionally strict so that accidental or misspelled fields can be detected.

Software-specific or experimental data should therefore be stored using the format's extension mechanism rather than by adding arbitrary fields to core objects.

This allows SciVault-QF to evolve while preserving interoperability.

## Compatibility

SciVault-QF itself does **not** depend on Java.

The format consists of JSON documents, a JSON Schema, and defined semantic rules.

The Java validator is the project's reference implementation only.

Other implementations may be written in languages such as:

- JavaScript or TypeScript;
- Python;
- C or C++;
- Rust;
- C#;
- Go;
- or any other language capable of processing JSON.

A compatible implementation should produce behavior consistent with the SciVault-QF specification and conformance suite.

## Status

SciVault-QF **0.1 is an early development specification**.

The format should not yet be considered frozen. Fields, validation rules, expression syntax, packaging conventions, and extension mechanisms may change before SciVault-QF 1.0.

Backward compatibility becomes increasingly important as the specification approaches a stable release.

## Related SciVault Components

SciVault-QF is intended to provide the shared question representation used by the wider SciVault Software suite.

Planned components include:

- Question Bank tools;
- Test Generator;
- Peer Instruction system.

Keeping the question representation separate from individual applications allows the same question banks to be reused across multiple instructional tools.

## Contributing

Contributions should preserve the interoperability goals of SciVault-QF.

Changes to the core format should normally include:

1. an update to the human-readable specification;
2. an update to the JSON Schema when applicable;
3. new or updated conformance fixtures;
4. updates to `manifest.json`;
5. corresponding reference-validator behavior; and
6. tests demonstrating the intended behavior.

A proposed format change should not be considered complete until the specification, schema, conformance suite, and reference implementation agree.
