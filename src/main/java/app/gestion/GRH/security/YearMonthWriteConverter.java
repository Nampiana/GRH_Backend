package app.gestion.GRH.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.time.YearMonth;

@WritingConverter
public class YearMonthWriteConverter implements Converter<YearMonth, String> {
    @Override public String convert(YearMonth source) { return source.toString(); } // "2025-08"
}
