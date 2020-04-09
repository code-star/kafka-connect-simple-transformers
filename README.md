Kafka Connect Transformers
==========================

A collection of Simple Message Transformers for Kafka Connect. 

- `Zip$Value`: compress the value of the record given as input.
- `Unzip$Value`: uncompress the value of the record given as input.

Build
-----

Run the following command to obtain a uber jar in `target/components`:

    mvn clean package
    
Use
---

### Zip

In the config that defines the connector, add the following lines:

    "transforms": "..., zip, ...",
    "transforms.zip.type": "nl.codestar.kafka.connect.transformers.compress.Zip$Value",

No extra configuration parameters are needed.

### Unzip

In the config that defines the connector, add the following lines:

    "transforms": "..., unzip, ...",
    "transforms.unzip.type": "nl.codestar.kafka.connect.transformers.compress.Unzip$Value",

No extra configuration parameters are needed.
