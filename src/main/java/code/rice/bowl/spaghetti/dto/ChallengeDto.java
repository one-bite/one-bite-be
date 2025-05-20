package code.rice.bowl.spaghetti.dto;

import code.rice.bowl.spaghetti.exception.InternalServerError;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
public class ChallengeDto {
    private int hp;
    private int score;
    private Set<Long> solved;

    public static ChallengeDto fromString(String raw) {
        String[] data = raw.split("-");

        if (data.length != 3) {
            throw new InternalServerError("ChallengeDto : 구조 양식이 맞지 않음.");
        }

        try {
            int hp = Integer.parseInt(data[0]);
            int score = Integer.parseInt(data[1]);

            Set<Long> solved = Arrays.stream(data[2].split(","))
                    .map(String::trim)              // 혹시 있을 앞뒤 공백 제거
                    .filter(s -> !s.isEmpty())      // 빈 문자열 제거
                    .map(Long::parseLong)
                    .collect(Collectors.toSet());


            return ChallengeDto.builder()
                    .hp(hp)
                    .score(score)
                    .solved(solved)
                    .build();
        } catch (Exception e) {
            throw new InternalServerError("ChallengeDto : 숫자 변환 실패.");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(hp).append('-').append(score).append('-');

        for (Long id: solved) {
            sb.append(id).append(',');
        }

        return sb.toString();
    }
}
