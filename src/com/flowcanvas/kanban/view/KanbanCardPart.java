package com.flowcanvas.kanban.view;

import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.flowcanvas.kanban.dao.KanbanCardDao;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;


public class KanbanCardPart extends JPanel {

	private CallBack callBack;
	
	private KanbanCardDao kanbanCardDao;
	
	private JButton btn_del;
	
	private int kanbanColumnId;
	
	
	public KanbanCardPart(
			int kanbanColumnId, int kanbanCardId, String kanbanCardName, 
			int userId, String nickName, int loginUserId, String loginNickName, 
			CallBack callBack) {

		this.callBack = callBack;
		this.kanbanCardDao = new KanbanCardDao();
		this.kanbanColumnId = kanbanColumnId;
		
		
		// 컴포넌트 생성
		initialize(kanbanCardId, kanbanCardName, nickName, loginUserId, loginNickName);
		
		// 작성자 로그인자 확인
		checkedUser(userId, loginUserId);
	}
	
	
	/*
	* ======================================== 
	* 컴포넌트 생성
	* ========================================
	*/
	private void initialize(int kanbanCardId, String kanbanCardName, String nickName, int loginUserId, String loginNickName) {
		
		Dimension dimension = new Dimension(280, 70);
		
		setPreferredSize(new Dimension(dimension));
		setMaximumSize(new Dimension(dimension));
		setBackground(Color.WHITE);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout(0, 0));
		
		
		Border bottomBorder = BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(200, 221, 242));
		JPanel pnl_card = new JPanel();
		add(pnl_card, BorderLayout.CENTER);
		pnl_card.setLayout(new BorderLayout(0, 0));
		pnl_card.setBorder(bottomBorder);
		
								
		JPanel pnl_user = new JPanel();
		pnl_card.add(pnl_user, BorderLayout.NORTH);
		pnl_user.setBackground(Color.WHITE);
		pnl_user.setLayout(new BorderLayout(0, 0));
		pnl_user.setBorder(new EmptyBorder(3, 3, 3, 3));
		
		
		JLabel lbl_user = new JLabel(nickName);
		lbl_user.setFont(new Font("굴림", Font.BOLD, 15));
		pnl_user.add(lbl_user, BorderLayout.CENTER);
		
		
		JPanel pnl_card_name = new JPanel();
		pnl_card.add(pnl_card_name, BorderLayout.CENTER);
		pnl_card_name.setLayout(new BorderLayout(0, 0));
		pnl_card_name.setBackground(Color.WHITE);
		pnl_card_name.setPreferredSize(new Dimension(245, getHeight()));
		pnl_card_name.setBorder(new EmptyBorder(3, 3, 3, 3));
		
		
		JButton btn_card_name = new JButton(kanbanCardName);
		pnl_card_name.add(btn_card_name, BorderLayout.CENTER);
		btn_card_name.setBorder(null);
		
		
		JPanel pnl_del = new JPanel();
		pnl_card.add(pnl_del, BorderLayout.EAST);
		pnl_del.setLayout(new BorderLayout(0, 0));
		pnl_del.setBackground(Color.WHITE);
		pnl_del.setBorder(new EmptyBorder(3, 3, 3, 3));
	
		
		btn_del = new JButton("X");
		pnl_del.add(btn_del, BorderLayout.EAST);
		btn_del.setBorder(null);
		btn_del.setPreferredSize(new Dimension(30, getHeight()));
		
		
		
        /*
		 * ======================================== 
		 * 이벤트
		 * ========================================
		 */
		
		// 칸반 카드 상세
		btn_card_name.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				KanbanCard kanbanCard = new KanbanCard(kanbanCardId, loginUserId, loginNickName);
		    	
		    	kanbanCard.setLocationRelativeTo(null);
		    	kanbanCard.setVisible(true);
			}
		});
		
		
		// 칸반 카드 삭제
		btn_del.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int delCheckMessage = 
			    		JOptionPane.showConfirmDialog(KanbanCardPart.this, "삭제하시겠습니까?", "삭제 확인", 
			    				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			    if (delCheckMessage == JOptionPane.YES_OPTION) {
			    	
					kanbanCardDao.delKanbanCard(kanbanCardId);
					callBack.onCardUpdated(kanbanColumnId);
			        
			        JOptionPane.showMessageDialog(KanbanCardPart.this, "삭제되었습니다.", "삭제 완료", JOptionPane.INFORMATION_MESSAGE);
			    }
			}
		});
	}

	// CallBack 인터페이스 정의
	public interface CallBack {
	
		void onCardUpdated(int kanbanColumnId);
	}


	// 작성자 로그인자 확인
	private void checkedUser(int userId, int loginUserId) {
	
		if (userId == loginUserId) {
			btn_del.setEnabled(true);
		} else {
			btn_del.setEnabled(false);
		}
	}
}
