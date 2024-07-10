package site.travellaboratory.be.common.infrastructure.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import site.travellaboratory.be.infrastructure.article.enums.TravelStyle;

@Converter(autoApply = true)
public class TravelStyleConverter implements AttributeConverter<TravelStyle, String> {

    @Override
    public String convertToDatabaseColumn(TravelStyle attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getName();
    }

    @Override
    public TravelStyle convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return TravelStyle.fromDbValue(dbData);
    }
}
