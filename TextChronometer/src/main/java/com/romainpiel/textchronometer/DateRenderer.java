package com.romainpiel.textchronometer;

import android.content.Context;


public class DateRenderer {

    public static final String SECONDS = "segundos";
    public static final String MINUTES = "minutos";
    public static final String HOURS = "horas";
    public static final String DAYS = "dias";
    public static final String WEEKS = "semanas";
    public static final String MONTHS = "meses";

    private final String ago, now, sec, secs, min, mins, hour, hours, day, days, week, weeks, month, months;

    public DateRenderer(Context ctx) {
        ago = ctx.getString(R.string.general_timeAgo_);
        now = ctx.getString(R.string.general_momentAgo);
        sec = ctx.getString(R.string.general_second);
        secs = ctx.getString(R.string.general_seconds);
        min = ctx.getString(R.string.general_minute);
        mins = ctx.getString(R.string.general_minutes);
        hour = ctx.getString(R.string.general_hour);
        hours = ctx.getString(R.string.general_hours);
        day = ctx.getString(R.string.general_day);
        days = ctx.getString(R.string.general_days);
        week = ctx.getString(R.string.general_week);
        weeks = ctx.getString(R.string.general_weeks);
        month = ctx.getString(R.string.general_month);
        months = ctx.getString(R.string.general_months);
    }

    /**
     * Converts a timestamp to how long ago syntax
     *
     * @param time The time in milliseconds
     * @return The formatted time
     */
    public TimeAgo timeAgo(double time) {
        Unit[] units = new Unit[]
                {
                        new Unit(SECONDS, sec, secs, 60, 1),
                        new Unit(MINUTES, min, mins, 3600, 60),
                        new Unit(HOURS, hour, hours, 86400, 3600),
                        new Unit(DAYS, day, days, 604800, 86400),
                        new Unit(WEEKS, week, weeks, 2629743, 604800),
                        new Unit(MONTHS, month, months, 31556926, 2629743)
                };

        long currentTime = System.currentTimeMillis();
        int difference = (int) ((currentTime - time) / 1000);

        if (difference < 5) {
            return new TimeAgo(units[0], now);
        }

        String formattedDate = null;
        Unit lastUnit = null;
        for (Unit unit : units) {
            if (difference < unit.limit) {
                formattedDate = getFormattedDate(unit, difference);
                lastUnit = unit;
                break;
            }
        }

        if (formattedDate == null) {
            lastUnit = units[units.length - 1];
            formattedDate = getFormattedDate(lastUnit, difference);
        }

        return new TimeAgo(lastUnit, formattedDate);
    }

    /**
     * Converts a timestamp to how long left syntax
     *
     * @param time The time in milliseconds
     * @return The formatted time
     */
    public TimeAgo timeLeft(double time) {
        Unit[] units = new Unit[]
                {
                        new Unit(SECONDS, sec, secs, 60, 1),
                        new Unit(MINUTES, min, mins, 3600, 60),
                        new Unit(HOURS, hour, hours, 86400, 3600),
                        new Unit(DAYS, day, days, 604800, 86400),
                        new Unit(WEEKS, week, weeks, 2629743, 604800),
                        new Unit(MONTHS, month, months, 31556926, 2629743)
                };

        long currentTime = System.currentTimeMillis();
        int difference = (int) ((time - currentTime) / 1000);

        if (difference < 5) {
            return new TimeAgo(units[0], now);
        }

        String formattedDate = null;
        Unit lastUnit = null;
        for (Unit unit : units) {
            if (difference < unit.limit) {
                formattedDate = getFormattedDate(unit, difference);
                lastUnit = unit;
                break;
            }
        }

        if (formattedDate == null) {
            lastUnit = units[units.length - 1];
            formattedDate = getFormattedDate(lastUnit, difference);
        }

        return new TimeAgo(lastUnit, formattedDate);
    }

    private String getFormattedDate(Unit unit, int difference) {
        int newDiff = (int) Math.floor(difference / unit.inSeconds);
        String resultValue = newDiff + " " + (newDiff <= 1? unit.name : unit.pluralName);
        return String.format(ago, resultValue);
    }

    public class TimeAgo {
        Unit unit;
        String formattedDate;

        public TimeAgo(Unit unit, String formattedDate) {
            this.unit = unit;
            this.formattedDate = formattedDate;
        }
    }

    static class Unit {

        public String type;
        public String name, pluralName;
        public int limit;
        public int inSeconds;

        public Unit(String type, String name, String pluralName, int limit, int inSeconds) {
            this.type = type;
            this.name = name;
            this.pluralName = pluralName;
            this.limit = limit;
            this.inSeconds = inSeconds;
        }
    }
}
