# SciVault-QF

**SciVault-QF (SciVault Question Format)** is an open, implementation-independent format for storing and exchanging educational questions and question banks.

SciVault-QF is part of the SciVault Software project. It provides a common question representation that can be shared among SciVault applications and other compatible educational software.

The current specification version is **SciVault-QF 0.1**.

## Goals

SciVault-QF is designed to be:

* open and implementation-independent;
* human-readable;
* machine-validated;
* suitable for both simple and advanced educational questions;
* capable of representing scientific and mathematical content;
* usable by assessment, peer-instruction, and question-bank software;
* extensible without requiring proprietary software.

Standalone SciVault-QF documents use JSON encoded as UTF-8.

SciVault-QF also defines the `.sqf` package format for distributing a question set together with local resources such as images.

## Features

SciVault-QF 0.1 supports:

* multiple-choice questions;
* multiple-select questions;
* true/false questions;
* short-answer questions;
* fill-in-the-blank questions;
* numerical questions;
* LaTeX mathematical and scientific notation;
* question difficulty on a 0–10 scale;
* tags and learning objectives;
* explanations and hints;
* dynamic integer, decimal, and choice variables;
* calculated numerical answers;
* answer tolerances and units;
* image, audio, and video resources;
* question and choice media;
* choice feedback;
* application-specific extension data;
* standalone JSON question sets; and
* portable `.sqf` packages containing a question set and its local resources.

## Specification

The human-readable SciVault-QF 0.1 specification is located in:

```text
qf/docs/scivault-qf-0.1-specification.tex
```

A compiled PDF is also maintained in:

```text
qf/docs/scivault-qf-0.1-specification.pdf
```

The specification defines both standalone SciVault-QF documents and SciVault-QF packages.

## Schemas

The JSON Schema for standalone SciVault-QF 0.1 documents is:

```text
qf/schema/scivault-qf-0.1.schema.json
```

The JSON Schema for SciVault-QF 0.1 package manifests is:

```text
qf/schema/scivault-qf-package-0.1.schema.json
```

JSON Schema handles structural validation. Additional semantic requirements are defined by the specification and checked by conforming implementations.

## Basic Document Structure

A standalone SciVault-QF document contains set metadata and an array of questions.

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

## SciVault-QF Packages

SciVault-QF 0.1 defines a portable package format using the `.sqf` filename extension.

An `.sqf` file is a ZIP archive containing exactly one SciVault-QF question set together with an optional collection of local resources.

A minimal package contains:

```text
example-set.sqf
├── manifest.json
└── questions.json
```

A package containing media might contain:

```text
example-set.sqf
├── manifest.json
├── questions.json
└── media/
    ├── diagram.svg
    ├── graph.png
    └── setup-photo.jpg
```

The package manifest identifies the contained SciVault-QF document:

```json
{
  "format": "SciVault-QF",
  "format_version": "0.1",
  "content": "questions.json"
}
```

Package paths are relative to the package root and use forward slashes.

Unsafe paths, duplicate archive entries, missing package content, missing referenced local resources, and invalid manifests make a package invalid.

Resources present in a package but not referenced by the SciVault-QF document may produce a warning but do not by themselves make the package invalid.

External media references such as HTTPS URLs are not package resources and do not need to correspond to archive entries.

## Conformance Suites

The SciVault-QF repository contains separate conformance suites for standalone documents and packages.

### Document Conformance

The document suite is located in:

```text
qf/conformance/
```

It contains fixtures divided into:

```text
valid/
valid-with-warnings/
invalid/
```

Expected results are defined by:

```text
qf/conformance/manifest.json
```

The SciVault-QF 0.1 document conformance suite currently contains **20 fixtures**.

### Package Conformance

The package suite is located in:

```text
qf/conformance/package/
```

Expected package results are defined by:

```text
qf/conformance/package/manifest.json
```

The SciVault-QF 0.1 package conformance suite currently contains **11 fixtures**.

The current reference implementation passes:

