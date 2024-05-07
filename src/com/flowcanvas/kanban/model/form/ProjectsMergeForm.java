package com.flowcanvas.kanban.model.form;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectsMergeForm {

	private int projectId;
	private int userId;
	private String projectName;
}
