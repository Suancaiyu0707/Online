package com.online.netty.jianan_study;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateTest {
    public static void main(String args[]) {
        testLocalDate();

        testMonthDay();

        testClock();

        compareDate();

        testYearMonth();

        calculateDiff();

        testInstant();

        testDateFormat();
    }

    //Java 8 中的 LocalDate 用于表示当天日期。和 java.util.Date 不同，它只有日期，不包含时间。当你仅需要表示日期时就用这个类。
    public static void testLocalDate() {
        System.out.println("========testLocalDate begin=========");
        LocalDate now = LocalDate.now();
        System.out.println(now);
        //LocalDate 类提供了获取年、月、日的快捷方法，其实例还包含很多其它的日期属性。
        // 通过调用这些方法就可以很方便的得到需要的日期信息，不用像以前一样需要依赖 java.util.Calendar 类了
        int year = now.getYear();
        int monthValue = now.getMonthValue();
        int dayOfMonth = now.getDayOfMonth();
        System.out.printf("year = %d, month = %d, day = %d", year, monthValue, dayOfMonth);
        System.out.println("");
        //调用另一个有用的工厂方法LocalDate.of() 创建任意日期， 该方法需要传入年、月、日做参数，返回对应的 LocalDate 实例。
        //这个方法的好处是没再犯老 API 的设计错误，比如年度起始于 1900，月份是从 0 开始等等。日期所见即所得，就像下面这个例子表示了 6 月 20 日，没有任何隐藏机关。
        LocalDate localDate2 = LocalDate.of(2018, 07, 20);
        System.out.println(localDate2);
        System.out.println("========testLocalDate end =========");

        //判断两个日期是否相等 LocalDate 重载了 equal 方法。
        //如果比较的日期是字符型的，需要先解析成日期对象再作判断
        LocalDate date = LocalDate.of(2018, 07, 30);
        if (date.equals(now)) {
            System.out.println("同一天");
        }


    }

    /***
     *  MonthDay 类
     *  这个类组合了月份和日，去掉了年，这意味着你可以用它判断每年都会发生事件。
     *  和这个类相似的还有一个 YearMonth 类。这些类也都是不可变并且线程安全的值类型。
     */
    public static void testMonthDay() {
        System.out.println("========testMonthDay begin=========");
        LocalDate now = LocalDate.now();
        LocalDate dateOfBirth = LocalDate.of(2018, 07, 30);
        MonthDay birthday = MonthDay.of(dateOfBirth.getMonth(), dateOfBirth.getDayOfMonth());
        MonthDay currentMonthDay = MonthDay.from(now);
        if (currentMonthDay.equals(birthday)) {
            System.out.println("Happy Birthday");
        } else {
            System.out.println("Sorry, today is not your birthday");
        }
    }

    /***
     * 与 Java 8 获取日期的例子很像，获取时间使用的是 LocalTime 类，一个只有时间没有日期的 LocalDate 近亲。
     * 可以调用静态工厂方法 now() 来获取当前时间。默认的格式是 hh:mm:ss:nnn。没有日期
     */
    public static void testLocalTime() {
        LocalTime localTime = LocalTime.now();
        System.out.println(localTime);

        //如果在现有的时间上加上小时
        //Java 8 除了不变类型和线程安全的好处之外，还提供了更好的plusHours() 方法替换 add()，并且是兼容的。
        // 注意，这些方法返回一个全新的 LocalTime 实例，由于其不可变性，返回后一定要用变量赋值
        System.out.println(localTime);
        LocalTime localTime1 = localTime.plusHours(2);//增加2小时
        System.out.println(localTime1);
        //通过 LocalDate 的 plus() 方法增加天数、周数或月数
        //如何计算一周后的日期
        LocalDate now = LocalDate.now();
        //加一个礼拜
        LocalDate plusDate = now.plus(1, ChronoUnit.WEEKS);
        System.out.println(now);
        System.out.println(plusDate);
        //计算一年前或一年后的日期  利用 minus() 方法计算一年前的日期。
        // LocalDate now = LocalDate.now();
        //加一年
        LocalDate minusDate = now.minus(1, ChronoUnit.YEARS);
        //减一年
        LocalDate plusDate1 = now.plus(1, ChronoUnit.YEARS);
        System.out.println(minusDate);
        System.out.println(plusDate1);

    }

    /***
     * Clock 时钟类用于获取当时的时间戳，或当前时区下的日期时间信息
     * System.currentTimeInMillis() 和 TimeZone.getDefault() 的地方都可用 Clock 替换
     */
    public static void testClock() {
        System.out.println("========testClock begin=========");
        Clock clock = Clock.systemUTC();
        Clock clock1 = Clock.systemDefaultZone();//带时区信息
        System.out.println(clock);
        System.out.println(clock1);//SystemClock[Asia/Shanghai]
        System.out.println(clock1.getZone());//获得时区
        System.out.println(clock.millis());//获得long类型的时间值
        System.out.println(new Date(clock.millis()));
    }

    /***
     * 在 Java 8 中，LocalDate 类有两类方法 isBefore() 和 isAfter() 用于比较日期。
     * 调用 isBefore() 方法时，如果给定日期小于当前日期则返回 true
     *
     * 不需要使用额外的 Calendar 类来做这些基础工作了。
     */
    public static void compareDate() {
        System.out.println("========compareDate begin=========");

        LocalDate now = LocalDate.now();
        LocalDate tomorrow = LocalDate.of(2018, 8, 1);
        if (tomorrow.isAfter(now)) {//判断tomorrow是否在now之后
            System.out.println("Tomorrow comes after today");
        }
        LocalDate yesterday = now.minus(1, ChronoUnit.DAYS);
        if (yesterday.isBefore(now)) {//判断yesterdayw是否在now之前
            System.out.println("Yesterday is day before today");
        }
    }

    /***
     * 获得某一年的某一月YearMonth
     * 可以获得当月有多少天
     * 用这个类得到 当月共有多少天，YearMonth 实例的 lengthOfMonth() 方法可以返回当月的天数，在判断 2 月有 28 天还是 29 天时非常有用
     */
    public static void testYearMonth() {
        System.out.println("========testYearMonth begin=========");

        YearMonth currentYearMonth = YearMonth.now();//获得当前日期
        System.out.println("当月有多少天:"+currentYearMonth.lengthOfMonth());//获得当月有多少天
        System.out.printf("Days in month year %s: %d%n", currentYearMonth, currentYearMonth.lengthOfMonth());
        YearMonth creditCardExpiry = YearMonth.of(2018, Month.FEBRUARY);
        System.out.println("今年2月份有多少天:"+creditCardExpiry.lengthOfMonth());//获得当月有多少天
        System.out.printf("Your credit card expires on %s %n", creditCardExpiry);


        LocalDate date = LocalDate.now();
        System.out.println("是否是闰年:"+ date.isLeapYear());
    }

    /***
     * 计算两个日期之间的天数和月数
     */
    public static void calculateDiff(){
        LocalDate now = LocalDate.now();
        LocalDate date = LocalDate.of(2019, Month.MARCH, 20);
        Period period = Period.between(now, date);//计算两个日期的时间差距，返回一个Period对象
        System.out.println("离下个时间还有" + period.getYears() + "年"+period.getMonths()+"个月"+period.getDays()+"天");

    }

    /***
     * 在 Java 8 中获取当前的时间戳
     * Instant 类有一个静态工厂方法 now() 会返回当前的时间戳
     */
    public static void testInstant(){
        Instant timestamp = Instant.now();
        System.out.println(timestamp);
    }

    /***
     * 用DateTimeFormatter做日期格式化
     * DateTimeFormatter是线程安全的
     */
    public static void testDateFormat(){
        LocalDateTime arrivalDate  = LocalDateTime.now();
        try {
            //设置格式化字符串
            DateTimeFormatter format = DateTimeFormatter.ofPattern("MMMdd yyyy  hh:mm a");
            String landing = arrivalDate.format(format);//进行格式化
            System.out.printf("Arriving at :  %s %n", landing);

            String dateStr= "2018年06月20日";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
            LocalDate date= LocalDate.parse(dateStr, formatter);

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter format2 = DateTimeFormatter.ofPattern("yyyy年MM月dd日 hh:mm a");
            String nowStr = now .format(format2);
        }catch (DateTimeException ex) {
            System.out.printf("%s can't be formatted!%n", arrivalDate);
            ex.printStackTrace();
        }
    }
}