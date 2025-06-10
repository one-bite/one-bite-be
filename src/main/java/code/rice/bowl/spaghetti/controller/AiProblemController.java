package code.rice.bowl.spaghetti.controller;

import code.rice.bowl.spaghetti.dto.ai.AiProblemRequest;
import code.rice.bowl.spaghetti.dto.ai.AiProblemResponse;
import code.rice.bowl.spaghetti.exception.InvalidRequestException;
import code.rice.bowl.spaghetti.service.AiService;
import code.rice.bowl.spaghetti.utils.QuestionType;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
@Tag(name = "AI Problem Generation")
public class AiProblemController {

    private final AiService aiService;

    @PostMapping("/problem")
    public ResponseEntity<AiProblemResponse> createAiProblem(
            @RequestBody @Valid AiProblemRequest dto
    ) {
        // 1) 들어온 DTO의 questionType에 맞춰 options 개수 검증
        validateAiProblemRequest(dto);

        // 2) 검증 통과 시 서비스 호출
        AiProblemResponse aiResp = aiService.generateProblemWithCommentary(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(aiResp);
    }

    /**
     * questionType에 따라 description JSON 내부의 options 개수를 검사
     * - true_false  : options 배열이 정확히 2개여야 함
     * - multiple_choice : options 배열이 정확히 4개여야 함
     * - short_answer : options 필드가 없어야 함
     * @param dto 검증할 AiProblemRequest
     */
    private void validateAiProblemRequest(AiProblemRequest dto) {
        // 1) questionType 문자열을 enum으로 변환 (invalid 시 예외 발생)
        QuestionType type = QuestionType.toType(dto.getQuestionType());

        // 2) description에서 options 노드 추출
        JsonNode descriptionNode = dto.getDescription();
        JsonNode optionsNode = descriptionNode.path("options");

        switch (type) {
            case TRUE_FALSE:
                if (!optionsNode.isArray() || optionsNode.size() != 2) {
                    throw new InvalidRequestException(
                            "QuestionType이 TRUE_FALSE일 때는 'options' 배열의 크기가 2여야 합니다."
                    );
                }
                break;

            case MULTIPLE_CHOICE:
                if (!optionsNode.isArray() || optionsNode.size() != 4) {
                    throw new InvalidRequestException(
                            "QuestionType이 MULTIPLE_CHOICE일 때는 'options' 배열의 크기가 4여야 합니다."
                    );
                }
                break;

            case SHORT_ANSWER:
                if (optionsNode.isArray() && !optionsNode.isEmpty()) {
                    throw new InvalidRequestException(
                            "QuestionType이 SHORT_ANSWER일 때는 'options' 필드가 없어야 하거나, 비어 있어야 합니다."
                    );
                }
                break;

            default:
                break;
        }
    }
}