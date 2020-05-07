package com.github.learn.java.time;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static java.util.Calendar.*;
import static org.assertj.core.api.BDDAssertions.then;

@Slf4j
public class DateTimeRelativeUsageTest {

    // 2020-05-02 00:00:00
    final LocalDateTime localDateTime = LocalDateTime.of(2020, 05, 02, 00, 00);
    // Asia/Shanghai
    final ZoneId zoneId = ZoneId.systemDefault();

    {
        Instant instant = new Date().toInstant();
        LocalDateTime localDateTime1 = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalDate localDate = localDateTime1.toLocalDate();
        LocalTime localTime = localDateTime1.toLocalTime();
    }

    @Test
    void givenDate_when_thenDemo() {
        Date date = new Date();
        // Fri Jan 03 22:46:55 CST 2020
        log.info("date: {}", date);
        // millis -> Date
        long currentTimeMillis = System.currentTimeMillis();
        Date date2 = new Date(currentTimeMillis);
        // Date -> millis
        long time = date2.getTime();
        boolean after = date2.after(date);
        then(time).isEqualTo(currentTimeMillis);
        then(after).isTrue();
    }

    /**
     * SimpleDateFormat 用于 DateString <=> Date 的相互转换
     */
    @Test
    void givenDateAndSimpleDateFormat_whenConvert_thenDemo() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = format.format(new Date());
        log.info("{}", dateStr);

        // 2020-01-03 22:58:58

