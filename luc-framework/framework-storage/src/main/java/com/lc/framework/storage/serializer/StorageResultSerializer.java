package com.lc.framework.storage.serializer;

import com.lc.framework.core.constants.StringConstants;
import com.lc.framework.storage.core.StorageResult;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

/**
 * <pre>
 * <pre/>
 * @author : Lu Cheng
 * @date : 2025/1/20 15:22
 * @version : 1.0
 */
public class StorageResultSerializer extends ValueSerializer<StorageResult> {
    @Override
    public void serialize(StorageResult value, JsonGenerator gen, SerializationContext serializers) {

        String accessUrl = "accessUrl";
        String filename = "filename";
        String bucket = "bucket";
        // 序列化为json串
        gen.writeStartObject();
        gen.writeStringProperty(accessUrl, value != null ? value.accessUrl() : StringConstants.EMPTY_STRING);
        gen.writeStringProperty(filename, value != null ? value.filename() : StringConstants.EMPTY_STRING);
        gen.writeStringProperty(bucket, value != null ? value.bucketInfo().getName() : StringConstants.EMPTY_STRING);
        gen.writeEndObject();
    }
}
