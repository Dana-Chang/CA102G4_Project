package com.qa_reply.model;

import java.util.List;

import com.qa_reply.model.Qa_replyVO;

public interface Qa_replyDAO_interface {
	
	public int insert(Qa_replyVO Qa_replyVO);
    public int update(Qa_replyVO Qa_replyVO);
    public int delete(String  reply_id);
    public Qa_replyVO findByPrimaryKey(String reply_id);
    public List<Qa_replyVO> getAll();
    public List<Qa_replyVO> findByPrimaryKey1(String question_id);
    public List<Qa_replyVO> find_by_State();
    
    public int updateR(Qa_replyVO Qa_replyVO);
}