        Date parse = format.parse(dateStr);
        log.info("{}", parse);
    }

    /**
     * Calendar 主要用于获取或更改 年月日时分秒 中的一个字段
     */
    @Test
    void givenCalendar_when_thenDemo() throws ParseException {
        Calendar instance = Calendar.getInstance();
        String dateStr = "2020-01-03 22:10:09";

        // Calendar <=> Date 相互转换
        Date now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
        instance.setTime(now);
        Date time = instance.getTime();
        then(time).isEqualTo(now);

        int year = instance.get(YEAR);
        then(year).isEqualTo(2020);
        // very important: the first month of the year is 0
        int month = instance.get(MONTH) + 1;
        then(month).isEqualTo(01);
        int dayOfMonth = instance.get(DAY_OF_MONTH);
        then(dayOfMonth).isEqualTo(03);

        // -1年
        instance.add(YEAR, -1);
        // +2小时
        instance.add(Calendar.HOUR, +2);

        Calendar.getInstance(TimeZone.getTimeZone("China/Shanghai"), Locale.CHINA);
    }

    @Test void givenZoneId_when_then() {
        ZoneId zoneId = ZoneId.systemDefault();
        log.info("{}", zoneId);
    }

    @Test void givenZoneOffset_when_then() {

    }

    @Test
    void givenInstant_when_then() {
        Instant now = Instant.now();
        log.info("Instant => {}", now);
        log.info("{}", Instant.now(Clock.systemUTC()));
        log.info("Instant.getEpochSecond => {}", now.getEpochSecond());
        log.info("Instant.toEpochMilli => {}", now.toEpochMilli());
    }

    @Test void givenLocale() {
        // default en_CN
        then(Locale.getDefault()).isEqualTo(new Locale("en", "CN"));
        // CHINA zh_CN
        then(Locale.CHINA).isEqualTo(new Locale("zh", "CN"));
    }

    @Test void givenDayOfWeek() {
        DayOfWeek tuesday = DayOfWeek.MONDAY.plus(1);
        then(tuesday).isEqualTo(DayOfWeek.TUESDAY);
        // Tuesday
        then(tuesday.getDisplayName(TextStyle.FULL, Locale.getDefault())).isEqualTo("Tuesday");
        // 星期二
        then(tuesday.getDisplayName(TextStyle.FULL, Locale.CHINA)).isEqualTo("星期二");
    }

    @Test void givenMonth() {
        Month january = Month.JANUARY;
    }

    @Test
    void givenLocalDate_when_then() {
        // 当前时间
        LocalDate now = LocalDate.now();
        log.info("LocalDate: {}", now);
        log.info("LocalDate => {}-{}-{}", now.getYear(), now.getMonthValue(), now.getDayOfMonth());

        LocalDate of = LocalDate.of(2020, 2, 1);
        log.info("LocalDate.of(): {}", of);

        LocalDate ofYearDay = LocalDate.ofYearDay(2020, 1);
        log.info("LocalDate.ofYearDay(): {}", ofYearDay);

        LocalDate parse = LocalDate.parse("2020-01-04");
        log.info("LocalDate.parse(): {}", parse);
        log.info("LocalDate.now() + 1 week: {}", parse.plusWeeks(1));

        now.with(TemporalAdjusters.lastDayOfMonth()).minusDays(2);
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        LocalDate with = now.with(TemporalAdjusters.nextOrSame(dayOfWeek));
        log.info("nextOrSame => {}", with);
        then(with).isEqualTo(now);

    }

    @Test
    void givenMonthDay_when_then() {
        LocalDate myBirthDay = LocalDate.of(1988, 03, 29);
        log.info("LocalDate: {}", myBirthDay);
        MonthDay monthDay = MonthDay.from(myBirthDay);
        log.info("MonthDay: {}", monthDay);
        LocalDate localDate = LocalDate.of(2019, 03, 29);
        boolean b = monthDay.equals(MonthDay.from(localDate));
        then(b).isTrue();
    }

    @Test
    void givenLocalTime_when_then() {
        LocalTime now = LocalTime.now();
        log.info("LocalTime: {}", now);
        then(now.plusHours(-1)).isEqualTo(now.minusHours(1));
        then(now.plus(-1, ChronoUnit.HOURS)).isEqualTo(now.minus(Duration.ofHours(1)));
    }

    @Test
    void givenLocalDateTime_when_then() {
        LocalDateTime now = LocalDateTime.now();
        log.info("LocalDateTime: {}", now);
        ZoneId zoneIdOfNY = ZoneId.of("America/New_York");
        LocalDateTime newYork = LocalDateTime.now(zoneIdOfNY);
        log.info("newYork: {}", newYork);

        Instant instant = Instant.now();
        log.info("Instant: {}", instant);
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);

        log.info("Asia/Shanghai LocalDateTime: {}", localDateTime);
        log.info("instant=>{}, zoneOfShanghai=>{}", instant, localDateTime.toInstant(ZoneOffset.UTC));
        log.info("instant=>{}, zoneOfShanghai=>{}", instant.toEpochMilli(),
            localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli());

        ZonedDateTime zonedDateTime = now.atZone(zoneIdOfNY);
        log.info("ZoneDateTime: {}", zonedDateTime);
        log.info("ZoneDateTime.toLocalDateTime(): {}", zonedDateTime.toLocalDateTime());
    }

    @Test void givenZonedDateTime() {
        ZonedDateTime now = ZonedDateTime.now();
        log.info("{}", now);
    }

    @Test
    void givenPeriod_when_then() {
        Period of = Period.of(1, 1, 1);
        LocalDate now = LocalDate.now();
        LocalDate future = now.plusYears(1).plusMonths(11).plusDays(66);
        log.info("now=>{}, future=>{}", now, future);
        Period period = Period.between(now, future);
        log.info("period=>{}, year=>{}, month=>{}, day=>{}", period, period.getYears(), period.getMonths(),
            period.getDays());
    }

    @Test
    void givenDuration_when_then() {
        Duration oneDay = Duration.of(1, ChronoUnit.DAYS);
        log.info("Duration: {}", oneDay);
        then(oneDay.get(ChronoUnit.SECONDS)).isEqualTo(24 * 60 * 60);
    }

    @Test
    void givenTimeZone_when_then() {
        TimeZone aDefault = TimeZone.getDefault();
        log.info("TimeZone default: {}", aDefault);
        String[] availableIDs = TimeZone.getAvailableIDs(8 * 60 * 60 * 1000);
        Arrays.stream(availableIDs).forEach(System.out::println);

        Clock systemUTC = Clock.systemUTC();
        log.info("Clock.systemUTC(): {}", systemUTC);

        ZoneId defaultZoneId = ZoneId.systemDefault();
        log.info("defaultZoneId: {}", defaultZoneId);
        ZoneId zoneIdOfShanghai = ZoneId.of("Asia/Shanghai");
        log.info("zoneIdOfShanghai: {}", zoneIdOfShanghai);
        Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();
        log.info("availableZoneIds: {}", availableZoneIds);
    }
}
