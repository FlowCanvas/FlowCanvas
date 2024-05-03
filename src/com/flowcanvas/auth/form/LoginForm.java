package com.flowcanvas.auth.form;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginForm {

    String email;
    String password;
}