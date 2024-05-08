package com.flowcanvas.kanban.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.flowcanvas.kanban.dao.KanbanColumnDao;
import com.flowcanvas.kanban.model.dto.KanbanColumnDto;
import javax.swing.BoxLayout;


public class KanbanColSettingDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	private List<JRadioButton> usageButtons = new ArrayList<>();
	private List<JTextField> columnNames = new ArrayList<>();
	
	private JPanel columnConfigPanel;
	
	
	private KanbanColumnDao kanbanColumnDao;
	private int projectId;

	/**
	 * Create the dialog.
	 */
	public KanbanColSettingDialog(int projectId) {
		this.projectId = projectId;
		this.kanbanColumnDao = new KanbanColumnDao();
		
		
		initialize();
		
		// 칸반 컬럼 조회
		getKanbanColumnList();
	}

	private void initialize() {
		
		setTitle("칸반 컬럼 설정");
		setBounds(100, 100, 475, 320);  // 높이를 약간 조정
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));

		// 컬럼 설정 부분을 스크롤 가능하게 처리
        columnConfigPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(columnConfigPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
		
		// 스크롤 양 조절
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // 단위 스크롤량 설정
        scrollPane.getVerticalScrollBar().setBlockIncrement(50); // 블록 스크롤량 설정

        
		// 새 컬럼 추가 부분
		JPanel newColumnPanel = new JPanel();
		JTextField txt_new_col_name = new JTextField(50);
		txt_new_col_name.setMaximumSize(new Dimension(400, 20));
		txt_new_col_name.setFont(new Font("D2Coding", Font.PLAIN, 12));  // 폰트 설정
		// 설정 고정 크기
        Dimension fieldSize = new Dimension(400, 20);
        txt_new_col_name.setMinimumSize(fieldSize);
        txt_new_col_name.setPreferredSize(fieldSize);
        
		JButton addButton = new JButton("추가");
		addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		addButton.setFont(new Font("D2Coding", Font.PLAIN, 12));  // 버튼 폰트 설정
		addButton.addActionListener(e -> {
			// 새 KanbanColumnDto 객체 생성
		    KanbanColumnDto newColumn = KanbanColumnDto.builder()
		    		.kanbanColumnName(txt_new_col_name.getText())
		    		.useFlag(0)
		    		.build();
		    
		    addColumnEntry(columnConfigPanel, newColumn);
			
			columnConfigPanel.revalidate();
			columnConfigPanel.repaint();
			txt_new_col_name.setText(""); // 필드 초기화
		});
		newColumnPanel.setLayout(new BoxLayout(newColumnPanel, BoxLayout.X_AXIS));
		newColumnPanel.add(txt_new_col_name);
        newColumnPanel.add(addButton);

        // newColumnPanel 높이 조정
        newColumnPanel.setPreferredSize(new Dimension(300, 20));
        
        contentPanel.add(newColumnPanel, BorderLayout.NORTH);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new GridLayout(0, 2, 0, 0));
			{
				JButton okButton = new JButton("적용");
				okButton.setFont(new Font("D2Coding", Font.PLAIN, 12));
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("취소");
				cancelButton.setFont(new Font("D2Coding", Font.PLAIN, 12));
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	// 칸반 컬럼 조회
	private void getKanbanColumnList() {
		// 칸반 컬럼 조회
        List<KanbanColumnDto> kanbanColumnDto =
        		kanbanColumnDao.getKanbancolumnData(projectId);
        
		// 예시 데이터, 실제로는 데이터베이스에서 읽어올 것입니다.
		for (KanbanColumnDto dto : kanbanColumnDto) {
			addColumnEntry(columnConfigPanel, dto);
		}
	}

	
	// 칸반 컬럼 조회 리스트 UI 설정
	private void addColumnEntry(JPanel panel, KanbanColumnDto kanbanColumnDto) {
		JPanel rowPanel = new JPanel(new BorderLayout());

        JTextField columnNameField = new JTextField(kanbanColumnDto.getKanbanColumnName());
        columnNameField.setBounds(new Rectangle(0, 0, 300, 20));
        columnNameField.setFont(new Font("D2Coding", Font.PLAIN, 12));  // 폰트 설정
        columnNameField.setHorizontalAlignment(JTextField.LEFT);
        // 고정 크기 설정
        Dimension fieldSize = new Dimension(300, 20);
        columnNameField.setMinimumSize(fieldSize);
        columnNameField.setPreferredSize(fieldSize);
        columnNameField.setHorizontalAlignment(JTextField.LEFT);
        
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        radioPanel.setBounds(new Rectangle(0, 0, 300, 20));
        JRadioButton usedButton = new JRadioButton("사용", (kanbanColumnDto.getUseFlag() == 0));
        JRadioButton unusedButton = new JRadioButton("미사용", (kanbanColumnDto.getUseFlag() != 0));
        ButtonGroup group = new ButtonGroup();
        group.add(usedButton);
        group.add(unusedButton);
        radioPanel.add(usedButton);
        radioPanel.add(unusedButton);
        columnConfigPanel.setLayout(new GridLayout(0, 1, 0, 0));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, columnNameField, radioPanel);
        splitPane.setResizeWeight(0.7);
        splitPane.setEnabled(false);  // 사용자가 조절 못하도록 고정

        rowPanel.add(splitPane);
        panel.add(rowPanel);
	}

}
