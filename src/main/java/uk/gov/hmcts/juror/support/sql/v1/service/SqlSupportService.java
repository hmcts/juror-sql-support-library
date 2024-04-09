package uk.gov.hmcts.juror.support.sql.v1.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromCollectionGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.util.RandomGenerator;
import uk.gov.hmcts.juror.support.sql.v1.Constants;
import uk.gov.hmcts.juror.support.sql.v1.Util;
import uk.gov.hmcts.juror.support.sql.v1.dto.JurorAccountDetailsDto;
import uk.gov.hmcts.juror.support.sql.v1.entity.CourtLocation;
import uk.gov.hmcts.juror.support.sql.v1.entity.Juror;
import uk.gov.hmcts.juror.support.sql.v1.entity.JurorGenerator;
import uk.gov.hmcts.juror.support.sql.v1.entity.JurorPool;
import uk.gov.hmcts.juror.support.sql.v1.entity.JurorPoolGenerator;
import uk.gov.hmcts.juror.support.sql.v1.entity.JurorStatus;
import uk.gov.hmcts.juror.support.sql.v1.entity.PoolRequest;
import uk.gov.hmcts.juror.support.sql.v1.entity.PoolRequestGenerator;
import uk.gov.hmcts.juror.support.sql.v1.entity.User;
import uk.gov.hmcts.juror.support.sql.v1.entity.UserGenerator;
import uk.gov.hmcts.juror.support.sql.v1.entity.jurorresponse.AbstractJurorResponse;
import uk.gov.hmcts.juror.support.sql.v1.entity.jurorresponse.AbstractJurorResponseGenerator;
import uk.gov.hmcts.juror.support.sql.v1.entity.jurorresponse.DigitalResponse;
import uk.gov.hmcts.juror.support.sql.v1.entity.jurorresponse.DigitalResponseGenerator;
import uk.gov.hmcts.juror.support.sql.v1.entity.jurorresponse.PaperResponse;
import uk.gov.hmcts.juror.support.sql.v1.entity.jurorresponse.PaperResponseGenerator;
import uk.gov.hmcts.juror.support.sql.v1.generators.DigitalResponseGeneratorUtil;
import uk.gov.hmcts.juror.support.sql.v1.generators.JurorGeneratorUtil;
import uk.gov.hmcts.juror.support.sql.v1.generators.JurorPoolGeneratorUtil;
import uk.gov.hmcts.juror.support.sql.v1.generators.PaperResponseGeneratorUtil;
import uk.gov.hmcts.juror.support.sql.v1.generators.PoolRequestGeneratorUtil;
import uk.gov.hmcts.juror.support.sql.v1.generators.UserGeneratorUtil;
import uk.gov.hmcts.juror.support.sql.v1.generators.VotersGeneratorUtil;
import uk.gov.hmcts.juror.support.sql.v1.repository.CourtLocationRepository;
import uk.gov.hmcts.juror.support.sql.v1.repository.DigitalResponseRepository;
import uk.gov.hmcts.juror.support.sql.v1.repository.JurorPoolRepository;
import uk.gov.hmcts.juror.support.sql.v1.repository.JurorRepository;
import uk.gov.hmcts.juror.support.sql.v1.repository.PaperResponseRepository;
import uk.gov.hmcts.juror.support.sql.v1.repository.PoolRequestRepository;
import uk.gov.hmcts.juror.support.sql.v1.repository.UserRepository;
import uk.gov.hmcts.juror.support.sql.v1.repository.VotersRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.ObjIntConsumer;
import java.util.stream.Collectors;

