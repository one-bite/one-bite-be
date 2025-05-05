package code.rice.bowl.spaghetti.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Set;

@Converter(autoApply = true)
public class HashSetConverter implements AttributeConverter<Set<String>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Set<String> strings) {
        try {
            return objectMapper.writeValueAsString(strings);
        } catch (Exception e) {
            throw new RuntimeException("HashSet -> Json : convert fail");
        }
    }

    @Override
    public Set<String> convertToEntityAttribute(String s) {
        try {
            return objectMapper.readValue(s, new TypeReference<Set<String>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Json -> HashSet : convert fail");
        }
    }
}
