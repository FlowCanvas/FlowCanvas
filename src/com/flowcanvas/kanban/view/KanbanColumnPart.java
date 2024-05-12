package com.flowcanvas.kanban.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.flowcanvas.kanban.dao.KanbanCardDao;
import com.flowcanvas.kanban.model.dto.KanbanCardInitDto;
import com.flowcanvas.kanban.model.form.KanbanCardForm;

public class KanbanColumnPart extends JPanel {

	private JPanel kanban_view_panel;
	private int kanbanColumnId;
	private String kanbanColumnName;
	private int loginUserId;
	private String loginNickName;
	
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
	public KanbanColumnPart(JPanel kanban_view_panel,
			int kanbanColumnId, String kanbanColumnName, int loginUserId, String loginNickName) {
		
		this.kanban_view_panel = kanban_view_panel;
		this.kanbanColumnId = kanbanColumnId;
		this.kanbanColumnName = kanbanColumnName;
		this.loginUserId = loginUserId;
		this.loginNickName = loginNickName;
		this.kanbanCardDao = new KanbanCardDao();
		
		
		// GUI
		initialize();
	}


	// GUI
	private void initialize() {
		
		setLayout(new BorderLayout(0, 0));
		setBorder(new EmptyBorder(3, 3, 3, 3));
		setBackground(Color.WHITE);
		setName(String.valueOf(kanbanColumnId));
		
		
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
        add(scrollPane, BorderLayout.CENTER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        
        pnl_kanban_card_button = new JPanel();
        scrollPane.setViewportView(pnl_kanban_card_button);
        pnl_kanban_card_button.setBackground(Color.WHITE);
        pnl_kanban_card_button.setBorder(new EmptyBorder(5, 5, 5, 5));
        pnl_kanban_card_button.setLayout(new BoxLayout(pnl_kanban_card_button, BoxLayout.Y_AXIS));


		
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
		add(scrollPane, BorderLayout.CENTER);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		
		pnl_kanban_card_button = new JPanel();
		scrollPane.setViewportView(pnl_kanban_card_button);
		pnl_kanban_card_button.setBackground(Color.WHITE);
		pnl_kanban_card_button.setBorder(new EmptyBorder(5, 5, 5, 5));
		pnl_kanban_card_button.setLayout(new BoxLayout(pnl_kanban_card_button, BoxLayout.Y_AXIS));

		
		// 칸반 컬럼의 카드 조회
		selKanbanCardInit(kanbanColumnId);
        
        
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
	private void selKanbanCardInit(int kanbanColumnId) {

		pnl_kanban_card_button.removeAll();
		List<KanbanCardInitDto> KanbanCardInitDtoList = kanbanCardDao.selKanbanCardInit(kanbanColumnId);

		for (KanbanCardInitDto dto : KanbanCardInitDtoList) {
			
			KanbanCardPart kanbanCard = new KanbanCardPart(
					dto.getKanbanColumnId(), dto.getKanbanCardId(), dto.getKanbanCardName(), 
					dto.getUserId(), dto.getNickName(), loginUserId, loginNickName, 
					new KanbanCardPart.CallBack() {
						@Override
						public void onCardUpdated(int kanbanColumnId) {
							selKanbanCardInit(kanbanColumnId);
						}
					});

			pnl_kanban_card_button.add(kanbanCard);
			setMouseEvents(kanbanCard, dto.getKanbanColumnId(),  dto.getKanbanCardId());
		}

		pnl_kanban_card_button.revalidate();
		pnl_kanban_card_button.repaint();
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
			kanbanCardDao.mergeKanbanCard(KanbanCardForm.builder().kanbanCardName(kanbanCardName)
					.kanbanColumnId(kanbanColumnId).userId(loginUserId).build());
		}
		selKanbanCardInit(kanbanColumnId);
	}
	
	
	// 칸반 카드 순서 변경
	private void updKanbanCardSeq(int kanbanCardId, int kanbanColumnId, int cardSeq) {

		kanbanCardDao.updKanbanCardSeq(kanbanCardId, kanbanColumnId, cardSeq);
		
		// 칸반 컬럼의 카드 조회
		selKanbanCardInit(this.kanbanColumnId);
	}

	
	// 입력 할 수 있는 text 생성
	private void addKanbanCard() {
		// 텍스트 필드 전환
		cardLayout.show(cardPanel, "Text Card");
		// 포커스 맞추기
		txt_input_kanban_card.requestFocusInWindow();
	}

	
	// 칸반 마우스 이벤트
	private void setMouseEvents(KanbanCardPart kanbanCard, int kanbanColumnId, int kanbanCardId) {
		kanbanCard.addMouseListener(new MouseAdapter() {

			private int choiceKanbanColumn = kanbanColumnId;
			private int choiceSeq;

			@Override
			public void mousePressed(MouseEvent e) {

				KanbanCardPart draggedButton = (KanbanCardPart) e.getSource();
				Container currentParent = draggedButton.getParent();
				JPanel currentPanel = (JPanel) currentParent;
				
			    // 칸반 칼럼 내 위치 확인
			    Component[] components = currentPanel.getComponents();
			    for (int i = 0; i < components.length; i++) {
			        if (components[i] == draggedButton) {
			        	choiceSeq = i;
			            break;
			        }
			    }
			}

			
			@Override
			public void mouseReleased(MouseEvent e) {
				
				KanbanCardPart draggedButton = (KanbanCardPart) e.getSource();
				Container currentParent = draggedButton.getParent();
				JPanel activePanel = kanban_view_panel;
				Point dropPoint = SwingUtilities.convertPoint(draggedButton, e.getPoint(), activePanel);
				currentParent.remove(draggedButton);

				boolean inserted = false;
				
				for (Component comp : activePanel.getComponents()) {
					
					if (comp instanceof JPanel) {
						JPanel panel = (JPanel) comp;
						Rectangle bounds = panel.getBounds();

						if (bounds.contains(dropPoint)) {
							Component[] components = panel.getComponents();
							
							for (int i = 0; i < components.length; i++) {
								
								if (components[i] instanceof JScrollPane) {
									JScrollPane sc = (JScrollPane) components[i];
									Component[] components1 = sc.getComponents();
									
									for (int j = 0; j < components1.length; j++) {
										
										if (components1[j] instanceof JViewport) {
											JViewport view = (JViewport) components1[j];
											Component[] components2 = view.getComponents();

											for (int k = 0; k < components2.length; k++) {

												if (components2[k] instanceof JPanel) {
													Rectangle tsetbounds = components2[k].getBounds();
													JPanel pal = (JPanel) components2[k];
													Component[] components3 = pal.getComponents();
													
													for (int l = 0; l <= components3.length; l++) {

														Rectangle componentBounds = l < components3.length ? components3[l].getBounds() : new Rectangle(0, tsetbounds.height, tsetbounds.width, 1);

														if ((dropPoint.y + scrollPane.getVerticalScrollBar().getValue()- 20) < componentBounds.y + componentBounds.height / 2) {
															pal.add(draggedButton, l);
															inserted = true;
															
															choiceSeq = l;
															choiceKanbanColumn = Integer.parseInt(panel.getName());
															break;
														}

													}
												}
												if (inserted) {
													break;
												}
											}
										}
										if (inserted) {
											break;
										}
									}
								}
								if (inserted) {
									break;
								}
							}
							break;
						}
					}
				}
				
				// 칸반 카드 순서 변경
				updKanbanCardSeq(kanbanCardId, choiceKanbanColumn, choiceSeq);
				activePanel.revalidate();
				activePanel.repaint();
			}
		});

		
		kanbanCard.addMouseMotionListener(new MouseMotionAdapter() {

			private int x;
			private int y;

			@Override
			public void mouseDragged(MouseEvent e) {

				// 버튼 클릭 위치 맞추기
				int tem_x = e.getX();
				int tem_y = e.getY();

				x += tem_x;
				y += tem_y;

				kanbanCard.setBounds(x, y, 280, 70);
				getParent().setComponentZOrder(kanbanCard, 0);
			}
		});
	}
}