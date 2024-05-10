package com.flowcanvas.kanban.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class KanbanColumnPart extends JPanel {

	private JPanel kanban_view_panel;
	private Dimension parentsSize;
	private int len;
	private int kanbanColumnId;
	private String kanbanColumnName;
	
	
	/**
	 * Create the panel.
	 */
	public KanbanColumnPart(JPanel kanban_view_panel, Dimension parentsSize, int len,
			int kanbanColumnId, String kanbanColumnName) {
		
		this.kanban_view_panel = kanban_view_panel;
		this.parentsSize = parentsSize;
		this.len = len;
		this.kanbanColumnId = kanbanColumnId;
		this.kanbanColumnName = kanbanColumnName;
		
		
		// GUI
		initialize();
	}


	// GUI
	private void initialize() {
		
		setLayout(new BorderLayout(0, 0));
		setName("MainPanel" + kanbanColumnId);
		setBorder(new EmptyBorder(3, 3, 3, 3));
		setBackground(Color.WHITE);
		
		// 스크롤 생성 시 밑부분 조정
		setPreferredSize(new Dimension(320, 620));
		
		// Top panel 설정
        JPanel pnl_Top = new JPanel();
        add(pnl_Top, BorderLayout.NORTH);
        pnl_Top.setLayout(new BorderLayout(0, 0));
        pnl_Top.setPreferredSize(new Dimension(getWidth(), 35));
        
        // 컬럼명
        JLabel kanban_column_name = new JLabel();
        kanban_column_name.setBorder(null);
        kanban_column_name.setText(kanbanColumnName);
        kanban_column_name.setHorizontalAlignment(SwingConstants.CENTER);
        kanban_column_name.setFont(new Font("D2Coding", Font.PLAIN, 14));
        kanban_column_name.setBackground(new Color(200, 221, 241));
        pnl_Top.add(kanban_column_name, BorderLayout.CENTER);
        
        // 칸반 카드 추가 버튼
        JButton btnNewButton = new JButton("칸반 카드 추가");
        btnNewButton.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		// 입력 할 수 있는 text 생성
        		addKanbanCard();
        	}

			private void addKanbanCard() {
				JPanel pnl_addKanbanCard = new JPanel();
//				pnl_addKanbanCard.
			}
        });
        btnNewButton.setBorder(null);
        btnNewButton.setPreferredSize(new Dimension(0, 25));
        btnNewButton.setFont(new Font("D2Coding", Font.PLAIN, 12));
        btnNewButton.setBackground(new Color(238, 238, 238));
        add(btnNewButton, BorderLayout.SOUTH);
        
        
        // 칸반 카드 생성
        for (int j = 0; j < 3; j++) {
			JButton btnTest = new JButton("카드" + (kanbanColumnId + j + 1));
			btnTest.setName("Button" + kanbanColumnId + "_" + j);
			btnTest.setPreferredSize(new Dimension((int)(getWidth() - 100) / len, 50));
			btnTest.setBackground(Color.lightGray);
//			setupButtonEvents(btnTest, kanban_column_panel);
		}
        
        
	}

}
