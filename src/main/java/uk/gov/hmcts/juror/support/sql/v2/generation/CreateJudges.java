package uk.gov.hmcts.juror.support.sql.v2.generation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.juror.support.generation.generators.code.Generator;
import uk.gov.hmcts.juror.support.generation.generators.value.FirstNameGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromCollectionGeneratorImpl;
import uk.gov.hmcts.juror.support.sql.v2.DataCreator;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.administration.JudgeCreateDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.AdministrationJudgeControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generation.dto.CourtDetails;
import uk.gov.hmcts.juror.support.sql.v2.generation.dto.User;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetailsBureau;
import uk.gov.hmcts.juror.support.sql.v2.support.Role;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CreateJudges {

    private static final FirstNameGeneratorImpl firstNameGenerator = new FirstNameGeneratorImpl();

    public void createJudges(int countPerCourt) {
        for (CourtDetails courtLoc : DataCreator.ENV.getCourts()) {
            Generator<User> userGenerator = new RandomFromCollectionGeneratorImpl<>(
                courtLoc.getUsernames()
            );
            String prefix = courtLoc.getCatchmentAreas().get(0).substring(0, 2);
            for (int i = 0; i < countPerCourt; i++) {
                new AdministrationJudgeControllerClient()
                    .createJudgeDetails(
                        new JwtDetailsBureau(userGenerator.generate())
                            .addRole(Role.MANAGER),
                        JudgeCreateDto.builder()
                            .judgeCode(prefix + i)
                            .judgeName(firstNameGenerator.generate())
                            .build());
            }
        }
    }
}
