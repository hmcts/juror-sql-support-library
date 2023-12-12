package uk.gov.hmcts.juror.support.sql.entity.jurorresponse;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uk.gov.hmcts.juror.support.generation.generators.code.GenerateGenerationConfig;
import uk.gov.hmcts.juror.support.generation.generators.value.NullValueGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromFileGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.RegexGenerator;
import uk.gov.hmcts.juror.support.sql.Constants;

import java.io.Serializable;

@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@GenerateGenerationConfig
public abstract class Address implements Serializable {

    @Column(name = "address_line_1")
    @RegexGenerator(regex = Constants.ADDRESS_LINE_1_REGEX)
    private String addressLine1;

    @Column(name = "address_line_2")
    @RegexGenerator(regex = Constants.ADDRESS_LINE_2_REGEX)
    private String addressLine2;

    @Column(name = "address_line_3")
    @RandomFromFileGenerator(file = "data/city.txt")
    private String addressLine3;

    @Column(name = "address_line_4")
    @RandomFromFileGenerator(file = "data/county.txt")
    private String addressLine4;

    @Column(name = "address_line_5")
    @NullValueGenerator
    private String addressLine5;

    @Column(name = "postcode")
    @RandomFromFileGenerator(file = "data/postcode.txt")
    private String postcode;

}

