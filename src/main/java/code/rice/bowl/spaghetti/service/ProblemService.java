package code.rice.bowl.spaghetti.service;

import code.rice.bowl.spaghetti.dto.problem.ProblemRequest;
import code.rice.bowl.spaghetti.dto.problem.ProblemResponse;
import code.rice.bowl.spaghetti.dto.problem.ProblemSimpleResponse;
import code.rice.bowl.spaghetti.entity.Problem;
import code.rice.bowl.spaghetti.entity.Topic;
import code.rice.bowl.spaghetti.exception.InvalidRequestException;
import code.rice.bowl.spaghetti.mapper.ProblemMapper;
import code.rice.bowl.spaghetti.repository.ProblemRepository;
import code.rice.bowl.spaghetti.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final TopicRepository topicRepository;

    public ProblemResponse create(ProblemRequest dto) {
        Topic topic = topicRepository.findById(dto.getTopicId())
                .orElseThrow(() -> new InvalidRequestException("Topic not found"));
        Problem problem = ProblemMapper.toEntity(dto, topic);
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
        Topic topic = topicRepository.findById(dto.getTopicId())
                .orElseThrow(() -> new InvalidRequestException("Topic not found"));

        problem.setTopic(topic);
        problem.setTitle(dto.getTitle());
        problem.setDescription(dto.getDescription());
        problem.setQuestionType(dto.getQuestionType());
        problem.setDifficulty(dto.getDifficulty());
        problem.setHint(dto.getHint());
        problem.setAnswer(dto.getAnswer());
        problem.setFeatures(dto.getFeatures());
        problem.setScore(dto.getScore());

        return ProblemMapper.toDto(problemRepository.save(problem));
    }

    public void delete(Long id) {
        problemRepository.deleteById(id);
    }
}