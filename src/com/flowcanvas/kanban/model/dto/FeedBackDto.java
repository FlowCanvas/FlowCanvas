package com.flowcanvas.kanban.model.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeedBackDto {

	private int feedBackId;
	private int KanbanCardId;
	private int userId;
	private String nickName;
	private String content;
	private LocalDateTime updatedAt;
	
}
