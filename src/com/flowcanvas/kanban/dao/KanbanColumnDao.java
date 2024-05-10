package com.flowcanvas.kanban.dao;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Struct;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.flowcanvas.common.db.DBConnection;
import com.flowcanvas.kanban.model.dto.KanbanColumnDto;
import com.flowcanvas.kanban.model.form.KanbanColumnForm;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.internal.OracleTypes;

public class KanbanColumnDao {
	
	private ResultSet rs;
	
	// 칸반 컬럼 조회
	public List<KanbanColumnDto> selKanbancolumnData(int projectId, String uiSelected) {
		
		try (Connection conn = DBConnection.getConnection();
				CallableStatement cs = conn.prepareCall("{call KANBAN_COLUMN_CRUD_PACK.SEL_KANBAN_COL(?, ?, ?)}")) {

			cs.setInt(1, projectId);
			cs.setString(2, uiSelected);
			cs.registerOutParameter(3, OracleTypes.CURSOR);

			cs.execute();

			rs = (ResultSet) cs.getObject(3);

			List<KanbanColumnDto> kanbanColumnList = new ArrayList<>();
			while (rs.next()) {
				KanbanColumnDto selKanbanColumn = KanbanColumnDto.builder()
						.kanbanColumnId(rs.getInt("kanban_column_id"))
						.projectId(rs.getInt("project_id"))
						.kanbanColumnName(rs.getString("kanban_column_name"))
						.useFlag(rs.getString("use_flag").charAt(0))
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
	
	
	// 칸반 컬럼 저장 및 수정
	public boolean mergeKanbanColumn(int projectId, List<KanbanColumnForm> kanbanColumnForm) {
		
		try (Connection conn = DBConnection.getConnection()) {
	        if (conn.isWrapperFor(OracleConnection.class)) {
	            OracleConnection oracleConn = conn.unwrap(OracleConnection.class);
	            
	            Struct[] structsArray = new Struct[kanbanColumnForm.size()];
	            for (int i = 0; i < kanbanColumnForm.size(); i++) {
	                KanbanColumnForm form = kanbanColumnForm.get(i);
	                Object[] attributes = new Object[] {
	                    form.getKanbanColumnId(),
	                    form.getProjectId(),
	                    form.getKanbanColumnName(),
	                    form.getUseFlag()
	                };
	                
	                structsArray[i] = oracleConn.createStruct("KANBAN_COLUMN_OBJ", attributes);
	            }

	            Array oracleArray = oracleConn.createOracleArray("KANBAN_COLUMN_TAB", structsArray);

	            try (CallableStatement cs = conn.prepareCall("{call KANBAN_COLUMN_CRUD_PACK.INS_UPD_KANBAN_COL(?, ?)}")) {
	                cs.setInt(1, projectId);
	                cs.setArray(2, oracleArray);
	                cs.execute();
	            }
	            
	            return true;
	        }
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		
	    return false;
	}


	// 칸반 컬럼에 카드여부 조회
	public boolean chkKanbanCard(int columnId) {
		
		try (Connection conn = DBConnection.getConnection();
				CallableStatement cs = conn.prepareCall("{call KANBAN_COLUMN_CRUD_PACK.CHK_KANBAN_CARD(?, ?)}")) {

			cs.setInt(1, columnId);
			cs.registerOutParameter(2, Types.INTEGER);

			cs.execute();
			
			int cardExists = cs.getInt(2);
			
			// 데이터가 존재하면 true
	        return (cardExists == 1);
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}


	// 칸반 컬럼 삭제
	public void delKanbanColumn(int columnId) {
		
		try (Connection conn = DBConnection.getConnection();
				CallableStatement cs = conn.prepareCall("{call KANBAN_COLUMN_CRUD_PACK.DEL_KANBAN_COL(?)}")) {

			cs.setInt(1, columnId);
			
			cs.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
