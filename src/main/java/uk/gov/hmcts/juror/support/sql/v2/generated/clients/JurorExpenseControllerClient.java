package uk.gov.hmcts.juror.support.sql.v2.generated.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import uk.gov.hmcts.juror.support.sql.v2.clients.BaseClient;
import uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.ApproveExpenseDto;
import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetailsBureau;

import java.util.Map;

public class JurorExpenseControllerClient extends BaseClient {

    public JurorExpenseControllerClient() {
        super(uk.gov.hmcts.juror.support.sql.v2.DataCreator.restTemplateBuilder);
    }

    public java.lang.Void submitDraftExpensesForApproval(JwtDetailsBureau jwtDetailsBureau,
                                                         uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.ExpenseItemsDto dto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/expenses/submit-for-approval",
            dto,
            Map.of(),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.DefaultExpenseResponseDto getDefaultExpenses(
        JwtDetailsBureau jwtDetailsBureau,
        java.lang.String jurorNumber) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/expenses/default-summary/{juror_number}",
            null,
            Map.of("juror_number", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.lang.Void postEditDailyExpense(JwtDetailsBureau jwtDetailsBureau,
                                               uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.ExpenseType type,
                                               java.lang.String jurorNumber,
                                               java.util.List request) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/expenses/{juror_number}/edit/{type}",
            request,
            Map.of("type", String.valueOf(type),
                "juror_number", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.expense.DailyExpenseResponse postDraftAttendedDayDailyExpense(
        JwtDetailsBureau jwtDetailsBureau,
        java.lang.String jurorNumber,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.draft.DailyExpense request) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/expenses/{juror_number}/draft/attended_day",
            request,
            Map.of("juror_number", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.expense.CombinedSimplifiedExpenseDetailDto getSimplifiedExpenseDetails(
        JwtDetailsBureau jwtDetailsBureau,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.ExpenseType type,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.JurorNumberAndPoolNumberDto request) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/expenses/view/{type}/simplified",
            request,
            Map.of("type", String.valueOf(type)),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    @SneakyThrows
    public java.lang.Void approveExpenses(JwtDetailsBureau jwtDetailsBureau,
                                          java.util.List<ApproveExpenseDto> dto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/expenses/approve",
            dto,
            Map.of(),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.CombinedExpenseDetailsDto<uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.ExpenseDetailsDto> getDraftExpenses(
        JwtDetailsBureau jwtDetailsBureau,
        java.lang.String jurorNumber,
        java.lang.String poolNumber) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/expenses/draft/{juror_number}/{pool_number}",
            null,
            Map.of("pool_number", String.valueOf(poolNumber),
                "juror_number", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.CombinedExpenseDetailsDto<uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.ExpenseDetailsDto> getExpenses(
        JwtDetailsBureau jwtDetailsBureau,
        java.lang.String jurorNumber,
        java.lang.String poolNumber,
        java.util.List dates) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/expenses/{juror_number}/{pool_number}",
            dates,
            Map.of("pool_number", String.valueOf(poolNumber),
                "juror_number", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.expense.DailyExpenseResponse postDraftNonAttendedDayDailyExpense(
        JwtDetailsBureau jwtDetailsBureau,
        java.lang.String jurorNumber,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.draft.DailyExpense request) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/expenses/{juror_number}/draft/non_attended_day",
            request,
            Map.of("juror_number", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.util.List<uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.expense.GetEnteredExpenseResponse> getEnteredExpenseDetails(
        JwtDetailsBureau jwtDetailsBureau,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.GetEnteredExpenseRequest request) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/expenses/entered",
            request,
            Map.of(),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.expense.ExpenseCount getCounts(
        JwtDetailsBureau jwtDetailsBureau,
        java.lang.String jurorNumber,
        java.lang.String poolNumber) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/expenses/counts/{juror_number}/{pool_number}",
            null,
            Map.of("pool_number", String.valueOf(poolNumber),
                "juror_number", String.valueOf(jurorNumber)),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.lang.Void apportionSmartCard(JwtDetailsBureau jwtDetailsBureau,
                                             uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.ApportionSmartCardRequest request) {
        return triggerApi(
            HttpMethod.PATCH,
            "/api/v1/moj/expenses/smartcard",
            request,
            Map.of(),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.expense.PendingApprovalList getExpensesForApproval(
        JwtDetailsBureau jwtDetailsBureau,
        java.lang.String locCode,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.enumeration.PaymentMethod paymentMethod,
        java.time.LocalDate fromInclusive,
        java.time.LocalDate toInclusive) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/expenses/approval/{loc_code}/{payment_method}",
            null,
            Map.of("loc_code", String.valueOf(locCode),
                "payment_method", String.valueOf(paymentMethod)),
            Map.of("from", fromInclusive == null ? "null" : String.valueOf(fromInclusive),
                "to", toInclusive == null ? "null" : String.valueOf(toInclusive)),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public java.lang.Void setDefaultExpenses(JwtDetailsBureau jwtDetailsBureau,
                                             uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.RequestDefaultExpensesDto dto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/expenses/set-default-expenses",
            dto,
            Map.of(),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.CombinedExpenseDetailsDto<uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.expense.ExpenseDetailsForTotals> calculateTotals(
        JwtDetailsBureau jwtDetailsBureau,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.request.expense.CalculateTotalExpenseRequestDto dto) {
        return triggerApi(
            HttpMethod.POST,
            "/api/v1/moj/expenses/calculate/totals",
            dto,
            Map.of(),
            Map.of(),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

    public org.springframework.data.domain.Page<uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.controller.response.expense.UnpaidExpenseSummaryResponseDto> getUnpaidExpensesForCourtLocation(
        JwtDetailsBureau jwtDetailsBureau,
        java.lang.String locCode,
        java.time.LocalDate minDate,
        java.time.LocalDate maxDate,
        int pageNumber,
        java.lang.String sortBy,
        uk.gov.hmcts.juror.support.sql.v2.generated.api.moj.domain.SortDirection sortOrder) {
        return triggerApi(
            HttpMethod.GET,
            "/api/v1/moj/expenses/unpaid-summary/{locCode}",
            null,
            Map.of("locCode", String.valueOf(locCode),
                "pageNumber", String.valueOf(pageNumber),
                "sortOrder", String.valueOf(sortOrder),
                "sortBy", String.valueOf(sortBy)),
            Map.of("max_date", String.valueOf(maxDate),
                "page_number", String.valueOf(pageNumber),
                "min_date", String.valueOf(minDate),
                "sort_by", String.valueOf(sortBy),
                "sort_order", String.valueOf(sortOrder)),
            jwtDetailsBureau,
            new ParameterizedTypeReference<>() {
            }
        );
    }

}
