package code.rice.bowl.spaghetti.dto.ai;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
 * ai api에서 보내주는 정보를 받아오기 위해서 설계했으나.
 * 현재는 그냥 ProblemRequest을 사용중이라 사용되는 곳이 없음.
 * 후에 상황보고 완전 필요없어지면 삭제할 예정
 */
@Getter
@NoArgsConstructor
public class AiProblemResponse {

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private String questionType;

    @NotNull
    private String hint;

    @NotNull
    private String answer;

    @NotNull
    private int point;

}
