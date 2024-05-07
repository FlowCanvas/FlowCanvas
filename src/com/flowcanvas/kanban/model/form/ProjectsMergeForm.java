package com.flowcanvas.kanban.model.form;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProjectsMergeForm {

	private int projectId;
	private int userId;
	private String projectName;
}
