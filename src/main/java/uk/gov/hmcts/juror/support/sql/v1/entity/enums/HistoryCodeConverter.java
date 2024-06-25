package uk.gov.hmcts.juror.support.sql.v1.entity.enums;

import jakarta.persistence.AttributeConverter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class HistoryCodeConverter implements AttributeConverter<HistoryCodeMod, String> {

    @Override
    public String convertToDatabaseColumn(HistoryCodeMod attribute) {
        return attribute.getCode();
    }

    @Override
    public HistoryCodeMod convertToEntityAttribute(String dbData) {
        if (dbData.isBlank()) {
            return null;
        }

        return Arrays.stream(HistoryCodeMod.values()).filter(historyCode ->
            historyCode.getCode().equalsIgnoreCase(dbData)).findFirst().orElse(null);
    }
}
