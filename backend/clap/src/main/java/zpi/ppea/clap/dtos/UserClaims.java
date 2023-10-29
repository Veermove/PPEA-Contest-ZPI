package zpi.ppea.clap.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserClaims {
    private String firstName;
    private String lastName;
    private Integer assessorId;
    private Integer personId;
    private Integer awardsRepresentativeId;
    private Integer juryMemberId;
    private Integer ipmaExpertId;
    private Integer applicantId;
}
