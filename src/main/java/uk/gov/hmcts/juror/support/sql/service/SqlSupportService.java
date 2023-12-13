package uk.gov.hmcts.juror.support.sql.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromCollectionGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.util.RandomGenerator;
import uk.gov.hmcts.juror.support.sql.Constants;
import uk.gov.hmcts.juror.support.sql.Util;
import uk.gov.hmcts.juror.support.sql.dto.JurorAccountDetailsDto;
import uk.gov.hmcts.juror.support.sql.entity.CourtLocation;
import uk.gov.hmcts.juror.support.sql.entity.Juror;
import uk.gov.hmcts.juror.support.sql.entity.JurorGenerator;
import uk.gov.hmcts.juror.support.sql.entity.JurorPool;
import uk.gov.hmcts.juror.support.sql.entity.JurorPoolGenerator;
import uk.gov.hmcts.juror.support.sql.entity.JurorStatus;
import uk.gov.hmcts.juror.support.sql.entity.PoolRequest;
import uk.gov.hmcts.juror.support.sql.entity.PoolRequestGenerator;
import uk.gov.hmcts.juror.support.sql.entity.jurorresponse.AbstractJurorResponse;
import uk.gov.hmcts.juror.support.sql.entity.jurorresponse.AbstractJurorResponseGenerator;
import uk.gov.hmcts.juror.support.sql.entity.jurorresponse.DigitalResponse;
import uk.gov.hmcts.juror.support.sql.entity.jurorresponse.DigitalResponseGenerator;
import uk.gov.hmcts.juror.support.sql.entity.jurorresponse.PaperResponse;
import uk.gov.hmcts.juror.support.sql.entity.jurorresponse.PaperResponseGenerator;
import uk.gov.hmcts.juror.support.sql.generators.DigitalResponseGeneratorUtil;
import uk.gov.hmcts.juror.support.sql.generators.JurorGeneratorUtil;
import uk.gov.hmcts.juror.support.sql.generators.JurorPoolGeneratorUtil;
import uk.gov.hmcts.juror.support.sql.generators.PaperResponseGeneratorUtil;
import uk.gov.hmcts.juror.support.sql.generators.PoolRequestGeneratorUtil;
import uk.gov.hmcts.juror.support.sql.repository.CourtLocationRepository;
import uk.gov.hmcts.juror.support.sql.repository.DigitalResponseRepository;
import uk.gov.hmcts.juror.support.sql.repository.JurorPoolRepository;
import uk.gov.hmcts.juror.support.sql.repository.JurorRepository;
import uk.gov.hmcts.juror.support.sql.repository.PaperResponseRepository;
import uk.gov.hmcts.juror.support.sql.repository.PoolRequestRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class SqlSupportService {
    private final JurorRepository jurorRepository;
    private final PoolRequestRepository poolRequestRepository;
    private final JurorPoolRepository jurorPoolRepository;
    private final DigitalResponseRepository digitalResponseRepository;
    private final PaperResponseRepository paperResponseRepository;
    private StopWatch stopWatch;

    private final CourtLocationRepository courtLocationRepository;


    private static final List<CourtLocation> courtLocations;
    private static final Set<String> courtOwners;
    private static final int BATCH_SIZE = 100;

    static {
        courtLocations = new ArrayList<>();
        courtOwners = new HashSet<>();
    }


    public static List<CourtLocation> getCourtLocations() {
        return Collections.unmodifiableList(courtLocations);
    }

    public static Set<String> getCourtOwners() {
        return Collections.unmodifiableSet(courtOwners);
    }


    @PostConstruct
    //Temporary for testing purposes
    public void postConstruct() {
        this.stopWatch = new StopWatch();
        courtLocationRepository.findAll().forEach(courtLocations::add);
        courtOwners.add("400");
        courtOwners.add("415");
        //TODO add back courtLocations.forEach(courtLocation -> courtOwners.add(courtLocation.getOwner()));
        clearDownDatabase();
        Map<JurorStatus, Integer> jurorStatusCountMapCourt = new EnumMap<>(JurorStatus.class);
        jurorStatusCountMapCourt.put(JurorStatus.DEFERRED, 12585);
        jurorStatusCountMapCourt.put(JurorStatus.DISQUALIFIED, 126);
        jurorStatusCountMapCourt.put(JurorStatus.EXCUSED, 15607);
        jurorStatusCountMapCourt.put(JurorStatus.FAILED_TO_ATTEND, 705);
        jurorStatusCountMapCourt.put(JurorStatus.JUROR, 3916);
        jurorStatusCountMapCourt.put(JurorStatus.PANEL, 472);
        jurorStatusCountMapCourt.put(JurorStatus.REASSIGNED, 41313);
        jurorStatusCountMapCourt.put(JurorStatus.RESPONDED, 208525);
        jurorStatusCountMapCourt.put(JurorStatus.SUMMONED, 56443);
        jurorStatusCountMapCourt.put(JurorStatus.TRANSFERRED, 1075);

        //TODO uncomment createJurorsAssociatedToPools(true, 12, 16, jurorStatusCountMapCourt);

        //TODO remove
        Map<JurorStatus, Integer> jurorStatusCountMapCourtTmp = new EnumMap<>(JurorStatus.class);
        jurorStatusCountMapCourtTmp.put(JurorStatus.SUMMONED, 100);
        jurorStatusCountMapCourtTmp.put(JurorStatus.RESPONDED, 100);
        jurorStatusCountMapCourtTmp.put(JurorStatus.DEFERRED, 100);
        jurorStatusCountMapCourtTmp.put(JurorStatus.EXCUSED, 100);
        //TODO remove end

        //TODO Confirm pool min / max
        createJurorsAssociatedToPools(true, 12, 16, jurorStatusCountMapCourtTmp);

        log.info("DONE: " + stopWatch.prettyPrint());
    }

    private void clearDownDatabase() {
        log.info("Clearing database");
        stopWatch.start("Cleaning database");
        jurorPoolRepository.deleteAll();
        poolRequestRepository.deleteAll();
        digitalResponseRepository.deleteAll();
        paperResponseRepository.deleteAll();
        jurorRepository.deleteAll();
        stopWatch.stop();
        log.info("Clearing database: DONE");
    }


    public void createJurorsAssociatedToPools(boolean isCourtOwned, int jurorsInPoolMinimumInclusive,
                                              int jurorsInPoolMaximumExclusive,
                                              Map<JurorStatus, Integer> jurorStatusCountMap) {
        final int totalCount = jurorStatusCountMap.values().stream().reduce(0, Integer::sum);

        log.info("Creating Jurors save dtos");
        List<JurorAccountDetailsDto> jurorAccountDetailsDtos =
            createJurorAccountDetailsDtos(isCourtOwned, jurorStatusCountMap);
        assert totalCount == jurorAccountDetailsDtos.size();

        log.info("Creating Pool Request's");

        List<JurorPool> needRandomPool = new ArrayList<>();
        List<PoolRequest> pools = new ArrayList<>(totalCount / jurorsInPoolMinimumInclusive);
        PoolRequestGenerator poolRequestGenerator =
            PoolRequestGeneratorUtil.create(isCourtOwned, Constants.POOL_REQUEST_WEIGHT_MAP);

        int remainingJurors = totalCount;
        Collections.shuffle(jurorAccountDetailsDtos); //Ensures pools get a random distribution of juror types
        while (remainingJurors > 0) {
            final int totalJurorsInPool = RandomGenerator.nextInt(
                jurorsInPoolMinimumInclusive, jurorsInPoolMaximumExclusive);
            PoolRequest poolRequest = poolRequestGenerator.generate();
            poolRequest.setTotalNoRequired(RandomGenerator.nextInt(10, 16));
            pools.add(poolRequest);

            List<JurorAccountDetailsDto> selectedJurors = jurorAccountDetailsDtos.subList(
                Math.max(0, remainingJurors - totalJurorsInPool),
                remainingJurors);

            selectedJurors.parallelStream().forEach(juror -> {
                    String firstJurorPoolOwner = null;
                    List<JurorPool> jurorPools = juror.getJurorPools();

                    for (JurorPool jurorPool : jurorPools) {
                        if (firstJurorPoolOwner == null) {
                            firstJurorPoolOwner = jurorPool.getOwner();
                        }
                        //Set owner so we know which pool this juror can not be in
                        jurorPool.setOwner(jurorPool.getOwner());
                        if (jurorPool.getOwner().equals(firstJurorPoolOwner)) {
                            jurorPool.setPoolNumber(poolRequest.getPoolNumber());
                            jurorPool.setStartDate(poolRequest.getReturnDate());
                        } else {
                            needRandomPool.add(jurorPool);
                        }
                    }
                }
            );
            remainingJurors -= totalJurorsInPool;
        }
        reassignJurorPoolsToRandomPoolWithNewOwner(needRandomPool, pools);

        stopWatch.start("Saving Pool Request's");
        log.info("Saving Pool Request's to database");
        Util.batchSave(poolRequestRepository, pools, BATCH_SIZE);
        stopWatch.stop();

        saveJurorAccountDetailsDtos(jurorAccountDetailsDtos);
    }

    private void saveJurorAccountDetailsDtos(List<JurorAccountDetailsDto> jurorAccountDetailsDtos) {
        stopWatch.start("Saving Juror's");
        log.info("Saving Juror's to database");
        Util.batchSave(jurorRepository,
            jurorAccountDetailsDtos.stream().map(JurorAccountDetailsDto::getJuror).toList(), BATCH_SIZE);
        stopWatch.stop();

        stopWatch.start("Saving Juror Paper Response's");
        log.info("Saving Juror Paper Response's");
        Util.batchSave(paperResponseRepository,
            jurorAccountDetailsDtos.stream().map(JurorAccountDetailsDto::getJurorResponse)
                .filter(PaperResponse.class::isInstance)
                .map(PaperResponse.class::cast)
                .toList(), BATCH_SIZE);
        stopWatch.stop();

        stopWatch.start("Saving Juror Digital Response's");
        log.info("Saving Juror Digital Response's");
        Util.batchSave(digitalResponseRepository,
            jurorAccountDetailsDtos.stream().map(JurorAccountDetailsDto::getJurorResponse)
                .filter(DigitalResponse.class::isInstance)
                .map(DigitalResponse.class::cast)
                .toList(), BATCH_SIZE);
        stopWatch.stop();

        stopWatch.start("Saving Juror Pool's");
        log.info("Saving Juror Pool's to database");
        Util.batchSave(jurorPoolRepository,
            jurorAccountDetailsDtos.stream().map(JurorAccountDetailsDto::getJurorPools).flatMap(List::stream).toList(),
            BATCH_SIZE);
        stopWatch.stop();
    }


    private void addSummonsReplyToJurorAccountDto(int digitalWeight, int paperWeight, JurorStatus jurorStatus,
                                                  List<JurorAccountDetailsDto> jurors) {
        stopWatch.start("Creating Summons replies from jurors");
        log.info("Creating {} juror responses from jurors", jurors.size());
        DigitalResponseGenerator digitalResponseGenerator = DigitalResponseGeneratorUtil.create(jurorStatus);
        PaperResponseGenerator paperResponseGenerator = PaperResponseGeneratorUtil.create(jurorStatus);

        Map<AbstractJurorResponseGenerator<?>, Integer> weightMap = Map.of(
            digitalResponseGenerator, digitalWeight,
            paperResponseGenerator, paperWeight
        );
        jurors.forEach(jurorAccountDetailsDto -> {
            AbstractJurorResponseGenerator<?> generator = Util.getWeightedRandomItem(weightMap);
            AbstractJurorResponse jurorResponse = generator.generate();
            mapJurorToJurorResponse(jurorAccountDetailsDto.getJuror(), jurorResponse);
            jurorAccountDetailsDto.setJurorResponse(jurorResponse);
        });
        stopWatch.stop();
    }


    private void mapJurorToJurorResponse(final Juror juror, final AbstractJurorResponse jurorResponse) {
        jurorResponse.setJurorNumber(juror.getJurorNumber());
        jurorResponse.setTitle(juror.getTitle());
        jurorResponse.setFirstName(juror.getFirstName());
        jurorResponse.setLastName(juror.getLastName());
        jurorResponse.setDateOfBirth(juror.getDateOfBirth());
        jurorResponse.setPhoneNumber(juror.getPhoneNumber());
        jurorResponse.setAltPhoneNumber(juror.getAltPhoneNumber());
        jurorResponse.setEmail(juror.getEmail());

        jurorResponse.setAddressLine1(juror.getAddressLine1());
        jurorResponse.setAddressLine2(juror.getAddressLine2());
        jurorResponse.setAddressLine3(juror.getAddressLine3());
        jurorResponse.setAddressLine4(juror.getAddressLine4());
        jurorResponse.setAddressLine5(juror.getAddressLine5());
        jurorResponse.setPostcode(juror.getPostcode());
    }

    private List<JurorAccountDetailsDto> createJurorAccountDetailsDtos(boolean isCourtOwned,
                                                                       Map<JurorStatus, Integer> jurorStatusCountMap) {

        List<JurorAccountDetailsDto> jurorAccountDetailsDtos = jurorStatusCountMap.entrySet().stream()
            .map(entity -> {
                    List<JurorAccountDetailsDto> jurors = createJurors(isCourtOwned, entity.getKey(),
                        entity.getValue());
                    addJurorPoolsToJurorAccountDto(isCourtOwned, entity.getKey(), jurors);
                    addSummonsReplyToJurorAccountDto(66, 34,
                        entity.getKey(), jurors);
                    return jurors;
                }
            )
            .flatMap(List::stream)
            .collect(Collectors.toCollection(ArrayList::new));

        Collections.shuffle(jurorAccountDetailsDtos);
        return jurorAccountDetailsDtos;
    }


    private void reassignJurorPoolsToRandomPoolWithNewOwner(List<JurorPool> needRandomPool, List<PoolRequest> pools) {
        RandomFromCollectionGeneratorImpl<PoolRequest> randomPoolRequestGenerator =
            new RandomFromCollectionGeneratorImpl<>(pools);

        for (JurorPool jurorPool : needRandomPool) {
            PoolRequest poolRequest;
            //Get a pool request which is not the current owner (ensures a new pool is assigned)
            do {
                poolRequest = randomPoolRequestGenerator.generate();
            } while (poolRequest.getOwner().equals(jurorPool.getOwner()));
            jurorPool.setOwner(poolRequest.getOwner());
            jurorPool.setPoolNumber(poolRequest.getPoolNumber());
            jurorPool.setStartDate(poolRequest.getReturnDate());
        }
    }


    private void addJurorPoolsToJurorAccountDto(boolean isCourtOwned, JurorStatus jurorStatus,
                                                List<JurorAccountDetailsDto> jurors) {
        stopWatch.start("Creating Juror Pools for status: " + jurorStatus);
        log.info("Creating juror pools with status {} for {} jurors", jurorStatus, jurors.size());

        final JurorPoolGenerator jurorPoolGenerator = JurorPoolGeneratorUtil.create(isCourtOwned, jurorStatus);
        final JurorPoolGenerator respondedJurorPoolGenerator = JurorPoolGeneratorUtil.create(isCourtOwned,
            JurorStatus.RESPONDED);


        BiFunction<Juror, JurorPool, List<JurorPool>> jurorPoolStatusConsumer = switch (jurorStatus) {
            case TRANSFERRED -> (juror, jurorPool) -> {
                JurorPool transferredToPool = respondedJurorPoolGenerator.generate();
                transferredToPool.setJurorNumber(juror.getJurorNumber());
                transferredToPool.setOwner(Util.getRandomItem(getCourtOwners(), List.of(jurorPool.getOwner())));
                return List.of(transferredToPool);
            };
            case REASSIGNED -> (juror, jurorPool) -> {
                JurorPool reassignedToPool = respondedJurorPoolGenerator.generate();
                reassignedToPool.setJurorNumber(juror.getJurorNumber());
                reassignedToPool.setOwner(jurorPool.getOwner());
                return List.of(reassignedToPool);
            };
            default -> (juror, jurorPool) -> List.of();
        };

        jurors.forEach(jurorAccountDetailsDto -> jurorAccountDetailsDto.setJurorPools(
            createJurorPoolsForJuror(
                jurorAccountDetailsDto.getJuror(),
                jurorPoolStatusConsumer,
                jurorPoolGenerator
            )
        ));
        stopWatch.stop();
    }

    private List<JurorPool> createJurorPoolsForJuror(Juror juror,
                                                     BiFunction<Juror, JurorPool, List<JurorPool>> jurorPoolStatusConsumer,
                                                     JurorPoolGenerator jurorPoolGenerator) {
        List<JurorPool> jurorPools = new ArrayList<>();
        JurorPool jurorPool = jurorPoolGenerator.generate();
        jurorPool.setJurorNumber(juror.getJurorNumber());

        jurorPools.add(jurorPool);
        jurorPools.addAll(jurorPoolStatusConsumer.apply(juror, jurorPool));
        return jurorPools;
    }


    private List<JurorAccountDetailsDto> createJurors(boolean isCourtOwned, JurorStatus jurorStatus, int count) {
        stopWatch.start("Creating Jurors");
        log.info("Creating {} jurors with status {}", count, jurorStatus);
        List<JurorAccountDetailsDto> jurors = new ArrayList<>(count);
        JurorGenerator jurorGenerator = JurorGeneratorUtil.create(isCourtOwned, jurorStatus);
        for (int i = 0; i < count; i++) {
            jurors.add(new JurorAccountDetailsDto(jurorGenerator.generate()));
        }
        stopWatch.stop();
        return jurors;
    }
}
