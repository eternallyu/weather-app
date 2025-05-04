package ru.eternallyu.dto;

import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserDto {

    private String login;

    private String password;
}
