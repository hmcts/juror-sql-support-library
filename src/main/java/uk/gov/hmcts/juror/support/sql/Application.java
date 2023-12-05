package uk.gov.hmcts.juror.support.sql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uk.gov.hmcts.juror.support.sql.dto.CreatePopulatedPoolRequest;
import uk.gov.hmcts.juror.support.sql.entity.JurorGenerator;
import uk.gov.hmcts.juror.support.sql.entity.PoolGenerator;
import uk.gov.hmcts.juror.support.sql.generators.JurorPoolRequestGeneratorUtil;

import java.util.Map;

@SpringBootApplication
@SuppressWarnings("HideUtilityClassConstructor") // Spring needs a constructor, its not a utility class
public class Application {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);

    }
}
