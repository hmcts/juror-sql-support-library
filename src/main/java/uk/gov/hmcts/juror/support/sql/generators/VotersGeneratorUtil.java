package uk.gov.hmcts.juror.support.sql.generators;

import uk.gov.hmcts.juror.support.generation.generators.value.FixedValueGeneratorImpl;
import uk.gov.hmcts.juror.support.sql.dto.JurorAccountDetailsDto;
import uk.gov.hmcts.juror.support.sql.entity.Juror;
import uk.gov.hmcts.juror.support.sql.entity.JurorPool;
import uk.gov.hmcts.juror.support.sql.entity.UserGenerator;
import uk.gov.hmcts.juror.support.sql.entity.VotersGenerator;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

public class VotersGeneratorUtil {

    private static final AtomicLong counter = new AtomicLong(0);


    public static VotersGenerator fromJurorAccountDetailsDto(JurorAccountDetailsDto jurorAccountDetailsDto) {
        Juror juror = jurorAccountDetailsDto.getJuror();
        JurorPool jurorPool = jurorAccountDetailsDto.getJurorPools().get(0);
        VotersGenerator generator = new VotersGenerator();
        generator.setLocCode(new FixedValueGeneratorImpl<>(jurorPool.getLocation()));
        generator.setJurorNumber(new FixedValueGeneratorImpl<>(juror.getJurorNumber()));
        generator.setTitle(new FixedValueGeneratorImpl<>(juror.getTitle()));

        generator.setAddress(new FixedValueGeneratorImpl<>(juror.getAddressLine1()));
        generator.setAddress2(new FixedValueGeneratorImpl<>(juror.getAddressLine2()));
        generator.setAddress3(new FixedValueGeneratorImpl<>(juror.getAddressLine3()));
        generator.setAddress4(new FixedValueGeneratorImpl<>(juror.getAddressLine4()));
        generator.setAddress5(new FixedValueGeneratorImpl<>(juror.getAddressLine5()));
        generator.setPostcode(new FixedValueGeneratorImpl<>(juror.getPostcode()));
        generator.setPollNumber(new FixedValueGeneratorImpl<>(juror.getPollNumber()));
        return generator;
    }
}
