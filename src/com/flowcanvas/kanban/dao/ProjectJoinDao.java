package com.flowcanvas.kanban.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.flowcanvas.common.db.DBConnection;

public class ProjectJoinDao {
	

	// 프로젝트 초대 시
	// 프로젝트 조인 저장
	public boolean insProjectJoin(int projectId, int userId) {

		try (Connection conn = DBConnection.getConnection();
				CallableStatement cs = conn.prepareCall("{call PROJECT_JOIN_C_PACK.INS_PROJECT_JOIN(?, ?)}")) {
	
			cs.setInt(1, projectId);
			cs.setInt(2, userId);

			cs.executeUpdate();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}