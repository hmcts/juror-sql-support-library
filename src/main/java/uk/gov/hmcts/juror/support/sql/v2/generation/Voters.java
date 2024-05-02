package uk.gov.hmcts.juror.support.sql.v2.generation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.juror.support.generation.generators.value.FixedValueGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromCollectionGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.RegexGeneratorImpl;
import uk.gov.hmcts.juror.support.sql.v1.Util;
import uk.gov.hmcts.juror.support.sql.v2.DataCreator;
import uk.gov.hmcts.juror.support.sql.v2.generation.dto.CourtDetails;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.VotersV2;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.VotersV2Generator;
import uk.gov.hmcts.juror.support.sql.v2.spring.repository.VotersV2Repository;
import uk.gov.hmcts.juror.support.sql.v2.support.Constants;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Voters {

    private final VotersV2Repository votersRepository;

    public void createVoters(List<CourtDetails> courts, int votersPerCourt) {
        RegexGeneratorImpl regexGenerator = new RegexGeneratorImpl(false, Constants.POSTCODE_SUFFIX);
        List<VotersV2> votersV2s = new ArrayList<>();
        for (CourtDetails courtLoc : courts) {
            for (String locCode : courtLoc.getLocCodes()) {
                VotersV2Generator votersGenerator = new VotersV2Generator();
                votersGenerator.setLocCode(new FixedValueGeneratorImpl<>(locCode));
                votersGenerator.setPostcode(new RandomFromCollectionGeneratorImpl<>(courtLoc.getCatchmentAreas()));
                votersGenerator.addPostGenerate(votersV2 -> {
                    votersV2.setPostcode(votersV2.getPostcode() + " " + regexGenerator.generate());
                });
                for (int i = 0; i < votersPerCourt; i++) {
                    votersV2s.add(votersGenerator.generate());
                }
                log.info("Voters generated for court {}", locCode);
            }

        }
        log.info("Generated {} voters", votersV2s.size());
        log.info("Saving voters");
        Util.batchSave(votersRepository, votersV2s, DataCreator.ENV.BATCH_SIZE);
        log.info("Voters saved");
    }
}
