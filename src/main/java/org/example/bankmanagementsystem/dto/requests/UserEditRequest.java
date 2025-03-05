package org.example.bankmanagementsystem.dto.requests;

import lombok.Data;

@Data
public class UserEditRequest {
    private Long id;
    private String firstName;
    private String lastName;
}
