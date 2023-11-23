package zpi.ppea.clap.dtos;

import data_store.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyVisitDto {
    private List<Question> questions;
}