```text
20/20 document conformance tests
11/11 package conformance tests
```

## Java Reference Implementation

SciVault-QF itself does **not** depend on Java.

The current Java reference implementation is located in:

```text
core/qf/
```

It provides reusable support for:

* reading SciVault-QF documents;
* writing SciVault-QF documents;
* JSON Schema validation;
* semantic validation;
* restricted dynamic-expression validation;
* reading `.sqf` packages;
* writing `.sqf` packages;
* package validation; and
* document and package conformance testing.

The implementation currently targets Java 11.

### Build and Test

From:

```text
core/qf/
```

run:

```bash
mvn test
```

For verbose conformance output:

```bash
mvn test -Dqf.verbose=true
```

At the SciVault-QF 0.1 release checkpoint, the reference implementation passes:

```text
43/43 Maven tests
20/20 document conformance tests
11/11 package conformance tests
```

## Validation

A conforming SciVault-QF implementation should perform both structural and semantic validation.

Structural validation includes requirements that can be represented directly in JSON Schema.

Semantic validation includes requirements such as:

* unique question IDs;
* unique choice IDs within a question;
* valid answer references;
* valid true/false choice IDs;
* valid variable ranges;
* defined placeholder variables;
* defined expression variables;
* required fill-in-the-blank markers;
* safe restricted expressions; and
* existence of referenced local media.

Warnings may identify potential authoring problems without making an otherwise conforming document invalid. Examples include missing recommended metadata and unused variables.

Package validation additionally checks the `.sqf` container, manifest, archive paths, package content, and local resource references.

Package validation and SciVault-QF document validation are logically distinct. A conforming `.sqf` package nevertheless contains a SciVault-QF document that conforms to SciVault-QF 0.1.

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

Implementations must not pass SciVault-QF expressions directly to a shell, JavaScript interpreter, Python interpreter, Java compiler, or other general-purpose execution environment.

SciVault-QF 0.1 defines a deliberately restricted expression language suitable for mathematical calculations.

## Media

Questions and individual choices may reference media.

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

Supported media types are:

* `image`;
* `audio`; and
* `video`.

For standalone documents, local media paths are resolved relative to the directory containing the SciVault-QF JSON document.

For `.sqf` packages, local media paths are resolved relative to the package root.

Images require an `alt` field. An empty `alt` string may be used when an image is intentionally decorative.

## Extensions

SciVault-QF core objects are intentionally strict so that accidental or misspelled fields can be detected.

Application-specific or experimental information should use the format's `extensions` mechanism rather than adding arbitrary fields to core objects.

This allows SciVault-QF to evolve while preserving interoperability.

## Compatibility

Because SciVault-QF is an implementation-independent format, compatible software may be written in Java, JavaScript or TypeScript, Python, C or C++, Rust, C#, Go, or any other language capable of processing JSON and ZIP archives.

A compatible implementation should behave consistently with the SciVault-QF specification and conformance suites.

## Status

SciVault-QF **0.1 is the initial version of the SciVault Question Format**.

Version 0.1 defines the core question model, validation rules, dynamic variables and expressions, media representation, extension mechanism, standalone JSON representation, and `.sqf` package format.

Future versions may extend these capabilities while preserving clearly defined version boundaries.

## Related SciVault Components

SciVault-QF provides the shared question representation for the wider SciVault Software suite.

Planned SciVault applications include:

* Question Bank tools;
* Test Generator;
* Peer Instruction system.

Keeping the question representation independent of individual applications allows the same question banks to be reused across multiple instructional tools.

## Contributing

Changes to the SciVault-QF core format should normally include:

1. an update to the human-readable specification;
2. an update to the appropriate JSON Schema when applicable;
3. new or updated conformance fixtures;
4. updates to the corresponding conformance manifest;
5. corresponding reference-implementation behavior; and
6. tests demonstrating the intended behavior.

A proposed format change should not be considered complete until the specification, schemas, conformance suites, and reference implementation agree.

