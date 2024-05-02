package uk.gov.hmcts.juror.support.sql.v2;


import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import uk.gov.hmcts.juror.support.generation.generators.code.Generator;
import uk.gov.hmcts.juror.support.generation.generators.value.FirstNameGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.LastNameGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.LocalTimeGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromCollectionGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromCollectionGeneratorWeightedImpl;
import uk.gov.hmcts.juror.support.generation.util.RandomGenerator;
import uk.gov.hmcts.juror.support.sql.v1.entity.CourtLocation;
import uk.gov.hmcts.juror.support.sql.v1.entity.Juror;
import uk.gov.hmcts.juror.support.sql.v1.entity.JurorPool;
import uk.gov.hmcts.juror.support.sql.v1.entity.PoliceCheck;
import uk.gov.hmcts.juror.support.sql.v1.entity.PoolRequest;
import uk.gov.hmcts.juror.support.sql.v1.repository.CourtLocationRepository;
import uk.gov.hmcts.juror.support.sql.v1.repository.JurorPoolRepository;
import uk.gov.hmcts.juror.support.sql.v1.repository.JurorRepository;
import uk.gov.hmcts.juror.support.sql.v1.repository.PoolRequestRepository;
import uk.gov.hmcts.juror.support.sql.v2.flows.enums.PoolType;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.CompleteServiceJurorNumberListDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.JurorAppearanceDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.PoliceCheckStatusDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.PoolRequestDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.ApproveExpenseDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.ExpenseItemsDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.draft.DailyExpense;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.draft.DailyExpenseFinancialLoss;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.draft.DailyExpenseFoodAndDrink;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.draft.DailyExpenseTime;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.draft.DailyExpenseTravel;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.jurormanagement.UpdateAttendanceDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.trial.CreatePanelDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.trial.EndTrialDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.trial.JurorDetailRequestDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.trial.JurorListRequestDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.trial.ReturnJuryDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.trial.TrialDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.expense.PendingApprovalList;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.trial.TrialSummaryDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.IJurorStatus;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.administration.CourtRoomDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.administration.JudgeCreateDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.AppearanceStage;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.FoodDrinkClaimType;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.PayAttendanceType;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.PaymentMethod;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.jurormanagement.UpdateAttendanceStatus;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.trial.PanelResult;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.trial.TrialType;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.AdministrationCourtRoomControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.AdministrationJudgeControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.CompleteServiceControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.JurorExpenseControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.JurorManagementControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.JurorRecordControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.TrialControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.trial.PanelControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generation.CreatePools;
import uk.gov.hmcts.juror.support.sql.v2.generation.Voters;
import uk.gov.hmcts.juror.support.sql.v2.generation.dto.CourtDetails;
import uk.gov.hmcts.juror.support.sql.v2.generation.dto.CourtRoom;
import uk.gov.hmcts.juror.support.sql.v2.generation.dto.Judge;
import uk.gov.hmcts.juror.support.sql.v2.generation.dto.User;
import uk.gov.hmcts.juror.support.sql.v2.spring.dto.JurorNumberLocCodeAndDate;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.Appearance;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.JurorTrialWithTrial;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.RequirePncCheckViewEntity;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.Trial;
import uk.gov.hmcts.juror.support.sql.v2.spring.repository.AppearanceRepository;
import uk.gov.hmcts.juror.support.sql.v2.spring.repository.CourtroomRepository;
import uk.gov.hmcts.juror.support.sql.v2.spring.repository.JudgeRepository;
import uk.gov.hmcts.juror.support.sql.v2.spring.repository.JurorTrialWithTrialRepository;
import uk.gov.hmcts.juror.support.sql.v2.spring.repository.RequirePncCheckViewRepository;
import uk.gov.hmcts.juror.support.sql.v2.spring.repository.TrialRepository;
import uk.gov.hmcts.juror.support.sql.v2.spring.repository.UserV2Repository;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetailsBureau;
import uk.gov.hmcts.juror.support.sql.v2.support.Role;
import uk.gov.hmcts.juror.support.sql.v2.support.UserType;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DataCreator {
    //Step 1: Crate judges
    //Step 2: Create courtrooms
    //Step 3: Create voters
    //Step 4: Create pools
    //Step 5: Assign police checks
    //Step 6: Transfer pools to court owned
    //Step 7 run court checks

    public static final Environment ENV = Environment.PVT;
    private static final User SYSTEM_USER = new User("AUTO", "400", UserType.SYSTEM, Set.of());



    public static RestTemplateBuilder restTemplateBuilder;
    private final JurorPoolRepository jurorPoolRepository;
    private final JurorRepository jurorRepository;
    private final PoolRequestRepository poolRequestRepository;
    private final RequirePncCheckViewRepository requirePncCheckViewRepository;
    private final AppearanceRepository appearanceRepository;
    private final JurorTrialWithTrialRepository jurorTrialWithTrialRepository;
    private final CourtLocationRepository courtLocationRepository;

    private final TrialRepository trialRepository;
    private final JudgeRepository judgeRepository;
    private final CourtroomRepository courtroomRepository;
    private final PlatformTransactionManager transactionManager;
    private final UserV2Repository userRepository;
    private final CreatePools createPools;
    private TransactionTemplate transactionTemplate;


    final List<CourtLoc> courts = new ArrayList<>();

    public final static CourtLoc BUREAU_COURT_LOC = new CourtLoc("400", List.of("400"), List.of(),
        null,
        null,
        null,
        null
    );

    private final RandomFromCollectionGeneratorWeightedImpl<PoolType> poolTypeGenerator =
        new RandomFromCollectionGeneratorWeightedImpl<>(Map.of(
            PoolType.CRO, 0.85,
            PoolType.CIV, 0.10,
            PoolType.HGH, 0.05
        ));


    private final Voters voters;

    @Autowired
    public DataCreator(RestTemplateBuilder restTemplateBuilder,
                       Voters voters,
                       JurorPoolRepository jurorPoolRepository,
                       JurorRepository jurorRepository,
                       PoolRequestRepository poolRequestRepository,
                       RequirePncCheckViewRepository requirePncCheckViewRepository,
                       AppearanceRepository appearanceRepository,
                       JurorTrialWithTrialRepository jurorTrialWithTrialRepository, TrialRepository trialRepository,
                       CourtLocationRepository courtLocationRepository,
                       JudgeRepository judgeRepository,
                       CourtroomRepository courtroomRepository,
                       PlatformTransactionManager transactionManager, UserV2Repository userRepository,
                       CreatePools createPools) {
        DataCreator.restTemplateBuilder = restTemplateBuilder;
        this.voters = voters;

        this.jurorPoolRepository = jurorPoolRepository;
        this.jurorRepository = jurorRepository;
        this.poolRequestRepository = poolRequestRepository;
        this.requirePncCheckViewRepository = requirePncCheckViewRepository;
        this.appearanceRepository = appearanceRepository;
        this.jurorTrialWithTrialRepository = jurorTrialWithTrialRepository;
        this.trialRepository = trialRepository;
        this.judgeRepository = judgeRepository;
        this.courtroomRepository = courtroomRepository;
        this.transactionManager = transactionManager;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.courtLocationRepository = courtLocationRepository;

//        this.poolDates = new LocalDateGeneratorImpl(LocalDate.now(), LocalDate.now().plusYears(1).minusMonths(1))
//            .addPostGenerate(localDate -> {
//                return localDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
//            })
//            .generate(20L);
        this.userRepository = userRepository;
        this.createPools = createPools;
    }

    @Getter
    @Setter
    public static class CourtLoc {
        String courtCode;
        List<String> locCodes;

        List<String> catchmentAreas;
        User expenseApprove;
        List<User> usernames;
        List<Judge> judges;

        Map<String, List<CourtRoom>> courtRoomMap;

        public List<CourtRoom> getCourtRooms(String roomCode) {
            return courtRoomMap.get(roomCode);
        }

        public CourtLoc(String courtCode, List<String> locCodes, List<String> catchmentAreas, User expenseApprove,
                        List<User> usernames, List<Judge> judges, Map<String, List<CourtRoom>> courtRoomMap) {
            this.courtCode = courtCode;
            this.locCodes = locCodes;
            this.catchmentAreas = catchmentAreas;
            this.expenseApprove = expenseApprove;
            this.usernames = usernames;
            this.judges = judges;
            this.courtRoomMap = courtRoomMap;

            if (locCodes != null && this.expenseApprove != null) {
                locCodes.forEach(string -> this.expenseApprove.addCourt(string));
            }
        }
    }


    Map<String, CourtLoc> courtLocMap = null;

    private CourtLoc getCourt(String locCode) {
        if (courtLocMap == null) {
            courtLocMap = createCourtLocMap();
        }
        return courtLocMap.get(locCode);
    }

    private Map<String, CourtLoc> createCourtLocMap() {
        Map<String, CourtLoc> courtLocMap = new HashMap<>();
        for (CourtLoc courtLoc : courts) {
            courtLoc.locCodes.forEach(string -> courtLocMap.put(string, courtLoc));
        }
        return courtLocMap;
    }

    private void addBureauUsers() {
        //TODO
    }

    private void addCourts() {
        for (CourtLocation courtLocation : courtLocationRepository.findAllByOwnerEqualsLocCode()) {
            log.info("Loading Court details for: " + courtLocation.getOwner());
            List<CourtLocation> courts = courtLocationRepository.findAllByOwner(courtLocation.getOwner());
            ENV.getCourts().add(
                CourtDetails.builder()
                    .courtCode(courtLocation.getOwner())
                    .locCodes(courts.stream().map(CourtLocation::getLocCode).collect(Collectors.toList()))
                    .catchmentAreas(CourtDetails.getCatchmentAreas(courtLocation.getOwner()))
                    .usernames(userRepository.findAllWithCourt(courtLocation.getOwner())
                        .stream().map(User::new).toList())
                    .judges(judgeRepository.findAllByOwner(courtLocation.getOwner())
                        .stream()
                        .map(Judge::new)
                        .toList())
                    .build()
            );
        }
        log.info("Court details loaded.");
    }

    @PostConstruct
    public void init() {
        try {
            log.info("DataCreator created");
            addBureauUsers();
            addCourts();
            DataCreator.ENV.setup();//Required at start

            ENV.getCourts().forEach(courtDetails -> System.out.println("TMP: Court: " + courtDetails));


            if (ENV.isCreateVoters()) {
                this.voters.createVoters(DataCreator.ENV.getCourts(), 10_000);
                return;
            }


            if (ENV.isCreateJudges()) {
                createJudges(courts, 10);
            }

            if (ENV.isCreateCourtrooms()) {
                createCourtrooms(courts, 10);
            }


            if (ENV.isShouldCreatePools()) {
                createPools.createPools();
            }

            if (ENV.isAssignPoliceChecks()) {
                updatePoliceChecks();
            }

            if (ENV.isRunCourtSteps()) {
                runCourtSteps();
            }


        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw throwable;
        }
    }


    private void runCourtSteps() {
        log.info("Running court steps");
        if (false) {
            createTrials();
        }

        if (false) {
            completeJurorsStatusJuror();
        }
        if (false) {//TODO confinue
            completeJurorsStatusNoneJuror();
        }
        if (false) {
            //Need to confirm logic here
            confirmAttendances();
        }
        if (false) {
            completeService();
        }
        if (true) {
            addExpenses();
        }
    }

    private void completeService() {
        log.info("Completing service");
        List<Map<String, Objects>> toComplete =
            appearanceRepository.findJurorPoolsForCompletion();
        AtomicLong counter = new AtomicLong(0);
        final long size = toComplete.size();
        toComplete
            .parallelStream()
            .map(JurorNumberLocCodeAndDate::new)
            .distinct()
            .forEach(jurorNumberLocCodeAndDate -> {
                log.info("Processing service completion: " + counter.incrementAndGet() + " of " + size);
                retryElseThrow(() -> {
                    User user = new RandomFromCollectionGeneratorImpl<>(
                        getCourt(jurorNumberLocCodeAndDate.getLocCode()).usernames).generate();
                    completeService(
                        user,
                        jurorNumberLocCodeAndDate.getPoolNumber(),
                        List.of(jurorNumberLocCodeAndDate.getJurorNumber()),
                        jurorNumberLocCodeAndDate.getMaxAttendanceDate()
                    );
                }, 1, false);
            });
    }

    private void confirmAttendances() {

        List<String> locCodes = new ArrayList<>();
        courts.stream()
            .filter(courtLoc -> !courtLoc.courtCode.equals("400"))
            .forEach(courtLoc -> locCodes.addAll(courtLoc.locCodes));

        List<LocalDate> attendanceDates = appearanceRepository.findDistinctAttendanceDate()
            .stream()
            .map(Date::toLocalDate)
            .toList();
        for (String locCode : locCodes) {
            Generator<User> userGenerator = new RandomFromCollectionGeneratorImpl<>(getCourt(locCode).usernames);

            attendanceDates
                .parallelStream()
                .forEach(localDate -> {
                    retryElseThrow(() -> {
                        confirm(
                            userGenerator.generate(),
                            locCode,
                            localDate,
                            appearanceRepository.findJurorNumbersByDateLocationCode(localDate, locCode)
                        );
                    }, 1, false);

                });
        }
    }

    private void addExpenses() {
        AtomicLong counter = new AtomicLong(0);
        List<JurorPool> pools = jurorPoolRepository.findAllByStatus(IJurorStatus.COMPLETED);
        final long size = pools.size();

        pools
            .forEach(jurorPool -> retryElseThrow(() -> {
                log.info("Processing expenses: " + counter.incrementAndGet() + " of " + size);
                String locCode = jurorPool.getLocCode(poolRequestRepository);
                CourtLoc courtLoc = getCourt(locCode);
                User user = new RandomFromCollectionGeneratorImpl<>(courtLoc.usernames).generate();
                addExpensesByAppearance(user, courtLoc, locCode,
                    appearanceRepository.findAllByPoolNumberAndJurorNumberAndAppearanceStage(
                        jurorPool.getPoolNumber(),
                        jurorPool.getJurorNumber(),
                        AppearanceStage.EXPENSE_ENTERED));
            }, 1, false));
    }


    private void completeJurorsStatusNoneJuror() {
        log.info("Completing juror status for jurors not in jury");
        Map<String, List<JurorPool>> jurorPoolsMap = new HashMap<>();

        for (JurorPool jurorPool : jurorPoolRepository.findJurorPoolsNotInJury()) {
            jurorPoolsMap.computeIfAbsent(jurorPool.getPoolNumber(),
                k -> new ArrayList<>()).add(jurorPool);
        }
        log.info("Mapping to loc code Juror pools: " + jurorPoolsMap.size());
        Map<String, List<JurorPool>> locCodeJurorPoolsMap = new HashMap<>();
        for (Map.Entry<String, List<JurorPool>> entry : jurorPoolsMap.entrySet()) {
            String locCode = poolRequestRepository.findById(entry.getKey()).get().getCourtLocation().getLocCode();
            locCodeJurorPoolsMap.computeIfAbsent(locCode,
                k -> new ArrayList<>()).addAll(entry.getValue());
        }


        log.info("Mapping to next data and loading Juror pools: " + jurorPoolsMap.size());
        long count = 0;
        long size = locCodeJurorPoolsMap.size();
        for (Map.Entry<String, List<JurorPool>> entry : locCodeJurorPoolsMap.entrySet()) {
            log.info("Processing loc code: " + entry.getKey() + " count " + count++ + " of " + size);
            String locCode = entry.getKey();
            Map<LocalDate, List<JurorPool>> nextDateJurorPools = new HashMap<>();
            for (JurorPool jurorPool : entry.getValue()) {
                nextDateJurorPools.computeIfAbsent(jurorPool.getNextDate(),
                    k -> new ArrayList<>()).add(jurorPool);
            }
            CourtLoc courtLoc = getCourt(locCode);
            User courtUser = new RandomFromCollectionGeneratorImpl<>(courtLoc.usernames).generate();

            nextDateJurorPools.entrySet()
                .parallelStream()
                .forEach(dateJurorPools -> {
                    LocalDate endDate = dateJurorPools.getKey().plusDays(RandomGenerator.nextInt(5, 15));
                    if (endDate.getDayOfWeek() == DayOfWeek.SATURDAY) {
                        endDate = endDate.minusDays(1);
                    } else if (endDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                        endDate = endDate.plusDays(1);
                    }
                    LocalDate finalEndDate = endDate;
                    retryElseThrow(() -> {
                        addAttendances(
                            courtUser,
                            courtLoc,
                            locCode,
                            dateJurorPools.getKey(),
                            finalEndDate,
                            entry.getValue()
                        );
                    }, 1, false);
                });

        }
    }

    @EqualsAndHashCode
    @Getter
    @Builder
    static class TrialNumAndStartDate {
        String trialNumber;
        LocalDate startDate;
        String locCode;
    }

    @Getter
    class JurorNumberPoolNumber {
        String jurorNumber;
        String poolNumber;

        public JurorNumberPoolNumber(String jurorNumber, String poolNumber) {
            this.jurorNumber = jurorNumber;
            this.poolNumber = poolNumber;
        }
    }

    private void completeJurorsStatusJuror() {
        log.info("Completing juror status for jurors in jury");
        List<JurorNumberPoolNumber> pools = new ArrayList<>();
        String lastTrialNumber = null;

        List<Runnable> tasks = new ArrayList<>();

        int count = 0;
        for (JurorTrialWithTrial jurorTrialWithTrial : jurorTrialWithTrialRepository.findJurorTrialsJuryNotEnded()) {
            if (!jurorTrialWithTrial.getTrialNumber().equals(lastTrialNumber)) {
                count++;
                log.info("Setup trial: " + count);
                if (lastTrialNumber != null) {
                    String finalLastTrialNumber = lastTrialNumber;
                    Trial trial = trialRepository.findByTrialNumber(lastTrialNumber);
                    List<JurorNumberPoolNumber> finalPools = pools;
                    tasks.add(() -> processTrialJurorPools(finalLastTrialNumber,
                        trial.getTrialStartDate(),
                        trial.getLocCode(),
                        finalPools));
                }
                pools = new ArrayList<>();
                lastTrialNumber = jurorTrialWithTrial.getTrialNumber();
            }
            pools.add(new JurorNumberPoolNumber(
                jurorTrialWithTrial.getJurorNumber(),
                jurorTrialWithTrial.getPoolNumber()));
        }

        AtomicLong processedCount = new AtomicLong(0);
        long totalCOunt = tasks.size();
        tasks.parallelStream()
            .forEach(runnable -> {
                log.info("Processing trial: " + processedCount.incrementAndGet() + " of " + totalCOunt);
                retryElseThrow(runnable, 1, false);
            });

    }

    private void processTrialJurorPools(String trialNumber, LocalDate trialStart,
                                        String locCode, List<JurorNumberPoolNumber> pools) {
        log.info("Completing juror status for trial: " + trialNumber);
        LocalDate trialEnd = trialStart.plusDays(RandomGenerator.nextInt(5, 15));
        if (trialEnd.getDayOfWeek() == DayOfWeek.SATURDAY) {
            trialEnd = trialEnd.minusDays(1);
        } else if (trialEnd.getDayOfWeek() == DayOfWeek.SUNDAY) {
            trialEnd = trialEnd.plusDays(1);
        }
        CourtLoc courtLoc = getCourt(locCode);
        User courtUser = new RandomFromCollectionGeneratorImpl<>(courtLoc.usernames).generate();

        List<JurorPool> jurorPools = pools.stream()
            .map(jurorPoolNumber -> jurorPoolRepository.findByPoolNumberAndJurorNumber(
                jurorPoolNumber.poolNumber,
                jurorPoolNumber.jurorNumber))
            .toList();
        addAttendances(courtUser, courtLoc,
            locCode,
            trialStart.plusDays(1), trialEnd, jurorPools);

        List<Juror> jurors =
            pools.stream().map(jurorPool -> jurorRepository.findById(jurorPool.getJurorNumber()).get()).toList();

        new TrialControllerClient()
            .returnJury(
                new JwtDetailsBureau(courtUser),
                trialNumber,
                locCode,
                ReturnJuryDto.builder()
                    .checkIn(null)
                    .checkOut(null)
                    .completed(false)
                    .attendanceDate(trialEnd)
                    .jurors(jurors.stream().map(
                        juror -> JurorDetailRequestDto.builder()
                            .jurorNumber(juror.getJurorNumber())
                            .firstName(juror.getFirstName())
                            .lastName(juror.getLastName())
                            .result(PanelResult.JUROR)
                            .build()).toList())
                    .build()
            );

        //End trial
        new TrialControllerClient()
            .endTrial(
                new JwtDetailsBureau(courtUser),
                EndTrialDto.builder()
                    .trialEndDate(trialEnd)
                    .trialNumber(trialNumber)
                    .locationCode(locCode)
                    .build()
            );

    }

    private void createTrials() {
        int maxPerTrial = 16;
        //Only get responded jurors
        List<List<JurorPool>> jurorPoolsForTrials = new ArrayList<>();
        String lastCourt = "";
        List<JurorPool> tmpList = new ArrayList<>();
        Generator<Boolean> skipTrailGen = new RandomFromCollectionGeneratorWeightedImpl<>(Map.of(
            true, 0.90,
            false, 0.10
        ));
        for (JurorPool jurorPool : jurorPoolRepository.findJurorPoolsByStatusOrdered(2)) {
            if (!lastCourt.equals(jurorPool.getLocCode(poolRequestRepository)) || tmpList.size() >= maxPerTrial) {
                jurorPoolsForTrials.add(tmpList);
                tmpList = new ArrayList<>();
            }
            if (skipTrailGen.generate()) {
                tmpList.add(jurorPool);
            }
            lastCourt = jurorPool.getLocCode(poolRequestRepository);
        }
        System.out.println("Trials: " + jurorPoolsForTrials.size());
        jurorPoolsForTrials
            .parallelStream()
            .filter(list -> !list.isEmpty())
            .forEach(jurorPools -> retryElseThrow(() -> createTrial(jurorPools), 2, false));
        log.info("Trials Creation finished");
    }

    private void closeUnusedJurors() {
        //TODO
    }

    AtomicLong caseIndex = new AtomicLong(2281);
    Generator<TrialType> trialTypeGenerator =
        new RandomFromCollectionGeneratorImpl<>(TrialType.values());


    private void createTrial(List<JurorPool> jurorPools) {
        JurorPool firstPool = jurorPools.get(0);
        String locCode = firstPool.getLocCode(poolRequestRepository);
        LocalDate nextDate = firstPool.getNextDate();//TODO random

        LocalDate trialEnd = nextDate.plusDays(RandomGenerator.nextInt(5, 15));
        if (trialEnd.getDayOfWeek() == DayOfWeek.SATURDAY) {
            trialEnd = trialEnd.minusDays(1);
        } else if (trialEnd.getDayOfWeek() == DayOfWeek.SUNDAY) {
            trialEnd = trialEnd.plusDays(1);
        }


        CourtLoc courtLoc = getCourt(locCode);
        User courtUser = new RandomFromCollectionGeneratorImpl<>(courtLoc.usernames).generate();
        long currentCaseIndex = caseIndex.getAndIncrement();
        log.info("Creating trial for court: " + locCode + " on " + nextDate + " with " + jurorPools.size() + " jurors");
        log.info("Case Index: " + currentCaseIndex);
        TrialSummaryDto trialSummaryDto = new TrialControllerClient()
            .createTrial(new JwtDetailsBureau(courtUser),
                TrialDto.builder()
                    .caseNumber("CASE" + locCode + "A" + currentCaseIndex)
                    .trialType(trialTypeGenerator.generate())
                    .defendant(createDefendant())
                    .startDate(nextDate)
                    .judgeId(new RandomFromCollectionGeneratorImpl<>(courtLoc.getJudges()).generate().getId())
                    .courtroomId(
                        new RandomFromCollectionGeneratorImpl<>(courtLoc.getCourtRooms(locCode)).generate().getId())
                    .courtLocation(locCode)
                    .protectedTrial(false)
                    .build()
            );

        retryElseThrow(() -> checkIn(courtUser, locCode, nextDate, jurorPools), 2, true);

        new PanelControllerClient()
            .createPanel(
                new JwtDetailsBureau(courtUser),
                CreatePanelDto.builder()
                    .attendanceDate(nextDate)
                    .trialNumber(trialSummaryDto.getTrialNumber())
                    .numberRequested(jurorPools.size())
                    .courtLocationCode(locCode)
                    .build());

        List<Juror> jurors = empanelledJury(courtUser, trialSummaryDto, nextDate,
            locCode, jurorPools, 12);


        List<String> jurorNumbers = jurorPools.stream().map(JurorPool::getJurorNumber).collect(Collectors.toList());
        checkOut(courtUser, locCode, nextDate, jurorNumbers);
        if (true) {
            return;//TODO
        }
        confirm(courtUser, locCode, nextDate, jurorNumbers);
        //Added trial days
        addAttendances(courtUser, courtLoc, locCode, nextDate.plusDays(1), trialEnd, jurorPools);
        //Return jury

        new TrialControllerClient()
            .returnJury(
                new JwtDetailsBureau(courtUser),
                trialSummaryDto.getTrialNumber(),
                locCode,
                ReturnJuryDto.builder()
                    .checkIn(null)
                    .checkOut(null)
                    .completed(false)
                    .attendanceDate(trialEnd)
                    .jurors(jurors.stream().map(
                        juror -> JurorDetailRequestDto.builder()
                            .jurorNumber(juror.getJurorNumber())
                            .firstName(juror.getFirstName())
                            .lastName(juror.getLastName())
                            .result(PanelResult.JUROR)
                            .build()).toList())
                    .build()
            );

        //End trial
        new TrialControllerClient()
            .endTrial(
                new JwtDetailsBureau(courtUser),
                EndTrialDto.builder()
                    .trialEndDate(trialEnd)
                    .trialNumber(trialSummaryDto.getTrialNumber())
                    .locationCode(locCode)
                    .build()
            );

        //Complete service
        completeService(courtUser, jurorPools, trialEnd);
        //Add expenses
        for (JurorPool jurorPool : jurorPools) {
            addExpensesByAppearance(courtUser, courtLoc, locCode,
                appearanceRepository.findAllByPoolNumberAndJurorNumberAndAppearanceStage(jurorPool.getPoolNumber(),
                    jurorPool.getJurorNumber(),
                    AppearanceStage.EXPENSE_ENTERED));
        }
    }

    private void retryElseThrow(Runnable runnable, int retryCount, boolean errorOnFail) {
        Throwable lastThrowable = null;
        for (int i = 0; i < retryCount; i++) {
            try {
                runnable.run();
                return;
            } catch (Throwable throwable) {
                lastThrowable = throwable;
                log.error("Error running step", throwable);
            }
        }
        if (errorOnFail) {
            throw new RuntimeException("Failed to run step", lastThrowable);
        }
    }

    private void completeService(User courtUser, List<JurorPool> jurorPools, LocalDate completionDate) {
        log.info("Completing service");
        Map<String, List<String>> poolNumberToJurorMap = new HashMap<>();

        for (JurorPool jurorPool : jurorPools) {
            List<String> jurorNumbers =
                poolNumberToJurorMap.computeIfAbsent(jurorPool.getPoolNumber(), k -> new ArrayList<>());
            jurorNumbers.add(jurorPool.getJurorNumber());
        }
        for (Map.Entry<String, List<String>> entry : poolNumberToJurorMap.entrySet()) {
            completeService(courtUser, entry.getKey(), entry.getValue(), completionDate);
        }
    }

    private void completeService(User courtUser, String poolNumber, List<String> jurorNumbers,
                                 LocalDate completionDate) {
        new CompleteServiceControllerClient()
            .completeService(
                new JwtDetailsBureau(courtUser),
                poolNumber,
                CompleteServiceJurorNumberListDto.builder()
                    .completionDate(completionDate)
                    .jurorNumbers(jurorNumbers)
                    .build()
            );
    }

    Generator<PanelResult> panelResultRejectGen = new RandomFromCollectionGeneratorWeightedImpl<>(
        Map.of(
            PanelResult.NOT_USED, 0.40,
            PanelResult.CHALLENGED, 0.60
        ));

    private List<Juror> empanelledJury(User user, TrialSummaryDto trialSummaryDto,
                                       LocalDate date,
                                       String locCode,
                                       List<JurorPool> jurorPools, int numberOfJurorsWanted) {
        final List<Juror> jurors = new ArrayList<>();
        AtomicInteger count = new AtomicInteger(0);

        BiFunction<JurorPool, Juror, PanelResult> getPanelResult = (jurorPool, juror) -> {
            if (count.incrementAndGet() <= numberOfJurorsWanted) {
                jurors.add(juror);
                return PanelResult.JUROR;
            }
            return panelResultRejectGen.generate();
        };
        new PanelControllerClient()
            .processEmpanelled(
                new JwtDetailsBureau(user),
                JurorListRequestDto.builder()
                    .jurors(
                        jurorPools.stream()
                            .map(jurorPool -> {
                                Juror juror =
                                    transactionTemplate.execute(
                                        status -> jurorRepository.findById(jurorPool.getJurorNumber())
                                            .orElseThrow());
                                assert juror != null;
                                return JurorDetailRequestDto.builder()
                                    .jurorNumber(jurorPool.getJurorNumber())
                                    .firstName(juror.getFirstName())
                                    .lastName(juror.getLastName())
                                    .result(getPanelResult.apply(jurorPool, juror))
                                    .build();
                            })
                            .collect(Collectors.toList())
                    )
                    .trialNumber(trialSummaryDto.getTrialNumber())
                    .courtLocationCode(locCode)
                    .numberRequested(Math.min(numberOfJurorsWanted, jurorPools.size()))
                    .attendanceDate(date)
                    .build()
            );
        return jurors;
    }

    private static final Generator<String> nameGenerator = new FirstNameGeneratorImpl().addPostGenerate(string ->
        string + " " + new LastNameGeneratorImpl().generate());
    private static final FirstNameGeneratorImpl firstNameGenerator = new FirstNameGeneratorImpl();
    private static final LastNameGeneratorImpl lastNameGenerator = new LastNameGeneratorImpl();


    private String createDefendant() {
        String name = nameGenerator.generate();

        if (name.length() > 16) {
            name = name.substring(0, 16);
        }
        return name;
    }


    private void updatePoliceChecks() {
        log.info("Assigning Police checks");
        JurorRecordControllerClient jurorRecordControllerClient = new JurorRecordControllerClient();

        Generator<PoliceCheck> policeCheckGenerator = new RandomFromCollectionGeneratorWeightedImpl<>(
            Map.of(
                PoliceCheck.ELIGIBLE, 0.85,
                PoliceCheck.INELIGIBLE, 0.10,
                PoliceCheck.UNCHECKED_MAX_RETRIES_EXCEEDED, 0.45,
                PoliceCheck.ERROR_RETRY_UNEXPECTED_EXCEPTION, 0.05
            ));

        for (RequirePncCheckViewEntity pncCheckViewEntity :
            requirePncCheckViewRepository.findAllByPoliceCheckIsNull()) {
            try {
                jurorRecordControllerClient.updatePncCheckStatus(
                    new JwtDetailsBureau(SYSTEM_USER),
                    pncCheckViewEntity.getJurorNumber(),
                    new PoliceCheckStatusDto(
                        policeCheckGenerator.generate())
                );
            } catch (Throwable throwable) {
                log.error("Error updating police check for juror: " + pncCheckViewEntity.getJurorNumber(), throwable);
            }
        }
    }

    private void addExpensesByAppearance(User user, CourtLoc courtLoc, String locCode, List<Appearance> appearances) {
        if (appearances.isEmpty()) {
            return;
        }
        JurorExpenseControllerClient jurorExpenseControllerClient = new JurorExpenseControllerClient();
        LocalTimeGeneratorImpl travelTimeGenerator = new LocalTimeGeneratorImpl(
            LocalTime.of(0, 0),
            LocalTime.of(5, 5));

        Generator<BigDecimal> amountGenerator = new Generator<>() {
            @Override
            public BigDecimal generate() {
                return BigDecimal.valueOf(RandomGenerator.nextDouble(0, 10));
            }
        };

        enum TravelMethod {
            CAR, MOTORCYCLE, BICYCLE, PUBLIC_TRANSPORT, TAXI
        }

        RandomFromCollectionGeneratorImpl<TravelMethod> travelMethodGenerator =
            new RandomFromCollectionGeneratorImpl<>(TravelMethod.values());

        @RequiredArgsConstructor
        @Getter
        class Data {
            final String jurorNumber;
            final String poolNumber;
            final List<Appearance> appearances = new ArrayList<>();
        }

        Map<String, Data> jurorDataMap = new HashMap<>();


        for (Appearance appearance : appearances) {
            retryElseThrow(() -> {
                Data data = jurorDataMap.computeIfAbsent(appearance.getJurorNumber() + "-" + appearance.getPoolNumber(),
                    k -> new Data(appearance.getJurorNumber(), appearance.getPoolNumber()));
                data.appearances.add(appearance);


                TravelMethod travelMethod = travelMethodGenerator.generate();

                jurorExpenseControllerClient.postDraftAttendedDayDailyExpense(
                    new JwtDetailsBureau(user),
                    appearance.getJurorNumber(),
                    DailyExpense.builder()
                        .dateOfExpense(appearance.getAttendanceDate())
                        .poolNumber(appearance.getPoolNumber())
                        .payCash(false)
                        .time(DailyExpenseTime.builder()
                            .payAttendance(PayAttendanceType.FULL_DAY)
                            .travelTime(travelTimeGenerator.generateValue())
                            .build())
                        .financialLoss(DailyExpenseFinancialLoss.builder()
                            .lossOfEarningsOrBenefits(amountGenerator.generate())
                            .extraCareCost(amountGenerator.generate())
                            .otherCosts(amountGenerator.generate())
                            .build())
                        .travel(DailyExpenseTravel.builder()
                            .traveledByCar(travelMethod == TravelMethod.CAR)
                            .jurorsTakenCar(travelMethod == TravelMethod.CAR ?
                                RandomGenerator.nextInt(0, 4)
                                : null)
                            .traveledByMotorcycle(travelMethod == TravelMethod.MOTORCYCLE)
                            .jurorsTakenMotorcycle(travelMethod == TravelMethod.MOTORCYCLE ?
                                RandomGenerator.nextInt(0, 2)
                                : null)
                            .traveledByBicycle(travelMethod == TravelMethod.BICYCLE)
                            .milesTraveled(RandomGenerator.nextInt(1, 20))
                            .publicTransport(travelMethod == TravelMethod.PUBLIC_TRANSPORT ?
                                amountGenerator.generate()
                                : null)
                            .taxi(travelMethod == TravelMethod.TAXI ?
                                amountGenerator.generate()
                                : null)
                            .build())
                        .foodAndDrink(DailyExpenseFoodAndDrink.builder()
                            .foodAndDrinkClaimType(FoodDrinkClaimType.LESS_THAN_OR_EQUAL_TO_10_HOURS)
                            .build())
                        .build());
            }, 1, false);
        }

        for (Data data : jurorDataMap.values()) {

            //Submit for approval
            jurorExpenseControllerClient
                .submitDraftExpensesForApproval(
                    new JwtDetailsBureau(user),
                    ExpenseItemsDto.builder()
                        .jurorNumber(data.getJurorNumber())
                        .poolNumber(data.getPoolNumber())
                        .attendanceDates(data.getAppearances().stream().map(Appearance::getAttendanceDate).toList())
                        .build()
                );

            //Approve expenses
            PendingApprovalList pendingApprovalList = jurorExpenseControllerClient
                .getExpensesForApproval(
                    new JwtDetailsBureau(courtLoc.expenseApprove),
                    locCode,
                    PaymentMethod.BACS,
                    null,
                    null);

            if (pendingApprovalList.getPendingApproval().isEmpty()) {
                return;
            }

            jurorExpenseControllerClient
                .approveExpenses(
                    new JwtDetailsBureau(courtLoc.expenseApprove),
                    pendingApprovalList.getPendingApproval().stream()
                        .map(pendingApproval -> ApproveExpenseDto.builder()
                            .jurorNumber(pendingApproval.getJurorNumber())
                            .poolNumber(pendingApproval.getPoolNumber())
                            .approvalType(ApproveExpenseDto.ApprovalType.FOR_APPROVAL)
                            .cashPayment(false)
                            .dateToRevisions(pendingApproval.getDateToRevisions().stream()
                                .map(dateToRevision -> ApproveExpenseDto.DateToRevision.builder()
                                    .attendanceDate(dateToRevision.getAttendanceDate())
                                    .version(dateToRevision.getVersion())
                                    .build())
                                .collect(Collectors.toList()))
                            .build())
                        .toList());
        }
    }

    private void addExpenses(User user, CourtLoc courtLoc, String locCode, List<JurorDetails> jurorDetails) {
        JurorExpenseControllerClient jurorExpenseControllerClient = new JurorExpenseControllerClient();
        LocalTimeGeneratorImpl travelTimeGenerator = new LocalTimeGeneratorImpl(
            LocalTime.of(0, 0),
            LocalTime.of(5, 5));

        Generator<BigDecimal> amountGenerator = new Generator<>() {
            @Override
            public BigDecimal generate() {
                return BigDecimal.valueOf(RandomGenerator.nextDouble(0, 10));
            }
        };

        enum TravelMethod {
            CAR, MOTORCYCLE, BICYCLE, PUBLIC_TRANSPORT, TAXI
        }

        RandomFromCollectionGeneratorImpl<TravelMethod> travelMethodGenerator =
            new RandomFromCollectionGeneratorImpl<>(TravelMethod.values());

        for (JurorDetails jurorDetail : jurorDetails) {
            log.info("Adding expenses for juror " + jurorDetail.getJurorPool().getJurorNumber());

            TravelMethod travelMethod = travelMethodGenerator.generate();

            for (LocalDate attendanceDate : jurorDetail.getAttendanceDates()) {
                jurorExpenseControllerClient.postDraftAttendedDayDailyExpense(
                    new JwtDetailsBureau(user),
                    jurorDetail.getJurorPool().getJurorNumber(),
                    DailyExpense.builder()
                        .dateOfExpense(attendanceDate)
                        .poolNumber(jurorDetail.getJurorPool().getPoolNumber())
                        .payCash(false)
                        .time(DailyExpenseTime.builder()
                            .payAttendance(PayAttendanceType.FULL_DAY)
                            .travelTime(travelTimeGenerator.generateValue())
                            .build())
                        .financialLoss(DailyExpenseFinancialLoss.builder()
                            .lossOfEarningsOrBenefits(amountGenerator.generate())
                            .extraCareCost(amountGenerator.generate())
                            .otherCosts(amountGenerator.generate())
                            .build())
                        .travel(DailyExpenseTravel.builder()
                            .traveledByCar(travelMethod == TravelMethod.CAR)
                            .jurorsTakenCar(travelMethod == TravelMethod.CAR ?
                                RandomGenerator.nextInt(0, 4)
                                : null)
                            .traveledByMotorcycle(travelMethod == TravelMethod.MOTORCYCLE)
                            .jurorsTakenMotorcycle(travelMethod == TravelMethod.MOTORCYCLE ?
                                RandomGenerator.nextInt(0, 2)
                                : null)
                            .traveledByBicycle(travelMethod == TravelMethod.BICYCLE)
                            .milesTraveled(RandomGenerator.nextInt(1, 20))
                            .publicTransport(travelMethod == TravelMethod.PUBLIC_TRANSPORT ?
                                amountGenerator.generate()
                                : null)
                            .taxi(travelMethod == TravelMethod.TAXI ?
                                amountGenerator.generate()
                                : null)
                            .build())
                        .foodAndDrink(DailyExpenseFoodAndDrink.builder()
                            .foodAndDrinkClaimType(FoodDrinkClaimType.LESS_THAN_OR_EQUAL_TO_10_HOURS)
                            .build())
                        .build());
            }

            //Submit for approval
            jurorExpenseControllerClient
                .submitDraftExpensesForApproval(
                    new JwtDetailsBureau(user),
                    ExpenseItemsDto.builder()
                        .jurorNumber(jurorDetail.getJurorPool().getJurorNumber())
                        .poolNumber(jurorDetail.getJurorPool().getPoolNumber())
                        .attendanceDates(jurorDetail.getAttendanceDates())
                        .build()
                );

            //Approve expenses

            PendingApprovalList pendingApprovalList = jurorExpenseControllerClient
                .getExpensesForApproval(
                    new JwtDetailsBureau(courtLoc.expenseApprove),
                    locCode,
                    PaymentMethod.BACS,
                    null,
                    null);


            jurorExpenseControllerClient
                .approveExpenses(
                    new JwtDetailsBureau(courtLoc.expenseApprove),
                    pendingApprovalList.getPendingApproval().stream()
                        .map(pendingApproval -> ApproveExpenseDto.builder()
                            .jurorNumber(pendingApproval.getJurorNumber())
                            .poolNumber(pendingApproval.getPoolNumber())
                            .approvalType(ApproveExpenseDto.ApprovalType.FOR_APPROVAL)
                            .cashPayment(false)
                            .dateToRevisions(pendingApproval.getDateToRevisions().stream()
                                .map(dateToRevision -> ApproveExpenseDto.DateToRevision.builder()
                                    .attendanceDate(dateToRevision.getAttendanceDate())
                                    .version(dateToRevision.getVersion())
                                    .build())
                                .collect(Collectors.toList()))
                            .build())
                        .toList());
        }
    }


    //TODO confirm
    private void transitionToCourtOwned(CourtLoc locCode, List<JurorPool> jurorPools) {
        log.info(locCode.courtCode + " has " + jurorPools.size() + " juror pools");
        if (jurorPools.isEmpty()) {
            return;
        }
        transactionTemplate.execute(status -> {
            for (JurorPool jurorPool : jurorPools) {
                jurorPool.setOwner(locCode.courtCode);
            }
            jurorPoolRepository.saveAll(jurorPools);

            PoolRequest poolRequest = poolRequestRepository.findById(jurorPools.get(0).getPoolNumber()).orElseThrow();
            poolRequest.setOwner(locCode.courtCode);
            poolRequestRepository.save(poolRequest);
            return null;
        });
    }

    @AllArgsConstructor
    @Builder
    @Getter
    static class JurorDetails {
        private JurorPool jurorPool;
        private List<LocalDate> attendanceDates;
    }

    private void addAttendances(User user, CourtLoc courtLoc, String locCode, LocalDate nextDate, LocalDate trialEnd,
                                List<JurorPool> jurorPools) {
        log.info("Adding attendances for " + locCode + " from " + nextDate + " to " + trialEnd);
        LocalDate date = nextDate;
        while (!date.isAfter(trialEnd)) {
            addAttendances(user, courtLoc, locCode, date, jurorPools);
            date = date.plusDays(1);
            if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
                date = date.plusDays(2);
            }
        }
    }

    private void checkIn(User user, String locCode, LocalDate date, List<JurorPool> jurorPools) {
        log.info("Checking in for " + locCode + " on " + date + " count " + jurorPools.size());
        JurorManagementControllerClient jurorManagementControllerClient = new JurorManagementControllerClient();
        for (JurorPool jurorPool : jurorPools) {
            //Check in
            jurorManagementControllerClient.processAppearance(
                new JwtDetailsBureau(user),
                JurorAppearanceDto.builder()
                    .jurorNumber(jurorPool.getJurorNumber())
                    .locationCode(locCode)//TODO confirm
                    .attendanceDate(date)
                    .checkInTime(LocalTime.of(8, 30))
                    .appearanceStage(AppearanceStage.CHECKED_IN)
                    .build());
        }
    }

    private void addAttendances(User user, CourtLoc courtLoc, String locCode, LocalDate date,
                                List<JurorPool> jurorPools) {
        log.info("Adding attendances for " + locCode + " on " + date);
        List<String> jurorNumbers = jurorPools.stream().map(JurorPool::getJurorNumber).toList();

        checkIn(user, locCode, date, jurorPools);
        checkOut(user, locCode, date, jurorNumbers);
//TODO        confirm(user, locCode, date, jurorNumbers);
    }

    public static <T> List<List<T>> getBatches(List<T> collection, int batchSize) {
        List<List<T>> batches = new ArrayList<>();

        for (int i = 0; i < collection.size(); i += batchSize) {
            batches.add(collection.subList(i, Math.min(i + batchSize, collection.size())));
        }

        return batches;
    }

    private void confirm(User user, String locCode, LocalDate date, List<String> jurorNumbers) {
        JurorManagementControllerClient jurorManagementControllerClient = new JurorManagementControllerClient();

        List<List<String>> batches = getBatches(jurorNumbers, 100);
        batches.forEach(batch -> jurorManagementControllerClient.updateAttendance(
            new JwtDetailsBureau(user),
            UpdateAttendanceDto.builder()
                .commonData(UpdateAttendanceDto.CommonData.builder()
                    .status(UpdateAttendanceStatus.CONFIRM_ATTENDANCE)
                    .attendanceDate(date)
                    .locationCode(locCode)
                    .checkOutTime(LocalTime.of(16, 30))
                    .singleJuror(false)
                    .build())
                .juror(batch)
                .build()));
    }

    private void checkOut(User user, String locCode, LocalDate date, List<String> jurorNumbers) {
        JurorManagementControllerClient jurorManagementControllerClient = new JurorManagementControllerClient();
        jurorManagementControllerClient.updateAttendance(
            new JwtDetailsBureau(user),
            UpdateAttendanceDto.builder()
                .commonData(UpdateAttendanceDto.CommonData.builder()
                    .status(UpdateAttendanceStatus.CHECK_OUT)
                    .attendanceDate(date)
                    .locationCode(locCode)
                    .checkOutTime(LocalTime.of(16, 30))
                    .singleJuror(false)
                    .build())
                .juror(jurorNumbers)
                .build());
    }

    private List<JurorDetails> addAttendances(User user, CourtLoc courtLoc, PoolRequestDto poolRequestDto,
                                              List<JurorPool> jurorPools) {
        JurorManagementControllerClient jurorManagementControllerClient = new JurorManagementControllerClient();
        Map<LocalDate, List<String>> dayToJurorNumber = new HashMap<>();
        final List<JurorDetails> jurorDetails = new ArrayList<>();
        for (JurorPool jurorPool : jurorPools) {
            if (!jurorPool.getStatus().equals(IJurorStatus.RESPONDED)) {
                continue;
            }
            LocalDate nextDate = poolRequestDto.getAttendanceDate();
            int numWeeks = RandomGenerator.nextInt(1, 3);
            List<LocalDate> attendanceDates = new ArrayList<>();
            for (int i = 0; i < (numWeeks * 7); i++) {
                if (nextDate.getDayOfWeek() == DayOfWeek.SATURDAY || nextDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    nextDate = nextDate.plusDays(1);
                    continue;
                }
                List<String> jurorNumbers = dayToJurorNumber.computeIfAbsent(nextDate, k -> new ArrayList<>());
                jurorNumbers.add(jurorPool.getJurorNumber());

                //Check in
                jurorManagementControllerClient.processAppearance(
                    new JwtDetailsBureau(user),
                    JurorAppearanceDto.builder()
                        .jurorNumber(jurorPool.getJurorNumber())
                        .locationCode(jurorPool.getOwner())//TODO confirm
                        .attendanceDate(nextDate)
                        .checkInTime(LocalTime.of(8, 30))
                        .appearanceStage(AppearanceStage.CHECKED_IN)
                        .build());

                attendanceDates.add(nextDate);
                nextDate = nextDate.plusDays(1);
            }
            jurorDetails.add(JurorDetails.builder().jurorPool(jurorPool).attendanceDates(attendanceDates).build());
        }
        log.info("Confirming attendance  for pool " + poolRequestDto.getPoolNumber());

        for (Map.Entry<LocalDate, List<String>> entry : dayToJurorNumber.entrySet()) {
            //Check out
            jurorManagementControllerClient.updateAttendance(
                new JwtDetailsBureau(user),
                UpdateAttendanceDto.builder()
                    .commonData(UpdateAttendanceDto.CommonData.builder()
                        .status(UpdateAttendanceStatus.CHECK_OUT)
                        .attendanceDate(entry.getKey())
                        .locationCode(courtLoc.courtCode)//TODO confirm
                        .checkOutTime(LocalTime.of(16, 30))
                        .singleJuror(false)
                        .build())
                    .juror(entry.getValue())
                    .build());
            //Confirm
            jurorManagementControllerClient.updateAttendance(
                new JwtDetailsBureau(user),
                UpdateAttendanceDto.builder()
                    .commonData(UpdateAttendanceDto.CommonData.builder()
                        .status(UpdateAttendanceStatus.CONFIRM_ATTENDANCE)
                        .attendanceDate(entry.getKey())
                        .locationCode(courtLoc.courtCode)//TODO confirm
                        .checkOutTime(LocalTime.of(16, 30))
                        .singleJuror(false)
                        .build())
                    .juror(entry.getValue())
                    .build());
        }
        return jurorDetails;
    }

    private void createCourtrooms(List<CourtLoc> courts, int countPerCourt) {
        for (CourtLoc courtLoc : courts) {
            Generator<User> userGenerator = new RandomFromCollectionGeneratorImpl<>(courtLoc.getUsernames());
            for (String locCode : courtLoc.locCodes) {
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

    private void createJudges(List<CourtLoc> courts, int countPerCourt) {
        FirstNameGeneratorImpl firstNameGenerator = new FirstNameGeneratorImpl();
        for (CourtLoc courtLoc : courts) {
            Generator<User> userGenerator = new RandomFromCollectionGeneratorImpl<>(courtLoc.getUsernames());
            String prefix = courtLoc.catchmentAreas.get(0).substring(0, 2);
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


    private List<String> getPostcodes(CourtLoc courtLoc) {
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
        return postCodes;
    }
}
