package uk.gov.hmcts.juror.support.sql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication(scanBasePackages = "uk.gov.hmcts.juror.support.sql.v1")
//@SpringBootApplication(scanBasePackages = "uk.gov.hmcts.juror.support.sql.v2")
@SpringBootApplication(scanBasePackages = "uk.gov.hmcts.juror.support.sql")
@SuppressWarnings("HideUtilityClassConstructor") // Spring needs a constructor, its not a utility class
public class Application {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
