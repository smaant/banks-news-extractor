package smaant.utils;

import java.util.Locale;
import java.util.TimeZone;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.trimou.engine.MustacheTagInfo;
import org.trimou.handlebars.i18n.AbstractTimeFormatHelper;

public class JodaDateTimeFormatHelper extends AbstractTimeFormatHelper<DateTime, String> {

  @Override
  protected String defaultFormat(DateTime value, Locale locale, TimeZone timeZone) {
    return format(value, "dd-MM-yyyy HH:mm", locale, timeZone);
  }

  @Override
  protected String parseStyle(String style, MustacheTagInfo tagInfo) {
    try {
      new DateTimeFormatterBuilder().appendPattern(style);
      return style;
    } catch (IllegalArgumentException e) {
      throw unknownStyle(style, tagInfo);
    }
  }

  @Override
  protected DateTime getFormattableObject(Object value, Locale locale, TimeZone timeZone, MustacheTagInfo tagInfo) {
    if (value instanceof DateTime) {
      return (DateTime) value;
    } else {
      throw valueNotAFormattableObject(value, tagInfo);
    }
  }

  @Override
  protected String format(DateTime value, String pattern, Locale locale, TimeZone timeZone) {
    return value.toString(pattern);
  }
}
