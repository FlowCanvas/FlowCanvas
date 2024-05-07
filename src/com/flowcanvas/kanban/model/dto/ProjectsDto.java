package com.flowcanvas.kanban.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectsDto {

	private int projectId;
	private int userId;
	private String projectName;
	private String projectJoinCode;
}
