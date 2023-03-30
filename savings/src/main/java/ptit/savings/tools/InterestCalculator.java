package ptit.savings.tools;

import ptit.savings.model.Interest;
import ptit.savings.model.Saving;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class InterestCalculator {
    public static Long update(Saving saving) {
        // Số tháng thực đã gửi
        Long actualMonth = ChronoUnit.MONTHS.between(saving.getCreated_at(), LocalDateTime.now());
        // Tính tổng tiền nhận được sau khi rút
        Long total = (long) ((saving.getInitial() * saving.getInterest().getRate()) / (12 * actualMonth));
        return saving.getCurrent() + total;
    }

//    public static Long withdrawal(Long amount, Interest interest) {
//        Long total = (long) ((amount * interest.getRate()) / (12 * interest.getMonths()));
//        return total;
//    }

    public static Long calculate(Long amount, Interest interest) {
        Long total = 0L;
        // Tiền gửi có kỳ hạn
        long months = interest.getMonths();
        double interestRate = interest.getRate();
        total = (long) ((amount * interestRate) / (12 * months));
        return total;
    }

    public static Long withdrawal(Saving saving) {
//        Số ngày thực đã gửi
        Long actualDay = ChronoUnit.DAYS.between(saving.getCreated_at(), LocalDateTime.now());
//        Tính tổng tiền nhận được sau khi rút
        Long total = (long) (saving.getInitial() * saving.getInterest().getRate() * actualDay) / 365;
        return saving.getCurrent() + total;
    }
}
