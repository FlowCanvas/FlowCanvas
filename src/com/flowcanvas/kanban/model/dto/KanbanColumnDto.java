package com.flowcanvas.kanban.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KanbanColumnDto {
	private int kanbanColumnId;
	private int projectId;
	private String kanbanColumnName;
	private int useFlag;
	
}
