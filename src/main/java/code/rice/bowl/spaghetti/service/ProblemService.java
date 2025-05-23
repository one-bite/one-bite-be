package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.problem.*;
import code.rice.bowl.spaghetti.entity.Category;
import code.rice.bowl.spaghetti.entity.Problem;
import code.rice.bowl.spaghetti.entity.Topic;
import code.rice.bowl.spaghetti.entity.User;
import code.rice.bowl.spaghetti.exception.InvalidRequestException;
import code.rice.bowl.spaghetti.mapper.ProblemMapper;
import code.rice.bowl.spaghetti.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final TopicRepository topicRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CourseService courseService;
    private final UserProblemHistoryRepository historyRepository;
    private final AuthService authService;
    private final AiService aiService;

    /**
     * 문제 추가
     */
    @Transactional
    public ProblemResponse create(ProblemRequest dto) {
        // 기본 카테고리 설정
        Long catId = (dto.getCategoryId() != null) ? dto.getCategoryId() : 1L;
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new InvalidRequestException("Category not found: " + catId));

        // 토픽 처리: 코드로 조회 후 없으면 생성
        List<Topic> topics = new ArrayList<>();
        if (dto.getTopicCodes() != null) {
            for (String code : dto.getTopicCodes()) {
                // 없는 토픽의 경우 자동으로 생성하여 저장.
                Topic topic = topicRepository.findByCode(code)
                        .orElseGet(() -> topicRepository.save(
                                Topic.builder()
                                        .code(code)
                                        .name(code)
                                        .total(0)
                                        .build()
                        ));
                topics.add(topic);
            }
        }

        // 사용자 처리 (AI 문제면 userId null)
        User user = null;
        if (dto.getUserId() != null) {
            user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new InvalidRequestException("User not found"));
        }

        // 문제 저장
        Problem problem = ProblemMapper.toEntity(dto, category, topics, user);

        Problem saved = problemRepository.save(problem);

        // 관리자가 만든 문제.
        if (user == null) {
            courseService.addCourse(saved);
        }

        return ProblemMapper.toDto(saved);
    }

    /**
     * 전체 문제 요약 조회
     */
    public List<ProblemSimpleResponse> findAll() {
        return problemRepository.findAll().stream()
                .map(ProblemMapper::toSimpleDto)
                .collect(Collectors.toList());
    }

    /**
     * 단일 문제 조회
     */
    public ProblemResponse findById(Long id) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new InvalidRequestException("Problem not found"));
        return ProblemMapper.toDto(problem);
    }

    public ProblemDetailResponse getProblemDetail(Long id) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new InvalidRequestException("Problem not found"));
        return ProblemMapper.toDetailDto(problem);
    }

    public long totalProblem() {
        return problemRepository.count();
    }

    @Transactional
    public ProblemResponse update(Long id, ProblemRequest dto) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new InvalidRequestException("Problem not found"));

        // 카테고리 업데이트
        Long catId = (dto.getCategoryId() != null) ? dto.getCategoryId() : 1L;
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new InvalidRequestException("Category not found: " + catId));
        problem.setCategory(category);

        // 토픽 업데이트
        List<Topic> topics = new ArrayList<>();
        if (dto.getTopicCodes() != null) {
            for (String code : dto.getTopicCodes()) {
                Topic topic = topicRepository.findByCode(code)
                        .orElseGet(() -> topicRepository.save(
                                Topic.builder().code(code).name(code).total(0).build()
                        ));
                topics.add(topic);
            }
        }
        problem.setTopics(topics);

        // 사용자 업데이트
        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new InvalidRequestException("User not found"));
            problem.setUser(user);
        } else {
            problem.setUser(null);
        }

        // 기타 필드 업데이트
        problem.setTitle(dto.getTitle());
        problem.setDescription(dto.getDescription());
        problem.setQuestionType(dto.getQuestionType());
        problem.setHint(dto.getHint());
        problem.setAnswer(dto.getAnswer());
        problem.setPoint(dto.getPoint());

        Problem updated = problemRepository.save(problem);
        return ProblemMapper.toDto(updated);
    }

    /**
     * 문제 삭제
     */
    @Transactional
    public void delete(Long id) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new InvalidRequestException("Problem not found"));

        // 삭제 전 카운트 감소
        for (Topic topic : problem.getTopics()) {
            topic.setTotal(topic.getTotal() - 1);
            topicRepository.save(topic);
        }
        Category category = problem.getCategory();
        category.setTotal(category.getTotal() - 1);
        categoryRepository.save(category);

        // 실제 삭제
        problemRepository.delete(problem);
    }

    @Transactional
    public String getOrGenerateCommentary(Long problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new InvalidRequestException("문제를 찾을 수 없습니다."));

        if (problem.getCommentary() != null && !problem.getCommentary().isBlank()) {
            return problem.getCommentary();
        }

        // AI에게 해설 요청
        String generatedCommentary = aiService.generateCommentary(problem);

        // 저장
        problem.setCommentary(generatedCommentary);
        problemRepository.save(problem);

        return generatedCommentary;
    }

    public ProblemStatsResponse getMyProblemStats() {
        Long userId = authService.getCurrentUserId();
        long total  = problemRepository.countByUserIsNull();
        long solved = historyRepository.countByUserUserIdAndProblemUserIsNull(userId);
        return new ProblemStatsResponse(total, solved);
    }
}
