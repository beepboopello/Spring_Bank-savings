package ptit.savings.tools;

import ptit.savings.model.Interest;
import ptit.savings.model.Saving;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class InterestCalculator {
    public static Long update(Saving saving) {
        // Số tháng thực đã gửi
        Double rate = 0.0;
        Double total = 0.0;
        if (saving.getInterest().getMonths() == 0) {
            // Tính tổng tiền nhận được sau khi rút
            rate = saving.getInterest().getRate() / 100;
            long actualDays = ChronoUnit.DAYS.between(saving.getCreated_at(), LocalDateTime.now());
            total = (saving.getInitial() * rate * actualDays) / 365;
            return Math.round(saving.getInitial() + total);
        } else {
            long actualMonth = ChronoUnit.MONTHS.between(saving.getCreated_at(), LocalDateTime.now());
            if (actualMonth == 0) {
                return saving.getCurrent();
            }
            rate = saving.getInterest().getRate() / 100;
            total = (saving.getInitial() * rate * ((double) actualMonth / 12));
//            System.out.println("saving.getInitial(): " + saving.getInitial());
//            System.out.println("actualMonth: " + actualMonth);
//            System.out.println("rate: " + rate);
//            System.out.println(total);
            return Math.round(saving.getInitial() + total);
        }
    }

    public static Long calculate(Long amount, Interest interest) {
        return amount + calculate_interest(amount, interest);
    }

    public static Long calculate_interest(Long amount, Interest interest) {
        long total = 0L;
//        System.out.println("amount: " + amount);
//        System.out.println("rate: " + interest.getRate());
//        System.out.println("months: " + interest.getMonths());
        long months = interest.getMonths();
        double rate = interest.getRate();
        total = Math.round(amount * rate / 100 * ((double) months / 12));
        return total;
    }

    public static Long withdrawal(Saving saving) {
//        Hàm tính lãi rút sớm hơn kỳ hạn
//        Số ngày thực đã gửi
        Long actualDay = ChronoUnit.DAYS.between(saving.getCreated_at(), LocalDateTime.now());
        double interestRate = saving.getInterest().getRate() / 100;
        Double total = (saving.getInitial() * interestRate * actualDay) / 365;
//        Tính tổng tiền nhận được sau khi rút = số tiền ban đầu gửi + số tiền lãi
        return Math.round(saving.getInitial() + total);
    }

}
