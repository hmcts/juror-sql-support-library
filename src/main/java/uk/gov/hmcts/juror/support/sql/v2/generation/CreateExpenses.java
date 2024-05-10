package uk.gov.hmcts.juror.support.sql.v2.generation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.juror.support.generation.generators.code.Generator;
import uk.gov.hmcts.juror.support.generation.generators.value.LocalTimeGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromCollectionGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.util.RandomGenerator;
import uk.gov.hmcts.juror.support.sql.v1.Util;
import uk.gov.hmcts.juror.support.sql.v1.entity.JurorPool;
import uk.gov.hmcts.juror.support.sql.v1.repository.JurorPoolRepository;
import uk.gov.hmcts.juror.support.sql.v1.repository.PoolRequestRepository;
import uk.gov.hmcts.juror.support.sql.v2.DataCreator;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.ApproveExpenseDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.DateDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.ExpenseType;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.draft.DailyExpense;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.draft.DailyExpenseFinancialLoss;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.draft.DailyExpenseFoodAndDrink;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.draft.DailyExpenseTime;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.draft.DailyExpenseTravel;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.expense.PendingApprovalList;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.IJurorStatus;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.AppearanceStage;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.FoodDrinkClaimType;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.PayAttendanceType;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.PaymentMethod;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.JurorExpenseControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generation.dto.CourtDetails;
import uk.gov.hmcts.juror.support.sql.v2.generation.dto.User;
import uk.gov.hmcts.juror.support.sql.v2.spring.entity.Appearance;
import uk.gov.hmcts.juror.support.sql.v2.spring.repository.AppearanceRepository;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetailsBureau;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CreateExpenses {
    private final JurorPoolRepository jurorPoolRepository;
    private final PoolRequestRepository poolRequestRepository;
    private final AppearanceRepository appearanceRepository;

    public void addExpenses() {
        AtomicLong counter = new AtomicLong(0);

        List<JurorPool> pools = jurorPoolRepository.findAllByStatus(IJurorStatus.COMPLETED);
        final long size = pools.size();

        pools
            .forEach(jurorPool -> Util.retryElseThrow(() -> {
                log.info("Processing expenses: " + counter.incrementAndGet() + " of " + size);
                String locCode = jurorPool.getLocCode(poolRequestRepository);
                CourtDetails courtLoc = DataCreator.ENV.getCourt(locCode);
                User user = new RandomFromCollectionGeneratorImpl<>(courtLoc.getUsernames()).generate();
                addExpensesByAppearance(user, courtLoc, locCode,
                    appearanceRepository.findAllByPoolNumberAndJurorNumberAndAppearanceStage(
                        jurorPool.getPoolNumber(),
                        jurorPool.getJurorNumber(),
                        AppearanceStage.EXPENSE_ENTERED));
            }, 1, false));
    }

    public void addExpensesByAppearance(User user, CourtDetails courtLoc, String locCode,
                                        List<uk.gov.hmcts.juror.support.sql.v2.spring.entity.Appearance> appearances) {
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
            final String locCode;
            final List<uk.gov.hmcts.juror.support.sql.v2.spring.entity.Appearance> appearances = new ArrayList<>();
        }

        Map<String, Data> jurorDataMap = new HashMap<>();


        for (uk.gov.hmcts.juror.support.sql.v2.spring.entity.Appearance appearance : appearances) {
            Util.retryElseThrow(() -> {
                Data data = jurorDataMap.computeIfAbsent(appearance.getJurorNumber() + "-" + appearance.getLocCode(),
                    k -> new Data(appearance.getJurorNumber(), appearance.getLocCode()));
                data.appearances.add(appearance);


                TravelMethod travelMethod = travelMethodGenerator.generate();

                jurorExpenseControllerClient
                    .postEditDailyExpense(
                        new JwtDetailsBureau(user),
                        appearance.getLocCode(),
                        ExpenseType.DRAFT,
                        appearance.getJurorNumber(),
                        List.of(DailyExpense.builder()
                            .dateOfExpense(appearance.getAttendanceDate())
                            .paymentMethod(PaymentMethod.BACS)
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
                            .build()));
            }, 1, false);
        }

        for (Data data : jurorDataMap.values()) {
            //Submit for approval
            jurorExpenseControllerClient
                .submitDraftExpensesForApproval(
                    new JwtDetailsBureau(user),
                    data.getLocCode(),
                    data.getJurorNumber(),
                    DateDto.builder()
                        .dates(data.getAppearances()
                            .stream()
                            .map(Appearance::getAttendanceDate)
                            .toList())
                        .build()
                );

            //Approve expenses
            PendingApprovalList pendingApprovalList = jurorExpenseControllerClient
                .getExpensesForApproval(
                    new JwtDetailsBureau(courtLoc.getExpenseApprove()),
                    locCode,
                    PaymentMethod.BACS,
                    null,
                    null);

            if (pendingApprovalList.getPendingApproval().isEmpty()) {
                return;
            }

            jurorExpenseControllerClient
                .approveExpenses(
                    new JwtDetailsBureau(courtLoc.getExpenseApprove()),
                    locCode,
                    PaymentMethod.BACS,
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

    public void addExpenses(User user, CourtDetails courtLoc, String locCode,
                            List<DataCreator.JurorDetails> jurorDetails) {
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

        for (DataCreator.JurorDetails jurorDetail : jurorDetails) {
            log.info("Adding expenses for juror " + jurorDetail.getJurorPool().getJurorNumber());

            TravelMethod travelMethod = travelMethodGenerator.generate();

            for (LocalDate attendanceDate : jurorDetail.getAttendanceDates()) {
                jurorExpenseControllerClient
                    .postEditDailyExpense(
                        new JwtDetailsBureau(user),
                        locCode,
                        ExpenseType.DRAFT,
                        jurorDetail.getJurorPool().getJurorNumber(),
                        List.of(DailyExpense.builder()
                            .dateOfExpense(attendanceDate)
                            .paymentMethod(PaymentMethod.BACS)
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
                            .build()));
            }

            //Submit for approval
            jurorExpenseControllerClient
                .submitDraftExpensesForApproval(
                    new JwtDetailsBureau(user),
                    locCode,
                    jurorDetail.getJurorPool().getJurorNumber(),
                    DateDto.builder()
                        .dates(jurorDetail.getAttendanceDates())
                        .build()
                );

            //Approve expenses

            PendingApprovalList pendingApprovalList = jurorExpenseControllerClient
                .getExpensesForApproval(
                    new JwtDetailsBureau(courtLoc.getExpenseApprove()),
                    locCode,
                    PaymentMethod.BACS,
                    null,
                    null);


            jurorExpenseControllerClient
                .approveExpenses(
                    new JwtDetailsBureau(courtLoc.getExpenseApprove()),
                    locCode,
                    PaymentMethod.BACS,
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
}
