package com.flowcanvas.kanban.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProjectsDto {

	private int projectId;
	private int userId;
	private String projectName;
	private String projectJoinCode;
}
