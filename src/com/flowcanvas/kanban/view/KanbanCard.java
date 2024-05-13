package com.flowcanvas.kanban.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.flowcanvas.common.socket.client.ClientServer;
import com.flowcanvas.kanban.dao.FeedBackDao;
import com.flowcanvas.kanban.dao.KanbanCardDao;
import com.flowcanvas.kanban.model.dto.FeedBackDto;
import com.flowcanvas.kanban.model.dto.KanbanCardDto;
import com.flowcanvas.kanban.model.enums.Priority;
import com.flowcanvas.kanban.model.enums.TaskSize;
import com.flowcanvas.kanban.model.form.FeedBackForm;
import com.flowcanvas.kanban.model.form.KanbanCardForm;

public class KanbanCard extends JDialog {
	
	private int loginUserId;
	private String loginNickName;
	private int kanbanCardId;
	private int kanbanColumnId;
	private int cardSeq;
	
	private JPanel pnl_content;
	private JLabel lbl_column_name;
	private JLabel lbl_write_user;
	private JTextField txt_title;
	private JTextArea txta_content;
	private JComboBox<Priority> cb_priority;
	private JComboBox<TaskSize> cb_taskSize;
	private JButton btn_ok;
	private JScrollPane scpnl_body;
	
	private JPanel pnl_feedback_list;
	private FeedbackPart feedbackPart;
	private JTextArea txta_feedback_write;
	
