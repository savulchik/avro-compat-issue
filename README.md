# avro-compat-issue
Sample project to show avro schema compatibility checking issue.

# Description

There is a discrepancy when using
`org.apache.avro.SchemaCompatibility.checkReaderWriterCompatibility` and
`org.apache.avro.ValidateCanRead.validate` for checking backward compatibility of schemas
if a reader schema uses aliases for some fields of a writer schema:

`writer.avsc`
```
{
  "name": "Foo",
  "type": "record",
  "fields": [
    {
      "name": "bar",
      "type": "string"
    }
  ]
}
```

`reader.avsc`
```
{
  "name": "Foo",
  "type": "record",
  "fields": [
    {
      "name": "baz",
      "type": "string",
      "aliases": ["bar"]
    }
  ]
}
```

The `org.apache.avro.SchemaCompatibility.checkReaderWriterCompatibility` reports that the reader schema is compatible with the writer schema.

But `org.apache.avro.ValidateCanRead.validate` reports otherwise.


# How to reproduce the issue?

Run `mvn test` and see the failed test output:

```
checkUsingSchemaValidator(ru.savulchik.SchemaBackwardCompatibilityTest)  Time elapsed: 0.017 sec  <<< ERROR!
org.apache.avro.SchemaValidationException: Unable to read schema:
{
  "type" : "record",
  "name" : "Foo",
  "fields" : [ {
    "name" : "bar",
    "type" : "string"
  } ]
}
using schema:
{
  "type" : "record",
  "name" : "Foo",
  "fields" : [ {
    "name" : "baz",
    "type" : "string",
    "aliases" : [ "bar" ]
  } ]
}
...
```
