package uk.gov.hmcts.juror.support.sql.v2.generation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromCollectionGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.util.RandomGenerator;
import uk.gov.hmcts.juror.support.sql.v1.Util;
import uk.gov.hmcts.juror.support.sql.v1.entity.JurorPool;
import uk.gov.hmcts.juror.support.sql.v1.repository.JurorPoolRepository;
import uk.gov.hmcts.juror.support.sql.v1.repository.PoolRequestRepository;
import uk.gov.hmcts.juror.support.sql.v2.DataCreator;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.CompleteServiceJurorNumberListDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.clients.CompleteServiceControllerClient;
import uk.gov.hmcts.juror.support.sql.v2.generation.dto.CourtDetails;
import uk.gov.hmcts.juror.support.sql.v2.generation.dto.User;
import uk.gov.hmcts.juror.support.sql.v2.spring.dto.JurorNumberLocCodeAndDate;
import uk.gov.hmcts.juror.support.sql.v2.spring.repository.AppearanceRepository;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetailsBureau;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CompleteService {

    private final Appearance appearance;
    private final AppearanceRepository appearanceRepository;
    private final JurorPoolRepository jurorPoolRepository;
    private final PoolRequestRepository poolRequestRepository;

    public void completeService(User courtUser, List<JurorPool> jurorPools, LocalDate completionDate) {
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

    public void completeService(User courtUser, String poolNumber, List<String> jurorNumbers,
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

    public void completeService() {
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
                Util.retryElseThrow(() -> {
                    User user = new RandomFromCollectionGeneratorImpl<>(
                        DataCreator.ENV.getCourt(jurorNumberLocCodeAndDate.getLocCode()).getUsernames()).generate();
                    completeService(
                        user,
                        jurorNumberLocCodeAndDate.getPoolNumber(),
                        List.of(jurorNumberLocCodeAndDate.getJurorNumber()),
                        jurorNumberLocCodeAndDate.getMaxAttendanceDate()
                    );
                }, 1, false);
            });
    }

    public void completeJurorsStatusNoneJuror() {
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
            CourtDetails courtLoc = DataCreator.ENV.getCourt(locCode);
            User courtUser = new RandomFromCollectionGeneratorImpl<>(courtLoc.getUsernames()).generate();

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
                    Util.retryElseThrow(() -> {
                        appearance.addAttendances(
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
}
