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
        Double rate = saving.getInterest().getRate() / 100;
        Long total = (long) ((saving.getInitial() * rate) / (12 * actualMonth));
        return saving.getCurrent() + total;
    }

//    public static Long withdrawal(Long amount, Interest interest) {
//        Long total = (long) ((amount * interest.getRate()) / (12 * interest.getMonths()));
//        return total;
//    }

    public static Long calculate(Long amount, Interest interest) {
        return amount + calculate_interest(amount, interest);
    }

    public static Long calculate_interest(Long amount, Interest interest) {
//        Long total = 0L;
        double total = 0;
        long months = interest.getMonths();
        double interestRate = interest.getRate() / 100;
        total = ((amount * (interestRate / 12) * months));
        return (long) total;
    }
    public static Long withdrawal(Saving saving) {
//        Hàm tính lãi rút sớm hơn kỳ hạn
//        Số ngày thực đã gửi
        Long actualDay = ChronoUnit.DAYS.between(saving.getCreated_at(), LocalDateTime.now());
        double interestRate = saving.getInterest().getRate() / 100;
        Long total = (long) (saving.getInitial() * interestRate * actualDay) / 365;
//        Tính tổng tiền nhận được sau khi rút = số tiền ban đầu gửi + số tiền lãi
        return saving.getInitial() + total;
    }

}
