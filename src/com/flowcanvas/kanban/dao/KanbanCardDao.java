package com.flowcanvas.kanban.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.flowcanvas.common.db.DBConnection;
import com.flowcanvas.kanban.model.dto.KanbanCardDto;
import com.flowcanvas.kanban.model.form.KanbanCardForm;

import oracle.jdbc.internal.OracleTypes;

public class KanbanCardDao {
	
	private ResultSet rs;
	
	
	// 칸반 카드 조회
	public KanbanCardDto selKanbanCard(int kanbanCardId) {

		try (Connection conn = DBConnection.getConnection();
				CallableStatement cs = conn.prepareCall("{call KANBAN_CARD_CRUD_PACK.SEL_KANBAN_CARD(?, ?)}")) {

			cs.setInt(1, kanbanCardId);
			cs.registerOutParameter(2, OracleTypes.CURSOR);

			cs.execute();

			rs = (ResultSet) cs.getObject(2);
			
			if(rs.next()) {
				return KanbanCardDto.builder()
						.kanbanCardId(rs.getInt("kanban_card_id"))
						.kanbanCardName(rs.getString("kanban_card_name"))
						.kanbanColumnId(rs.getInt("kanban_column_id"))
						.kanbanColumnName(rs.getString("kanban_column_name"))
						.userId(rs.getInt("user_id"))
						.nickName(rs.getString("nick_name"))
						.cardSeq(rs.getInt("card_seq"))
						.priority(rs.getInt("priority"))
						.taskSize(rs.getInt("task_size"))
						.content(rs.getString("content"))
						.build();
			}
			rs.close();
			return null;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	// 칸반 카드 저장 및 수정
	public void mergeKanbanCard(KanbanCardForm kcf) {

		try (Connection conn = DBConnection.getConnection();
				CallableStatement cs = conn.prepareCall("{call KANBAN_CARD_CRUD_PACK.INS_UPD_KANBAN_CARD(?, ?, ?, ?, ?, ?, ?, ?)}")) {
	
			cs.setInt(1, kcf.getKanbanCardId());
			cs.setInt(2, kcf.getKanbanColumnId());
			cs.setInt(3, kcf.getUserId());
			cs.setString(4, kcf.getKanbanCardName());
			cs.setInt(5, kcf.getCardSeq());
			cs.setInt(6, kcf.getPriority());
			cs.setInt(7, kcf.getTaskSize());
			cs.setString(8, kcf.getContent());

			cs.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
