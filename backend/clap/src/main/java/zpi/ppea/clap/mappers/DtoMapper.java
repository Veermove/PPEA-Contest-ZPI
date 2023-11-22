package zpi.ppea.clap.mappers;

import data_store.*;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import zpi.ppea.clap.dtos.*;
import zpi.ppea.clap.security.FirebaseAgent;

import java.util.List;
import java.util.Optional;

@Mapper
public interface DtoMapper {

    DtoMapper INSTANCE = Mappers.getMapper(DtoMapper.class);

    @Mapping(target = "partialRatings", source = "partialRatingsList")
    @Mapping(target = "draft", source = "isDraft")
    AssessorRatingsDto assessorRatingsToDtos(AssessorRatings assessorRatings);

    List<AssessorRatingsDto> assessorRatingsListToDtos(List<AssessorRatings> assessorRatings);

    CriterionDto criterionToDto(Criterion rating);

    List<CriterionDto> criterionListToDto(List<Criterion> rating);

    PartialRatingDto partialRatingToDtos(PartialRating partialRatings);

    List<PartialRatingDto> partialRatingListToDtos(List<PartialRating> partialRatings);

    @Mapping(target = "appReportDto", source = "report")
    DetailsSubmissionResponseDto detailsSubmissionToDto(DetailsSubmissionResponse response);

    AppReportDto appReportToDto(AppReport report);

    List<SubmissionDto> submissionListToDtos(List<Submission> submissions);

    @Mapping(target = "assessors", source = "assessorsList")
    @Mapping(target = "ratings", source = "ratingsList")
    SubmissionDto submissionToDto(Submission submission);

    RatingDto ratingToDto(Rating rating);

    PartialRatingRequest dtoToPartialRatingRequest(UpdatePartialRatingDto dto);

    @Mapping(target = "questions", source = "questionsList")
    StudyVisitDto toStudyVisitDto(StudyVisitResponse studyVisitResponse);

    @Mapping(target = "content", source = "content")
    @Mapping(target = "answers", source = "answersList")
    QuestionDto toQuestionDto(Question question);

    @Mapping(target = "description", source = "description")
    @Mapping(target = "files", source = "filesList")
    AnswerDto toAnswerDto(Answer answer);

    List<QuestionDto> toQuestionDtoList(List<Question> questions);
    List<AnswerDto> toAnswerDtoList(List<Answer> answers);
    @AfterMapping
    default void setAssessorId(@MappingTarget PartialRatingRequest.Builder builder, FirebaseAgent.UserAuthData authentication) {
        builder.setAssessorId(authentication.getClaims().getAssessorId());
    }

    default RatingsSubmissionResponseDto ratingsResponseToDto(RatingsSubmissionResponse ratings) {
        final AssessorRatings initialRatings = ratings.getInitial();
        final AssessorRatings finalRatings = ratings.getFinal();
        return RatingsSubmissionResponseDto.builder()
                .criteria(DtoMapper.INSTANCE.criterionListToDto(
                        Optional.of(ratings.getCriteriaList()).orElse(List.of())
                ))
                .individualRatings(DtoMapper.INSTANCE.assessorRatingsListToDtos(
                        Optional.of(ratings.getIndividualList()).orElse(List.of())
                ))
                .initialRating(initialRatings.getAssessorId() == 0 ? null : DtoMapper.INSTANCE.assessorRatingsToDtos(initialRatings))
                .finalRating(finalRatings.getAssessorId() == 0 ? null : DtoMapper.INSTANCE.assessorRatingsToDtos(finalRatings))
                .build();
    }

}
