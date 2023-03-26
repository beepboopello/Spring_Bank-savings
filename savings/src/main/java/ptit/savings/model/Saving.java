package ptit.savings.model;

import java.time.LocalDateTime;
import java.util.Random;

import io.micrometer.common.lang.NonNull;
import io.micrometer.common.lang.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import ptit.savings.tools.InterestCalculator;

@Entity
@Data
@NoArgsConstructor
public class Saving {

    @Id
    @GeneratedValue
    private Long id;

    @Positive
    @NonNull
    private Long initial;           // tien ban dau gui

    @NonNull
    private Long current, mature;  // current = tien hien co / mature = tien nhan duoc cuoi ky han

    @NotNull
    private String number;          //

    @NotNull
    private LocalDateTime created_at, updated_at;

    @Nullable
    private LocalDateTime receive_at;

    @NotNull
    private int status; //status = 0: da rut dung ky han, status = 1: dang gui, status = -1: da rut truoc ky han

    @NotNull
    @ManyToOne
    @JoinColumn(name = "interest_id")
    private Interest interest;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    public Saving(Account account, Long initial, Interest interest, String number) {
        this.account = account;
        this.initial = initial;
        this.number = number;
        this.current = initial;
        this.interest = interest;
        this.created_at = LocalDateTime.now();
        this.updated_at = this.created_at;
        if (interest.getMonths() != 0) {
            this.receive_at = this.created_at.plusMonths(interest.getMonths());
        }
//        this.mature = InterestCalculator.withdrawal(this.initial, this.interest,this);
        this.mature = InterestCalculator.withdrawal(this.initial, this.interest);
        this.status = 1;
    }

    public void dailyUpdate() {
        this.current = InterestCalculator.update(this);
        if (LocalDateTime.now().isAfter(this.receive_at)) {
            withdrawal();
        }
        this.updated_at = LocalDateTime.now();
    }

    private void withdrawal() {
//        khi đáo hạn thì tất cả tiền đều được cộng vào tài khoản
        this.account.setBalance(this.account.getBalance() + this.mature);
        this.status = -1;
    }

    public void prematureWithdrawal() {
        this.current = InterestCalculator.prematureWithdrawal(this);
        this.mature = this.current;
        this.status = -1;
    }
}
