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
    private final CourseService courseService;

    @Transactional
    public ProblemResponse create(ProblemRequest dto) {
        Long catId = (dto.getCategoryId() != null) ? dto.getCategoryId() : 1L;
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new InvalidRequestException("Category not found: " + catId));

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

        User user = null;
        if (dto.getUserId() != null) {
            user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new InvalidRequestException("User not found"));
        }

        Problem problem = ProblemMapper.toEntity(dto, category, topics, user);

        Problem saved = problemRepository.save(problem);

        // 관리자가 만든 문제.
        if (user == null) {
            courseService.addCourse(saved);
        }

        return ProblemMapper.toDto(saved);
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

        Long catId = (dto.getCategoryId() != null) ? dto.getCategoryId() : 1L;
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new InvalidRequestException("Category not found: " + catId));
        problem.setCategory(category);

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

        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new InvalidRequestException("User not found"));
            problem.setUser(user);
        } else {
            problem.setUser(null);
        }

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
