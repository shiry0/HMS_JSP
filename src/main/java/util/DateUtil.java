package util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public final class DateUtil {
    private DateUtil() {
    }

    public static String format(java.util.Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static Date toSqlDate(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        return new Date(sdf.parse(dateStr).getTime());
    }

    public static boolean isFutureDate(Date date) {
        return date != null && date.after(new Date(System.currentTimeMillis()));
    }

    public static boolean isTodayOrFuture(Date date) {
        return date != null && !date.before(new Date(System.currentTimeMillis()));
    }

    public static boolean isPastDate(Date date) {
        return date != null && date.before(new Date(System.currentTimeMillis()));
    }
}
