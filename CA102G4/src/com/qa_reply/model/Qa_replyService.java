package com.qa_reply.model;

import java.sql.Date;
import java.util.List;


public class Qa_replyService {
	
	private	Qa_replyDAO_interface dao;

	public	Qa_replyService() {
		dao = new Qa_replyDAO();
	}
	
	public	Qa_replyVO addQa_reply(String question_id, String mem_id, String reply_content, Date reply_date,Integer r_state) {
		
		Qa_replyVO Qa_replyVO = new Qa_replyVO();
		
		Qa_replyVO.setQuestion_id(question_id);
		Qa_replyVO.setMem_id(mem_id);
		Qa_replyVO.setReply_content(reply_content);
		Qa_replyVO.setReply_date(reply_date);
		Qa_replyVO.setR_state(r_state);
		
		dao.insert(Qa_replyVO);
		
		return Qa_replyVO;
	}
	
	public	Qa_replyVO updateQa_reply(String reply_id, String question_id, String mem_id, String reply_content, Date reply_date,Integer r_state) {
		
		Qa_replyVO Qa_replyVO = new Qa_replyVO();
		
		Qa_replyVO.setReply_id(reply_id);
		Qa_replyVO.setQuestion_id(question_id);
		Qa_replyVO.setMem_id(mem_id);
		Qa_replyVO.setReply_content(reply_content);
		Qa_replyVO.setReply_date(reply_date);
		Qa_replyVO.setR_state(r_state);
		
		dao.update(Qa_replyVO);
		
		return Qa_replyVO;
	}
	
	public void deleteQa_reply(String reply_id) {
		dao.delete(reply_id);
	}
	
	public Qa_replyVO getOneQa_reply(String reply_id) {
		return dao.findByPrimaryKey(reply_id);
	}

	public List<Qa_replyVO> getAll() {
		return dao.getAll();
	}
	
	public List<Qa_replyVO> getQa_replyByQuestion_id(String question_id) {
		return dao.findByPrimaryKey1(question_id);
	}
	
	public List<Qa_replyVO> getOneState() {
		return dao.find_by_State();
	}
	
	public	Qa_replyVO updateR(String reply_id,Integer r_state) {
		
		Qa_replyVO Qa_replyVO = new Qa_replyVO();
		
		Qa_replyVO.setReply_id(reply_id);
		Qa_replyVO.setR_state(r_state);
		
		dao.updateR(Qa_replyVO);
		
		return Qa_replyVO;
	}
}	

