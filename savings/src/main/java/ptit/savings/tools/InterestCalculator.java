package ptit.savings.tools;

import ptit.savings.model.Interest;
import ptit.savings.model.Saving;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class InterestCalculator {
    public static Long update(Saving saving) {

        return Long.valueOf(0);
    }

//    public static Long withdrawal(Long amount, Interest interest) {
//        Long total = (long) ((amount * interest.getRate()) / (12 * interest.getMonths()));
//        return total;
//    }

    public static Long withdrawal(Long amount, Interest interest, Saving saving) {
        Long total = 0L;
        if (interest.getMonths() == 0) {
            // Tiền gửi không kỳ hạn
            LocalDateTime now = LocalDateTime.now();
            Duration duration = Duration.between(saving.getCreated_at(), now);
            long days = duration.toDays();
            double interestRate = saving.getInterest().getRate();
            total = (long) ((amount * interestRate * days) / 365);
        } else {
            // Tiền gửi có kỳ hạn
            long months = interest.getMonths();
            double interestRate = interest.getRate();
            total = (long) ((amount * interestRate) / (12 * months));
        }
        return total;
    }
    public static Long prematureWithdrawal(Saving saving) {
//        Số ngày thực đã gửi
        Long actualDay = ChronoUnit.DAYS.between(saving.getCreated_at(), LocalDateTime.now());
//        Tính tổng tiền nhận được sau khi rút
        Long total = (long) (saving.getInitial() * saving.getInterest().getRate() * actualDay) / 365;
        return saving.getCurrent() + total;
    }
}
