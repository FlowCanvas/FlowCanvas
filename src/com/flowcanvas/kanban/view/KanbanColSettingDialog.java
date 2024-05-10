package com.flowcanvas.kanban.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.flowcanvas.kanban.dao.KanbanColumnDao;
import com.flowcanvas.kanban.model.dto.KanbanColumnDto;
import com.flowcanvas.kanban.model.form.KanbanColumnForm;


public class KanbanColSettingDialog extends JDialog {
	
	// Callback 인터페이스 정의
    public interface Callback {
        void onColumnsUpdated();
    }

    
    // Callback
    private Callback callback;

	private final JPanel contentPanel = new JPanel();
	private JPanel columnConfigPanel;
	
	private KanbanColumnDao kanbanColumnDao;
	private int projectId;
	
	// gridLayout row count
	private int rowCount = 6;
	private int realRowCount = 0;

	/**
	 * Create the dialog.
	 */
	public KanbanColSettingDialog(int projectId, Callback callback) {
		this.projectId = projectId;
		this.kanbanColumnDao = new KanbanColumnDao();
		this.callback = callback;
		
		// UI
		initialize();
		
		// 칸반 컬럼 조회
		getKanbanColumnList("sub");
	}

	
	// UI
	private void initialize() {
		
		setTitle("칸반 컬럼 설정");
		setBounds(100, 100, 500, 320);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));

		
        columnConfigPanel = new JPanel();
        columnConfigPanel.setLayout(new GridLayout(6, 1, 0, 0));
        // 컬럼 설정 부분을 스크롤 가능하게 처리
        JScrollPane scrollPane = new JScrollPane(columnConfigPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        contentPanel.add(scrollPane);
        scrollPane.getViewport().setPreferredSize(new Dimension(480, 320));
		
        
		// 스크롤 양 조절
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // 단위 스크롤량 설정
        scrollPane.getVerticalScrollBar().setBlockIncrement(50); // 블록 스크롤량 설정

        
		// 새 컬럼 추가 부분
		JPanel newColumnPanel = new JPanel();
		newColumnPanel.setBorder(null);
		JTextField txt_new_col_name = new JTextField(50);
		txt_new_col_name.setMaximumSize(new Dimension(400, 20));
		txt_new_col_name.setFont(new Font("D2Coding", Font.PLAIN, 12));  // 폰트 설정
		// 설정 고정 크기
        Dimension fieldSize = new Dimension(400, 20);
        txt_new_col_name.setMinimumSize(fieldSize);
        txt_new_col_name.setPreferredSize(new Dimension(400, 25));
        
		JButton addButton = new JButton("추가");
		addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		addButton.setFont(new Font("D2Coding", Font.PLAIN, 12));  // 버튼 폰트 설정
		
		addButton.addActionListener(e -> {
			// 추가 시 검증
			if(txt_new_col_name.getText().isEmpty() || txt_new_col_name.getText().isBlank()) {
				JOptionPane.showMessageDialog(KanbanColSettingDialog.this, "컬럼명을 입력하세요", "메시지",
			            JOptionPane.WARNING_MESSAGE);
				
				return;
			}
			
			// 추가
			addColumnName(txt_new_col_name);
		});
		
		// 엔터 키 이벤트를 버튼의 액션과 연결
		txt_new_col_name.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	addButton.doClick();
            }
        });
		newColumnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		newColumnPanel.add(txt_new_col_name);
        newColumnPanel.add(addButton);

        // newColumnPanel 높이 조정
        newColumnPanel.setPreferredSize(new Dimension(300, 35));
        
        contentPanel.add(newColumnPanel, BorderLayout.NORTH);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new GridLayout(0, 2, 0, 0));
			{
				JButton btn_ok = new JButton("적용");
				btn_ok.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						// 칸반 컬럼 설정 저장
						boolean result = insertKanbanColumn(projectId);
						if(result) {
							dispose();
						}
					}
				});
				
				
				btn_ok.setFont(new Font("D2Coding", Font.PLAIN, 12));
				btn_ok.setActionCommand("OK");
				buttonPane.add(btn_ok);
				getRootPane().setDefaultButton(btn_ok);
			}
			{
				JButton btn_cancel = new JButton("취소");
				btn_cancel.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (callback != null) {
			            	// 콜백 호출
			                callback.onColumnsUpdated();  
			            }
						dispose();
					}
				});
				
				btn_cancel.setFont(new Font("D2Coding", Font.PLAIN, 12));
				btn_cancel.setActionCommand("Cancel");
				buttonPane.add(btn_cancel);
			}
		}
	}


	private void addColumnName(JTextField txt_new_col_name) {
		
		
		// 새 KanbanColumnDto 객체 생성
		KanbanColumnDto newColumn = KanbanColumnDto.builder()
				.kanbanColumnName(txt_new_col_name.getText())
				.useFlag('Y')
				.build();
		
		// 추가
		addColumnEntry(columnConfigPanel, newColumn);
		
		columnConfigPanel.revalidate();
		columnConfigPanel.repaint();
		
		// 필드 초기화
		txt_new_col_name.setText("");
	}

	
	// 칸반 컬럼 조회
	private void getKanbanColumnList(String uiSelected) {
        List<KanbanColumnDto> kanbanColumnDto =
        		kanbanColumnDao.selKanbancolumnData(projectId, uiSelected);
        
        // ui 설정
		for (KanbanColumnDto dto : kanbanColumnDto) {
			addColumnEntry(columnConfigPanel, dto);
		}
	}

	
	// 칸반 컬럼 조회 리스트 UI 설정
	private void addColumnEntry(JPanel panel, KanbanColumnDto kanbanColumnDto) {
		JPanel rowPanel = new JPanel(new BorderLayout());
		rowPanel.setAlignmentY(0.0f);
		rowPanel.setPreferredSize(new Dimension(400, 35));
		rowPanel.setBorder(null);

        JTextField txt_columnName = new JTextField(kanbanColumnDto.getKanbanColumnName());
        txt_columnName.setBounds(new Rectangle(0, 0, 300, 35));
        txt_columnName.setFont(new Font("D2Coding", Font.PLAIN, 12));
        txt_columnName.setHorizontalAlignment(JTextField.LEFT);
        txt_columnName.setToolTipText(Integer.toString(kanbanColumnDto.getKanbanColumnId()));
        txt_columnName.setName("txt_columnName");
        
        // 고정 크기 설정
        Dimension fieldSize = new Dimension(300, 25);
        txt_columnName.setMinimumSize(fieldSize);
        txt_columnName.setPreferredSize(fieldSize);
        txt_columnName.setHorizontalAlignment(JTextField.LEFT);
        
        JPanel radioPanel = new JPanel();
        radioPanel.setBorder(null);
        radioPanel.setBackground(UIManager.getColor("menu"));
        radioPanel.setBounds(new Rectangle(0, 0, 300, 35));
        
        // Y: 사용, N: 미사용
        JRadioButton rdo_use_flag_Y = new JRadioButton("사용", (kanbanColumnDto.getUseFlag() == 'Y'));
        rdo_use_flag_Y.setName("rdo_use_flag_Y");
        JRadioButton rdo_use_flag_N = new JRadioButton("미사용", (kanbanColumnDto.getUseFlag() != 'Y'));
        rdo_use_flag_N.setName("rdo_use_flag_N");
        
        ButtonGroup group = new ButtonGroup();
        group.add(rdo_use_flag_Y);
        group.add(rdo_use_flag_N);
        radioPanel.setLayout(new GridLayout(0, 2, 0, 0));
        radioPanel.add(rdo_use_flag_Y);
        radioPanel.add(rdo_use_flag_N);

        // 삭제 버튼 생성
        JButton btnDelete = new JButton(" X ");
        btnDelete.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		int columnId = Integer.parseInt(txt_columnName.getToolTipText());
        		viewDelColumn(columnId, e);
        	}
        });
        
        btnDelete.setBorder(null);
        btnDelete.setBackground(new Color(238, 238, 238));
        btnDelete.setPreferredSize(new Dimension(25, 23));
        btnDelete.setFont(new Font("D2Coding", Font.PLAIN, 12));
        btnDelete.setVerticalAlignment(SwingConstants.CENTER);
        
        
        // 새로운 패널 생성 및 구성
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(null);
        rightPanel.add(radioPanel, BorderLayout.CENTER);
        rightPanel.add(btnDelete, BorderLayout.EAST);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, txt_columnName, rightPanel);
        splitPane.setBorder(null);
        splitPane.setResizeWeight(0.2);
        splitPane.setDividerSize(0);
        
        // 사용자가 조절 못하도록 고정
        splitPane.setEnabled(false);

        rowPanel.add(splitPane);
        
        // 행 수 증가
        realRowCount++;
        
        // 레이아웃 업데이트
        panel.setLayout(new GridLayout(
        		(realRowCount <= 6 ? rowCount : realRowCount), 1, 0, 0));
        panel.add(rowPanel);
        
        panel.revalidate();  // 패널 재검증
        panel.repaint();  // 패널 다시 그리기
	}
	
	
	// 칸반 컬럼 설정 저장
	private boolean insertKanbanColumn(int projectId) {
		
		List<KanbanColumnForm> kanbanColumnFormList = collectColumnData();
		boolean isMergeRight =
				kanbanColumnDao.mergeKanbanColumn(projectId, kanbanColumnFormList);
		
		if (isMergeRight) {
            if (callback != null) {
            	// 콜백 호출
                callback.onColumnsUpdated();  
            }
            
            dispose();
            return true;
        }
		
        return false;
	}
	
	// 컬럼 삭제
	private void viewDelColumn(int columnId, MouseEvent e) {
		
		// 컬럼의 카드가 있는지 확인
		boolean hasKanbanCard = kanbanColumnDao.chkKanbanCard(columnId);
		
		if (hasKanbanCard) {
	        int userResponse = JOptionPane.showConfirmDialog(KanbanColSettingDialog.this,
	            "해당 컬럼에 카드가 있습니다.\n삭제하시겠습니까?", 
	            "삭제 확인", 
	            JOptionPane.YES_NO_OPTION, 
	            JOptionPane.QUESTION_MESSAGE
	        );

	        // 사용자가 'No'를 선택한 경우, 함수 종료
	        if (userResponse != JOptionPane.YES_OPTION) {
	            return;
	        }
	        
	        // 칸반 컬럼 삭제
		    kanbanColumnDao.delKanbanColumn(columnId);
	        
	    } else {
	    	// 칸반 컬럼 삭제
		    kanbanColumnDao.delKanbanColumn(columnId);
	    }
	    
	    // UI에서 컬럼 제거
	    removeColumnFromUI(e);
	}
	
	
	// 칸반 컬럼 설정 list 반환
	private List<KanbanColumnForm> collectColumnData() {
	    List<KanbanColumnForm> columnList = new ArrayList<>();
	    
	    // columnConfigPanel 내의 모든 rowPanel을 순회
	    Component[] rowPanels = columnConfigPanel.getComponents();
	    
	    for (Component comp : rowPanels) {
	    	
	        if (comp instanceof JPanel) {
	        	
	        	// 컨트롤 Name으로 찾기
	            JPanel rowPanel = (JPanel) comp;
	            JTextField columnNameField = (JTextField) findComponentByName(rowPanel, "txt_columnName");
	            JRadioButton usedButton = (JRadioButton) findComponentByName(rowPanel, "rdo_use_flag_Y");
	            JRadioButton unusedButton = (JRadioButton) findComponentByName(rowPanel, "rdo_use_flag_N");

	            if (columnNameField != null && (usedButton != null || unusedButton != null)) {
	            	// 사용여부가 사용
	            	boolean isActive = usedButton != null && usedButton.isSelected();
	                
	            	KanbanColumnForm kanbanColumnDto = KanbanColumnForm.builder()
	                		.kanbanColumnId(Integer.parseInt(columnNameField.getToolTipText()))
	                		.projectId(projectId)
	                		.kanbanColumnName(columnNameField.getText())
	                		.useFlag(isActive ? 'Y' : 'N')	// Y: 사용, N: 미사용
	                		.build();
	                
	                columnList.add(kanbanColumnDto);
	            }
	        }
	    }
	    
	    return columnList;
	}
	
	// 컨트롤 찾기
	private Component findComponentByName(Container container, String name) {
		
	    for (Component component : container.getComponents()) {
	    	// 이름이 일치하는 컴포넌트를 찾으면 반환
	        if (name.equals(component.getName())) {
	            return component; 
	        }
	        
	        if (component instanceof Container) {
	        	// 재귀
	            Component found = findComponentByName((Container) component, name);
	            if (found != null) {
	                return found; 
	            }
	        }
	    }
	    
	    return null;
	}
	
	
	// UI에서 컬럼 제거
	private void removeColumnFromUI(MouseEvent e) {
		// 이벤트가 발생한 컴포넌트에서 최상위 컨테이너 찾기
	    Component component = (Component) e.getSource();
	    Container container = component.getParent().getParent().getParent();

	    // 컬럼 패널에서 컨테이너 제거
	    columnConfigPanel.remove(container);
	    
	    // 행 수 감소
	    realRowCount--;
	    
        // 레이아웃 업데이트
	    columnConfigPanel.setLayout(new GridLayout(
	    		(realRowCount <= rowCount ? rowCount : realRowCount), 1, 0, 0));
        
	    columnConfigPanel.revalidate();
	    columnConfigPanel.repaint();
	}
	
}
