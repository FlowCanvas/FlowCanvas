package com.flowcanvas.kanban.model.enums;

import lombok.Getter;

@Getter
public enum TaskSize {
	
	CHOICE("선택하세요.", -1),
	SMALL("작음", 0),
	MEDIUM("중간", 1),
	LARGE("높음", 2),
	EXTRALARGE("매우 높음", 3);
	
	
	private final String title;
	private final int value;

	
	TaskSize(String title, int value) {
		
		this.title = title;
		this.value = value;
	}
	
	
    @Override
    public String toString() {
        return title;
    }
}
