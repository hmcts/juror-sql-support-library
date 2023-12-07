package uk.gov.hmcts.juror.support.sql.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Entity representing standing data for a court location.
 */
@Getter
@Setter
@Entity
@Table(name = "court_location", schema = "juror_mod")
@SuppressWarnings("PMD.TooManyFields")
public class CourtLocation implements Serializable {

    @Id
    @Column(name = "loc_code")
    private String locCode;

    @Column(name = "loc_name")
    private String name;

    @Column(name = "loc_court_name")
    private String locCourtName;

    @Column(name = "loc_attend_time")
    private String courtAttendTime;

    @Column(name = "loc_address1")
    private String address1;

    @Column(name = "loc_address2")
    private String address2;

    @Column(name = "loc_address3")
    private String address3;

    @Column(name = "loc_address4")
    private String address4;

    @Column(name = "loc_address5")
    private String address5;

    @Column(name = "loc_address6")
    private String address6;

    @Column(name = "loc_zip")
    private String postcode;

    @Column(name = "loc_phone")
    private String locPhone;

    @Column(name = "jury_officer_phone")
    private String juryOfficerPhone;
    @Column(name = "location_address")
    private String locationAddress;

    @Column(name = "yield")
    private BigDecimal yield;

    @Column(name = "voters_lock")
    private Integer votersLock;

//    @ManyToOne
//    @JoinColumn(name = "region_id")
//    private CourtRegion courtRegion;

    @Column(name = "term_of_service")
    private String insertIndicators;

    @Column(name = "tdd_phone")
    private String courtFaxNo;

    @Column(name = "loc_signature")
    private String signatory;

    @OneToMany(mappedBy = "courtLocation")
    private List<PoolRequest> poolRequests;

    @Column(name = "owner")
    private String owner;

}
