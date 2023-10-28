package zpi.ppea.clap.logic;

import data_store.Criterion;
import data_store.DataStoreGrpc;
import data_store.PartialRating;
import data_store.RatingsSubmissionRequest;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BusinessLogicService {

    @GrpcClient("dataStore")
    DataStoreGrpc.DataStoreBlockingStub dataStoreBlockingStub;

    public Double getRatingPointsByArea(Integer submissionId) {
        var ratings = dataStoreBlockingStub.getSubmissionRatings(RatingsSubmissionRequest.newBuilder()
                .setSubmissionId(submissionId).build());

        // Take criterias and partial ratings
        var criterias = ratings.getCriteriaList();
        var partialRatings = new ArrayList<PartialRating>();
        partialRatings.addAll(ratings.getFinal().getPartialRatingsList());
        partialRatings.addAll(ratings.getInitial().getPartialRatingsList());
        ratings.getIndividualList().forEach(i -> partialRatings.addAll(i.getPartialRatingsList()));

        // Create a map to index the CriterionDto objects by criterionId
        Map<Integer, Criterion> criteriaMap = criterias.stream().collect(Collectors.toMap(Criterion::getCriterionId, c -> c));

        // Perform the left join operation
        List<BigCriterion> leftJoinCriteria = partialRatings.stream()
                .map(partialRating -> {
                    Criterion criterion = criteriaMap.get(partialRating.getCriterionId());
                    if (criterion != null) {
                        // Create a new BigCriterion with the combined information
                        return new BigCriterion(
                                criterion.getCriterionId(),
                                criterion.getName(),
                                criterion.getDescription(),
                                criterion.getArea(),
                                criterion.getCriteria(),
                                criterion.getSubcriteria(),
                                partialRating.getPartialRatingId(),
                                partialRating.getPoints(),
                                partialRating.getJustification(),
                                partialRating.getModified(),
                                partialRating.getModifiedBy()
                        );
                    } else {
                        // Create a new BigCriterion with partialRating information
                        return new BigCriterion(
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                partialRating.getPartialRatingId(),
                                partialRating.getPoints(),
                                partialRating.getJustification(),
                                partialRating.getModified(),
                                partialRating.getModifiedBy()
                        );
                    }
                })
                .toList();

        // Group by "area" and calculate the mean of "points"
        Map<String, Double> averagePointsByArea = leftJoinCriteria.stream()
                .collect(Collectors.groupingBy(BigCriterion::getArea,
                        Collectors.averagingInt(BigCriterion::getPoints)));

        // Print the result
        averagePointsByArea.forEach((area, averagePoints) ->
                log.info("Area: {}, Average Points: {}", area, averagePoints)
        );

        return averagePointsByArea.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

}
