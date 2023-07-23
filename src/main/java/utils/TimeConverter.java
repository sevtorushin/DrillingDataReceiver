package utils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Converter(autoApply = true)
public class TimeConverter  implements AttributeConverter<LocalTime, String> {
    @Override
    public String convertToDatabaseColumn(LocalTime localTime) {
        return localTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    @Override
    public LocalTime convertToEntityAttribute(String s) {
        return LocalTime.parse(s, DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}
