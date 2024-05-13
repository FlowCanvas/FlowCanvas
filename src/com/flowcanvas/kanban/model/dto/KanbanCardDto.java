package com.flowcanvas.kanban.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KanbanCardDto {
	
	private int kanbanCardId;
	private String kanbanCardName;
	private int kanbanColumnId;
	private String kanbanColumnName;
	private int userId;
	private String nickName;
	private Integer cardSeq;
	private Integer priority;
	private Integer taskSize;
	private String content;
}
