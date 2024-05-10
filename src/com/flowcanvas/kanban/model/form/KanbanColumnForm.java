package com.flowcanvas.kanban.model.form;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KanbanColumnForm {

	private int kanbanColumnId;
	private int projectId;
	private String kanbanColumnName;
	private char useFlag;
}
