package code.rice.bowl.spaghetti.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateUtils {
    /**
     * 오늘 날짜 정보를 yyyy-mm-dd 형식으로 반환.
     * @return 날짜 형식.
     */
    public static String today() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * before와 today가 하루 차이인지 확인.
     * @param before    yyyy-mm-dd 형식의 날짜 서식.
     * @param today     yyyy-mm-dd 형식의 날짜 서식.
     * @return true (하루 차이), false (하루 차이 초과)
     */
    public static boolean diffOneDay(String before, String today) {
        LocalDate now = LocalDate.parse(today);
        LocalDate last = LocalDate.parse(before);

        return last.plusDays(1).equals(now);
    }

}
