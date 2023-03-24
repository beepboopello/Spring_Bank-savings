package ptit.savings.model.requestBody.Saving;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import ptit.savings.model.Account;

@Data
public class AddSavingBody {

//    private Account account;
    @Positive(message = "Negative")
    @NotNull(message = "Empty initial value")
    private Long initial;

    @NotNull(message = "Empty interest id")
    private Long interestId;

    @NotNull(message = "Empty token")
    private String token;
}
