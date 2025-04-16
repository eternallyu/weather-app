package ru.eternallyu.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserDto {

    private String login;

    private String password;
}
