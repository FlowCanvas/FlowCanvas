package com.flowcanvas.kanban.view;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class KanbanCardPart extends JPanel {

	/**
	 * Create the panel.
	 */
	public KanbanCardPart(int kanbanCardId, String kanbanCardName, int loginUserId
			, String loginNickName) {
		
		setBackground(Color.WHITE);
		
		Dimension dimension = new Dimension(280, 50);
		
		setPreferredSize(new Dimension(dimension));
		setMaximumSize(new Dimension(dimension));
		
		setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setLayout(new BorderLayout(0, 0));
		
		JPanel pnl_card_del = new JPanel();
		add(pnl_card_del, BorderLayout.EAST);
		
		JPanel pnl_card = new JPanel();
		add(pnl_card, BorderLayout.CENTER);
		pnl_card.setLayout(new BorderLayout(0, 0));
		
		JButton btn_card = new JButton(kanbanCardName);
		
		btn_card.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				KanbanCard kanbanCard = new KanbanCard(kanbanCardId, loginUserId
		    			, loginNickName);
		    	
		    	kanbanCard.setLocationRelativeTo(null);
		    	kanbanCard.setVisible(true);
			}
		});
		
		pnl_card.add(btn_card, BorderLayout.CENTER);

	}

}
