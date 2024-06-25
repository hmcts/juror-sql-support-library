package uk.gov.hmcts.juror.support.sql.v2.generation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.juror.support.generation.generators.code.Generator;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromCollectionGeneratorImpl;
import uk.gov.hmcts.juror.support.sql.v2.DataCreator;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.administration.CourtRoomDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.AdministrationCourtRoomControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generation.dto.CourtDetails;
import uk.gov.hmcts.juror.support.sql.v2.generation.dto.User;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetailsBureau;
import uk.gov.hmcts.juror.support.sql.v2.support.Role;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CreateCourtRooms {


    public void createCourtrooms(int countPerCourt) {
        for (CourtDetails courtLoc : DataCreator.ENV.getCourts()) {
            Generator<User> userGenerator = new RandomFromCollectionGeneratorImpl<>(courtLoc.getUsernames());
            for (String locCode : courtLoc.getLocCodes()) {
                for (int i = 0; i < countPerCourt; i++) {
                    new AdministrationCourtRoomControllerClient()
                        .createCourtRoom(
                            new JwtDetailsBureau(userGenerator.generate())
                                .addRole(Role.MANAGER)
                                .updateOwnerAndLoc(locCode),
                            locCode,
                            CourtRoomDto.builder()
                                .roomName(locCode + " " + i)
                                .roomDescription(locCode + " Room " + i + " desc")
                                .build());
                }
            }
        }
    }
}
