package ptit.savings.model.requestBody.Saving;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddSavingBody {
    @Positive(message = "Negative")
    @NotNull(message = "Empty initial value")
    private Long initial;

    @NotNull(message = "Empty interest id")
    private int interestId;

    @NotNull(message = "Empty token")
    private String token;
}
