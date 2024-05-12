package com.flowcanvas.kanban.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.flowcanvas.kanban.dao.FeedBackDao;
import com.flowcanvas.kanban.model.dto.FeedBackDto;
import com.flowcanvas.kanban.model.form.FeedBackForm;

public class FeedbackPart extends JPanel {

	// Callback 인터페이스 정의
    public interface Callback {
        void onFeedBackModify();
    }
	
    // Callback
    private Callback callback;
	
	private JLabel lbl_user_nickname;
	private JLabel lbl_updated_at;
	private JTextArea txta_feedback_content;
	
	private FeedBackDto feedBackDto;
	private int loginUserId;
	
	private FeedBackDao feedBackDao;
	
	
	/**
	 * Create the panel.
	 */
	public FeedbackPart(FeedBackDto feedBackDto, int loginUserId, Callback callback) {
		
		this.feedBackDto = feedBackDto;
		this.loginUserId = loginUserId;
		this.feedBackDao = new FeedBackDao();
		this.callback = callback;
		
		// 컴포넌트 생성
		initialize();
	}
	
	
	/*
	* ======================================== 
	* 컴포넌트 생성
	* ========================================
	*/
	private void initialize() {
		
		setBorder(null);
		setFont(new Font("D2Coding", Font.PLAIN, 12));
		setLayout(new BorderLayout(0, 0));
		
		Dimension dimension = new Dimension(350, 100);
		setPreferredSize(new Dimension(dimension));
		setMaximumSize(new Dimension(dimension));
		
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setPreferredSize(new Dimension(getWidth(), 30));
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.WHITE);
		panel.add(panel_3, BorderLayout.CENTER);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		
		lbl_user_nickname = new JLabel(feedBackDto.getNickName());
		lbl_user_nickname.setToolTipText(Integer.toString(feedBackDto.getFeedBackId()));
		
		lbl_user_nickname.setBorder(new EmptyBorder(3, 3, 3, 3));
		lbl_user_nickname.setFont(new Font("D2Coding", Font.PLAIN, 12));
		panel_3.add(lbl_user_nickname, BorderLayout.CENTER);
		lbl_user_nickname.setPreferredSize(new Dimension(120, panel_3.getHeight()));

		
		JPanel pnl_datetime_del = new JPanel();
		pnl_datetime_del.setBackground(Color.WHITE);
		pnl_datetime_del.setLayout(new BorderLayout(0, 0));
		panel_3.add(pnl_datetime_del, BorderLayout.EAST);
		
		
		// yyyy-mm-dd HH:mm 형태
		lbl_updated_at = new JLabel(transLocalDateTime(feedBackDto.getUpdatedAt()));
		lbl_updated_at.setBorder(new EmptyBorder(3, 3, 3, 3));
		lbl_updated_at.setPreferredSize(new Dimension(110, panel_3.getHeight()));
		lbl_updated_at.setFont(new Font("D2Coding", Font.PLAIN, 12));
		lbl_updated_at.setHorizontalAlignment(SwingConstants.CENTER);
		pnl_datetime_del.add(lbl_updated_at, BorderLayout.CENTER);
		
		
		JButton btn_del = new JButton(" X ");
		btn_del.setVisible(loginUserId == feedBackDto.getUserId());
		btn_del.setBorder(null);
		btn_del.setBackground(Color.WHITE);
		btn_del.setFont(new Font("D2Coding", Font.PLAIN, 12));
		pnl_datetime_del.add(btn_del, BorderLayout.EAST);
		
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(null);
		panel_1.setPreferredSize(new Dimension(60, 80));
		panel_1.setMaximumSize(new Dimension(60, 80));
		add(panel_1, BorderLayout.EAST);
		
		
		JButton btn_modify = new JButton("수정");
		btn_modify.setVisible(loginUserId == feedBackDto.getUserId());
		panel_1.add(btn_modify);
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		add(scrollPane, BorderLayout.CENTER);
		
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(null);
		scrollPane.setViewportView(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		
		txta_feedback_content = new JTextArea(feedBackDto.getContent());
		txta_feedback_content.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_2.add(txta_feedback_content, BorderLayout.CENTER);
		txta_feedback_content.setFont(new Font("굴림", Font.PLAIN, 15));
		txta_feedback_content.setEditable(loginUserId == feedBackDto.getUserId());
		
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(null);
		add(panel_4, BorderLayout.SOUTH);
		panel_4.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lbl_dividing_line = new JLabel("──────────────────────────");
		panel_4.add(lbl_dividing_line);
		
		
		/*
		* ======================================== 
		* 이벤트
		* ========================================
		*/
		
		// 피드백 수정 버튼
		btn_modify.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String strFeedBack = txta_feedback_content.getText();
				
				if(strFeedBack.isBlank() || strFeedBack.isEmpty()) {
					JOptionPane.showMessageDialog(FeedbackPart.this, "피드백을 입력해주세요", "입력 확인"
							, JOptionPane.WARNING_MESSAGE);
					
					txta_feedback_content.requestFocusInWindow();
					return;
				}
				
				mergeFeedBack(strFeedBack, Integer.parseInt(lbl_user_nickname.getToolTipText()),
						feedBackDto.getKanbanCardId(), feedBackDto.getUserId());
			}
		});
		
		
		// 피드백 삭제 버튼
		btn_del.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int userResponse = JOptionPane.showConfirmDialog(FeedbackPart.this,
						"삭제하시겠습니까?", 
			            "삭제 확인", 
			            JOptionPane.YES_NO_OPTION, 
			            JOptionPane.QUESTION_MESSAGE
			        );
				
				// 사용자가 'No'를 선택한 경우, 함수 종료
		        if (userResponse != JOptionPane.YES_OPTION) {
		            return;
		        }
		        
		        delFeedBack(Integer.parseInt(lbl_user_nickname.getToolTipText()),
						feedBackDto.getKanbanCardId());
			}
		});
	}


	// LocalDateTime 변환
	private String transLocalDateTime(LocalDateTime localDateTime) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		return localDateTime.format(formatter);
	}
	

	// 피드백 수정
	private void mergeFeedBack(String strFeedBack, int feedBackId, int kanbanCardId, int userId) {
		
		FeedBackForm feedBackForm = FeedBackForm.builder()
				.content(strFeedBack)
				.build();
		
		feedBackDao.mergerFeedBack(feedBackForm, feedBackId, kanbanCardId, userId);
		JOptionPane.showMessageDialog(this, "수정되었습니다.", "수정 완료"
				, JOptionPane.INFORMATION_MESSAGE);
		
		// 콜백 호출 추가
	    if (callback != null) {
	        callback.onFeedBackModify();
	    }
	}
	
	// 피드백 삭제
	private void delFeedBack(int feedBackId, int kanbanCardId) {
		
		feedBackDao.delFeedBack(feedBackId, kanbanCardId);
		JOptionPane.showMessageDialog(this, "삭제되었습니다.", "삭제 완료"
				, JOptionPane.INFORMATION_MESSAGE);
		
		// 콜백 호출 추가
	    if (callback != null) {
	        callback.onFeedBackModify();
	    }
	}

}
