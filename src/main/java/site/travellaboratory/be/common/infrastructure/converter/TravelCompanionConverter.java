package site.travellaboratory.be.common.infrastructure.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import site.travellaboratory.be.infrastructure.article.enums.TravelCompanion;

@Converter(autoApply = true)
public class TravelCompanionConverter implements AttributeConverter<TravelCompanion, String> {

    @Override
    public String convertToDatabaseColumn(TravelCompanion attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getName();
    }

    @Override
    public TravelCompanion convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return TravelCompanion.from(dbData);
    }
}

