package uk.gov.hmcts.juror.support.sql.v2.generation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import uk.gov.hmcts.juror.support.generation.util.RandomGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@ToString
public class CourtDetails {
    public static final Map<String, String> OWNER_TO_CATCHMENT_PREFIX;

    static {
        OWNER_TO_CATCHMENT_PREFIX = new HashMap<>();
        OWNER_TO_CATCHMENT_PREFIX.put("445", "AA");
        OWNER_TO_CATCHMENT_PREFIX.put("450", "AB");
        OWNER_TO_CATCHMENT_PREFIX.put("471", "AC");
        OWNER_TO_CATCHMENT_PREFIX.put("415", "AD");
        OWNER_TO_CATCHMENT_PREFIX.put("470", "AE");
        OWNER_TO_CATCHMENT_PREFIX.put("472", "AF");
        OWNER_TO_CATCHMENT_PREFIX.put("432", "AG");
        OWNER_TO_CATCHMENT_PREFIX.put("457", "AH");
        OWNER_TO_CATCHMENT_PREFIX.put("433", "AK");
        OWNER_TO_CATCHMENT_PREFIX.put("411", "AL");
        OWNER_TO_CATCHMENT_PREFIX.put("454", "AM");
        OWNER_TO_CATCHMENT_PREFIX.put("419", "AN");
        OWNER_TO_CATCHMENT_PREFIX.put("426", "AO");
        OWNER_TO_CATCHMENT_PREFIX.put("423", "AP");
        OWNER_TO_CATCHMENT_PREFIX.put("421", "AQ");
        OWNER_TO_CATCHMENT_PREFIX.put("427", "AR");
        OWNER_TO_CATCHMENT_PREFIX.put("463", "AS");
        OWNER_TO_CATCHMENT_PREFIX.put("430", "AT");
        OWNER_TO_CATCHMENT_PREFIX.put("449", "AU");
        OWNER_TO_CATCHMENT_PREFIX.put("420", "AV");
        OWNER_TO_CATCHMENT_PREFIX.put("460", "AW");
        OWNER_TO_CATCHMENT_PREFIX.put("455", "AX");
        OWNER_TO_CATCHMENT_PREFIX.put("402", "AY");
        OWNER_TO_CATCHMENT_PREFIX.put("465", "BA");
        OWNER_TO_CATCHMENT_PREFIX.put("440", "BB");
        OWNER_TO_CATCHMENT_PREFIX.put("448", "BC");
        OWNER_TO_CATCHMENT_PREFIX.put("469", "BD");
        OWNER_TO_CATCHMENT_PREFIX.put("435", "BE");
        OWNER_TO_CATCHMENT_PREFIX.put("477", "BF");
        OWNER_TO_CATCHMENT_PREFIX.put("452", "BG");
        OWNER_TO_CATCHMENT_PREFIX.put("410", "BH");
        OWNER_TO_CATCHMENT_PREFIX.put("468", "BK");
        OWNER_TO_CATCHMENT_PREFIX.put("458", "BL");
        OWNER_TO_CATCHMENT_PREFIX.put("444", "BM");
        OWNER_TO_CATCHMENT_PREFIX.put("409", "BN");
        OWNER_TO_CATCHMENT_PREFIX.put("436", "BO");
        OWNER_TO_CATCHMENT_PREFIX.put("417", "BP");
        OWNER_TO_CATCHMENT_PREFIX.put("461", "BQ");
        OWNER_TO_CATCHMENT_PREFIX.put("799", "BR");
        OWNER_TO_CATCHMENT_PREFIX.put("443", "BS");
        OWNER_TO_CATCHMENT_PREFIX.put("475", "BT");
        OWNER_TO_CATCHMENT_PREFIX.put("437", "BU");
        OWNER_TO_CATCHMENT_PREFIX.put("447", "BV");
        OWNER_TO_CATCHMENT_PREFIX.put("404", "BW");
        OWNER_TO_CATCHMENT_PREFIX.put("407", "BX");
        OWNER_TO_CATCHMENT_PREFIX.put("453", "BY");
        OWNER_TO_CATCHMENT_PREFIX.put("476", "CA");
        OWNER_TO_CATCHMENT_PREFIX.put("769", "CB");
        OWNER_TO_CATCHMENT_PREFIX.put("406", "CC");
        OWNER_TO_CATCHMENT_PREFIX.put("424", "CD");
        OWNER_TO_CATCHMENT_PREFIX.put("439", "CE");
        OWNER_TO_CATCHMENT_PREFIX.put("428", "CF");
        OWNER_TO_CATCHMENT_PREFIX.put("466", "CG");
        OWNER_TO_CATCHMENT_PREFIX.put("478", "CH");
        OWNER_TO_CATCHMENT_PREFIX.put("403", "CK");
        OWNER_TO_CATCHMENT_PREFIX.put("480", "CL");
        OWNER_TO_CATCHMENT_PREFIX.put("416", "CM");
        OWNER_TO_CATCHMENT_PREFIX.put("474", "CN");
        OWNER_TO_CATCHMENT_PREFIX.put("422", "CO");
        OWNER_TO_CATCHMENT_PREFIX.put("413", "CP");
        OWNER_TO_CATCHMENT_PREFIX.put("456", "CQ");
        OWNER_TO_CATCHMENT_PREFIX.put("414", "CR");
        OWNER_TO_CATCHMENT_PREFIX.put("425", "CS");
        OWNER_TO_CATCHMENT_PREFIX.put("431", "CT");
        OWNER_TO_CATCHMENT_PREFIX.put("467", "CU");
        OWNER_TO_CATCHMENT_PREFIX.put("434", "CV");
        OWNER_TO_CATCHMENT_PREFIX.put("418", "CW");
        OWNER_TO_CATCHMENT_PREFIX.put("626", "CX");
        OWNER_TO_CATCHMENT_PREFIX.put("408", "CY");
        OWNER_TO_CATCHMENT_PREFIX.put("473", "DA");
        OWNER_TO_CATCHMENT_PREFIX.put("429", "DB");
        OWNER_TO_CATCHMENT_PREFIX.put("459", "DC");
        OWNER_TO_CATCHMENT_PREFIX.put("446", "DD");
        OWNER_TO_CATCHMENT_PREFIX.put("479", "DE");
        OWNER_TO_CATCHMENT_PREFIX.put("442", "DF");
        OWNER_TO_CATCHMENT_PREFIX.put("401", "DG");
        OWNER_TO_CATCHMENT_PREFIX.put("451", "DH");
        OWNER_TO_CATCHMENT_PREFIX.put("412", "DK");
    }


