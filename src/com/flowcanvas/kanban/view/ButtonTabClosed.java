package com.flowcanvas.kanban.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
 

public class ButtonTabClosed extends JPanel {
	
	private JTabbedPane tabpnl;
	private JLabel lbl_title;
	private JButton btn_close;
	private Consumer<Integer> onClose;
	
	
	public ButtonTabClosed(String title, JTabbedPane tabpnl, Consumer<Integer> onClose) {

		this.tabpnl = tabpnl;
		this.onClose = onClose;
		
		initialize(title);
		setupLayout();
	 }
	
	
	private void initialize(String title) {
		
		lbl_title = new JLabel(title);
        btn_close = new CustomButton("x");
        
        add(lbl_title);
        add(btn_close);
	}
	
	
	private void setupLayout() {
		
		 setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		 setBorder(new EmptyBorder(0, 0, 0, 0));
		 setOpaque(false);
		 
		 lbl_title.setBorder(new EmptyBorder(0, 10, 0, 10));
	}
	
	
	private class CustomButton extends JButton implements MouseListener {
		
		public CustomButton(String text) {
			
			// 버튼 크기 및 텍스트
			int size = 15;
            setPreferredSize(new Dimension(size, size));
            setText(text);
            // 배경 제외
            setContentAreaFilled(false);
            // 테두리
            setBorder(new EtchedBorder());
            setBorderPainted(false);
            // 이벤트 추가
            addMouseListener(this);
        }
 
		
        @Override
        public void mouseClicked(MouseEvent e) {
        	
            int tabIndex = tabpnl.indexOfTabComponent(ButtonTabClosed.this);
            if (tabIndex != -1) {
            	onClose.accept(tabIndex);
            	tabpnl.remove(tabIndex);
            }
        }
        
        
        @Override
        public void mouseEntered(MouseEvent e) {
        	
            setForeground(Color.RED);
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
        	
            setForeground(Color.BLACK);
        }
 
        
        @Override
        public void mousePressed(MouseEvent e) {}
        @Override
        public void mouseReleased(MouseEvent e) {}
    }
}