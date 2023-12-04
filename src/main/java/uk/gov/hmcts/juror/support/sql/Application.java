package uk.gov.hmcts.juror.support.sql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uk.gov.hmcts.juror.support.sql.entity.JurorGenerator;

@SpringBootApplication
@SuppressWarnings("HideUtilityClassConstructor") // Spring needs a constructor, its not a utility class
public class Application {

    public static void main(final String[] args) {
//        SpringApplication.run(Application.class, args);
        JurorGenerator jurorGenerator = new JurorGenerator();
        System.out.println(jurorGenerator.generate());
        System.out.println(jurorGenerator.generate());
        System.out.println(jurorGenerator.generate());
    }
}
