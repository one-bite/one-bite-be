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

    public ProblemResponse create(ProblemRequest dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new InvalidRequestException("Category not found"));

        List<Topic> topics = new ArrayList<>();
        if (dto.getTopicIds() != null) {
            for (Long topicId : dto.getTopicIds()) {
                Topic topic = topicRepository.findById(topicId)
                        .orElseThrow(() -> new InvalidRequestException("Topic not found: id=" + topicId));
                topics.add(topic);
            }
        }

        User user = null;
        if (dto.getUserId() != null) {
            user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new InvalidRequestException("User not found"));
        }

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

    public ProblemResponse update(Long id, ProblemRequest dto) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new InvalidRequestException("Problem not found"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new InvalidRequestException("Category not found"));
        problem.setCategory(category);

        List<Topic> topics = new ArrayList<>();
        if (dto.getTopicIds() != null) {
            for (Long topicId : dto.getTopicIds()) {
                Topic topic = topicRepository.findById(topicId)
                        .orElseThrow(() -> new InvalidRequestException("Topic not found: id=" + topicId));
                topics.add(topic);
            }
        }
        problem.setTopics(topics);

        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new InvalidRequestException("User not found"));
            problem.setUser(user);
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