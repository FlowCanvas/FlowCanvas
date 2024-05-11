package com.flowcanvas.kanban.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

public class KanbanCardButton extends JButton {
	
	private static final int ARC_WIDTH = 45; // 모서리 둥글기의 너비
    private static final int ARC_HEIGHT = 45; // 모서리 둥글기의 높이
	
	public KanbanCardButton() {
		setSize(new Dimension(300, 50));
		setText("제목입니다.");
		setFont(new Font("D2Coding", Font.BOLD, 14));
		setBorder(new EmptyBorder(new Insets(10, 10, 10, 10))); // 컨텐츠와 테두리 간의 간격
        setSize(new Dimension(300, 100));
        
        // 기본 버튼 배경 그리기 비활성화
        setContentAreaFilled(false); 
        // 투명하게 설정
        setOpaque(false); 
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground()); // 배경색 설정
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), ARC_WIDTH, ARC_HEIGHT); // 둥근 사각형 그리기
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getForeground()); // 테두리 색 설정
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ARC_WIDTH, ARC_HEIGHT); // 둥근 테두리 그리기
    }
	
}
