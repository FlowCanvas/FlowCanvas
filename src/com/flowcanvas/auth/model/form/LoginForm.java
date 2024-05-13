package com.flowcanvas.auth.model.form;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginForm {

    String email; 
    String password;
}