package code.rice.bowl.spaghetti.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 내부: AI 서버에 보낼 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AiServerProblemRequest {
    private String description;
    private List<String> topics;
    private String questionType;
}
