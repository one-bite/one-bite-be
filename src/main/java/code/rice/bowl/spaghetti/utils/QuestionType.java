package code.rice.bowl.spaghetti.utils;


import code.rice.bowl.spaghetti.exception.InvalidRequestException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum QuestionType {
    MULTIPLE_CHOICE("multiple_choice"),
    SHORT_ANSWER("short_answer"),
    TRUE_FALSE("true_false");

    @JsonValue
    private final String value;

    @JsonCreator
    public static QuestionType toType(String value) {
        for (QuestionType data: values()) {
            if (data.value.equalsIgnoreCase(value))
                return data;
        }

        throw new InvalidRequestException("invalid QuestionType : " + value);
    }
}
