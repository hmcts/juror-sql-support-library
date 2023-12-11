package uk.gov.hmcts.juror.support.sql.entity.jurorresponse;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import uk.gov.hmcts.juror.support.generation.generators.value.NullValueGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.RandomFromFileGenerator;
import uk.gov.hmcts.juror.support.generation.generators.value.RegexGenerator;
import uk.gov.hmcts.juror.support.sql.Constants;

import java.io.Serializable;

@MappedSuperclass
@Getter
@Setter
@EqualsAndHashCode
public abstract class Address implements Serializable {

    @Column(name = "address_line_1")
    @NotBlank
    @RegexGenerator(regex = Constants.ADDRESSONE_REGEX)
    private String addressLine1;

    @Column(name = "address_line_2")
    @RegexGenerator(regex = Constants.ADDRESSTWO_REGEX)
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

