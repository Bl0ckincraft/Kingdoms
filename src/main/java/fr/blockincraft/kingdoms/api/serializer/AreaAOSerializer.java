package fr.blockincraft.kingdoms.api.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import fr.blockincraft.kingdoms.api.objects.AreaAO;

import java.io.IOException;

public class AreaAOSerializer extends JsonSerializer<AreaAO> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void serialize(AreaAO value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeFieldName(mapper.writeValueAsString(value));
    }
}
