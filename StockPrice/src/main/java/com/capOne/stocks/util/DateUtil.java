package com.capOne.stocks.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateUtil {

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static String removeddFromDate(String date) throws ParseException {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-mm-dd");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm");
        return formatter.format(parser.parse(date));
    }
}
