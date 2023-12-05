package uk.gov.hmcts.juror.support.sql.service;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.juror.support.sql.entity.Juror;
import uk.gov.hmcts.juror.support.sql.entity.JurorGenerator;
import uk.gov.hmcts.juror.support.sql.generators.JurorGeneratorUtil;
import uk.gov.hmcts.juror.support.sql.repository.JurorRepository;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SqlSupportService {
    private final JurorRepository jurorRepository;


    @PostConstruct
        //Temporary for testing purposes
    void postConstruct() {
        JurorGenerator jurorGenerator = JurorGeneratorUtil.responded();
        Juror juror = jurorGenerator.generate();
        System.out.println(juror);
        jurorRepository.save(juror);

//        System.out.println(jurorGenerator.generate());
//        System.out.println(jurorGenerator.generate());
//
//        System.out.println(SqlDataLoad.createPopulatedPool(
//            CreatePopulatedPoolRequest.builder()
//                .poolGenerator(new PoolGenerator())
//                .jurorCountMap(Map.of(
//                    JurorPoolRequestGeneratorUtil.responded(),
//                    10,
//                    JurorPoolRequestGeneratorUtil.disqualified(),
//                    2
//                ))
//                .build()
//        ));
    }

}
