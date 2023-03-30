package ptit.savings.model.requestBody.Saving;

import lombok.Data;

@Data
public class WithdrawalBody {
    private String number;
    private int option;
    private String token;
}
