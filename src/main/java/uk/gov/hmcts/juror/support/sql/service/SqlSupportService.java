package uk.gov.hmcts.juror.support.sql.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import uk.gov.hmcts.juror.support.generation.generators.value.FixedValueGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.NullValueGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromCollectionGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.util.RandomGenerator;
import uk.gov.hmcts.juror.support.sql.Constants;
import uk.gov.hmcts.juror.support.sql.Util;
import uk.gov.hmcts.juror.support.sql.dto.CreateJurorPoolRequest;
import uk.gov.hmcts.juror.support.sql.dto.CreatePopulatedPoolRequest;
import uk.gov.hmcts.juror.support.sql.entity.CourtLocation;
import uk.gov.hmcts.juror.support.sql.entity.Juror;
import uk.gov.hmcts.juror.support.sql.entity.JurorGenerator;
import uk.gov.hmcts.juror.support.sql.entity.JurorPool;
import uk.gov.hmcts.juror.support.sql.entity.JurorPoolGenerator;
import uk.gov.hmcts.juror.support.sql.entity.JurorStatus;
import uk.gov.hmcts.juror.support.sql.entity.PoolRequest;
import uk.gov.hmcts.juror.support.sql.entity.PoolRequestGenerator;
import uk.gov.hmcts.juror.support.sql.generators.JurorGeneratorUtil;
import uk.gov.hmcts.juror.support.sql.generators.JurorPoolGeneratorUtil;
import uk.gov.hmcts.juror.support.sql.generators.PoolRequestGeneratorUtil;
import uk.gov.hmcts.juror.support.sql.repository.CourtLocationRepository;
import uk.gov.hmcts.juror.support.sql.repository.JurorPoolRepository;
import uk.gov.hmcts.juror.support.sql.repository.JurorRepository;
import uk.gov.hmcts.juror.support.sql.repository.PoolRequestRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
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
    private StopWatch stopWatch;

    private final CourtLocationRepository courtLocationRepository;


    private static final List<CourtLocation> courtLocations;
    private static final Set<String> courtOwners;

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
    void postConstruct() {
        this.stopWatch = new StopWatch();
        courtLocationRepository.findAll().forEach(courtLocations::add);
        courtLocations.forEach(courtLocation -> courtOwners.add(courtLocation.getOwner()));
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

        createJurorsAssociatedToPools(true, 12, 16, jurorStatusCountMapCourt);

        log.info("DONE: " + stopWatch.prettyPrint());
    }

    private void clearDownDatabase() {
        log.info("Cleaning database");
        stopWatch.start("Cleaning database");
        jurorPoolRepository.deleteAll();
        poolRequestRepository.deleteAll();
        jurorRepository.deleteAll();
        stopWatch.stop();
        log.info("Cleaning database: DONE");
    }


    public void createJurorsAssociatedToPools(boolean isCourtOwned, int jurorsInPoolMinimumInclusive,
                                              int jurorsInPoolMaximumExclusive,
                                              Map<JurorStatus, Integer> jurorStatusCountMap) {
        final int totalCount = jurorStatusCountMap.values().stream().reduce(0, Integer::sum);

        List<PoolRequest> pools = new ArrayList<>(totalCount / jurorsInPoolMinimumInclusive);

        PoolRequestGenerator poolRequestGenerator =
            PoolRequestGeneratorUtil.create(isCourtOwned, Constants.POOL_REQUEST_WEIGHT_MAP);

        int remainingJurors = totalCount;


        log.info("Creating Jurors & Pools");
        Map<Juror, List<JurorPool>> jurorToJurorPoolMap = createJurorAndJurorPools(isCourtOwned, jurorStatusCountMap);
        log.info("Creating Pool Request's");
        List<Juror> allJurors = new ArrayList<>(jurorToJurorPoolMap.keySet());
        List<JurorPool> needRandomPool = new ArrayList<>();

        while (remainingJurors > 0) {
            final int totalJurorsInPool = RandomGenerator.nextInt(
                jurorsInPoolMinimumInclusive, jurorsInPoolMaximumExclusive);
            PoolRequest poolRequest = poolRequestGenerator.generate();
            poolRequest.setTotalNoRequired(RandomGenerator.nextInt(10, 16));
            pools.add(poolRequest);

            List<Juror> selectedJurors = allJurors.subList(
                Math.max(0, remainingJurors - totalJurorsInPool),
                remainingJurors);

            selectedJurors.parallelStream().forEach(juror -> {
                    String firstJurorPoolOwner = null;
                    List<JurorPool> jurorPools = jurorToJurorPoolMap.get(juror);

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

        stopWatch.start("Saving Juror's");
        log.info("Saving Juror's to database");
        Util.batchSave(jurorRepository, allJurors, 100);
        stopWatch.stop();

        stopWatch.start("Saving Pool Request's");
        log.info("Saving Pool Request's to database");
        Util.batchSave(poolRequestRepository, pools, 100);
        stopWatch.stop();

        stopWatch.start("Saving Juror Pool's");
        log.info("Saving Juror Pool's to database");
        Util.batchSave(jurorPoolRepository, jurorToJurorPoolMap.values().stream().flatMap(List::stream).toList(), 100);
        stopWatch.stop();
    }

    public Map<Juror, List<JurorPool>> createJurorAndJurorPools(boolean isCourtOwned,
                                                                Map<JurorStatus, Integer> jurorStatusCountMap) {
        return jurorStatusCountMap.entrySet().stream()
            .map(entity -> createBaseJurorPoolFromStatus(
                isCourtOwned, entity.getKey(),
                createJurors(isCourtOwned, entity.getKey(), entity.getValue()))
                .entrySet()
            )
            .flatMap(Set::stream)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void reassignJurorPoolsToRandomPoolWithNewOwner(List<JurorPool> needRandomPool, List<PoolRequest> pools) {
        for (JurorPool jurorPool : needRandomPool) {
            RandomFromCollectionGeneratorImpl<PoolRequest> randomPoolRequestGenerator =
                new RandomFromCollectionGeneratorImpl<>(pools);

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

    private Map<Juror, List<JurorPool>> createBaseJurorPoolFromStatus(boolean isCourtOwned, JurorStatus jurorStatus,
                                                                      List<Juror> jurors) {
        stopWatch.start("Creating Juror Pools for status: " + jurorStatus);
        log.info("Creating juror pools with status {} for {} jurors", jurorStatus, jurors.size());

        final Map<Juror, List<JurorPool>> jurorToJurorPools = new HashMap<>();
        final JurorPoolGenerator jurorPoolGenerator = JurorPoolGeneratorUtil.create(isCourtOwned, jurorStatus);


        final JurorPoolGenerator respondeedJurorPoolGenerator = JurorPoolGeneratorUtil.create(isCourtOwned,
            JurorStatus.RESPONDED);

        BiFunction<Juror, JurorPool, List<JurorPool>> jurorPoolStatusConsumer = switch (jurorStatus) {
            case TRANSFERRED -> (juror, jurorPool) -> {
                JurorPool transferredToPool = respondeedJurorPoolGenerator.generate();
                transferredToPool.setJurorNumber(juror.getJurorNumber());
                transferredToPool.setOwner(Util.getRandomItem(getCourtOwners(), List.of(jurorPool.getOwner())));
                return List.of(transferredToPool);
            };
            case REASSIGNED -> (juror, jurorPool) -> {
                JurorPool reassignedToPool = respondeedJurorPoolGenerator.generate();
                reassignedToPool.setJurorNumber(juror.getJurorNumber());
                reassignedToPool.setOwner(jurorPool.getOwner());
                return List.of(reassignedToPool);
            };
            default -> (juror, jurorPool) -> List.of();
        };

        for (Juror juror : jurors) {
            List<JurorPool> jurorPools = new ArrayList<>();
            JurorPool jurorPool = jurorPoolGenerator.generate();
            jurorPool.setJurorNumber(juror.getJurorNumber());

            jurorPools.add(jurorPool);
            jurorPools.addAll(jurorPoolStatusConsumer.apply(juror, jurorPool));
            jurorToJurorPools.put(juror, jurorPools);
        }

        stopWatch.stop();
        return jurorToJurorPools;
    }

    private List<JurorPool> createBaseJurorPoolFromStatus(boolean isCourtOwned, JurorStatus jurorStatus, int count) {
        stopWatch.start("Creating Juror Pools for status: " + jurorStatus);
        log.info("Creating {} juror pools with status {}", count, jurorStatus);
        final JurorPoolGenerator jurorPoolGenerator = JurorPoolGeneratorUtil.create(isCourtOwned, jurorStatus);
        final List<JurorPool> jurorPools = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            jurorPools.add(jurorPoolGenerator.generate());
        }
        stopWatch.stop();
        return jurorPools;
    }

    private List<Juror> createJurors(boolean isCourtOwned, JurorStatus jurorStatus, int count) {
        stopWatch.start("Creating Jurors");
        log.info("Creating {} jurors with status {}", count, jurorStatus);
        List<Juror> jurors = new ArrayList<>(count);
        JurorGenerator jurorGenerator = JurorGeneratorUtil.create(isCourtOwned, jurorStatus);
        for (int i = 0; i < count; i++) {
            jurors.add(jurorGenerator.generate());
        }
        stopWatch.stop();
        return jurors;
    }


    public List<JurorPool> createPopulatedPool(CreatePopulatedPoolRequest populatedPoolRequest) {
        final PoolRequest pool = populatedPoolRequest.getPoolRequestGenerator().generate();
        poolRequestRepository.save(pool);

        List<JurorPool> jurorPools = new ArrayList<>();
        populatedPoolRequest.getJurorCountMap()
            .forEach((key, value) -> jurorPools.addAll(createJurorPools(pool, key, value)));
        return jurorPools;
    }

    public List<JurorPool> createJurorPools(PoolRequest pool, CreateJurorPoolRequest jurorPoolRequest, int count) {
        stopWatch.start("Creating Juror Pools");
        final List<Juror> jurors = new ArrayList<>(count);
        final List<JurorPool> jurorPools = new ArrayList<>(count);


        final JurorPoolGenerator jurorPoolGenerator = jurorPoolRequest.getJurorPoolGenerator();
        jurorPoolGenerator.setPoolNumber(new FixedValueGeneratorImpl<>(pool.getPoolNumber()));
        jurorPoolGenerator.setOwner(new FixedValueGeneratorImpl<>(pool.getOwner()));
        jurorPoolGenerator.setStartDate(new FixedValueGeneratorImpl<>(pool.getReturnDate()));
        jurorPoolGenerator.setJurorNumber(new NullValueGeneratorImpl<>());

        for (int i = 0; i < count; i++) {
            Juror juror = jurorPoolRequest.getJurorGenerator().generate();
            jurors.add(juror);
            JurorPool jurorPool = jurorPoolGenerator.generate();
            jurorPool.setJurorNumber(juror.getJurorNumber());
            jurorPools.add(jurorPool);
        }
        jurorRepository.saveAll(jurors);
        jurorPoolRepository.saveAll(jurorPools);
        stopWatch.stop();
        return jurorPools;
    }
}
