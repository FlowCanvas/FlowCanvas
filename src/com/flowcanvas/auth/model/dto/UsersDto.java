package com.flowcanvas.auth.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UsersDto {

	private int userId;
    private String email;
    private String nickName;
}
