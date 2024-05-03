package com.flowcanvas.auth.form;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegistForm {
	String email;
	String password;
	String nickName;
}
