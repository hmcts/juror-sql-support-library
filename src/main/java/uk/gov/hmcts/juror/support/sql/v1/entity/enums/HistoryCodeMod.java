package uk.gov.hmcts.juror.support.sql.v1.entity.enums;

import lombok.Getter;

@Getter
public enum HistoryCodeMod {

    MANUAL_JUROR_AUTHORISATION("AUTH", "Manual Juror Authorisation"),
    POLICE_CHECK_UNDO("POLX", "Police Check - Undo"),
    NOTIFY_MESSAGE_REQUESTED("RNOT", "Notify Message Requested"),
    INCLUDED_IN_MESSAGE_EXPORT_FILE("RMES", "Included in Message Export File"),
    ARAMIS_EXPENSES_FILE_CREATED("AEDF", "Aramis Expenses File Created"),
    EDIT_ATTENDANCE("AEDT", "Edit Attendance"),
    JURY_ATTENDANCE("AJUR", "Jury Attendance"),
    ATTENDANCE_LETTER("ALET", "Attendance Letter"),
    POOL_ATTENDANCE("APOL", "Pool Attendance"),
    AWAITING_FURTHER_INFORMATION("AWFI", "Awaiting Further Information"),
    CHECK_ID("CHID", "Check ID"),
    CHANGE_DEFERRAL_RECORD("DCHG", "Change Deferral Record"),
    POOL_DELETED("DELP", "Pool Deleted"),
    RETURN_TO_VOTERS("DRET", "Return to Voters"),
    APPEARANCE_PAYMENTS("FADD", "Appearance Payments"),
    EDIT_PAYMENTS("FEDT", "Edit Payments"),
    CHANGE_NEXT_REPORT_DATE("PCHD", "Change Next Report Date"),
    DEFERRED_POOL_MEMBER("PDEF", "Deferred Pool Member"),
    DISQUALIFY_POOL_MEMBER("PDIS", "Disqualify Pool Member"),
    POOL_EDIT("PEDT", "Pool Edit"),
    EXCUSE_POOL_MEMBER("PEXC", "Excuse Pool Member"),
    CHANGE_POOL_JURISDICTION("PJUR", "Change Pool Jurisdiction"),
    AWAITING_RESULT_OF_POLICE_CHECK("POLA", "Awaiting Result of Police Check"),
    POLICE_CHECK_ON_BAIL("POLB", "Police Check - On Bail"),
    POLICE_CHECK_CRIMINAL_RECORD("POLC", "Police Check - Criminal Record"),
    POLICE_CHECK_NO_RECORD("POLD", "Police Check - No Record"),
    SERVICE_POSTPONED("POST", "Service Postponed"),
    REASSIGN_POOL_MEMBER("PREA", "Reassign Pool Member"),
    TRANSFER_POOL_MEMBER("PTRA", "Transfer Pool Member"),
    CHANGE_POOL_TYPE("PTYP", "Change Pool Type"),
    MAKE_UNAVAILABLE("PUNA", "Make Unavailable"),
    UNDELIVERED_SUMMONS("PUND", "Undelivered Summons"),
    CERTIFICATE_OF_RECOGNITION("RCER", "Certificate of Recognition"),
    NON_DEFERRED_LETTER("RDDL", "Non-Deferred Letter"),
    DEFERRED_LETTER("RDEF", "Deferred Letter"),
    WITHDRAWAL_LETTER("RDIS", "Withdrawal Letter"),
    WORD_PROCESSING_DOCUMENTS("RDOC", "Word Processing Documents"),
    NON_EXCUSED_LETTER("REDL", "Non-Excused Letter"),
    EXCUSED_LETTER("REXC", "Excused Letter"),
    SHOW_CAUSE_LETTER("RSHC", "Show Cause Letter"),
    FAILED_TO_ATTEND_LETTER("RFTA", "Failed To Attend Letter"),
    NON_RESPONDED_LETTER("RNRE", "Non Responded Letter"),
    POSTPONED_LETTER("RPST", "Postponed Letter"),
    RESPONDED_LETTER("RRES", "Responded Letter"),
    PRINT_SUMMONS("RSUM", "Print Summons"),
    SUMMONS_REPRINTED("RSUP", "Summons Reprinted"),
    COMPLETE_SERVICE("SCOM", "Complete Service"),
    JURY_EMPANELMENT("TADD", "Jury Empanelment"),
    ADD_TO_PANEL("VADD", "Add To Panel"),
    CREATE_NEW_PANEL("VCRE", "Create New Panel"),
    REASSIGN_PANEL("VREA", "Reassign Panel"),
    RETURN_PANEL("VRET", "Return Panel"),
    RESPONDED_POSITIVELY("RESP", "Responded Positively"),
    CERTIFICATE_OF_EXEMPTION("REXE", "Certificate of Exemption"),
    CASH_PAYMENT_APPROVAL("CASH", "Cash Payment Approval"),
    NUMBER_OF_SUMMONS_ISSUED("PHSI", "Number of Summons Issued"),
    NUMBER_OF_DEFERRALS_IN("PHDI", "Number of Deferrals in"),
    NUMBER_OF_REMINDERS_SENT("PHRS", "Number of Reminders Sent"),
    CHANGE_PERSONAL_DETAILS("PDET", "Change Personal Details"),
    DELETE_REQUEST_MORE_INFO("DWFI", "Delete Request More Info"),
    POLICE_CHECK_REQUEST("POLE", "Elec. Pol. Check Request"),
    POLICE_CHECK_FAILED("POLF", "Elec. Pol. Check Failed"),
    POLICE_CHECK_COMPLETE("POLG", "Elec. Pol. Check Complete"),
    INSUFFICIENT_INFORMATION("POLI", "Insufficient Information"),
    FAILED_TO_ATTEND("PFTA", "Failed To Attend"),
    CHANGE_POOL_REQUEST_DETAILS("PREQ", "Change Pool Request Details"),
    PENDING_JUROR_AUTHORISED("AUTH", "Pending Juror Authorised");

    private final String code;
    private final String description;

    HistoryCodeMod(String code, String description) {
        this.code = code;
        this.description = description;
    }

}