    String courtCode;
    List<String> locCodes;

    List<String> catchmentAreas;
    User expenseApprove;
    List<User> usernames;
    List<Judge> judges;

    Map<String, List<CourtRoom>> courtRoomMap;

    public static List<String> getCatchmentAreas(String owner) {
        String prefix = OWNER_TO_CATCHMENT_PREFIX.get(owner);
        return List.of(
            prefix + "1",
            prefix + "2",
            prefix + "3"
        );
    }

    public List<CourtRoom> getCourtRooms(String roomCode) {
        return courtRoomMap.get(roomCode);
    }

    public CourtDetails(String courtCode, List<String> locCodes, List<String> catchmentAreas,
                        User expenseApprove,
                        List<User> usernames, List<Judge> judges,
                        Map<String, List<CourtRoom>> courtRoomMap) {
        this.courtCode = courtCode;
        this.locCodes = locCodes;
        this.catchmentAreas = catchmentAreas;
        this.expenseApprove = expenseApprove;
        this.usernames = usernames;
        this.judges = judges;
        this.courtRoomMap = courtRoomMap;

        if (locCodes != null && this.expenseApprove != null) {
            locCodes.forEach(string -> this.expenseApprove.addCourt(string));
        }
    }

    public List<String> getPostcodes() {
        List<String> postCodes = new ArrayList<>();
        int size = 1;
        while (size <= this.getCatchmentAreas().size()) {
            if (RandomGenerator.nextBoolean()) {
                postCodes.add(this.getCatchmentAreas().get(size - 1));
            }
            size++;
            if (size == this.getCatchmentAreas().size() && postCodes.isEmpty()) {
                size = 1;
            }
        }
        return postCodes;
    }
}