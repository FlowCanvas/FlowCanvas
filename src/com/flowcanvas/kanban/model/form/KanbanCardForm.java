package com.flowcanvas.kanban.model.form;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KanbanCardForm {
	
	private int kanbanCardId;
	private String kanbanCardName;
	private int kanbanColumnId;
	private int userId;
	private Integer cardSeq;
	private Integer priority;
	private Integer taskSize;
	private String content;
}
