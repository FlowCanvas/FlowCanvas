package com.flowcanvas.kanban.view;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import java.awt.Color;

public class KanbanCardPart extends JPanel {

	/**
	 * Create the panel.
	 */
	public KanbanCardPart(String kanbanCardName) {
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
		pnl_card.add(btn_card, BorderLayout.CENTER);

	}

}
