package code.rice.bowl.spaghetti.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.HashSet;

@Converter
public class HashSetConverter implements AttributeConverter<HashSet<String>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(HashSet<String> strings) {
        try {
            return objectMapper.writeValueAsString(strings);
        } catch (Exception e) {
            throw new RuntimeException("HashSet -> Json : convert fail");
        }
    }

    @Override
    public HashSet<String> convertToEntityAttribute(String s) {
        try {
            return objectMapper.readValue(s, new TypeReference<HashSet<String>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Json -> HashSet : convert fail");
        }
    }
}
