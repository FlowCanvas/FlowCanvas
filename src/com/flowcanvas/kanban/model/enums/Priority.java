package com.flowcanvas.kanban.model.enums;

import lombok.Getter;

@Getter
public enum Priority {

	CHOICE("선택하세요.", -1),
	SMALL("낮음", 0),
	MIDDLE("중간", 1),
	LARGE("높음", 2),
	EMERGENCY("긴급", 3);
	
	
    private final String title;
    private final int value;

    
    Priority(String title, int value) {
        
    	this.title = title;
        this.value = value;
    }
    
    
    @Override
    public String toString() {
        return title;
    }
}
