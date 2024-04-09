package uk.gov.hmcts.juror.support.sql.v2.flows;

import lombok.extern.slf4j.Slf4j;
import uk.gov.hmcts.juror.support.sql.v2.flows.enums.PoolType;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.PoolRequestDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.RequestPoolControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
public class CreatePool {

    public static PoolRequestDto createPoolOnBehalfOfCourt(String locCode,
                                                   LocalDateTime attendanceDateTime,
                                                   PoolType poolType,
                                                   boolean courtOnly,
                                                   int numberOfJurorsRequired,
                                                   int numberOfDeferralsToInclude) {
        String poolNumber = generatePoolNumber(locCode, attendanceDateTime.toLocalDate());

        PoolRequestDto poolRequestDto = PoolRequestDto.builder()
            .poolNumber(poolNumber)
            .locationCode(locCode)
            .attendanceDate(attendanceDateTime.toLocalDate())
            .numberRequested(numberOfJurorsRequired)
            .poolType(poolType.name())
            .attendanceTime(attendanceDateTime.toLocalTime())
            .deferralsUsed(numberOfDeferralsToInclude)
            .courtOnly(courtOnly)
            .build();


        new RequestPoolControllerClient()
            .createPoolRequest(
                new JwtDetails("400"),
                poolRequestDto
            );
        log.info("Pool created with number: " + poolNumber);
        return poolRequestDto;
    }


    public static String generatePoolNumber(String locCode, LocalDate attendanceDate) {
        return new RequestPoolControllerClient()
            .generatePoolNumber(new JwtDetails("400")
                    .updateOwnerAndLoc(locCode),
                locCode, attendanceDate);
    }
}
