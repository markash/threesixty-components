package com.github.markash.ui.component.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class LocalDateUtils {

    public static LocalDate toLocalDate(
            final Date value) {

        return new java.sql.Date(value.getTime()).toLocalDate();
    }

    public static Date toDate(
            final LocalDate value) {

        return java.util.Date
                .from(value
                        .atStartOfDay()
                        .atZone(ZoneId.systemDefault())
                        .toInstant());
    }
}