//TODO commented out to disable load @Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class SqlSupportService {
    private final JurorRepository jurorRepository;
    private final PoolRequestRepository poolRequestRepository;
    private final JurorPoolRepository jurorPoolRepository;
    private final DigitalResponseRepository digitalResponseRepository;
    private final PaperResponseRepository paperResponseRepository;
    private final UserRepository userRepository;
    private final VotersRepository votersRepository;
    private StopWatch stopWatch;

    private final CourtLocationRepository courtLocationRepository;


    private static final List<CourtLocation> courtLocations;
    private static final List<User> users;
    private static final Map<String, List<User>> userMap;
    private static final Set<String> courtOwners;
    private static final int BATCH_SIZE = 100;

    static {
        courtLocations = new ArrayList<>();
        courtOwners = new HashSet<>();
        users = new ArrayList<>();
        userMap = new HashMap<>();
    }


    public static List<CourtLocation> getCourtLocations() {
        return Collections.unmodifiableList(courtLocations);
    }

    public static Set<String> getCourtOwners() {
        return Collections.unmodifiableSet(courtOwners);
    }


    //TODO confirm user assignment
    private RandomFromCollectionGeneratorImpl<User> randomUserGenerator;

    private User getRandomUser() {
        return randomUserGenerator.generate();
    }


    @PostConstruct
    //Temporary for testing purposes
    public void postConstruct() {
        this.stopWatch = new StopWatch();
        clearDownDatabase();
        courtLocationRepository.findAll().forEach(courtLocations::add);
        courtLocations.forEach(courtLocation -> courtOwners.add(courtLocation.getOwner()));

        createAllUsers(5, 1, 40, 5);//TODO confirm counts
        userRepository.findAll().forEach(users::add);
        users.forEach(user -> userMap.computeIfAbsent(user.getOwner(), k -> new ArrayList<>()).add(user));
        randomUserGenerator =
            new RandomFromCollectionGeneratorImpl<>(users);

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

        Map<JurorStatus, Integer> jurorStatusBureauMapCourt = new EnumMap<>(JurorStatus.class);
        jurorStatusBureauMapCourt.put(JurorStatus.DEFERRED, 93620);
        jurorStatusCountMapCourt.put(JurorStatus.DISQUALIFIED, 30319);
        jurorStatusBureauMapCourt.put(JurorStatus.EXCUSED, 146869);
        jurorStatusBureauMapCourt.put(JurorStatus.REASSIGNED, 16712);
        jurorStatusBureauMapCourt.put(JurorStatus.RESPONDED, 240168);
        jurorStatusBureauMapCourt.put(JurorStatus.SUMMONED, 70288);
        //TODO undeliverable
        createJurorsAssociatedToPools(false, 30, 40, jurorStatusBureauMapCourt);

        //TODO remove
        Map<JurorStatus, Integer> jurorStatusCountMapCourtTmp = new EnumMap<>(JurorStatus.class);
        jurorStatusCountMapCourtTmp.put(JurorStatus.SUMMONED, 10000);
        jurorStatusCountMapCourtTmp.put(JurorStatus.RESPONDED, 10000);
        jurorStatusCountMapCourtTmp.put(JurorStatus.DEFERRED, 100);
        jurorStatusCountMapCourtTmp.put(JurorStatus.EXCUSED, 100);
//        createJurorsAssociatedToPools(true, 12, 16, jurorStatusCountMapCourtTmp);
        //TODO remove end


        log.info("DONE: " + stopWatch.prettyPrint());
    }

    private void createAllUsers(int courtStandardCount,
                                int courtSeniorCount,
                                int bureauStandardCount,
                                int bureauTeamLeadCount) {
        stopWatch.start("Creating users");

        List<User> users = new ArrayList<>();

        ObjIntConsumer<UserGenerator> createUsers = (userGenerator, count) -> {
            for (int i = 0; i < count; i++) {
                users.add(userGenerator.generate());
            }
        };

        Set<String> courtOwners = courtLocations.stream()
            .map(CourtLocation::getOwner)
            .filter(s -> !s.equals("400"))
            .collect(Collectors.toSet());

        courtOwners.forEach(owner -> {
            createUsers.accept(UserGeneratorUtil.standardCourtUser(owner), courtStandardCount);
            createUsers.accept(UserGeneratorUtil.seniorJurorOfficerCourtUser(owner), courtSeniorCount);
        });
        createUsers.accept(UserGeneratorUtil.standardBureauUser(), bureauStandardCount);
        createUsers.accept(UserGeneratorUtil.teamLeadBureauUser(), bureauTeamLeadCount);

        stopWatch.stop();
        stopWatch.start("Saving users");
        userRepository.saveAll(users);
//        Util.batchSave(userRepository, users, BATCH_SIZE);
        stopWatch.stop();
    }

    private void clearDownDatabase() {
        log.info("Clearing database");
        stopWatch.start("Cleaning database");
        jurorPoolRepository.deleteAll();
        poolRequestRepository.deleteAll();
        digitalResponseRepository.deleteAll();
        paperResponseRepository.deleteAll();
        jurorRepository.deleteAll();
        userRepository.deleteAll();
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

        createVoters(jurorAccountDetailsDtos);
        saveJurorAccountDetailsDtos(jurorAccountDetailsDtos);
    }

    private void createVoters(List<JurorAccountDetailsDto> jurorAccountDetailsDtos) {
        stopWatch.start("Creating Voters");
        jurorAccountDetailsDtos.forEach(jurorAccountDetailsDto ->
            jurorAccountDetailsDto
                .setVoters(
                    VotersGeneratorUtil.fromJurorAccountDetailsDto(jurorAccountDetailsDto).generate()
                )
        );
        stopWatch.stop();
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


        stopWatch.start("Saving Voter's");
        log.info("Saving Voter's to database");
        Util.batchSave(votersRepository,
            jurorAccountDetailsDtos.stream().map(JurorAccountDetailsDto::getVoters).toList(),
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
        jurorResponse.setStaff(getRandomUser());
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
