package com.flowcanvas.kanban.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import com.flowcanvas.kanban.model.dto.ProjectsDto;

public class ButtonListRenderer extends JPanel implements ListCellRenderer<ProjectsDto> {

	private int userId;
	private JLabel lbl_project_name;
	private JButton btn_modify_project;
	// 리스트 row 테두리
	private Border listBorder;
	// 리스트 row 마진
	private Border listMargin;
	// 테두리, 마진 결합
	private Border combineBorder;

	
	public ButtonListRenderer(int userId) {

		this.userId = userId;

		setLayout(new BorderLayout());

		lbl_project_name = new JLabel();
		btn_modify_project = new JButton("···");

		add(lbl_project_name, BorderLayout.CENTER);
		add(btn_modify_project, BorderLayout.EAST);

		listBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY);
		listMargin = new EmptyBorder(5, 0, 5, 5);
		combineBorder = new CompoundBorder(listBorder, listMargin);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends ProjectsDto> listProjectDto, ProjectsDto projectDto,
			int index, boolean isSelected, boolean cellHasFocus) {

		// 리스트에 보여줄 텍스트
		lbl_project_name.setText(projectDto.getProjectName());

		// 프로젝트 생성자 일경우에만 버튼 활성화
		if (projectDto.getUserId() != userId) {
			btn_modify_project.setEnabled(false);
		} else {
			btn_modify_project.setEnabled(true);
		}

		// 리스트 선택시 배경 컬러
		if (isSelected) {
			setBackground(listProjectDto.getSelectionBackground());
			lbl_project_name.setForeground(listProjectDto.getSelectionForeground());
		} else {
			setBackground(listProjectDto.getBackground());
			lbl_project_name.setForeground(listProjectDto.getForeground());
		}

		// 테두리, 마진 결합 세팅
		setBorder(combineBorder);

		return this;
	}
}