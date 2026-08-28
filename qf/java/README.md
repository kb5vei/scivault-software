# SciVault-QF Java

Maven multi-module Java implementation of SciVault Question Format 0.1.

## Modules

- `scivault-qf-core`: reusable validation/core library; no CLI or GUI dependency.
- `scivault-qf-cli`: command-line validator built on `scivault-qf-core`.

## Repository location

```text
qf/
├── schema/
├── conformance/
└── java/
    ├── pom.xml
    ├── scivault-qf-core/
    └── scivault-qf-cli/
```

## Build

From `qf/java/`:

```bash
mvn clean package
```

## Run conformance suite

```bash
java -jar scivault-qf-cli/target/scivault-qf-cli-0.1.0-all.jar \
  conformance ../conformance/manifest.json \
  --schema ../schema/scivault-qf-0.1.schema.json
```

Expected result:

```text
20/20 conformance tests passed.
```

## Validate one file

```bash
java -jar scivault-qf-cli/target/scivault-qf-cli-0.1.0-all.jar \
  validate ../conformance/valid/basic-multiple-choice.json \
  --schema ../schema/scivault-qf-0.1.schema.json
```
