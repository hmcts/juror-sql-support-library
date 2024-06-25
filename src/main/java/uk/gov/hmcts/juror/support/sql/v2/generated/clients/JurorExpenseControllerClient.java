package uk.gov.hmcts.juror.support.sql.v2.generated.clients;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import uk.gov.hmcts.juror.support.sql.v2.clients.BaseClient;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.expense.DailyExpenseResponse;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.expense.GetEnteredExpenseResponse;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.expense.UnpaidExpenseSummaryResponseDto;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetails;

import java.util.Map;

public class JurorExpenseControllerClient extends BaseClient {

    public JurorExpenseControllerClient() {
        super(uk.gov.hmcts.juror.support.sql.v2.DataCreator.restTemplateBuilder);
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.expense.CombinedSimplifiedExpenseDetailDto getSimplifiedExpenseDetails(
        JwtDetails jwtDetails,
        java.lang.String locCode,
        java.lang.String jurorNumber,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.ExpenseType type) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/expenses/{loc_code}/{juror_number}/{type}/view/simplified",
            null,
            Map.of("loc_code", String.valueOf(locCode),
                "type", String.valueOf(type),
                "juror_number", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.DefaultExpenseResponseDto getDefaultExpenses(
        JwtDetails jwtDetails,
        java.lang.String locCode,
        java.lang.String jurorNumber) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/expenses/{loc_code}/{juror_number}/default-expenses",
            null,
            Map.of("loc_code", String.valueOf(locCode),
                "juror_number", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.CombinedExpenseDetailsDto<uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.ExpenseDetailsDto> getExpenses(
        JwtDetails jwtDetails,
        java.lang.String locCode,
        java.lang.String jurorNumber,
        java.util.List dates) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/expenses/{loc_code}/{juror_number}/view",
            dates,
            Map.of("loc_code", String.valueOf(locCode),
                "juror_number", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.util.List<DailyExpenseResponse> postEditDailyExpense(
        JwtDetails jwtDetails,
        java.lang.String locCode,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.ExpenseType type,
        java.lang.String jurorNumber,
        java.util.List request) {
        return triggerApi(
            HttpMethod.PUT,
            "/api/v1/moj/expenses/{loc_code}/{juror_number}/{type}/edit",
            request,
            Map.of("loc_code", String.valueOf(locCode),
                "type", String.valueOf(type),
                "juror_number", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.expense.SummaryExpenseDetailsDto calculateSummaryTotals(
        JwtDetails jwtDetails,
        java.lang.String locCode,
        java.lang.String jurorNumber) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/expenses/{loc_code}/{juror_number}/summary/totals",
            null,
            Map.of("loc_code", String.valueOf(locCode),
                "juror_number", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.lang.Void submitDraftExpensesForApproval(JwtDetails jwtDetails,
                                                         java.lang.String locCode,
                                                         java.lang.String jurorNumber,
                                                         uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.DateDto dto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/expenses/{loc_code}/{juror_number}/submit-for-approval",
            dto,
            Map.of("loc_code", String.valueOf(locCode),
                "juror_number", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.CombinedExpenseDetailsDto<uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.ExpenseDetailsDto> getDraftExpenses(
        JwtDetails jwtDetails,
        java.lang.String locCode,
        java.lang.String jurorNumber) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/expenses/{loc_code}/{juror_number}/DRAFT/view",
            null,
            Map.of("loc_code", String.valueOf(locCode),
                "juror_number", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.expense.PendingApprovalList getExpensesForApproval(
        JwtDetails jwtDetails,
        java.lang.String locCode,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.PaymentMethod paymentMethod,
        java.time.LocalDate fromInclusive,
        java.time.LocalDate toInclusive) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/expenses/{loc_code}/{payment_method}/pending-approval",
            null,
            Map.of("loc_code", String.valueOf(locCode),
                "payment_method", String.valueOf(paymentMethod)),
            Map.of("from", String.valueOf(fromInclusive),
                "to", String.valueOf(toInclusive)),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.expense.ExpenseCount getCounts(
        JwtDetails jwtDetails,
        java.lang.String locCode,
        java.lang.String jurorNumber) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/expenses/{loc_code}/{juror_number}/counts",
            null,
            Map.of("loc_code", String.valueOf(locCode),
                "juror_number", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public org.springframework.data.domain.Page<UnpaidExpenseSummaryResponseDto> getUnpaidExpensesForCourtLocation(
        JwtDetails jwtDetails,
        java.lang.String locCode,
        java.time.LocalDate minDate,
        java.time.LocalDate maxDate,
        int pageNumber,
        java.lang.String sortBy,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.SortDirection sortOrder) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/expenses/{loc_code}/unpaid-summary",
            null,
            Map.of("pageNumber", String.valueOf(pageNumber),
                "loc_code", String.valueOf(locCode),
                "sortOrder", String.valueOf(sortOrder),
                "sortBy", String.valueOf(sortBy)),
            Map.of("max_date", String.valueOf(maxDate),
                "page_number", String.valueOf(pageNumber),
                "min_date", String.valueOf(minDate),
                "sort_by", String.valueOf(sortBy),
                "sort_order", String.valueOf(sortOrder)),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.lang.Void setDefaultExpenses(JwtDetails jwtDetails,
                                             java.lang.String locCode,
                                             java.lang.String jurorNumber,
                                             uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.RequestDefaultExpensesDto dto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/expenses/{loc_code}/{juror_number}/default-expenses",
            dto,
            Map.of("loc_code", String.valueOf(locCode),
                "juror_number", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.lang.Void apportionSmartCard(JwtDetails jwtDetails,
                                             java.lang.String locCode,
                                             java.lang.String jurorNumber,
                                             uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.ApportionSmartCardRequest request) {
        return triggerApi(
            HttpMethod.PATCH,
            "/api/v1/moj/expenses/{loc_code}/{juror_number}/smartcard",
            request,
            Map.of("loc_code", String.valueOf(locCode),
                "juror_number", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.lang.Void approveExpenses(JwtDetails jwtDetails,
                                          java.lang.String locCode,
                                          uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.PaymentMethod paymentMethod,
                                          java.util.List dto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/expenses/{loc_code}/{payment_method}/approve",
            dto,
            Map.of("loc_code", String.valueOf(locCode),
                "payment_method", String.valueOf(paymentMethod)),
            Map.of(),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.CombinedExpenseDetailsDto<uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.expense.ExpenseDetailsForTotals> calculateTotals(
        JwtDetails jwtDetails,
        java.lang.String locCode,
        java.lang.String jurorNumber,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.CalculateTotalExpenseRequestDto dto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/expenses/{loc_code}/{juror_number}/calculate/totals",
            dto,
            Map.of("loc_code", String.valueOf(locCode),
                "juror_number", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.util.List<GetEnteredExpenseResponse> getEnteredExpenseDetails(
        JwtDetails jwtDetails,
        java.lang.String locCode,
        java.lang.String jurorNumber,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.GetEnteredExpenseRequest request) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/expenses/{loc_code}/{juror_number}/entered",
            request,
            Map.of("loc_code", String.valueOf(locCode),
                "juror_number", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetails,
            new ParameterizedTypeReference<>() {
            }
        );
    }

}
