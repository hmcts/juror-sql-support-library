package uk.gov.hmcts.juror.support.sql.v2.generation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.juror.support.generation.generators.code.Generator;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromCollectionGeneratorWeightedImpl;
import uk.gov.hmcts.juror.support.sql.v1.entity.PoliceCheck;
import uk.gov.hmcts.juror.support.sql.v2.DataCreator;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.PoliceCheckStatusDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.JurorRecordControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.RequirePncCheckViewEntity;
import uk.gov.hmcts.juror.support.sql.v2.spring.repository.RequirePncCheckViewRepository;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetailsBureau;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UpdatePoliceChecks {

    private final RequirePncCheckViewRepository requirePncCheckViewRepository;


    public void updatePoliceChecks() {
        log.info("Assigning Police checks");
        JurorRecordControllerClient jurorRecordControllerClient = new JurorRecordControllerClient();

        Generator<PoliceCheck> policeCheckGenerator = new RandomFromCollectionGeneratorWeightedImpl<>(
            Map.of(
                PoliceCheck.ELIGIBLE, 0.85,
                PoliceCheck.INELIGIBLE, 0.10,
                PoliceCheck.UNCHECKED_MAX_RETRIES_EXCEEDED, 0.45,
                PoliceCheck.ERROR_RETRY_UNEXPECTED_EXCEPTION, 0.05
            ));


        List<RequirePncCheckViewEntity> requirePncCheckViewEntities =
            requirePncCheckViewRepository.findAllByPoliceCheckIs(PoliceCheck.NOT_CHECKED);

        requirePncCheckViewEntities
            .parallelStream()
            .forEach(pncCheckViewEntity -> {
                try {
                    jurorRecordControllerClient.updatePncCheckStatus(
                        new JwtDetailsBureau(DataCreator.ENV.getSystemUser()),
                        pncCheckViewEntity.getJurorNumber(),
                        new PoliceCheckStatusDto(policeCheckGenerator.generate())
                    );
                } catch (Throwable throwable) {
                    log.error("Error updating police check for juror: " + pncCheckViewEntity.getJurorNumber(),
                        throwable);
                }
            });
    }
}
