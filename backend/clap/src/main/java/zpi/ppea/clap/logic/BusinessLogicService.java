package zpi.ppea.clap.logic;

import data_store.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import zpi.ppea.clap.service.DataStoreService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class BusinessLogicService {

    public static Double calculateSubmissionRating(RatingsSubmissionResponse ratings, Integer submissionId, Integer assessorId) {

        // Take criterias (distinct by id) and partial ratings
        var criterias = ratings.getCriteriaList().stream()
                .collect(Collectors.toMap(Criterion::getCriterionId, criterion -> criterion, (existing, replacement) -> existing))
                .values()
                .stream()
                .toList();
        var partialRatings = new ArrayList<PartialRating>();
        partialRatings.addAll(ratings.getFinal().getPartialRatingsList());
        partialRatings.addAll(ratings.getInitial().getPartialRatingsList());
        ratings.getIndividualList().forEach(i -> partialRatings.addAll(i.getPartialRatingsList()));

        // Create a map to index the CriterionDto objects by criterionId
        Map<Integer, Criterion> criteriaMap = criterias.stream().collect(Collectors.toMap(Criterion::getCriterionId, c -> c));

        // Perform the inner join operation
        List<RatedCriterion> innerJoinCriteria = partialRatings.stream()
                .map(partialRating -> {
                    Criterion criterion = criteriaMap.get(partialRating.getCriterionId());
                    if (criterion != null) {
                        return new RatedCriterion(
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
                        return null; // Return null for non-matching criteria
                    }
                })
                .filter(Objects::nonNull) // Filter out null entries for inner join
                .toList();

        // Group by "area" and calculate the mean of "points"
        Map<String, Double> averagePointsByArea = innerJoinCriteria.stream()
                .collect(Collectors.groupingBy(RatedCriterion::getArea,
                        Collectors.averagingInt(RatedCriterion::getPoints)));

        // Print the result
        averagePointsByArea.forEach((area, averagePoints) ->
                log.info("Area: {}, Average Points: {}", area, averagePoints)
        );

        return BigDecimal.valueOf(averagePointsByArea.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0)).setScale(2, RoundingMode.CEILING).doubleValue();
    }

}
