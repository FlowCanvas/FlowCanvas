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
	private int cardSeq;
	private int priority;
	private int taskSize;
	private String content;
}
