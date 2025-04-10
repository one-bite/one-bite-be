package code.rice.bowl.spaghetti.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TopicRequest {
    private String name;
    private String description;
    private int total;
}