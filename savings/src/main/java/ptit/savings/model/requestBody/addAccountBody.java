package ptit.savings.model.requestBody;

import java.io.Serializable;

import lombok.Data;

@Data
public class addAccountBody implements Serializable{
    private String email,owner,cccd;
}
