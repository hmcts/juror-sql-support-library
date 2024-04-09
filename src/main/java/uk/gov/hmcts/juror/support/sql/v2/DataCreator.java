package uk.gov.hmcts.juror.support.sql.v2;


import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.juror.support.generation.generators.value.FixedValueGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromCollectionGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromCollectionGeneratorWeightedImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.RegexGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.util.RandomGenerator;
import uk.gov.hmcts.juror.support.sql.v1.Util;
import uk.gov.hmcts.juror.support.sql.v1.entity.JurorPool;
import uk.gov.hmcts.juror.support.sql.v1.repository.JurorPoolRepository;
import uk.gov.hmcts.juror.support.sql.v2.flows.CreatePool;
import uk.gov.hmcts.juror.support.sql.v2.flows.enums.PoolType;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.PoolCreateRequestDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.PoolRequestDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.CreatePoolControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.VotersV2;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.VotersV2Generator;
import uk.gov.hmcts.juror.support.sql.v2.spring.repository.VotersV2Repository;
import uk.gov.hmcts.juror.support.sql.v2.support.Constants;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class DataCreator {
    public static RestTemplateBuilder restTemplateBuilder;
    public static String jwtSecret;
    private final VotersV2Repository votersRepository;
    private final JurorPoolRepository jurorPoolRepository;
    private static final int BATCH_SIZE = 500;


    @Autowired
    public DataCreator(RestTemplateBuilder restTemplateBuilder,
                       @Value("${uk.gov.hmcts.juror.security.secret}")
                       String jwtSecret,
                       VotersV2Repository votersRepository,
                       JurorPoolRepository jurorPoolRepository) {
        DataCreator.restTemplateBuilder = restTemplateBuilder;
        DataCreator.jwtSecret = jwtSecret;
        this.votersRepository = votersRepository;
        this.jurorPoolRepository = jurorPoolRepository;
    }

    @AllArgsConstructor
    static class CourtLoc {
        String courtCode;
        List<String> locCodes;

        List<String> catchmentAreas;
    }

    @PostConstruct
    @Transactional
    public void init() {
        try {
            log.info("DataCreator created");
            final List<CourtLoc> courts = new ArrayList<>();
            courts.add(new CourtLoc("433", List.of("433"), List.of("AA1", "AA2", "AA3")));
            courts.add(new CourtLoc("411", List.of("411", "441"), List.of("BB1", "BB2", "BB3")));
            courts.add(new CourtLoc("426", List.of("426", "754"), List.of("CC1", "CC2", "CC3")));
            courts.add(new CourtLoc("419", List.of("419"), List.of("DD1", "DD2", "DD3")));
            courts.add(new CourtLoc("454", List.of("454"), List.of("EE1", "EE2", "EE3")));
            courts.add(new CourtLoc("412", List.of("412"), List.of("FF1", "FF2", "FF3")));
            courts.add(new CourtLoc("432", List.of("432", "249"), List.of("GG1", "GG2", "GG3")));
            courts.add(new CourtLoc("445", List.of("445"), List.of("HH1", "HH2", "HH3")));
            courts.add(new CourtLoc("470", List.of("470"), List.of("KK1", "KK2", "KK3")));
            courts.add(new CourtLoc("415", List.of("415", "462", "767"), List.of("LL1", "LL2", "LL3")));
            courts.add(new CourtLoc("423", List.of("423", "750"), List.of("MM1", "MM2", "MM3")));
            courts.add(new CourtLoc("450", List.of("450"), List.of("NN1", "NN2", "NN3")));
            courts.add(new CourtLoc("451", List.of("451"), List.of("OO1", "OO2", "OO3")));
            courts.add(new CourtLoc("472", List.of("472"), List.of("PP1", "PP2", "PP3")));
            courts.add(new CourtLoc("471", List.of("471", "464"), List.of("RR1", "RR2", "RR3")));

//        createVoters(courts, (100 * 1000));
//add some nil pools
            RandomFromCollectionGeneratorWeightedImpl<PoolType> poolTypeGenerator =
                new RandomFromCollectionGeneratorWeightedImpl<>(Map.of(
                    PoolType.CRO, 0.85,
                    PoolType.CIV, 0.10,
                    PoolType.HGH, 0.05
                ));

            final int numberOfJurorsPerPool = 10;
            final int totalPool = 10;

            for (int index = 0; index < totalPool; index++) {
                CourtLoc courtLoc = courts.get(index % courts.size());
                String locCode = courtLoc.locCodes.get(index % (courtLoc.locCodes.size()));


                PoolRequestDto poolRequestDto = CreatePool.createPoolOnBehalfOfCourt(
                    locCode,
                    LocalDateTime.of(2024, (index % 6) + 1, (index % 28) + 1, 8, 30),
                    poolTypeGenerator.generate(),
                    false,
                    numberOfJurorsPerPool,
                    0
                );
                //5% change to not summon jurors
                if (RandomGenerator.nextInt(0, 100) > 5) {
                    summonJurors(courtLoc, poolRequestDto, numberOfJurorsPerPool);
                }
            }

        }catch (Throwable throwable){
            throwable.printStackTrace();
            throw  throwable;
        }
    }

    private void summonJurors(CourtLoc courtLoc, PoolRequestDto poolRequestDto, int numToSummon) {
        log.info("Summoning jurors for pool number {}", poolRequestDto.getPoolNumber());
        List<String> postCodes = new ArrayList<>();
        int size = 1;
        while (size <= courtLoc.catchmentAreas.size()) {
            if (RandomGenerator.nextBoolean()) {
                postCodes.add(courtLoc.catchmentAreas.get(size - 1));
            }
            size++;
            if (size == courtLoc.catchmentAreas.size() && postCodes.isEmpty()) {
                size = 1;
            }
        }

        new CreatePoolControllerClient()
            .createPoolRequest(
                new JwtDetails("400"),
                PoolCreateRequestDto.builder()
                    .poolNumber(poolRequestDto.getPoolNumber())
                    .startDate(poolRequestDto.getAttendanceDate())
                    .attendTime(
                        LocalDateTime.of(poolRequestDto.getAttendanceDate(), poolRequestDto.getAttendanceTime()))
                    .noRequested(numToSummon)
                    .bureauDeferrals(0)//TODO
                    .numberRequired(numToSummon)
                    .citizensToSummon(numToSummon)
                    .catchmentArea(poolRequestDto.getLocationCode())
                    .postcodes(postCodes)
                    .build());
        enterSummonsReply(jurorPoolRepository.findAllByPoolNumber(poolRequestDto.getPoolNumber()));
    }

    private void enterSummonsReply(List<JurorPool> jurorPools) {
        //TODO digital response

        //Paper Response
        ///api/v1/moj/juror-paper-response/response


    }

    private void createVoters(List<CourtLoc> courts, int votersPerCourt) {
        RegexGeneratorImpl regexGenerator = new RegexGeneratorImpl(false, Constants.POSTCODE_SUFFIX);
        List<VotersV2> votersV2s = new ArrayList<>();
        for (CourtLoc courtLoc : courts) {
            for (String locCode : courtLoc.locCodes) {
                VotersV2Generator votersGenerator = new VotersV2Generator();
                votersGenerator.setLocCode(new FixedValueGeneratorImpl<>(locCode));
                votersGenerator.setPostcode(new RandomFromCollectionGeneratorImpl<>(courtLoc.catchmentAreas));
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
        Util.batchSave(votersRepository, votersV2s, BATCH_SIZE);
//        votersRepository.saveAll(votersV2s);
        log.info("Voters saved");
    }
}
