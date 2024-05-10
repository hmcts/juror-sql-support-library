package uk.gov.hmcts.juror.support.sql.v2.generation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.juror.support.generation.generators.code.Generator;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromCollectionGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.util.RandomGenerator;
import uk.gov.hmcts.juror.support.sql.v1.Util;
import uk.gov.hmcts.juror.support.sql.v1.entity.JurorPool;
import uk.gov.hmcts.juror.support.sql.v2.DataCreator;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.JurorAppearanceDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.PoolRequestDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.jurormanagement.UpdateAttendanceDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.IJurorStatus;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.AppearanceStage;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.jurormanagement.UpdateAttendanceStatus;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.JurorManagementControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generation.dto.CourtDetails;
import uk.gov.hmcts.juror.support.sql.v2.generation.dto.User;
import uk.gov.hmcts.juror.support.sql.v2.spring.repository.AppearanceRepository;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetailsBureau;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Appearance {
    private final AppearanceRepository appearanceRepository;

    public void checkIn(User user, String locCode, LocalDate date, List<JurorPool> jurorPools) {
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

    public void checkOut(User user, String locCode, LocalDate date, List<String> jurorNumbers) {
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

    public void confirm(User user, String locCode, LocalDate date, List<String> jurorNumbers) {
        JurorManagementControllerClient jurorManagementControllerClient = new JurorManagementControllerClient();

        List<List<String>> batches = Util.getBatches(jurorNumbers, 100);
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

    public List<DataCreator.JurorDetails> addAttendances(User user, CourtDetails courtLoc,
                                                         PoolRequestDto poolRequestDto,
                                                         List<JurorPool> jurorPools) {
        JurorManagementControllerClient jurorManagementControllerClient = new JurorManagementControllerClient();
        Map<LocalDate, List<String>> dayToJurorNumber = new HashMap<>();
        final List<DataCreator.JurorDetails> jurorDetails = new ArrayList<>();
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
            jurorDetails.add(
                DataCreator.JurorDetails.builder().jurorPool(jurorPool).attendanceDates(attendanceDates).build());
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
                        .locationCode(courtLoc.getCourtCode())//TODO confirm
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
                        .locationCode(courtLoc.getCourtCode())//TODO confirm
                        .checkOutTime(LocalTime.of(16, 30))
                        .singleJuror(false)
                        .build())
                    .juror(entry.getValue())
                    .build());
        }
        return jurorDetails;
    }


    public void confirmAttendances() {
        List<String> locCodes = new ArrayList<>();
        DataCreator.ENV.getCourts()
            .forEach(courtLoc -> locCodes.addAll(courtLoc.getLocCodes()));

        List<LocalDate> attendanceDates = appearanceRepository.findDistinctAttendanceDate()
            .stream()
            .map(Date::toLocalDate)
            .toList();
        for (String locCode : locCodes) {
            Generator<User> userGenerator =
                new RandomFromCollectionGeneratorImpl<>(DataCreator.ENV.getCourt(locCode).getUsernames());

            attendanceDates
                .parallelStream()
                .forEach(localDate -> {
                    Util.retryElseThrow(() -> {
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

    public void addAttendances(User user, CourtDetails courtLoc, String locCode, LocalDate date,
                               List<JurorPool> jurorPools) {
        log.info("Adding attendances for " + locCode + " on " + date);
        List<String> jurorNumbers = jurorPools.stream().map(JurorPool::getJurorNumber).toList();

        checkIn(user, locCode, date, jurorPools);
        checkOut(user, locCode, date, jurorNumbers);
//TODO        confirm(user, locCode, date, jurorNumbers);
    }

    public void addAttendances(User user, CourtDetails courtLoc, String locCode, LocalDate nextDate,
                               LocalDate trialEnd,
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
}
