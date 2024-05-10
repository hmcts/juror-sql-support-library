package uk.gov.hmcts.juror.support.sql.v2.spring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.juror.support.sql.v1.entity.CourtLocation;
import uk.gov.hmcts.juror.support.sql.v2.generation.dto.CourtRoom;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "courtroom", schema = "juror_mod")
public class CourtRoomEntity {
    @Id
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "loc_code", nullable = false, updatable = false)
    private CourtLocation courtLocation;


    @Column(name = "room_number")
//    @Length(max = 6)
    private String roomNumber;

    @Column(name = "description")
//    @Length(max = 30)
    private String description;

    public CourtRoom toCourtRoom() {
        return CourtRoom.builder()
            .id(id)
            .courtLocation(courtLocation)
            .roomNumber(roomNumber)
            .description(description)
            .build();
    }
}
