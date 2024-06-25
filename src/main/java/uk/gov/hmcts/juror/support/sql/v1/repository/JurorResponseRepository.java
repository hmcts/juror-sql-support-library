package uk.gov.hmcts.juror.support.sql.v1.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import uk.gov.hmcts.juror.support.sql.v1.entity.jurorresponse.AbstractJurorResponse;

@NoRepositoryBean
public interface JurorResponseRepository<T extends AbstractJurorResponse> extends CrudRepository<T,
        String> {
}
