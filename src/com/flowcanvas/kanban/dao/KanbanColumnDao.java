package com.flowcanvas.kanban.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.flowcanvas.common.db.DBConnection;
import com.flowcanvas.kanban.model.dto.KanbanColumnDto;

import oracle.jdbc.internal.OracleTypes;

public class KanbanColumnDao {
	
	private ResultSet rs;
	
	// 칸반 컬럼 조회
	public List<KanbanColumnDto> getKanbancolumnData(int projectId) {
		try (Connection conn = DBConnection.getConnection();
				CallableStatement cs = conn.prepareCall("{call KANBAN_COLUNM_CRUD_PACK.SEL_KANBAN_COL(?, ?)}")) {

			cs.setInt(1, projectId);
			cs.registerOutParameter(2, OracleTypes.CURSOR);

			cs.execute();

			rs = (ResultSet) cs.getObject(2);

			List<KanbanColumnDto> kanbanColumnList = new ArrayList<>();
			while (rs.next()) {
				KanbanColumnDto selKanbanColumn = KanbanColumnDto.builder()
						.kanbanColumnId(rs.getInt("kanban_column_id"))
						.projectId(rs.getInt("project_id"))
						.kanbanColumnName(rs.getString("kanban_column_name"))
						.useFlag(rs.getInt("use_flag"))
						.build();

				kanbanColumnList.add(selKanbanColumn);
			}
			
			rs.close();
			
			return kanbanColumnList;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
