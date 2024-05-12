package com.flowcanvas.kanban.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KanbanCardInitDto {

	private int kanbanCardId;
	private String kanbanCardName;
	private int kanbanColumnId;
	private int userId;
	private String nickName;
}
