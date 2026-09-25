# SciVault Software

**SciVault Software** is an open-source educational software project focused on reusable tools for creating, managing, delivering, and analyzing educational assessments and instructional content.

The project is designed around **SciVault-QF (SciVault Question Format)**, an open, implementation-independent format that allows questions and question banks to be shared among compatible applications.

## SciVault-QF

SciVault-QF 0.1 supports:

* multiple-choice, multiple-select, true/false, short-answer, fill-in-the-blank, and numerical questions;
* mathematical and scientific notation;
* dynamic variables and calculated answers;
* difficulty, tags, learning objectives, explanations, and hints;
* media resources;
* application-specific extensions;
* standalone JSON question sets; and
* portable `.sqf` packages containing questions and local resources.

The SciVault-QF specification, schemas, conformance suites, and documentation are located in:

```text
qf/
```

The current Java reference implementation is located in:

```text
core/qf/
```

See [`qf/readme.md`](qf/readme.md) for details about SciVault-QF.

## Planned Applications

SciVault-QF is intended to provide a common foundation for SciVault educational applications, including:

* Question Bank tools;
* Test Generator;
* Peer Instruction system; and
* other assessment and classroom tools.

Individual applications can share question banks without making the question format dependent on a particular user interface, operating system, or programming language.

## Project Status

SciVault Software is under active development.

**SciVault-QF 0.1** is the first defined version of the project's common question format. Its specification, schemas, conformance suites, and Java reference implementation are maintained in this repository.

Other SciVault applications are at earlier stages of development.

## License

See [`LICENSE`](LICENSE) for the license covering this repository.

