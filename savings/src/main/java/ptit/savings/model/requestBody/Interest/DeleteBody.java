package ptit.savings.model.requestBody.Interest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

public class DeleteBody {
    private Long id;
    private String token;
}
