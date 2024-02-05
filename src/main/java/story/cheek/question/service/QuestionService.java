package story.cheek.question.service;

import static story.cheek.common.exception.ErrorCode.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import story.cheek.common.exception.ErrorCode;
import story.cheek.common.exception.NotFoundMemberException;
import story.cheek.common.exception.NotFoundQuestionException;
import story.cheek.member.domain.Member;
import story.cheek.member.repository.MemberRepository;
import story.cheek.question.Question;
import story.cheek.question.dto.request.QuestionCreateRequest;
import story.cheek.question.dto.response.QuestionDetailResponse;
import story.cheek.question.repository.QuestionRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionService {

    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public Long save(Member member, QuestionCreateRequest request) {

        Question question = Question.createQuestion(
                request.occupation(),
                request.title(),
                request.content(),
                member
        );

        questionRepository.save(question);

        return question.getId();
    }

    public QuestionDetailResponse findDetailById(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundQuestionException(QUESTION_NOT_FOUND));

        return QuestionDetailResponse.of(question);
    }
}
