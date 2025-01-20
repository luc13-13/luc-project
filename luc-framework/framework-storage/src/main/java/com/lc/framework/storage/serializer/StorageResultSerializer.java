package com.lc.framework.storage.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.lc.framework.core.constants.StringConstants;
import com.lc.framework.storage.core.StorageResult;
import java.io.IOException;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/1/20 15:22
 * @version : 1.0
 */
public class StorageResultSerializer extends JsonSerializer<StorageResult> {
    @Override
    public void serialize(StorageResult value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        String accessUrl = "accessUrl";
        String filename = "filename";
        String bucket = "bucket";
        // 序列化为json串
        gen.writeStartObject();
        gen.writeStringField(accessUrl, value != null ? value.accessUrl() : StringConstants.EMPTY_STRING);
        gen.writeStringField(filename, value != null ? value.filename() : StringConstants.EMPTY_STRING);
        gen.writeStringField(bucket, value != null ? value.bucketName() : StringConstants.EMPTY_STRING);
        gen.writeEndObject();
    }
}
