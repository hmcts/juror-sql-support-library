package uk.gov.hmcts.juror.support.sql.v2;


import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.juror.support.sql.v1.entity.CourtLocation;
import uk.gov.hmcts.juror.support.sql.v1.entity.JurorPool;
import uk.gov.hmcts.juror.support.sql.v1.repository.CourtLocationRepository;
import uk.gov.hmcts.juror.support.sql.v2.generation.Appearance;
import uk.gov.hmcts.juror.support.sql.v2.generation.CompleteService;
import uk.gov.hmcts.juror.support.sql.v2.generation.CreateCourtRooms;
import uk.gov.hmcts.juror.support.sql.v2.generation.CreateExpenses;
import uk.gov.hmcts.juror.support.sql.v2.generation.CreateJudges;
import uk.gov.hmcts.juror.support.sql.v2.generation.CreatePools;
import uk.gov.hmcts.juror.support.sql.v2.generation.CreateTrials;
import uk.gov.hmcts.juror.support.sql.v2.generation.Jury;
import uk.gov.hmcts.juror.support.sql.v2.generation.UpdatePoliceChecks;
import uk.gov.hmcts.juror.support.sql.v2.generation.Voters;
import uk.gov.hmcts.juror.support.sql.v2.generation.dto.CourtDetails;
import uk.gov.hmcts.juror.support.sql.v2.generation.dto.Judge;
import uk.gov.hmcts.juror.support.sql.v2.generation.dto.User;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.CourtRoomEntity;
import uk.gov.hmcts.juror.support.sql.v2.spring.repository.CourtroomRepository;
import uk.gov.hmcts.juror.support.sql.v2.spring.repository.JudgeRepository;
import uk.gov.hmcts.juror.support.sql.v2.spring.repository.UserV2Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DataCreator {


    public static final Environment ENV = Environment.DEMO;


    public static RestTemplateBuilder restTemplateBuilder;
    private final CourtLocationRepository courtLocationRepository;
    private final JudgeRepository judgeRepository;
    private final UserV2Repository userRepository;
    private final CreatePools createPools;
    private final CreateJudges createJudges;
    private final CreateCourtRooms createCourtRooms;
    private final UpdatePoliceChecks updatePoliceChecks;
    private final CreateTrials createTrials;
    private final Appearance appearance;
    private final CompleteService completeService;
    private final CreateExpenses createExpenses;
    private final Jury jury;

    private final Voters voters;
    private final CourtroomRepository courtroomRepository;

    @Autowired
    public DataCreator(RestTemplateBuilder restTemplateBuilder,
                       Voters voters,
                       CourtLocationRepository courtLocationRepository,
                       JudgeRepository judgeRepository,
                       UserV2Repository userRepository,
                       CreatePools createPools, CreateJudges createJudges,
                       CreateCourtRooms createCourtRooms, UpdatePoliceChecks updatePoliceChecks,
                       CreateTrials createTrials, Appearance appearance, CompleteService completeService,
                       CreateExpenses createExpenses, Jury jury, CourtroomRepository courtroomRepository) {
        DataCreator.restTemplateBuilder = restTemplateBuilder;
        this.voters = voters;

        this.updatePoliceChecks = updatePoliceChecks;
        this.judgeRepository = judgeRepository;
        this.courtLocationRepository = courtLocationRepository;

//        this.poolDates = new LocalDateGeneratorImpl(LocalDate.now(), LocalDate.now().plusYears(1).minusMonths(1))
//            .addPostGenerate(localDate -> {
//                return localDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
//            })
//            .generate(20L);
        this.userRepository = userRepository;
        this.createPools = createPools;
        this.createJudges = createJudges;
        this.createCourtRooms = createCourtRooms;
        this.createTrials = createTrials;
        this.appearance = appearance;
        this.completeService = completeService;
        this.createExpenses = createExpenses;
        this.jury = jury;
        this.courtroomRepository = courtroomRepository;
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
                    .usernames(new ArrayList<>(userRepository.findAllWithCourt(courtLocation.getOwner())
                        .stream()
                        .map(User::new)
                        .distinct()
                        .toList()))
                    .judges(judgeRepository.findAllByOwner(courtLocation.getOwner())
                        .stream()
                        .map(Judge::new)
                        .toList())
                    .courtRooms(courtroomRepository.findByCourtLocationOwner(courtLocation.getOwner())
                        .stream()
                        .map(CourtRoomEntity::toCourtRoom)
                        .toList())
                    .build()
            );
        }
        log.info("Court details loaded.");
    }

    @PostConstruct
    @SneakyThrows
    public void init() {
        log.info("DataCreator Started");
        addCourts();
        DataCreator.ENV.setup();//Required at start

        if (ENV.isCreateVoters()) {
            this.voters.createVoters(DataCreator.ENV.getCourts(), 10_000);
            return;
        }

        if (ENV.isCreateJudges()) {
            createJudges.createJudges(10);
        }

        if (ENV.isCreateCourtrooms()) {
            createCourtRooms.createCourtrooms(10);
        }

        if (ENV.isShouldCreatePools()) {
            createPools.createPools();
        }

        if (ENV.isAssignPoliceChecks()) {
            updatePoliceChecks.updatePoliceChecks();
        }

        if (ENV.isRunCourtSteps()) {
            runCourtSteps();
        }
        log.info("DataCreator Completed");
    }


    private void runCourtSteps() {
        log.info("Running court steps");
        if (DataCreator.ENV.isCreateTrials()) {
            createTrials.createTrials();
        }

        if (DataCreator.ENV.isCompleteJurors()) {
            jury.completeJurorsStatusJuror();
        }

        if (DataCreator.ENV.isCompleteNoneJuror()) {
            completeService.completeJurorsStatusNoneJuror();
        }
        if (DataCreator.ENV.isConfirmAttendances()) {
            //Need to confirm logic here
            appearance.confirmAttendances();
        }
        if (DataCreator.ENV.isCompleteService()) {
            completeService.completeService();
        }
        if (DataCreator.ENV.isAddExpenses()) {
            createExpenses.addExpenses();
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
    public static class JurorNumberPoolNumber {
        String jurorNumber;
        String poolNumber;

        public JurorNumberPoolNumber(String jurorNumber, String poolNumber) {
            this.jurorNumber = jurorNumber;
            this.poolNumber = poolNumber;
        }
    }

    @AllArgsConstructor
    @Builder
    @Getter
    public static class JurorDetails {
        private JurorPool jurorPool;
        private List<LocalDate> attendanceDates;
    }


}
