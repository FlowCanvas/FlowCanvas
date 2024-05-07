package com.flowcanvas.kanban.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.flowcanvas.common.db.DBConnection;
import com.flowcanvas.kanban.model.dto.ProjectsDto;
import com.flowcanvas.kanban.model.form.ProjectsMergeForm;

import oracle.jdbc.internal.OracleTypes;

public class ProjectsDao {

	private ResultSet rs;

	
	// 프로젝트 조회
	public List<ProjectsDto> selProjets(int userId) {

		try (Connection conn = DBConnection.getConnection();
				CallableStatement cs = conn.prepareCall("{call PROJECT_CRUD_PACK.SEL_PROJECT(?, ?)}")) {

			cs.setInt(1, userId);
			cs.registerOutParameter(2, OracleTypes.CURSOR);

			cs.execute();

			rs = (ResultSet) cs.getObject(2);

			List<ProjectsDto> projectsList = new ArrayList<>();
			while (rs.next()) {
				ProjectsDto selProject = ProjectsDto.builder()
											.projectId(rs.getInt("project_id"))
											.projectName(rs.getString("project_name"))
											.projectJoinCode(rs.getString("project_join_code"))
											.userId(rs.getInt("user_id"))
											.build();

				projectsList.add(selProject);
			}
			return projectsList;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	// 프로젝트 저장 및 수정
	public void mergeProject(ProjectsMergeForm pmf) {

		try (Connection conn = DBConnection.getConnection();
				CallableStatement cs = conn.prepareCall("{call PROJECT_CRUD_PACK.INS_UPD_PROJECT(?, ?, ?)}")) {

			cs.setInt(1, pmf.getProjectId());
			cs.setString(2, pmf.getProjectName());
			cs.setInt(3, pmf.getUserId());

			cs.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	// 프로젝트 삭제
	public void delProject(int projectId) {
		
		try (Connection conn = DBConnection.getConnection();
				CallableStatement cs = conn.prepareCall("{call PROJECT_CRUD_PACK.DEL_PROJECT(?)}")) {

			cs.setInt(1, projectId);

			cs.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
