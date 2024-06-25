package uk.gov.hmcts.juror.support.sql.v2.generation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.juror.support.generation.generators.code.Generator;
import uk.gov.hmcts.juror.support.generation.generators.value.FirstNameGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.LastNameGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.LocalDateGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromCollectionGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromFileGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.generators.value.RegexGeneratorImpl;
import uk.gov.hmcts.juror.support.generation.util.RandomGenerator;
import uk.gov.hmcts.juror.support.sql.v1.Util;
import uk.gov.hmcts.juror.support.sql.v1.entity.Juror;
import uk.gov.hmcts.juror.support.sql.v1.entity.JurorPool;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.juror.controller.request.JurorResponseDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.JurorPaperResponseDto;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.ReplyMethod;
import uk.gov.hmcts.juror.support.sql.v2.support.Constants;
import uk.gov.hmcts.juror.support.sql.v2.support.ReasonableAdjustmentsEnum;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JurorResponse {
    private static final RandomFromFileGeneratorImpl emailSuffixGenerator =
        new RandomFromFileGeneratorImpl("data/emailsSuffix.txt");
    private static final LocalDateGeneratorImpl dateOfBirthGenerator = new LocalDateGeneratorImpl(
        LocalDate.now().minusYears(75),
        LocalDate.now().minusYears(18));
    private static final RegexGeneratorImpl phoneGenerator = new RegexGeneratorImpl(false, Constants.PHONE_REGEX);
    private static final RegexGeneratorImpl phoneGenerator2 = new RegexGeneratorImpl(false, "[0-9]{8,15}");
    private static final Generator<String> nameGenerator = new FirstNameGeneratorImpl().addPostGenerate(string ->
        string + " " + new LastNameGeneratorImpl().generate());
    private static final FirstNameGeneratorImpl firstNameGenerator = new FirstNameGeneratorImpl();
    private static final LastNameGeneratorImpl lastNameGenerator = new LastNameGeneratorImpl();


    public JurorResponseDto getValidJurorResponseDto(Juror juror) {
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
            .secondaryPhone(Util.nullOrGenerate(phoneGenerator2, 0.2))
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

    public JurorPaperResponseDto getValidJurorPaperResponseDto(JurorPool jurorPool, Juror juror) {

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
            .primaryPhone(Util.nullOrGenerate(phoneGenerator, 0.05))
            .secondaryPhone(Util.nullOrGenerate(phoneGenerator, 0.2))
            .emailAddress(Util.nullOrGenerate(emailSuffixGenerator,
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
}
