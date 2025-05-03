package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.problem.ProblemRequest;
import code.rice.bowl.spaghetti.dto.problem.ProblemResponse;
import code.rice.bowl.spaghetti.dto.problem.ProblemSimpleResponse;
import code.rice.bowl.spaghetti.entity.Category;
import code.rice.bowl.spaghetti.entity.Problem;
import code.rice.bowl.spaghetti.entity.Topic;
import code.rice.bowl.spaghetti.entity.User;
import code.rice.bowl.spaghetti.exception.InvalidRequestException;
import code.rice.bowl.spaghetti.mapper.ProblemMapper;
import code.rice.bowl.spaghetti.repository.CategoryRepository;
import code.rice.bowl.spaghetti.repository.ProblemRepository;
import code.rice.bowl.spaghetti.repository.TopicRepository;
import code.rice.bowl.spaghetti.repository.UserRepository;
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

    @Transactional
    public ProblemResponse create(ProblemRequest dto) {
        // 1. Category 조회
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new InvalidRequestException("Category not found"));

        // 2. Topic code 목록 처리: 없는 코드는 자동 생성
        List<Topic> topics = new ArrayList<>();
        if (dto.getTopicCodes() != null) {
            for (String code : dto.getTopicCodes()) {
                Topic topic = topicRepository.findByCode(code)
                        .orElseGet(() -> topicRepository.save(
                                Topic.builder()
                                        .code(code)
                                        .name(code)    // 초기엔 code를 name으로 설정
                                        .total(0)
                                        .build()
                        ));
                topics.add(topic);
            }
        }

        // 3. User 처리 (관리자 문제면 null)
        User user = null;
        if (dto.getUserId() != null) {
            user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new InvalidRequestException("User not found"));
        }

        // 4. Problem 저장
        Problem problem = ProblemMapper.toEntity(dto, category, topics, user);
        return ProblemMapper.toDto(problemRepository.save(problem));
    }

    public List<ProblemSimpleResponse> findAll() {
        return problemRepository.findAll().stream()
                .map(ProblemMapper::toSimpleDto)
                .collect(Collectors.toList());
    }

    public ProblemResponse findById(Long id) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new InvalidRequestException("Problem not found"));
        return ProblemMapper.toDto(problem);
    }

    @Transactional
    public ProblemResponse update(Long id, ProblemRequest dto) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new InvalidRequestException("Problem not found"));

        // 1. Category
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new InvalidRequestException("Category not found"));
        problem.setCategory(category);

        // 2. Topic code 목록 처리
        List<Topic> topics = new ArrayList<>();
        if (dto.getTopicCodes() != null) {
            for (String code : dto.getTopicCodes()) {
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
        problem.setTopics(topics);

        // 3. User
        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new InvalidRequestException("User not found"));
            problem.setUser(user);
        } else {
            problem.setUser(null);
        }

        // 4. 나머지 필드
        problem.setTitle(dto.getTitle());
        problem.setDescription(dto.getDescription());
        problem.setQuestionType(dto.getQuestionType());
        problem.setHint(dto.getHint());
        problem.setAnswer(dto.getAnswer());
        problem.setPoint(dto.getPoint());

        return ProblemMapper.toDto(problemRepository.save(problem));
    }

    public void delete(Long id) {
        problemRepository.deleteById(id);
    }
}
