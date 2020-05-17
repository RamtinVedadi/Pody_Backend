package com.pody.dto.requests;


import com.pody.model.ValidationError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {
    private String username;
    private String password;

    public List<ValidationError> validate() {

        List<ValidationError> errors = new ArrayList<>();
        if (username == null || password == null) {
            errors.add(new ValidationError("username or password", "empty"));
        }
        return errors;
    }
}
