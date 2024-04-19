package uk.gov.hmcts.juror.support.sql.v2;


import io.jsonwebtoken.lang.Collections;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import uk.gov.hmcts.juror.support.generation.generators.code.Generator;
import uk.gov.hmcts.juror.support.generation.generators.value.FirstNameGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.FixedValueGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.LastNameGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.LocalDateGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.LocalTimeGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromCollectionGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromCollectionGeneratorWeightedImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromFileGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.RegexGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.ValueGenerator;
import uk.gov.hmcts.juror.support.generation.util.RandomGenerator;
import uk.gov.hmcts.juror.support.sql.v1.Util;
import uk.gov.hmcts.juror.support.sql.v1.entity.Juror;
import uk.gov.hmcts.juror.support.sql.v1.entity.JurorPool;
import uk.gov.hmcts.juror.support.sql.v1.entity.PoliceCheck;
import uk.gov.hmcts.juror.support.sql.v1.entity.PoolRequest;
import uk.gov.hmcts.juror.support.sql.v1.repository.JurorPoolRepository;
import uk.gov.hmcts.juror.support.sql.v1.repository.JurorRepository;
import uk.gov.hmcts.juror.support.sql.v1.repository.PoolRequestRepository;
import uk.gov.hmcts.juror.support.sql.v2.flows.CreatePool;
import uk.gov.hmcts.juror.support.sql.v2.flows.enums.PoolType;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.bureau.controller.request.BureauResponseStatusUpdateDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.juror.controller.request.JurorResponseDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.juror.domain.ProcessingStatus;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.CompleteServiceJurorNumberListDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.DeferralReasonRequestDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.ExcusalDecisionDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.JurorAppearanceDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.JurorPaperResponseDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.PoliceCheckStatusDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.PoolCreateRequestDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.PoolRequestDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.ApproveExpenseDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.ExpenseItemsDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.draft.DailyExpense;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.draft.DailyExpenseFinancialLoss;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.draft.DailyExpenseFoodAndDrink;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.draft.DailyExpenseTime;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.draft.DailyExpenseTravel;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.jurormanagement.UpdateAttendanceDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.summonsmanagement.DisqualifyJurorDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.trial.CreatePanelDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.trial.EndTrialDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.trial.JurorDetailRequestDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.trial.JurorListRequestDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.trial.ReturnJuryDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.trial.TrialDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.expense.PendingApprovalList;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.trial.TrialSummaryDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.ExcusalDecision;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.IJurorStatus;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.administration.CourtRoomDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.administration.JudgeCreateDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.AppearanceStage;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.DisqualifyCodeEnum;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.FoodDrinkClaimType;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.PayAttendanceType;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.PaymentMethod;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.ReplyMethod;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.jurormanagement.UpdateAttendanceStatus;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.trial.PanelResult;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.trial.TrialType;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.AdministrationCourtRoomControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.AdministrationJudgeControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.CompleteServiceControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.CreatePoolControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.DeferralMaintenanceControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.DisqualifyJurorControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.ExcusalResponseControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.JurorExpenseControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.JurorManagementControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.JurorPaperResponseControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.JurorRecordControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.JurorResponseControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.PublicEndpointControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.TrialControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.trial.PanelControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.spring.dto.JurorNumberLocCodeAndDate;
import uk.gov.hmcts.juror.support.sql.v2.spring.dto.ToBeClosed;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.Appearance;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.CourtRoomEntity;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.JurorTrialWithTrial;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.RequirePncCheckViewEntity;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.Trial;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.VotersV2;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.VotersV2Generator;
import uk.gov.hmcts.juror.support.sql.v2.spring.repository.AppearanceRepository;
import uk.gov.hmcts.juror.support.sql.v2.spring.repository.CourtroomRepository;
import uk.gov.hmcts.juror.support.sql.v2.spring.repository.JudgeRepository;
import uk.gov.hmcts.juror.support.sql.v2.spring.repository.JurorTrialWithTrialRepository;
import uk.gov.hmcts.juror.support.sql.v2.spring.repository.RequirePncCheckViewRepository;
import uk.gov.hmcts.juror.support.sql.v2.spring.repository.TrialRepository;
import uk.gov.hmcts.juror.support.sql.v2.spring.repository.VotersV2Repository;
import uk.gov.hmcts.juror.support.sql.v2.support.Constants;
import uk.gov.hmcts.juror.support.sql.v2.support.ExcusalCodeEnum;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetailsBureau;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetailsPublic;
import uk.gov.hmcts.juror.support.sql.v2.support.ReasonableAdjustmentsEnum;
import uk.gov.hmcts.juror.support.sql.v2.support.Role;
import uk.gov.hmcts.juror.support.sql.v2.support.RollingList;
import uk.gov.hmcts.juror.support.sql.v2.support.UserType;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
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


    public static final boolean IS_DEMO = true;
    private static final User SYSTEM_USER = new User("AUTO", "400", UserType.SYSTEM, Set.of());
    public static String publicJwtSecret = IS_DEMO
        ? "tbc"
        : "tbc";
    final boolean createVoters = false;
    final boolean createJudges = false;
    final boolean createCourtrooms = false;
    final boolean createPools = false;

    final boolean assignPoliceChecks = false;
    final boolean runCourtSteps = true; //TODO step 7
    //    final int numberOfJurorsPerPool = 10;
    final int numberOfJurorsPerPool = 100; //100
    final int totalPool = 50;
    //    final int totalPool = 1000;
    final int numberOfDeferrals = 200;
    final boolean createDeferralPools = false;
    public static double paperChange = 0.5;


    public static RestTemplateBuilder restTemplateBuilder;
    public static String jwtSecret = IS_DEMO
        ? "tbc"
        : "tbc";
    private final VotersV2Repository votersRepository;
    private final JurorPoolRepository jurorPoolRepository;
    private final JurorRepository jurorRepository;
    private final PoolRequestRepository poolRequestRepository;
    private final RequirePncCheckViewRepository requirePncCheckViewRepository;
    private final AppearanceRepository appearanceRepository;
    private final JurorTrialWithTrialRepository jurorTrialWithTrialRepository;

    private final TrialRepository trialRepository;
    private final JudgeRepository judgeRepository;
    private final CourtroomRepository courtroomRepository;
    private final PlatformTransactionManager transactionManager;
    private TransactionTemplate transactionTemplate;
    private static final int BATCH_SIZE = 5000;

    private final LocalDateGeneratorImpl dateOfBirthGenerator = new LocalDateGeneratorImpl(
        LocalDate.now().minusYears(75),
        LocalDate.now().minusYears(18));
    private final RegexGeneratorImpl phoneGenerator = new RegexGeneratorImpl(false, Constants.PHONE_REGEX);
    private final RegexGeneratorImpl phoneGenerator2 = new RegexGeneratorImpl(false, "[0-9]{8,15}");

    private final RandomFromFileGeneratorImpl emailSuffixGenerator =
        new RandomFromFileGeneratorImpl("data/emailsSuffix.txt");

    final List<CourtLoc> courts = new ArrayList<>();

    public final static CourtLoc BUREAU_COURT_LOC = new CourtLoc("400", List.of("400"), List.of(),
        null,
        List.of(
            new User("bethany.clarke", "400", UserType.BUREAU, Set.of()),
            new User("luke.murgatroyd", "400", UserType.BUREAU, Set.of()),
            new User("dawn.fensom", "400", UserType.BUREAU, Set.of()),
            new User("Mikaela.Harrison", "400", UserType.BUREAU, Set.of(Role.TEAM_LEADER)),
            new User("Chelsey.Reed", "400", UserType.BUREAU, Set.of(Role.TEAM_LEADER)),
            new User("Pamela.Collins", "400", UserType.BUREAU, Set.of(Role.TEAM_LEADER)),
            new User("Katie.Wardle", "400", UserType.BUREAU, Set.of(Role.TEAM_LEADER))
        ),
        null,
        null
    );


    private final List<LocalDate> poolDates;

    private final RandomFromCollectionGeneratorWeightedImpl<PoolType> poolTypeGenerator =
        new RandomFromCollectionGeneratorWeightedImpl<>(Map.of(
            PoolType.CRO, 0.85,
            PoolType.CIV, 0.10,
            PoolType.HGH, 0.05
        ));


    private final RandomFromCollectionGeneratorImpl<LocalDate> poolStartDatesGen;

    public User getRandomBureauUser() {
        return new RandomFromCollectionGeneratorImpl<>(BUREAU_COURT_LOC.getUsernames()).generate();
    }

    @Autowired
    public DataCreator(RestTemplateBuilder restTemplateBuilder,
                       @Value("${uk.gov.hmcts.juror.security.secret}")
                       String jwtSecret,
                       VotersV2Repository votersRepository,
                       JurorPoolRepository jurorPoolRepository,
                       JurorRepository jurorRepository,
                       PoolRequestRepository poolRequestRepository,
                       RequirePncCheckViewRepository requirePncCheckViewRepository,
                       AppearanceRepository appearanceRepository,
                       JurorTrialWithTrialRepository jurorTrialWithTrialRepository, TrialRepository trialRepository,
                       JudgeRepository judgeRepository,
                       CourtroomRepository courtroomRepository,
                       PlatformTransactionManager transactionManager) {
        DataCreator.restTemplateBuilder = restTemplateBuilder;
        this.votersRepository = votersRepository;
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

//        this.poolDates = new LocalDateGeneratorImpl(LocalDate.now(), LocalDate.now().plusYears(1).minusMonths(1))
//            .addPostGenerate(localDate -> {
//                return localDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
//            })
//            .generate(20L);


        this.poolDates = new ArrayList<>();
        this.poolDates.addAll(
            List.of(
                LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)),
                LocalDate.now().plusWeeks(1).with(TemporalAdjusters.next(DayOfWeek.MONDAY)),
                LocalDate.now().plusWeeks(1).with(TemporalAdjusters.next(DayOfWeek.MONDAY)),
                LocalDate.now().plusWeeks(2).with(TemporalAdjusters.next(DayOfWeek.MONDAY)),
                LocalDate.now().plusWeeks(3).with(TemporalAdjusters.next(DayOfWeek.MONDAY)),
                LocalDate.now().plusWeeks(4).with(TemporalAdjusters.next(DayOfWeek.MONDAY)),
                LocalDate.now().plusWeeks(5).with(TemporalAdjusters.next(DayOfWeek.MONDAY)),
                LocalDate.now().plusWeeks(6).with(TemporalAdjusters.next(DayOfWeek.MONDAY)),
                LocalDate.now().plusWeeks(7).with(TemporalAdjusters.next(DayOfWeek.MONDAY)),
                LocalDate.now().plusWeeks(8).with(TemporalAdjusters.next(DayOfWeek.MONDAY)),

                LocalDate.now().minusWeeks(1).with(TemporalAdjusters.next(DayOfWeek.MONDAY)),
                LocalDate.now().minusWeeks(2).with(TemporalAdjusters.next(DayOfWeek.MONDAY)),
                LocalDate.now().minusWeeks(3).with(TemporalAdjusters.next(DayOfWeek.MONDAY))
            ));
        this.poolStartDatesGen =
            new RandomFromCollectionGeneratorImpl<>(poolDates);
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

    @AllArgsConstructor
    @Getter
    public static class Judge {
        long id;
    }

    @AllArgsConstructor
    @Getter
    public static class CourtRoom {
        long id;
    }

    @RequiredArgsConstructor
    @Getter
    public static class User {
        final String username;
        final String owner;
        List<String> courts = new ArrayList<>();
        final UserType userType;
        final Collection<Role> roles;

        public User addCourt(String court) {
            courts.add(court);
            return this;
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

    @PostConstruct
    public void init() {
        try {
            log.info("DataCreator created");
            if (false) {
            appearanceRepository.deleteAll(appearanceRepository.getToBeClosed()
                .stream()
                .map(ToBeClosed::new)
                .map(toBeClosed -> appearanceRepository
                    .findAllByPoolNumberAndJurorNumberAndAttendanceDateAndAppearanceStageIsNull(
                        toBeClosed.getPoolNumber(),
                        toBeClosed.getJurorNumber(),
                        toBeClosed.getAttendanceDate()
                    ))
                .toList());
            log.info("DataCreator Done");
                return;
            }

            courts.add(new CourtLoc("433", List.of("433"), List.of("AA1", "AA2", "AA3"),
                new User("expense.approve.433", "433", UserType.COURT, Set.of(Role.MANAGER)),
                List.of(
                    new User("Mary.panchal", "433", UserType.COURT, Set.of(Role.MANAGER)),
                    new User("Claire.sutton", "433", UserType.COURT, Set.of(Role.MANAGER)),
                    new User("Sarah.Ravenscroft", "433", UserType.COURT, Set.of()),
                    new User("Holly.murphy2", "433", UserType.COURT, Set.of()),
                    new User("Hayden.thompson", "433", UserType.COURT, Set.of()),
                    new User("Ian.edwards6", "433", UserType.COURT, Set.of())
                ),
                List.of(),
                Map.of()));
            courts.add(new CourtLoc("411", List.of("411", "441"), List.of("BB1", "BB2", "BB3"),
                new User("expense.approve.411", "411", UserType.COURT, Set.of(Role.MANAGER)),
                List.of(new User("catherine.rees", "411", UserType.COURT, Set.of(Role.MANAGER)),
                    new User("janet.page", "411", UserType.COURT, Set.of()),
                    new User("lynette.howells", "411", UserType.COURT, Set.of()),
                    new User("Amy.Lineham", "411", UserType.COURT, Set.of()),
                    new User("isabelle.davies", "411", UserType.COURT, Set.of())),
                List.of(),
                Map.of()
            ));
            courts.add(new CourtLoc("426", List.of("426", "754"), List.of("CC1", "CC2", "CC3"),
                new User("expense.approve.426", "426", UserType.COURT, Set.of(Role.MANAGER)),
                List.of(new User("Zoe.Wickham", "426", UserType.COURT, Set.of(Role.MANAGER))),
                List.of(),
                Map.of()));
            courts.add(new CourtLoc("419", List.of("419"), List.of("DD1", "DD2", "DD3"),
                new User("expense.approve.419", "419", UserType.COURT, Set.of(Role.MANAGER)),
                List.of(new User("fiona.edwards", "419", UserType.COURT, Set.of(Role.MANAGER))),
                List.of(),
                Map.of()));
            courts.add(new CourtLoc("454", List.of("454"), List.of("EE1", "EE2", "EE3"),
                new User("expense.approve.454", "454", UserType.COURT, Set.of(Role.MANAGER)),
                List.of(new User("Fiona.Bryan", "454", UserType.COURT, Set.of()),
                    new User("Carolina.dejesus", "454", UserType.COURT, Set.of())),
                List.of(),
                Map.of()));
            courts.add(new CourtLoc("412", List.of("412"), List.of("FF1", "FF2", "FF3"),
                new User("expense.approve.412", "412", UserType.COURT, Set.of(Role.MANAGER)),
                List.of(new User("michelle.stealey", "412", UserType.COURT, Set.of()),
                    new User("lynn.ellison", "412", UserType.COURT, Set.of())),
                List.of(),
                Map.of()));
            courts.add(new CourtLoc("432", List.of("432", "249"), List.of("GG1", "GG2", "GG3"),
                new User("expense.approve.432", "432", UserType.COURT, Set.of(Role.MANAGER)),
                List.of(
                    new User("ruth.green", "432", UserType.COURT, Set.of(Role.MANAGER, Role.SENIOR_JUROR_OFFICER))),
                List.of(),
                Map.of()));
            courts.add(new CourtLoc("445", List.of("445"), List.of("HH1", "HH2", "HH3"),
                new User("expense.approve.445", "445", UserType.COURT, Set.of(Role.MANAGER)),
                List.of(new User("Steven.Chen", "445", UserType.COURT, Set.of(Role.SENIOR_JUROR_OFFICER))),
                List.of(),
                Map.of()));
            courts.add(new CourtLoc("470", List.of("470"), List.of("KK1", "KK2", "KK3"),
                new User("expense.approve.470", "470", UserType.COURT, Set.of(Role.MANAGER)),
                List.of(new User("kathryn.isherwood", "470", UserType.COURT, Set.of())),
                List.of(),
                Map.of()));
            courts.add(new CourtLoc("415", List.of("415", "462", "767"), List.of("LL1", "LL2", "LL3"),
                new User("expense.approve.415", "415", UserType.COURT, Set.of(Role.MANAGER)),
                List.of(new User("Jean.Spindler", "415", UserType.COURT, Set.of())),
                List.of(),
                Map.of()));
            courts.add(new CourtLoc("423", List.of("423", "750"), List.of("MM1", "MM2", "MM3"),
                new User("expense.approve.423", "423", UserType.COURT, Set.of(Role.MANAGER)),
                List.of(new User("Jason.Batty", "423", UserType.COURT, Set.of())),
                List.of(),
                Map.of()));
            courts.add(new CourtLoc("450", List.of("450"), List.of("NN1", "NN2", "NN3"),
                new User("expense.approve.450", "450", UserType.COURT, Set.of(Role.MANAGER)),
                List.of(new User("Hannah.Deebanks", "450", UserType.COURT, Set.of())),
                List.of(),
                Map.of()));
            courts.add(new CourtLoc("451", List.of("451"), List.of("OO1", "OO2", "OO3"),
                new User("expense.approve.451", "451", UserType.COURT, Set.of(Role.MANAGER)),
                List.of(new User("Jack.Watkinson", "451", UserType.COURT, Set.of()),
                    new User("Peter.Herrington", "451", UserType.COURT, Set.of()),
                    new User("Anya.Cummings", "451", UserType.COURT, Set.of())),
                List.of(),
                Map.of()));
            courts.add(new CourtLoc("472", List.of("472"), List.of("PP1", "PP2", "PP3"),
                new User("expense.approve.472", "472", UserType.COURT, Set.of(Role.MANAGER)),
                List.of(new User("Angela.Newman5", "472", UserType.COURT, Set.of(Role.MANAGER))),
                List.of(),
                Map.of()));
            courts.add(new CourtLoc("471", List.of("471", "464"), List.of("RR1", "RR2", "RR3"),
                new User("expense.approve.472", "472", UserType.COURT, Set.of(Role.MANAGER)),
                List.of(new User("brooke.hutson", "471", UserType.COURT, Set.of()),
                    new User("esther.fernandez-abd", "471", UserType.COURT, Set.of())),
                List.of(new Judge(301), new Judge(302), new Judge(303), new Judge(304), new Judge(305), new Judge(306),
                    new Judge(307), new Judge(308), new Judge(309), new Judge(310)),
                Map.of()));

            loadCourtData(courts);

            if (createVoters) {
                createVoters(courts, (100 * 1000));
                return;
            }

            if (createJudges) {
                createJudges(courts, 10);
            }

            if (createCourtrooms) {
                createCourtrooms(courts, 10);
            }

//add some nil pools
            if (createPools) {

                //Create deferral pools
                if (createDeferralPools) {
                    createDeferralPools();
                }

                log.info("Creating Bureau Pools");

                for (int index = 0; index < totalPool; index++) {
                    log.info("Creating pool with index: " + index);
                    CourtLoc courtLoc = courts.get(index % courts.size());
                    String locCode = new RandomFromCollectionGeneratorImpl<>(courtLoc.locCodes).generate();

                    User user = getRandomBureauUser();
                    PoolRequestDto poolRequestDto = CreatePool.createPoolOnBehalfOfCourt(
                        user,
                        locCode,
                        LocalDateTime.of(poolStartDatesGen.generate(), LocalTime.of(8, 30)),
                        poolTypeGenerator.generate(),
                        false,
                        (int) Math.round(numberOfJurorsPerPool * 0.60),
                        0
                    );
                    //5% change to not summon jurors
                    if (RandomGenerator.nextInt(0, 100) > 5) {
                        summonJurors(user, courtLoc, poolRequestDto, numberOfJurorsPerPool, numberOfDeferrals);
                    }
                }
            }

            if (assignPoliceChecks) {
                updatePoliceChecks();
            }

            if (runCourtSteps) {
                runCourtSteps();
            }


        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw throwable;
        }
    }

    private void createOnePoolPerCourt() {
        List<String> locCode = new ArrayList<>();
        courts.forEach(courtLoc -> locCode.addAll(courtLoc.locCodes));
        locCode
            .forEach(string -> retryElseThrow(() -> createOnePool(string), 1, false));
    }

    private void createOnePool(String locCode) {
        CourtLoc courtLoc = getCourt(locCode);
        User user = getRandomBureauUser();
        PoolRequestDto poolRequestDto = CreatePool.createPoolOnBehalfOfCourt(
            user,
            locCode,
            LocalDateTime.of(
                LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)),
                LocalTime.of(8, 30)
            ),
            poolTypeGenerator.generate(),
            false,
            (int) Math.round(numberOfJurorsPerPool * 0.60),
            0
        );

        List<String> postCodes = getPostcodes(courtLoc);
        new CreatePoolControllerClient()
            .createPoolRequest(
                jurorPoolRepository,
                new JwtDetailsBureau(user),
                PoolCreateRequestDto.builder()
                    .poolNumber(poolRequestDto.getPoolNumber())
                    .startDate(poolRequestDto.getAttendanceDate())
                    .attendTime(
                        LocalDateTime.of(poolRequestDto.getAttendanceDate(), poolRequestDto.getAttendanceTime()))
                    .noRequested(numberOfJurorsPerPool)
                    .bureauDeferrals(0)
                    .numberRequired((int) Math.round(numberOfJurorsPerPool * 0.60))
                    .citizensToSummon(numberOfJurorsPerPool)
                    .catchmentArea(poolRequestDto.getLocationCode())
                    .postcodes(postCodes)
                    .build());

        List<JurorPool> jurorPools =
            transactionTemplate.execute(
                status -> jurorPoolRepository.findAllByPoolNumber(poolRequestDto.getPoolNumber()));

        assert jurorPools != null;
        for (JurorPool jurorPool : jurorPools) {
            Juror juror = transactionTemplate.execute(
                status -> jurorRepository.findById(jurorPool.getJurorNumber()).orElseThrow());
            if (juror.isResponded()) {
                continue;
            }
            retryElseThrow(() -> straightForwardSummon(user, jurorPool, juror), 1, false);
        }
    }

    private void loadCourtData(List<CourtLoc> courts) {
        for (CourtLoc courtLoc : courts) {
            List<Judge> judges = judgeRepository.findAllByOwner(courtLoc.courtCode)
                .stream()
                .map(judgeEntity -> new Judge(judgeEntity.getId()))
                .toList();
            courtLoc.setJudges(Collections.immutable(judges));

            List<CourtRoomEntity> courtRooms = courtroomRepository.findByCourtLocationOwner(courtLoc.courtCode);
            Map<String, List<CourtRoom>> courtRoomMap = new HashMap<>();
            for (CourtRoomEntity courtRoomEntity : courtRooms) {
                List<CourtRoom> jurorNumbers =
                    courtRoomMap.computeIfAbsent(courtRoomEntity.getCourtLocation().getLocCode(),
                        k -> new ArrayList<>());
                jurorNumbers.add(new CourtRoom(courtRoomEntity.getId()));
            }
            courtLoc.setCourtRoomMap(courtRoomMap);
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


    Generator<String> nameGenerator = new FirstNameGeneratorImpl().addPostGenerate(string ->
        string + " " + new LastNameGeneratorImpl().generate());

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

    private void createDeferralPools() {
        log.info("Creating deferral Pools");
        boolean skip = true;
        for (CourtLoc courtLoc : courts) {
            List<String> postcodes = getPostcodes(courtLoc);
            for (String locCode : courtLoc.locCodes) {
                User user = getRandomBureauUser();
                PoolRequestDto poolRequestDto = CreatePool.createPoolOnBehalfOfCourt(
                    user,
                    locCode,
                    LocalDateTime.of(poolStartDatesGen.generate(), LocalTime.of(1, 30)),
                    poolTypeGenerator.generate(),
                    false,
                    numberOfJurorsPerPool * 2,
                    0
                );


                new CreatePoolControllerClient()
                    .createPoolRequest(
                        jurorPoolRepository,
                        new JwtDetailsBureau(user),
                        PoolCreateRequestDto.builder()
                            .poolNumber(poolRequestDto.getPoolNumber())
                            .startDate(poolRequestDto.getAttendanceDate())
                            .attendTime(
                                LocalDateTime.of(poolRequestDto.getAttendanceDate(),
                                    poolRequestDto.getAttendanceTime()))
                            .noRequested(numberOfJurorsPerPool * 2)
                            .bureauDeferrals(numberOfDeferrals)
                            .numberRequired(numberOfJurorsPerPool)
                            .citizensToSummon(numberOfJurorsPerPool * 2)
                            .catchmentArea(poolRequestDto.getLocationCode())
                            .postcodes(postcodes)
                            .build());

                List<JurorPool> jurorPools =
                    transactionTemplate.execute(
                        status -> jurorPoolRepository.findAllByPoolNumber(poolRequestDto.getPoolNumber()));

                RollingList<LocalDate> attendanceDates = new RollingList<>(poolDates);
                for (JurorPool jurorPool : jurorPools) {
                    Juror juror = transactionTemplate.execute(
                        status -> jurorRepository.findById(jurorPool.getJurorNumber()).orElseThrow());
                    deferralSummon(user, jurorPool, juror, attendanceDates.getNext());
                }
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

    private void summonJurors(User user, CourtLoc courtLoc, PoolRequestDto poolRequestDto, final int numToSummon,
                              int numberOfDeferrals) {
        log.info("Summoning jurors for pool number {}", poolRequestDto.getPoolNumber());
        List<String> postCodes = getPostcodes(courtLoc);
        Long availableDeferrals = new CreatePoolControllerClient()
            .getBureauDeferrals(
                new JwtDetailsBureau(user),
                poolRequestDto.getLocationCode(),
                poolRequestDto.getAttendanceDate()
            );

        if (availableDeferrals < numberOfDeferrals) {
            numberOfDeferrals = availableDeferrals.intValue();
        }

        int numToSummonExcludingDeferrals = numToSummon - numberOfDeferrals;

        new CreatePoolControllerClient()
            .createPoolRequest(
                jurorPoolRepository,
                new JwtDetailsBureau(user),
                PoolCreateRequestDto.builder()
                    .poolNumber(poolRequestDto.getPoolNumber())
                    .startDate(poolRequestDto.getAttendanceDate())
                    .attendTime(
                        LocalDateTime.of(poolRequestDto.getAttendanceDate(), poolRequestDto.getAttendanceTime()))
                    .noRequested(numToSummon)
                    .bureauDeferrals(numberOfDeferrals)//TODO
                    .numberRequired((int) Math.round(numToSummon * 0.50))
                    .citizensToSummon(numToSummonExcludingDeferrals)
                    .catchmentArea(poolRequestDto.getLocationCode())
                    .postcodes(postCodes)
                    .build());
        List<JurorPool> jurorPools =
            transactionTemplate.execute(
                status -> jurorPoolRepository.findAllByPoolNumber(poolRequestDto.getPoolNumber()));
        enterSummonsReply(user, jurorPools);
    }


    private void enterSummonsReply(User user, List<JurorPool> jurorPools) {
        for (JurorPool jurorPool : jurorPools) {
            try {
                Juror juror = transactionTemplate.execute(
                    status -> jurorRepository.findById(jurorPool.getJurorNumber()).orElseThrow());

                if (juror.isResponded()) {
                    continue;
                }
                log.info("Processing juror {}. Pool {}", juror.getJurorNumber(), jurorPool.getPoolNumber());

                RandomFromCollectionGeneratorWeightedImpl<Runnable> summonMethodGenerator =
                    new RandomFromCollectionGeneratorWeightedImpl<>(Map.of(
                        () -> straightForwardSummon(user, jurorPool, juror), 0.75,
                        () -> ineligibleSummon(user, jurorPool, juror), 0.05,
                        () -> deferralSummon(user, jurorPool, juror, getFutureDate(jurorPool.getNextDate())), 0.05,
                        () -> excusalSummon(user, jurorPool, juror), 0.05,
                        () -> disqualifySummon(user, jurorPool, juror), 0.05,
                        () -> log.info("No action taken"), 0.05
                    ));
                summonMethodGenerator.generate().run();
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Error processing juror {}. Pool {}",
                    jurorPool.getJurorNumber(),
                    jurorPool.getPoolNumber(),
                    e);
            }
        }
    }

    private void ineligibleSummon(User user, JurorPool jurorPool, Juror juror) {
        if (RandomGenerator.nextDouble(0, 1) > paperChange) {
            ineligibleSummonPaper(user, jurorPool, juror);
        } else {
            ineligibleSummonDigital(user, jurorPool, juror);
        }
    }

    private void ineligibleSummonDigital(User user, JurorPool jurorPool, Juror juror) {
        JurorResponseDto jurorResponseDto =
            getValidJurorResponseDto(juror);
        int random = RandomGenerator.nextInt(0, 32);

        jurorResponseDto.setQualify(JurorResponseDto.Qualify.builder()
            .onBail(
                JurorResponseDto.Qualify.Answerable.builder()
                    .answer(random % 2 == 0)
                    .details(random % 2 == 0 ? "Some Details 1" : null)
                    .build())
            .mentalHealthAct(JurorResponseDto.Qualify.Answerable.builder()
                .answer(random % 3 == 0)
                .details(random % 3 == 0 ? "Some Details 2" : null)
                .build())
            .livedConsecutive(
                JurorResponseDto.Qualify.Answerable.builder()
                    .answer(random % 3 == 0)
                    .details(random % 3 == 0 ? "Some Details 3" : null)
                    .build())
            .convicted(JurorResponseDto.Qualify.Answerable.builder()
                .answer(random % 4 == 0)
                .details(random % 4 == 0 ? "Some Details 4" : null)
                .build())
            .build());

        new PublicEndpointControllerClient()
            .respondToSummons(
                new JwtDetailsPublic(juror),
                jurorResponseDto);
        processReplyDisqualify(user, jurorPool, ReplyMethod.DIGITAL);
    }

    private void ineligibleSummonPaper(User user, JurorPool jurorPool, Juror juror) {
        JurorPaperResponseDto jurorPaperResponseDto =
            getValidJurorPaperResponseDto(jurorPool, juror);
        int random = RandomGenerator.nextInt(0, 32);
        jurorPaperResponseDto.setEligibility(JurorPaperResponseDto.Eligibility.builder()
            .onBail(random % 2 == 0)
            .livedConsecutive(random % 3 == 0)
            .convicted(random % 4 == 0)
            .mentalHealthAct(random % 5 == 0)
            .mentalHealthCapacity(random % 6 == 0)
            .build());

        new JurorPaperResponseControllerClient()
            .respondToSummons(
                new JwtDetailsBureau(user),
                jurorPaperResponseDto);

        processReplyDisqualify(user, jurorPool, ReplyMethod.PAPER);
    }


    private void processReplyResponded(User user, JurorPool jurorPool) {
        if (RandomGenerator.nextDouble(0, 1) > 0.98) {
            return;
        }
        new JurorPaperResponseControllerClient()
            .updateJurorPaperResponseStatus(
                new JwtDetailsBureau(user),
                jurorPool.getJurorNumber(),
                ProcessingStatus.CLOSED
            );
    }

    private void processReplyRespondedDigital(User user, JurorPool jurorPool) {
        if (RandomGenerator.nextDouble(0, 1) > 0.98) {
            return;
        }
        new JurorResponseControllerClient()
            .updateResponseStatus(
                new JwtDetailsBureau(user),
                jurorPool.getJurorNumber(),
                BureauResponseStatusUpdateDto.builder()
                    .jurorNumber(jurorPool.getJurorNumber())
                    .version(1)//TODO confirm
                    .status(ProcessingStatus.CLOSED)
                    .build()
            );
    }

    private void disqualifySummon(User user, JurorPool jurorPool, Juror juror) {
        if (RandomGenerator.nextDouble(0, 1) > paperChange) {
            disqualifySummonPaper(user, jurorPool, juror);
        } else {
            disqualifySummonDigital(user, jurorPool, juror);
        }
    }

    private void disqualifySummonPaper(User user, JurorPool jurorPool, Juror juror) {
        new JurorPaperResponseControllerClient()
            .respondToSummons(
                new JwtDetailsBureau(user),
                getValidJurorPaperResponseDto(jurorPool, juror));
        processReplyDisqualify(user, jurorPool, ReplyMethod.PAPER);
    }

    private void disqualifySummonDigital(User user, JurorPool jurorPool, Juror juror) {
        new PublicEndpointControllerClient()
            .respondToSummons(
                new JwtDetailsPublic(juror),
                getValidJurorResponseDto(juror));
        processReplyDisqualify(user, jurorPool, ReplyMethod.DIGITAL);
    }


    private void processReplyDisqualify(User user, JurorPool jurorPool, ReplyMethod replyMethod) {
        if (RandomGenerator.nextDouble(0, 1) > 0.98) {
            return;
        }
        if (Boolean.TRUE.equals(transactionTemplate.execute(
            status -> jurorRepository.findById(jurorPool.getJurorNumber()).orElseThrow().isResponded()))) {
            return;
        }
        new DisqualifyJurorControllerClient()
            .disqualifyJuror(new JwtDetailsBureau(user),
                jurorPool.getJurorNumber(),
                DisqualifyJurorDto.builder()
                    .replyMethod(replyMethod)
                    .code(new RandomFromCollectionGeneratorImpl<>(DisqualifyCodeEnum.values()).generate())
                    .build());
    }

    private void processReplyDeferral(User user, JurorPool jurorPool, ReplyMethod replyMethod,
                                      LocalDate deferralDate) {
        if (RandomGenerator.nextDouble(0, 1) > 0.98) {
            return;
        }
        new DeferralMaintenanceControllerClient()
            .processJurorDeferral(new JwtDetailsBureau(user),
                jurorPool.getJurorNumber(),
                DeferralReasonRequestDto.builder()
                    .deferralDate(deferralDate)
                    .excusalReasonCode(
                        new RandomFromCollectionGeneratorImpl<>(ExcusalCodeEnum.values()).generate().getCode())
                    .replyMethod(replyMethod)
                    .build());
    }

    private void processReplyExcusal(User user, JurorPool jurorPool, ReplyMethod replyMethod) {
        if (RandomGenerator.nextDouble(0, 1) > 0.98) {
            return;
        }
        new ExcusalResponseControllerClient()
            .respondToExcusalRequest(new JwtDetailsBureau(user),
                ExcusalDecisionDto.builder()
                    .excusalReasonCode(
                        new RandomFromCollectionGeneratorImpl<>(ExcusalCodeEnum.values()).generate().getCode())
                    .excusalDecision(new RandomFromCollectionGeneratorWeightedImpl<>(
                        Map.of(ExcusalDecision.GRANT, 0.95, ExcusalDecision.REFUSE, 0.05)).generate())
                    .replyMethod(replyMethod)
                    .build(),
                jurorPool.getJurorNumber());
    }


    private LocalDate getFutureDate(LocalDate minDate) {
        List<LocalDate> allowedDates =
            poolDates.stream().filter(localDate -> localDate.isAfter(minDate)).toList();
        LocalDate deferralDate;
        if (!allowedDates.isEmpty()) {
            deferralDate = new RandomFromCollectionGeneratorImpl<>(allowedDates).generate();
        } else {
            deferralDate = minDate.plusWeeks(1);
        }
        return deferralDate;
    }

    private final FirstNameGeneratorImpl firstNameGenerator = new FirstNameGeneratorImpl();
    private final LastNameGeneratorImpl lastNameGenerator = new LastNameGeneratorImpl();

    private JurorResponseDto getValidJurorResponseDto(Juror juror) {
        JurorResponseDto.ThirdParty thirdParty = null;

        if (RandomGenerator.nextBoolean(.95, .05)) {
            thirdParty = new JurorResponseDto.ThirdParty();

            thirdParty.setThirdPartyFName(firstNameGenerator.generate());
            thirdParty.setThirdPartyLName(lastNameGenerator.generate());
            thirdParty.setContactEmail(thirdParty.getThirdPartyFName() + "." + thirdParty.getThirdPartyLName() + "@"
                + emailSuffixGenerator.generate());
            thirdParty.setMainPhone(phoneGenerator2.generate());
            thirdParty.setOtherPhone(phoneGenerator2.generate());
            thirdParty.setEmailAddress(thirdParty.getThirdPartyFName() + "." + thirdParty.getThirdPartyLName() + "@"
                + emailSuffixGenerator.generate());

            thirdParty.setUseJurorEmailDetails(RandomGenerator.nextBoolean(.95, .05));
            thirdParty.setUseJurorPhoneDetails(RandomGenerator.nextBoolean(.95, .05));
            thirdParty.setRelationship(new RandomFromCollectionGeneratorImpl<>(List.of(
                "Spouse", "Partner", "Parent", "Child", "Sibling", "Friend", "Carer", "Other"
            )).generate());
            thirdParty.setThirdPartyReason(new RandomFromCollectionGeneratorImpl<>(List.of(
                "Juror is not there",
                "Juror is unable to reply by themselves",
                "Juror is deceased",
                "Disability", "Mental Health", "Physical Health", "Other"
            )).generate());
        }

        List<JurorResponseDto.CjsEmployment> cjsEmployment = null;

//        if (RandomGenerator.nextBoolean(.95, .05)) {
//            cjsEmployment = new ArrayList<>();
//            int count = RandomGenerator.nextInt(0, 3);
//            Set<String> employers = new HashSet<>(List.of(
//                "Police Force", "HM Prison Service", "National Crime Agency", "Judiciary",
//                "HMCTS", "Other"
//            ));
//            do {
//                String employer = new RandomFromCollectionGeneratorImpl<>(employers).generate();
//                cjsEmployment.add(JurorResponseDto.CjsEmployment.builder()
//                    .cjsEmployer(employer)
//                    .cjsEmployerDetails(new RandomFromCollectionGeneratorImpl<>(List.of(
//                        "", "", "Sample Part 1", "Sample Part 2", "Sample Part 3", "Sample Part 4", "Sample Part 5"
//                    )).generate())
//                    .build());
//                employers.remove(employer);
//            } while (count-- > 0);
//        }


        String assistanceSpecialArrangements = null;
        List<JurorResponseDto.ReasonableAdjustment> reasonableAdjustment = null;
//        if (RandomGenerator.nextBoolean(.95, .05)) {
//            reasonableAdjustment = List.of(
//                JurorResponseDto.ReasonableAdjustment
//                    .builder()
//                    .assistanceType(
//                        new RandomFromCollectionGeneratorImpl<>(ReasonableAdjustmentsEnum.values())
//                            .generate().getCode()
//                    )
//                    .assistanceTypeDetails(
//                        new RandomFromCollectionGeneratorImpl<>(List.of(
//                            "Sample Details 1", "Sample Details 2", "Sample Details 3",
//                            "Sample Details 4", "Sample Details 5"
//                        )).generate()
//                    )
//                    .build()
//            );
//            assistanceSpecialArrangements = reasonableAdjustment.get(0).getAssistanceTypeDetails();
//        }
        return JurorResponseDto.builder()
            .jurorNumber(juror.getJurorNumber())
            .title(juror.getTitle())
            .firstName(juror.getFirstName())
            .lastName(juror.getLastName())
            .addressLineOne(juror.getAddressLine1())
            .addressLineTwo(juror.getAddressLine2())
            .addressLineThree(juror.getAddressLine3())
            .addressTown(juror.getAddressLine4())
            .addressCounty(juror.getAddressLine5())
            .addressPostcode(juror.getPostcode())
            .dateOfBirth(dateOfBirthGenerator.generateValue())
            .primaryPhone(phoneGenerator2.generate())
            .secondaryPhone(nullOrGenerate(phoneGenerator2, 0.2))
            .emailAddress((juror.getFirstName() + "." + juror.getLastName() + "@") + emailSuffixGenerator.generate())
            .cjsEmployment(cjsEmployment)
            .specialNeeds(reasonableAdjustment)
            .assistanceSpecialArrangements(assistanceSpecialArrangements)
            .deferral(null)
            .excusal(null)
            .qualify(JurorResponseDto.Qualify.builder()
                .livedConsecutive(JurorResponseDto.Qualify.Answerable.builder().answer(true).build())
                .mentalHealthAct(JurorResponseDto.Qualify.Answerable.builder().answer(false).build())
                .onBail(JurorResponseDto.Qualify.Answerable.builder().answer(false).build())
                .convicted(JurorResponseDto.Qualify.Answerable.builder().answer(false).build())
                .build())
            .version(1)//TODO
            .replyMethod(ReplyMethod.DIGITAL)
            .thirdParty(thirdParty)
            .welsh(false)
            .build();

    }

    private JurorPaperResponseDto getValidJurorPaperResponseDto(JurorPool jurorPool, Juror juror) {

        JurorPaperResponseDto.ThirdParty thirdParty = JurorPaperResponseDto.ThirdParty.builder()
            .relationship(null)
            .thirdPartyReason(null)
            .build();

        if (RandomGenerator.nextBoolean(.95, .05)) {
            thirdParty.setRelationship(new RandomFromCollectionGeneratorImpl<>(List.of(
                "Spouse", "Partner", "Parent", "Child", "Sibling", "Friend", "Carer", "Other"
            )).generate());
            thirdParty.setThirdPartyReason(new RandomFromCollectionGeneratorImpl<>(List.of(
                "Juror is not there",
                "Juror is unable to reply by themselves",
                "Juror is deceased",
                "Disability", "Mental Health", "Physical Health", "Other"
            )).generate());
        }

        List<JurorPaperResponseDto.CjsEmployment> cjsEmployment = null;

        if (RandomGenerator.nextBoolean(.95, .05)) {
            cjsEmployment = new ArrayList<>();
            int count = RandomGenerator.nextInt(0, 3);
            Set<String> employers = new HashSet<>(List.of(
                "Police Force", "HM Prison Service", "National Crime Agency", "Judiciary",
                "HMCTS", "Other"
            ));
            do {
                String employer = new RandomFromCollectionGeneratorImpl<>(employers).generate();
                cjsEmployment.add(JurorPaperResponseDto.CjsEmployment.builder()
                    .cjsEmployer(employer)
                    .cjsEmployerDetails(new RandomFromCollectionGeneratorImpl<>(List.of(
                        "", "", "Sample Part 1", "Sample Part 2", "Sample Part 3", "Sample Part 4", "Sample Part 5"
                    )).generate())
                    .build());
                employers.remove(employer);
            } while (count-- > 0);
        }


        List<JurorPaperResponseDto.ReasonableAdjustment> reasonableAdjustment = null;
        if (RandomGenerator.nextBoolean(.95, .05)) {
            reasonableAdjustment = List.of(
                JurorPaperResponseDto.ReasonableAdjustment
                    .builder()
                    .assistanceType(
                        new RandomFromCollectionGeneratorImpl<>(ReasonableAdjustmentsEnum.values())
                            .generate().getCode()
                    )
                    .assistanceTypeDetails(
                        new RandomFromCollectionGeneratorImpl<>(List.of(
                            "", "Sample Details 1", "Sample Details 2", "Sample Details 3",
                            "Sample Details 4", "Sample Details 5"
                        )).generate()
                    )
                    .build()
            );
        }


        return JurorPaperResponseDto.builder()
            .jurorNumber(jurorPool.getJurorNumber())
            .title(juror.getTitle())
            .firstName(juror.getFirstName())
            .lastName(juror.getLastName())
            .addressLineOne(juror.getAddressLine1())
            .addressLineTwo(juror.getAddressLine2())
            .addressLineThree(juror.getAddressLine3())
            .addressTown(juror.getAddressLine4())
            .addressCounty(juror.getAddressLine5())
            .addressPostcode(juror.getPostcode())
            .dateOfBirth(dateOfBirthGenerator.generateValue())
            .primaryPhone(nullOrGenerate(phoneGenerator, 0.05))
            .secondaryPhone(nullOrGenerate(phoneGenerator, 0.2))
            .emailAddress(nullOrGenerate(emailSuffixGenerator,
                (juror.getFirstName() + "." + juror.getLastName() + "@"),
                0.05))
            .cjsEmployment(cjsEmployment)
            .reasonableAdjustments(reasonableAdjustment)
            .canServeOnSummonsDate(true)
            .deferral(false)
            .excusal(false)
            .eligibility(JurorPaperResponseDto.Eligibility.builder()
                .livedConsecutive(true)
                .mentalHealthAct(false)
                .mentalHealthCapacity(false)
                .onBail(false)
                .convicted(false)
                .build())
            .signed(true)
            .thirdParty(thirdParty)
            .build();
    }

    private void straightForwardSummon(User user, JurorPool jurorPool, Juror juror) {
        if (RandomGenerator.nextDouble(0, 1) > paperChange) {//TODO
            straightForwardSummonPaper(user, jurorPool, juror);
        } else {
            straightForwardSummonDigital(user, jurorPool, juror);
        }
    }

    private void straightForwardSummonDigital(User user, JurorPool jurorPool, Juror juror) {
        new PublicEndpointControllerClient()
            .respondToSummons(
                new JwtDetailsPublic(juror),
                getValidJurorResponseDto(juror));

        Juror juror1 =
            transactionTemplate.execute(status -> jurorRepository.findById(juror.getJurorNumber()).orElseThrow());
        if (juror1.isResponded()) {
            return;
        }
        processReplyRespondedDigital(user, jurorPool);
    }


    private void straightForwardSummonPaper(User user, JurorPool jurorPool, Juror juror) {
        new JurorPaperResponseControllerClient()
            .respondToSummons(
                new JwtDetailsBureau(user),
                getValidJurorPaperResponseDto(jurorPool, juror));
        processReplyResponded(user, jurorPool);
    }

    private void deferralSummon(User user, JurorPool jurorPool, Juror juror,
                                LocalDate deferralDate) {
        if (RandomGenerator.nextDouble(0, 1) > paperChange) {
            deferralSummonPaper(user, jurorPool, juror, deferralDate);
        } else {
            deferralSummonDigital(user, jurorPool, juror, deferralDate);
        }
    }

    private void deferralSummonDigital(User user, JurorPool jurorPool, Juror juror, LocalDate deferralDate) {
        JurorResponseDto jurorResponseDto = getValidJurorResponseDto(juror);
        jurorResponseDto.setDeferral(JurorResponseDto.Deferral.builder()
            .dates(deferralDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            .reason("I am busy this day")
            .build());

        new PublicEndpointControllerClient()
            .respondToSummons(
                new JwtDetailsPublic(juror),
                jurorResponseDto);
        processReplyDeferral(user, jurorPool, ReplyMethod.DIGITAL, deferralDate);
    }

    private void deferralSummonPaper(User user, JurorPool jurorPool, Juror juror, LocalDate deferralDate) {
        JurorPaperResponseDto jurorPaperResponseDto =
            getValidJurorPaperResponseDto(jurorPool, juror);
        jurorPaperResponseDto.setDeferral(true);
        new JurorPaperResponseControllerClient()
            .respondToSummons(new JwtDetailsBureau(user), jurorPaperResponseDto);
        processReplyDeferral(user, jurorPool, ReplyMethod.PAPER, deferralDate);
    }

    private void excusalSummon(User user, JurorPool jurorPool, Juror juror) {
        if (RandomGenerator.nextDouble(0, 1) > paperChange) {
            excusalSummonPaper(user, jurorPool, juror);
        } else {
            excusalSummonDigital(user, jurorPool, juror);
        }
    }

    private void excusalSummonDigital(User user, JurorPool jurorPool, Juror juror) {
        JurorResponseDto jurorResponseDto = getValidJurorResponseDto(juror);
        jurorResponseDto.setExcusal(JurorResponseDto.Excusal.builder()
            .reason("I am busy this day")
            .build());

        new PublicEndpointControllerClient()
            .respondToSummons(
                new JwtDetailsPublic(juror),
                jurorResponseDto);
        processReplyExcusal(user, jurorPool, ReplyMethod.DIGITAL);
    }

    private void excusalSummonPaper(User user, JurorPool jurorPool, Juror juror) {
        JurorPaperResponseDto jurorPaperResponseDto =
            getValidJurorPaperResponseDto(jurorPool, juror);
        jurorPaperResponseDto.setExcusal(true);
        new JurorPaperResponseControllerClient()
            .respondToSummons(new JwtDetailsBureau(user), jurorPaperResponseDto);
        processReplyExcusal(user, jurorPool, ReplyMethod.PAPER);
    }


    private String nullOrGenerate(ValueGenerator<String> generator, double chanceOfNull) {
        return nullOrGenerate(generator, "", chanceOfNull);
    }

    private String nullOrGenerate(ValueGenerator<String> generator, String prefix, double chanceOfNull) {
        if (RandomGenerator.nextDouble(0, 1) <= chanceOfNull) {
            return null;
        }
        return prefix + generator.generate();
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
