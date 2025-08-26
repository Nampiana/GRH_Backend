package app.gestion.GRH.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.time.YearMonth;

@ReadingConverter
public class YearMonthReadConverter implements Converter<String, YearMonth> {
    @Override public YearMonth convert(String source) { return YearMonth.parse(source); }
}
