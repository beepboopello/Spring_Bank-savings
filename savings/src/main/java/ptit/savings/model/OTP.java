package ptit.savings.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class OTP {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String account;
    private String action,strValue;
}
