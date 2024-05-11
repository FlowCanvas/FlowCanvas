package com.flowcanvas.kanban.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.flowcanvas.kanban.dao.KanbanCardDao;
import com.flowcanvas.kanban.model.form.KanbanCardForm;

public class KanbanColumnPart extends JPanel {

	private JPanel kanban_view_panel;
	private Dimension parentsSize;
	private int len;
	private int kanbanColumnId;
	private String kanbanColumnName;
	private int loginUserId;
	
	private JButton btn_add_kanban_card;
    private JTextField txt_input_kanban_card;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    
    private KanbanCardDao kanbanCardDao;
    private JScrollPane scrollPane;
    private JPanel pnl_kanban_card_button;
    
	
	/**
	 * Create the panel.
	 */
	public KanbanColumnPart(JPanel kanban_view_panel, Dimension parentsSize, int len,
			int kanbanColumnId, String kanbanColumnName, int loginUserId) {
		
		this.kanban_view_panel = kanban_view_panel;
		this.parentsSize = parentsSize;
		this.len = len;
		this.kanbanColumnId = kanbanColumnId;
		this.kanbanColumnName = kanbanColumnName;
		this.loginUserId = loginUserId;
		this.kanbanCardDao = new KanbanCardDao();
		
		
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
		setPreferredSize(new Dimension(320, 600));
		
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
        kanban_column_name.setFont(new Font("D2Coding", Font.BOLD, 14));
        kanban_column_name.setBackground(new Color(200, 221, 241));
        pnl_Top.add(kanban_column_name, BorderLayout.CENTER);
        
        // 카드 레이아웃 생성
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        add(cardPanel, BorderLayout.SOUTH);
        
        // 칸반 카드 추가 버튼
        btn_add_kanban_card = new JButton("칸반 카드 추가 +");
        btn_add_kanban_card.setBorder(null);
        btn_add_kanban_card.setPreferredSize(new Dimension(0, 25));
        btn_add_kanban_card.setFont(new Font("D2Coding", Font.BOLD, 12));
        btn_add_kanban_card.setBackground(new Color(238, 238, 238));
        cardPanel.add(btn_add_kanban_card, "Button Card");
        
        // 칸반 카드 추가 입력 텍스트
        txt_input_kanban_card = new JTextField(50);
        txt_input_kanban_card.setFont(new Font("D2Coding", Font.PLAIN, 12));
        cardPanel.add(txt_input_kanban_card, "Text Card");
        
        // 초기 화면 버튼
        cardLayout.show(cardPanel, "Button Card");
        
        scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
        
        pnl_kanban_card_button = new JPanel();
        pnl_kanban_card_button.setBackground(Color.WHITE);
        pnl_kanban_card_button.setBorder(new EmptyBorder(5, 5, 5, 5));
        scrollPane.setViewportView(pnl_kanban_card_button);
        pnl_kanban_card_button.setLayout(new GridLayout(5, 1, 0, 7));
        

        // 칸반 컬럼의 카드 조회
        selKanbanCard(kanbanColumnId);
        
        // 칸반 카드 생성
//        for (KanbanCardDto dto : cardDtos) {
//        	KanbanCardButton kanbanCardButton = new KanbanCardButton();
//        	kanbanCardButton.setText(dto.getKanbanCardName());
//        	
//        	pnl_kanban_card_button.add(kanbanCardButton);
//		}
        
        
        /*
		 * ======================================== 
		 * 이벤트
		 * ========================================
		 */
        
        // 칸반 카드 생성 버튼 클릭 이벤트
        btn_add_kanban_card.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		// 입력 할 수 있는 text 생성
        		addKanbanCard();
        	}
        });
        
        
        // 키 이벤트
        setupKeyBindings();
	}
	
	// 칸반 컬럼의 카드 조회
	private void selKanbanCard(int kanbanColumnId) {
		kanbanCardDao.selKanbanCard(kanbanColumnId);
	}


	// 키 이벤트
	private void setupKeyBindings() {
		// esc 키 이벤트
		txt_input_kanban_card.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");
        txt_input_kanban_card.getActionMap().put("Cancel", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txt_input_kanban_card.setText("");
                showButtonCard();
            }
        });
        
        // enter 이벤트
        txt_input_kanban_card.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Submit");
        txt_input_kanban_card.getActionMap().put("Submit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(txt_input_kanban_card.getText().isEmpty() || txt_input_kanban_card.getText().isBlank()) {
        			return;
        		}
            	
                submitData();
                txt_input_kanban_card.setText("");
                showButtonCard();
            }
        });
        
	}

	// 버튼 보여주기
	private void showButtonCard() {
		cardLayout.show(cardPanel, "Button Card");
	}
	
	// 칸반 카드 저장
	private void submitData() {
		String kanbanCardName = txt_input_kanban_card.getText().trim();
        if (!kanbanCardName.isEmpty()) {
        	kanbanCardDao.mergeKanbanCard(KanbanCardForm.builder()
        			.kanbanCardName(kanbanCardName)
        			.kanbanColumnId(kanbanColumnId)
        			.userId(loginUserId)
        			.build());
        }
	}

	// 입력 할 수 있는 text 생성
	private void addKanbanCard() {
		// 텍스트 필드 전환
		cardLayout.show(cardPanel, "Text Card");
		// 포커스 맞추기
        txt_input_kanban_card.requestFocusInWindow();
	}

}
