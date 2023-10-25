package zpi.ppea.clap.mapstruct.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessorDto {
    private String firstName;
    private String lastName;
    private int assessorId;
}
