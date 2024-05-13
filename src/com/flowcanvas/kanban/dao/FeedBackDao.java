package com.flowcanvas.kanban.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.flowcanvas.common.db.DBConnection;
import com.flowcanvas.kanban.model.dto.FeedBackDto;
import com.flowcanvas.kanban.model.form.FeedBackForm;

import oracle.jdbc.internal.OracleTypes;

public class FeedBackDao {

	private ResultSet rs;
	
	
	// 피드백 조회
	public List<FeedBackDto> selFeedBack(int kanbanCardId) {
		
		List<FeedBackDto> feedBackDtoList = new ArrayList<>();
		
		try (Connection conn = DBConnection.getConnection();
				CallableStatement cs = conn.prepareCall("{call FEEDBACK_CRUD_PACK.SEL_FEEDBACK(?, ?)}")) {

			cs.setInt(1, kanbanCardId);
			cs.registerOutParameter(2, OracleTypes.CURSOR);

			cs.execute();

			rs = (ResultSet) cs.getObject(2);

			while (rs.next()) {
				FeedBackDto feedBackDto = FeedBackDto.builder()
						.feedBackId(rs.getInt("feedback_id"))
						.KanbanCardId(rs.getInt("kanban_card_id"))
						.userId(rs.getInt("user_id"))
						.nickName(rs.getString("nick_name"))
						.content(rs.getString("content"))
						.updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
						.build();

				feedBackDtoList.add(feedBackDto);
			}
			
			rs.close();
			
			return feedBackDtoList;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	// 피드백 저장/수정
	public void mergerFeedBack(FeedBackForm feedBackForm, int feedBackId, int kanbanCardId, int userId) {
		
		try (Connection conn = DBConnection.getConnection();
				CallableStatement cs = conn.prepareCall("{call FEEDBACK_CRUD_PACK.INS_UPD_FEEDBACK(?, ?, ?, ?)}")) {
	
			cs.setInt(1, (feedBackId > 0 ? feedBackId : 0));
			cs.setInt(2, kanbanCardId);
			cs.setInt(3, userId);
			cs.setString(4, feedBackForm.getContent());

			cs.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	// 피드백 삭제
	public void delFeedBack(int feedBackId, int kanbanCardId) {
		try (Connection conn = DBConnection.getConnection();
				CallableStatement cs = conn.prepareCall("{call FEEDBACK_CRUD_PACK.DEL_FEEDBACK(?, ?)}")) {
	
			cs.setInt(1, feedBackId);
			cs.setInt(2, kanbanCardId);

			cs.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
