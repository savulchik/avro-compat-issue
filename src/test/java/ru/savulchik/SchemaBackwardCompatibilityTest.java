package ru.savulchik;

import org.apache.avro.Schema;
import org.apache.avro.SchemaCompatibility;
import org.apache.avro.SchemaCompatibility.SchemaCompatibilityType;
import org.apache.avro.SchemaValidatorBuilder;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * @author Stanislav Savulchik
 * @since 27.05.16
 */
public class SchemaBackwardCompatibilityTest {
    private Schema writerSchema = new Schema.Parser().parse("{\n" +
            "  \"name\": \"Foo\",\n" +
            "  \"type\": \"record\",\n" +
            "  \"fields\": [\n" +
            "    {\n" +
            "      \"name\": \"bar\",\n" +
            "      \"type\": \"string\"\n" +
            "    }\n" +
            "  ]\n" +
            "}");

    private Schema readerSchema = new Schema.Parser().parse("{\n" +
            "  \"name\": \"Foo\",\n" +
            "  \"type\": \"record\",\n" +
            "  \"fields\": [\n" +
            "    {\n" +
            "      \"name\": \"baz\",\n" +
            "      \"type\": \"string\",\n" +
            "      \"aliases\": [\"bar\"]\n" +
            "    }\n" +
            "  ]\n" +
            "}");

    @Test
    public void checkUsingSchemaCompatibility() {
        SchemaCompatibilityType actual = SchemaCompatibility
                .checkReaderWriterCompatibility(readerSchema, writerSchema)
                .getType();
        assertEquals(SchemaCompatibilityType.COMPATIBLE, actual);
    }

    @Test
    public void checkUsingSchemaValidator() throws Exception {
        new SchemaValidatorBuilder()
                .canReadStrategy()
                .validateLatest()
                .validate(readerSchema, Collections.singletonList(writerSchema));
    }
}