	private KanbanCardDao kanbanCardDao;
	private FeedBackDao feedBackDao;
	
	
	private ClientServer clientServer;
	private int projectId;
	
	
	public KanbanCard(int kanbanCardId, int loginUserId, String loginNickName,
			ClientServer clientServer, int projectId) {
		
		this.kanbanCardId = kanbanCardId;
		this.loginNickName = loginNickName;
		this.loginUserId = loginUserId;
		this.kanbanCardDao = new KanbanCardDao();
		this.feedBackDao = new FeedBackDao();
		
		this.clientServer = clientServer;
		this.projectId = projectId;
		
		
		// 컴포넌트 생성
		initialize();
		
	    // 칸반 카드 조회
        selKanbanCard();
        
        // 피드백 조회
        selFeedBack();
	}
	
	
	/*
	* ======================================== 
	* 컴포넌트 생성
	* ========================================
	*/
	private void initialize() {
		
		setTitle("칸반 카드 상세");
		getContentPane().setLayout(new BorderLayout(0, 0));
		setBounds(0, 0, 450, 550);
		
		
		JPanel pnl_top = new JPanel();
		getContentPane().add(pnl_top, BorderLayout.NORTH);
		pnl_top.setPreferredSize(new Dimension(this.getWidth(), 70));
		pnl_top.setLayout(new BorderLayout(0, 0));
		
		
		JPanel pnl_close = new JPanel();
		pnl_top.add(pnl_close, BorderLayout.NORTH);
		pnl_close.setPreferredSize(new Dimension(pnl_top.getWidth(), 20));
		pnl_close.setLayout(new BorderLayout(0, 0));
		pnl_close.setBorder(new EmptyBorder(2, 2, 2, 2));
		
		
		lbl_column_name = new JLabel("칸반 칼럼");
		pnl_close.add(lbl_column_name, BorderLayout.WEST);
		lbl_column_name.setBorder(new EmptyBorder(2, 5, 2, 2));
			
		
		JPanel pnl_title_back = new JPanel();
		pnl_top.add(pnl_title_back, BorderLayout.CENTER);
		pnl_title_back.setLayout(new BorderLayout(0, 0));
		pnl_title_back.setBorder(new EmptyBorder(2, 2, 2, 2));
		
		
		JPanel pnl_title = new JPanel();
		pnl_title_back.add(pnl_title, BorderLayout.CENTER);
		pnl_title.setLayout(new BorderLayout(0, 0));
		
		
		txt_title = new JTextField();
		txt_title.setText("칸반 카드 제목");
		txt_title.setFont(new Font("굴림", Font.BOLD, 25));
		txt_title.setMargin(new Insets(2, 10, 2, 2));
		pnl_title.add(txt_title, BorderLayout.CENTER);
		txt_title.setColumns(10);
		
		
		JPanel pnl_body = new JPanel();
		pnl_body.setBorder(new EmptyBorder(3, 3, 3, 3));
		getContentPane().add(pnl_body, BorderLayout.CENTER);
		pnl_body.setLayout(new BorderLayout(0, 0));
		pnl_body.setPreferredSize(new Dimension(getWidth(),getHeight()));
		
		
		JPanel pnl_bottom_line = new JPanel();
		pnl_bottom_line.setBackground(Color.WHITE);
		pnl_body.add(pnl_bottom_line, BorderLayout.NORTH);
        Border bottomBorder = BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 221, 242));
        pnl_bottom_line.setBorder(bottomBorder);
        
        
        scpnl_body = new JScrollPane();
        pnl_body.add(scpnl_body, BorderLayout.CENTER);
        scpnl_body.getVerticalScrollBar().setUnitIncrement(16); // 단위 스크롤량 설정
        scpnl_body.getVerticalScrollBar().setBlockIncrement(50); // 블록 스크롤량 설정
        
        
        JPanel pnl_division = new JPanel();
        scpnl_body.setViewportView(pnl_division);
        pnl_division.setLayout(new BorderLayout(0, 0));
        
   
        GridBagLayout gbl_pnl_write_division = new GridBagLayout();
        gbl_pnl_write_division.rowHeights = new int[]{270, 195, 0};
        gbl_pnl_write_division.columnWeights = new double[]{1.0};
        
        JPanel pnl_write_division = new JPanel();
        pnl_division.add(pnl_write_division, BorderLayout.NORTH);
        pnl_write_division.setLayout(gbl_pnl_write_division);
        
        
        GridBagConstraints gbc_pnl_content = new GridBagConstraints();
        gbc_pnl_content.fill = GridBagConstraints.BOTH;
        
        pnl_content = new JPanel();
        pnl_write_division.add(pnl_content, gbc_pnl_content);
        pnl_content.setLayout(new BorderLayout(0, 0));
        
        
        JPanel pnl_write_user = new JPanel();
        pnl_content.add(pnl_write_user, BorderLayout.NORTH);
        pnl_write_user.setBackground(Color.WHITE);
        pnl_write_user.setLayout(new BorderLayout(0, 0));
        pnl_write_user.setBorder(new EmptyBorder(2, 20, 2, 2));
        
        
        lbl_write_user = new JLabel("작성자 닉네임");
        pnl_write_user.add(lbl_write_user, BorderLayout.CENTER);
        
        
        JPanel pnl_content_body = new JPanel();
        pnl_content.add(pnl_content_body, BorderLayout.CENTER);
        pnl_content_body.setLayout(new BorderLayout(0, 0));
        pnl_content_body.setBorder(bottomBorder);
        
        
        JPanel pnl_content_button = new JPanel();
        pnl_content_body.add(pnl_content_button, BorderLayout.SOUTH);
        pnl_content_button.setBackground(Color.WHITE);
        pnl_content_button.setPreferredSize(new Dimension(pnl_content_body.getWidth(), 30));
        pnl_content_button.setBorder(new EmptyBorder(2, 2, 2, 2));
        pnl_content_button.setLayout(new BorderLayout(0, 0));
      
        
        btn_ok = new JButton("저장");
        pnl_content_button.add(btn_ok);
        
        
        JPanel pnl_content_write = new JPanel();
        pnl_content_body.add(pnl_content_write, BorderLayout.CENTER);
        pnl_content_write.setLayout(new BorderLayout(0, 0));
        
        
        JPanel pnl_status = new JPanel();
        pnl_content_write.add(pnl_status, BorderLayout.NORTH);
        pnl_status.setBackground(Color.WHITE);
        pnl_status.setPreferredSize(new Dimension(pnl_content_write.getWidth(), 60));
        pnl_status.setLayout(new GridLayout(0, 2, 50, 0));
        pnl_status.setBorder(new EmptyBorder(10, 50, 5, 50));
        
        
        JLabel lbl_priority = new JLabel("작업 우선순위");
        lbl_priority.setHorizontalAlignment(SwingConstants.CENTER);
        pnl_status.add(lbl_priority);
        
        
        JLabel lbl_task_size = new JLabel("작업크기");
        lbl_task_size.setHorizontalAlignment(SwingConstants.CENTER);
        pnl_status.add(lbl_task_size);
        
        
        cb_priority = new JComboBox<>(Priority.values());
        pnl_status.add(cb_priority);
        
     
        cb_taskSize = new JComboBox<>(TaskSize.values());
        pnl_status.add(cb_taskSize);
        
        
        txta_content = new JTextArea();
        pnl_content_write.add(txta_content, BorderLayout.CENTER);
        txta_content.setFont(new Font("굴림", Font.PLAIN, 15));
        txta_content.setMargin(new Insets(15, 20, 2, 2));
        
        
        GridBagConstraints gbc_pnl_feedback = new GridBagConstraints();
        gbc_pnl_feedback.fill = GridBagConstraints.BOTH;
        gbc_pnl_feedback.gridy = 1;
        
        
        JPanel pnl_feedback = new JPanel();
        pnl_write_division.add(pnl_feedback, gbc_pnl_feedback);
        pnl_feedback.setLayout(new BorderLayout(0, 0));
        
        
        JPanel pnl_feedback_article = new JPanel();
        pnl_feedback.add(pnl_feedback_article, BorderLayout.NORTH);
        pnl_feedback_article.setBackground(Color.WHITE);
        pnl_feedback_article.setPreferredSize(new Dimension(pnl_feedback.getWidth(), 40));
        pnl_feedback_article.setLayout(new BorderLayout(0, 0));
        pnl_feedback_article.setBorder(new EmptyBorder(15, 20, 5, 2));
        
        
        JLabel lbl_feedback_article = new JLabel("피드백");
        lbl_feedback_article.setFont(new Font("굴림", Font.BOLD, 20));
        pnl_feedback_article.add(lbl_feedback_article);
        
        
        JPanel pnl_feedback_content = new JPanel();
        pnl_feedback.add(pnl_feedback_content, BorderLayout.CENTER);
        pnl_feedback_content.setLayout(new BorderLayout(0, 0));
        
        
        JPanel pnl_feedback_login_user = new JPanel();
        pnl_feedback_login_user.setBackground(Color.WHITE);
        pnl_feedback_content.add(pnl_feedback_login_user, BorderLayout.NORTH);
        pnl_feedback_login_user.setLayout(new BorderLayout(0, 0));
        
        
        JLabel lbl_feedback_login_user = new JLabel(loginNickName);
        pnl_feedback_login_user.add(lbl_feedback_login_user, BorderLayout.CENTER);
        lbl_feedback_login_user.setPreferredSize(new Dimension(pnl_feedback_login_user.getWidth(), 30));
        lbl_feedback_login_user.setBorder(new EmptyBorder(15, 20, 2, 2));
        
        
        JPanel pnl_feedback_write = new JPanel();
        pnl_feedback_content.add(pnl_feedback_write, BorderLayout.CENTER);
        pnl_feedback_write.setLayout(new BorderLayout(0, 0));
        
        
        txta_feedback_write = new JTextArea();
        pnl_feedback_write.add(txta_feedback_write, BorderLayout.CENTER);
        txta_feedback_write.setFont(new Font("굴림", Font.PLAIN, 15));
        txta_feedback_write.setMargin(new Insets(15, 20, 2, 2));
        
        
        JPanel pnl_feedback_ok = new JPanel();
        pnl_feedback_ok.setBackground(Color.WHITE);
        pnl_feedback_write.add(pnl_feedback_ok, BorderLayout.SOUTH);
        pnl_feedback_ok.setLayout(new BorderLayout(0, 0));
        pnl_feedback_ok.setPreferredSize(new Dimension(pnl_content_body.getWidth(), 30));
        pnl_feedback_ok.setBorder(new EmptyBorder(2, 2, 2, 2));
        
        
        JButton btn_feedback_ok = new JButton("등록");
        pnl_feedback_ok.add(btn_feedback_ok, BorderLayout.CENTER);
        
        
        pnl_feedback_list = new JPanel();
        pnl_feedback_list.setBorder(new EmptyBorder(5, 5, 5, 5));
        pnl_division.add(pnl_feedback_list, BorderLayout.CENTER);
        pnl_feedback_list.setLayout(new BoxLayout(pnl_feedback_list, BoxLayout.Y_AXIS));
        
        
		/*
		* ======================================== 
		* 이벤트
		* ========================================
		*/
        
        btn_ok.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		mergeKanbanCard();
        	}
        });
        
        // 피드백 등록 이벤트
        btn_feedback_ok.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		
        		String strFeedback = txta_feedback_write.getText();
        		if(strFeedback.isBlank() || strFeedback.isEmpty()) {
        			JOptionPane.showMessageDialog(KanbanCard.this, "피드백을 입력해주세요", "입력 확인"
        					, JOptionPane.WARNING_MESSAGE);
        			
        			txta_feedback_write.requestFocusInWindow();
        			return;
        		}
        		
        		mergeFeedBack(strFeedback);
        	}
        });
	}
	
	
	// 칸반 카드 조회
	private void selKanbanCard() {
		
		KanbanCardDto kanbanCardInfo = kanbanCardDao.selKanbanCard(kanbanCardId);

		kanbanColumnId = kanbanCardInfo.getKanbanColumnId();
		cardSeq = kanbanCardInfo.getCardSeq();
		
		lbl_column_name.setText(kanbanCardInfo.getKanbanColumnName());
		lbl_write_user.setText(kanbanCardInfo.getNickName());
		txt_title.setText(kanbanCardInfo.getKanbanCardName());
		txta_content.setText(kanbanCardInfo.getContent());
		
		for (int i = 0; i < cb_priority.getItemCount(); i++) {
	        if (((Priority)cb_priority.getItemAt(i)).getValue() == kanbanCardInfo.getPriority()) {
	        	cb_priority.setSelectedIndex(i);
	            break;
	        }
	    }
		
		for (int i = 0; i < cb_taskSize.getItemCount(); i++) {
	        if (((TaskSize)cb_taskSize.getItemAt(i)).getValue() == kanbanCardInfo.getTaskSize()) {
	        	cb_taskSize.setSelectedIndex(i);
	        	break;
	        }
	    }
		
		// 로그인 아이디와 작성자 아이디가 다를 경우 카드 내용 작성 불가
		if (loginUserId != kanbanCardInfo.getUserId()) {
			txt_title.setEnabled(false);
			cb_priority.setEnabled(false);
			cb_taskSize.setEnabled(false);
			txta_content.setEnabled(false);
			btn_ok.setEnabled(false);
		}
		
		// 스크롤 맨위로 올리기
		scrollToTop();
		
		// 스크롤 맨위로 올리기를 이벤트 디스패치 큐의 끝으로 미룸
        SwingUtilities.invokeLater(this::scrollToTop);
	}
	
	
	// 칸반 카드 내용 확인
	private boolean mergeKanbanCardBefore() {
		
		// 작업 우선순위 확인
		Priority selectedPriority = (Priority) cb_priority.getSelectedItem();
		if (selectedPriority.getValue() == -1) {
			JOptionPane.showMessageDialog(null, "작업 우선순위를 선택해 주세요.", "선택 확인", JOptionPane.WARNING_MESSAGE);
			cb_priority.requestFocusInWindow();
			return false;
		}
		
		
		// 작업크기 확인
		TaskSize selectedTaskSize = (TaskSize) cb_taskSize.getSelectedItem();
		if (selectedTaskSize.getValue() == -1) {
			JOptionPane.showMessageDialog(null, "작업크기를 선택해 주세요.", "선택 확인", JOptionPane.WARNING_MESSAGE);
			cb_taskSize.requestFocusInWindow();
			return false;
		}
		
		
		// 칸반 카드 내용 확인
		String txtContext = txta_content.getText().trim();
		if (txtContext.isEmpty()) {
			JOptionPane.showMessageDialog(null, "카드 내용을 입력해주세요.", "입력 확인", JOptionPane.WARNING_MESSAGE);
			txta_content.requestFocusInWindow();
			return false;
		}
		
		return true;
	}
	
	
	// 칸반 카드 저장 수정
	private void mergeKanbanCard() {
		
		if (mergeKanbanCardBefore()) {
			Priority selectedPriority = (Priority) cb_priority.getSelectedItem();
			TaskSize selectedTaskSize = (TaskSize) cb_taskSize.getSelectedItem(); 
			
			KanbanCardForm kcf = KanbanCardForm.builder()
					.kanbanCardId(kanbanCardId)
					.kanbanCardName(txt_title.getText())
					.kanbanColumnId(kanbanColumnId)
					.userId(loginUserId)
					.cardSeq(cardSeq)
					.priority(selectedPriority.getValue())
					.taskSize(selectedTaskSize.getValue())
					.content(txta_content.getText())
					.build();

			kanbanCardDao.mergeKanbanCard(kcf);
			
			JOptionPane.showMessageDialog(this, "저장되었습니다.", "저장 완료", JOptionPane.INFORMATION_MESSAGE);
			
			// 칸반 카드 조회
			selKanbanCard();
			
			
			try {
                clientServer.sendMessage("cardplus:" + projectId);
                
             } catch (IOException e1) {
                e1.printStackTrace();
             }
			
		} else {
			// 스크롤 맨위로 올리기
			scrollToTop();
			
			// 스크롤 맨위로 올리기를 이벤트 디스패치 큐의 끝으로 미룸
	        SwingUtilities.invokeLater(this::scrollToTop);
		}
	}
	
	
	// 스크롤 맨위로 올리기
	private void scrollToTop() {
		
		scpnl_body.getVerticalScrollBar().setValue(0);
	}
	
	
	// 피드백 조회
	private void selFeedBack() {
		
		if(feedbackPart != null) {
			pnl_feedback_list.removeAll();
		}
		
		List<FeedBackDto> feedBackDtoList = feedBackDao.selFeedBack(kanbanCardId);
		if(feedBackDtoList != null) {
			
			// 데이터 바인딩
			for(FeedBackDto dto : feedBackDtoList) {
				feedbackPart = new FeedbackPart(dto, loginUserId, new FeedbackPart.Callback() {
					@Override
					public void onFeedBackModify() {
						// 피드백 조회
				        selFeedBack();
					}
				});
				
				pnl_feedback_list.add(feedbackPart);
			}
			
			pnl_feedback_list.revalidate();
			pnl_feedback_list.repaint();
		}
	}
	
	// 피드백 등록
	private void mergeFeedBack(String strFeedBack) {
		
		FeedBackForm feedBackForm = FeedBackForm.builder()
				.content(strFeedBack)
				.build();
		
		feedBackDao.mergerFeedBack(feedBackForm, 0, kanbanCardId, loginUserId);
		
		txta_feedback_write.setText("");
		JOptionPane.showMessageDialog(this, "저장되었습니다.", "저장 완료"
				, JOptionPane.INFORMATION_MESSAGE);
		
		// 피드백 조회
		selFeedBack();
	}
}
