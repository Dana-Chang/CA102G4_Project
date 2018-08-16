package com.blog.controller;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import com.blog.model.*;
import com.blog_collect.model.*;
import com.blog_message.model.*;
import com.blog_message_report.model.*;
import com.blog_report.model.*;
import com.blog_tag.model.*;
import com.blog_tag_name.model.*;
import com.grp.model.*;
import com.mem.model.*;
import com.question.model.*;
import com.attractions.model.*;

@MultipartConfig(maxFileSize = 50 * 1024 * 1024, maxRequestSize = 50 * 50 * 1024 * 1024)

public class BlogServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action"); // 判斷做什麼動作
		String orderby = req.getParameter("orderby"); // 判斷排列方式
		String item = req.getParameter("item"); // 搜尋選單 標題/內容 or 標籤
		String keyword = req.getParameter("keyword"); // 使用者輸入的值
		// if(orderby==null)
		// orderby="recent";

		/*************************** 旅遊記文章依照發文日期由新到舊排列 ********************************/

		if ("recent".equals(orderby)) { // 選擇依照日期由新至舊排列
			List<String> errorMsgs = new LinkedList<String>(); // 判斷關鍵字是不是搜尋標題/內容
			req.setAttribute("errorMsgs", errorMsgs);

			blogService blogSvc = new blogService();
			blogTagNameService blogTagNameSvc = new blogTagNameService();
			blogTagService blogTagSvc = new blogTagService();
			try {
				if ("keyword".equals(action) && "content".equals(item)) { // 如果是送keyword值代表按搜尋按鈕

					/*************************** 2.開始查詢資料 *****************************************/

					List<blogVO> list = blogSvc.getAllByKeywordOrderByDate(keyword.trim()); // 把使用者輸入的keyword放到參數中取得list
					if (list.isEmpty()) { // 如果list是空的代表沒有資料
						errorMsgs.add("查無資料");
					}

					if (!errorMsgs.isEmpty()) { // 如果錯誤List不是空的就return，把錯誤資訊轉交到blog.jsp
						RequestDispatcher failureView = req.getRequestDispatcher("/blog");
						failureView.forward(req, res);
						return;
					}

					/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/
					req.setAttribute("item", item);
					req.setAttribute("action", action);
					req.setAttribute("orderby", orderby);
					req.setAttribute("keyword", keyword);
					req.setAttribute("list", list); // 將list存到req中
					String url = "/blog";
					RequestDispatcher successView = req.getRequestDispatcher(url);
					successView.forward(req, res); // 把結果forward到blog.jsp

				} else if ("keyword".equals(action) && "tag".equals(item)) { // 判斷關鍵字是不是搜尋tag
					List<String> tagErrorMsgs = new LinkedList<String>();
					req.setAttribute("tagErrorMsgs", tagErrorMsgs);

					/*************************** 2.開始查詢資料 *****************************************/

					Set<blog_tagVO> blogTagList = new LinkedHashSet<>();

					List<blog_tag_nameVO> tagNameList = blogTagNameSvc.getAllBytagClass(keyword.trim()); // 把使用者輸入的keyword放到參數中取得list
					if (tagNameList.isEmpty()) { // 如果list是空的代表沒有資料
						errorMsgs.add("沒有此標籤唷!");
					}

					if (!errorMsgs.isEmpty()) { // 如果錯誤List不是空的就return，把錯誤資訊轉交到blog.jsp
						RequestDispatcher failureView = req.getRequestDispatcher("/blog");
						failureView.forward(req, res);
						return;
					}

					List<blog_tagVO> tagList = blogTagSvc.getAll();
					if (tagList.isEmpty()) { // 如果list是空的代表沒有資料
						errorMsgs.add("查無文章");
					}

					if (!errorMsgs.isEmpty()) { // 如果錯誤List不是空的就return，把錯誤資訊轉交到blog.jsp
						RequestDispatcher failureView = req.getRequestDispatcher("/blog");
						failureView.forward(req, res);
						return;
					}

					for (int i = 0; i < tagList.size(); i++) {
						for (int j = 0; j < tagNameList.size(); j++) {
							if ((tagList.get(i).getBtn_id().equals(tagNameList.get(j).getBtn_id()))
									&& (blogSvc.findByPrimaryKey(tagList.get(i).getBlog_id()) != null)) {
								blogTagList
										.add(new blog_tagVO(tagList.get(i).getBlog_id(), tagList.get(i).getBtn_id()));
							}
						}
					}

					/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/

					req.setAttribute("item", item);
					req.setAttribute("action", action);
					req.setAttribute("orderby", orderby);
					req.setAttribute("keyword", keyword);
					req.setAttribute("blogTagList", blogTagList); // 將list存到req中
					String url = "/blog";
					RequestDispatcher successView = req.getRequestDispatcher(url);
					successView.forward(req, res); // 把結果forward到blog.jsp

				} else {
					List<blogVO> list = blogSvc.getAllByNew();

					if (list.isEmpty()) { // 如果list是空的代表沒有資料
						errorMsgs.add("查無資料");
					}

					if (!errorMsgs.isEmpty()) { // 如果錯誤List不是空的就return，把錯誤資訊轉交到blog.jsp
						RequestDispatcher failureView = req.getRequestDispatcher("/blog");
						failureView.forward(req, res);
						return;
					}

					/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/
					req.setAttribute("item", item);
					req.setAttribute("action", action);
					req.setAttribute("orderby", orderby);
					req.setAttribute("keyword", keyword);
					req.setAttribute("list", list); // 將list存到req中
					String url = "/blog";
					RequestDispatcher successView = req.getRequestDispatcher(url);
					successView.forward(req, res); // 把結果forward到blog.jsp
				}

			} catch (Exception e) {
				errorMsgs.add("無法取得資料" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/blog");
				failureView.forward(req, res);
			}
		}

		/*************************** 旅遊記文章依照瀏覽數由多至少排列 ********************************/
		if ("popular".equals(orderby)) {
			List<String> errorMsgs = new LinkedList<String>(); // 判斷關鍵字是不是搜尋標題/內容
			req.setAttribute("errorMsgs", errorMsgs);
			blogService blogSvc = new blogService();
			blogTagNameService blogTagNameSvc = new blogTagNameService();
			blogTagService blogTagSvc = new blogTagService();

			/*************************** 2.開始查詢資料 *****************************************/
			try {
				if ("keyword".equals(action) && "content".equals(item)) { // 如果是送keyword值代表按搜尋按鈕

					/*************************** 2.開始查詢資料 *****************************************/

					List<blogVO> list = blogSvc.getAllByKeywordOrderByViews(keyword.trim()); // 把使用者輸入的keyword放到參數中取得list
					if (list.isEmpty()) { // 如果list是空的代表沒有資料
						errorMsgs.add("查無資料");
					}

					if (!errorMsgs.isEmpty()) { // 如果錯誤List不是空的就return，把錯誤資訊轉交到blog.jsp
						RequestDispatcher failureView = req.getRequestDispatcher("/blog");
						failureView.forward(req, res);
						return;
					}

					/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/
					req.setAttribute("item", item);
					req.setAttribute("action", action);
					req.setAttribute("orderby", orderby);
					req.setAttribute("keyword", keyword);
					req.setAttribute("list", list); // 將list存到req中
					String url = "/blog";
					RequestDispatcher successView = req.getRequestDispatcher(url);
					successView.forward(req, res); // 把結果forward到blog.jsp
				} else if ("keyword".equals(action) && "tag".equals(item)) { // 判斷關鍵字是不是搜尋tag
					List<String> tagErrorMsgs = new LinkedList<String>();
					req.setAttribute("tagErrorMsgs", tagErrorMsgs);

					/*************************** 2.開始查詢資料 *****************************************/

					Set<blog_tagVO> blogTagListHot = new LinkedHashSet<>();

					List<blog_tag_nameVO> tagNameList = blogTagNameSvc.getAllBytagClass(keyword.trim()); // 把使用者輸入的keyword放到參數中取得list
					if (tagNameList.isEmpty()) { // 如果list是空的代表沒有資料
						errorMsgs.add("沒有此標籤唷!");
					}

					if (!errorMsgs.isEmpty()) { // 如果錯誤List不是空的就return，把錯誤資訊轉交到blog.jsp
						RequestDispatcher failureView = req.getRequestDispatcher("/blog");
						failureView.forward(req, res);
						return;
					}

					List<blog_tagVO> tagList = blogTagSvc.getAll();
					if (tagNameList.isEmpty()) { // 如果list是空的代表沒有資料
						errorMsgs.add("查無文章");
					}

					if (!errorMsgs.isEmpty()) { // 如果錯誤List不是空的就return，把錯誤資訊轉交到blog.jsp
						RequestDispatcher failureView = req.getRequestDispatcher("/blog");
						failureView.forward(req, res);
						return;
					}

					for (int i = 0; i < tagList.size(); i++) {
						for (int j = 0; j < tagNameList.size(); j++) {
							if ((tagList.get(i).getBtn_id().equals(tagNameList.get(j).getBtn_id()))
									&& (blogSvc.findByPrimaryKey(tagList.get(i).getBlog_id()) != null)) {
								blogTagListHot
										.add(new blog_tagVO(tagList.get(i).getBlog_id(), tagList.get(i).getBtn_id()));
							}
						}
					}

					/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/

					req.setAttribute("item", item);
					req.setAttribute("action", action);
					req.setAttribute("orderby", orderby);
					req.setAttribute("keyword", keyword);
					req.setAttribute("blogTagListHot", blogTagListHot); // 將list存到req中
					String url = "/blog";
					RequestDispatcher successView = req.getRequestDispatcher(url);
					successView.forward(req, res); // 把結果forward到blog.jsp

				} else {
					List<blogVO> list = blogSvc.getAllByHot();

					if (list.isEmpty()) { // 如果list是空的代表沒有資料
						errorMsgs.add("查無資料");
					}

					if (!errorMsgs.isEmpty()) { // 如果錯誤List不是空的就return，把錯誤資訊轉交到blog.jsp
						RequestDispatcher failureView = req.getRequestDispatcher("/blog");
						failureView.forward(req, res);
						return;
					}

					/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/
					req.setAttribute("item", item);
					req.setAttribute("action", action);
					req.setAttribute("orderby", orderby);
					req.setAttribute("keyword", keyword);
					req.setAttribute("list", list); // 將list存到req中
					String url = "/blog";
					RequestDispatcher successView = req.getRequestDispatcher(url);
					successView.forward(req, res); // 把結果forward到blog.jsp
				}
			} catch (Exception e) {
				errorMsgs.add("無法取得資料" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/blog");
				failureView.forward(req, res);
			}
		}

		/*************************** 新增旅遊記 **********************************/
		if ("insert".equals(action)) { // 如果是送insert值代表按新增按鈕
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			try {
				String blog_title = req.getParameter("title"); // 標題
				String blog_content = req.getParameter("editor1"); // 內容
				java.sql.Date travel_date = null; // 行程日期
				String trip_no = "T000000001"; // 行程編號
				String mem_id = ((MemberVO) req.getSession().getAttribute("memberVO")).getMem_Id(); // 會員編號
				Integer blog_views = 0; // 瀏覽次數預設給0
				String btn_id = req.getParameter("tag"); // 取得選擇的tagID
				String[] btn_id_spilt = btn_id.split(",");
				
				long token = Long.parseLong(req.getParameter("token")); // input送的token
				long tokenSession = Long.parseLong(req.getSession().getAttribute("token") + ""); // session存的token

				if (token == tokenSession) {
					// 如果是第一次送表單，產生新的token
					req.getSession().setAttribute("token", System.currentTimeMillis());
				} else {
					res.sendRedirect(req.getContextPath() + "/blog.index");
					return;
				}

				if ((btn_id_spilt != null) && !(btn_id_spilt[0].isEmpty())) {
					
					List blogTagList = new ArrayList();
					
					for(int i = 0 ;i<btn_id_spilt.length;i++) {
						blogTagList.add(btn_id_spilt[i]);
					}
					req.setAttribute("blogTagList", blogTagList);
				}
				
				// 封面照片
				byte[] blog_coverimage = getPictureByteArray(req.getPart("travel_coverimage"));

				if (blog_title == null || (blog_title.trim()).length() == 0) {
					errorMsgs.add("請輸入標題，請勿空白!!");
				}

				if (blog_content == null || (blog_content.trim()).length() == 0) {
					errorMsgs.add("請輸入內容，請勿空白!!");
				}

				try {
					travel_date = java.sql.Date.valueOf(req.getParameter("travel_date").trim()); // 行程日期
				} catch (IllegalArgumentException e) {
					travel_date = new java.sql.Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000L);
					errorMsgs.add("請選擇日期!");
				}

				blogVO blogVO = new blogVO();
				blogVO.setBlog_title(blog_title);
				blogVO.setBlog_content(blog_content);
				blogVO.setTravel_date(travel_date);
				blogVO.setTrip_no(trip_no);
				blogVO.setMem_id(mem_id);
				blogVO.setBlog_views(blog_views);
				blogVO.setBlog_coverimage(blog_coverimage);
				// 如果錯誤List不是空的就return，把錯誤資訊轉交到blog_recent.jsp
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("blogVO", blogVO);
					RequestDispatcher failureView = req.getRequestDispatcher("/front_end/blog/blog_add.jsp");
					failureView.forward(req, res);
					return;
				}

				/*************************** 2.開始新增資料 ***************************************/

				blogService blogSvc = new blogService();
				String blog_id = blogSvc.addBlog(trip_no, mem_id, blog_title, blog_content, travel_date, blog_views,
						blog_coverimage);

				/**************************** 2-1.繼續新增標籤資料 ********************************/

				if ((btn_id_spilt != null) && !(btn_id_spilt[0].isEmpty())) {
					blogTagService blogTagNameSvc = new blogTagService();
					for (int i = 0; i < btn_id_spilt.length; i++) {
						blogTagNameSvc.addBlogTag(blog_id, btn_id_spilt[i]);
					}
				}

				/*************************** 3.新增完成,準備轉交(Send the Success view) ***********/

				String url = "/blog.index";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);

				/*************************** 其他可能的錯誤處理 **********************************/

			} catch (Exception e) {
				errorMsgs.add(e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/blog/blog_add.jsp");
				failureView.forward(req, res);
			}
		}

		/*************************** 修改文章的編輯頁面 **********************************/
		if ("update".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			String mem_id = null;

			try {

				/*************************** 1.接收請求參數 - 找不到文章的錯誤處理 **********************/

				MemberVO memVO = (MemberVO) req.getSession().getAttribute("memberVO");
				String blog_id = req.getParameter("blog_id");
				mem_id = req.getParameter("mem_id");

				if (!memVO.getMem_Id().equals(mem_id)) {
					RequestDispatcher failureView = req.getRequestDispatcher("/front_end/blog/personal_area_blog.jsp");
					failureView.forward(req, res);
					return;
				}

				if (blog_id == null || (blog_id.trim()).length() == 0) {
					errorMsgs.add("找不到該文章");
				}

				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/blog.do?action=myBlog");
					failureView.forward(req, res);
					return;
				}

				/*************************** 2.開始查詢資料 *****************************************/

				blogService blogSvc = new blogService();
				blogVO blogVO = blogSvc.findByPrimaryKey(blog_id);
				if (blogVO == null) {
					errorMsgs.add("查無資料");
				}

				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req.getRequestDispatcher("/blog.do?action=myBlog");
					failureView.forward(req, res);
					return;
				}

				/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/

				req.setAttribute("blogVO", blogVO);
				String url = "/front_end/blog/blog_update.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);

				/*************************** 其他可能的錯誤處理 *************************************/

			} catch (Exception e) {
				errorMsgs.add("無法取得資料:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/blog.do?action=myBlog");
				failureView.forward(req, res);
				return;
			}
		}

		/*************************** 修改文章 **********************************/
		if ("updating".equals(action)) {

			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			try {
				MemberVO memVO = (MemberVO) req.getSession().getAttribute("memberVO");
				String blog_PicReg = "^(jpeg|jpg|bmp|png|gif|ico)$"; // 上傳檔案副檔名判斷
				String blog_id = req.getParameter("blog_id"); // blog_id
				String blog_title = req.getParameter("title"); // 標題
				String blog_content = req.getParameter("editor1"); // 內容
				java.sql.Date travel_date = null; // 行程日期
				String trip_no = "T000000001"; // 行程編號
				byte[] blog_coverimage = null; // 封面照片
				String btn_id = req.getParameter("blogTag"); // 取得選擇的tagID
				String[] btn_id_spilt = btn_id.split(",");
				String mem_id = memVO.getMem_Id();

				try {
					// 行程日期
					travel_date = java.sql.Date.valueOf(req.getParameter("travel_date").trim());
				} catch (IllegalArgumentException e) {
					travel_date = new java.sql.Date(System.currentTimeMillis());
					errorMsgs.add("請選擇日期!");
				}

				if (blog_title == null || (blog_title.trim()).length() == 0) {
					errorMsgs.add("請輸入標題，請勿空白!!");
				}

				if (blog_content == null || (blog_content.trim()).length() == 0) {
					errorMsgs.add("請輸入內容，請勿空白!!");
				}
				
				Part part = req.getPart("travel_coverimage");

				if (getFileNameFromPart(part) == null) {
					blogService blogSvc = new blogService();
					blogVO blogVO = blogSvc.findByPrimaryKey(blog_id);
					blog_coverimage = blogVO.getBlog_coverimage();
				} else if (!getFileNameFromPart(part).matches(blog_PicReg)) {
					errorMsgs.add("請選擇圖片檔");
				} else {
					blog_coverimage = getPictureByteArray(part);
				}

				blogVO blogVO = new blogVO();
				blogVO.setBlog_title(blog_title);
				blogVO.setBlog_content(blog_content);
				blogVO.setTravel_date(travel_date);
				blogVO.setTrip_no(trip_no);
				blogVO.setBlog_coverimage(blog_coverimage);
				blogVO.setBlog_id(blog_id);

				// 如果錯誤List不是空的就return，把錯誤資訊轉交到blog_update.jsp
				if (!errorMsgs.isEmpty()) {
					req.setAttribute("blogVO", blogVO);
					RequestDispatcher failureView = req.getRequestDispatcher("/front_end/blog/blog_update.jsp");
					failureView.forward(req, res);
					return;
				}

				/*************************** 2.開始修改資料 *****************************************/

				blogService blogSvc = new blogService();
				blogVO = blogSvc.updateBlog(trip_no, blog_title, blog_content, travel_date, blog_coverimage, blog_id);

				/**************************** 2-1.繼續新增標籤資料 ***********************************/

				blogTagService blogTagNameSvc = new blogTagService();
				blogTagNameSvc.deleteBlogTagAll(blog_id); // 先刪除所有標籤如果陣列是空的或null下面就不幫他新增了

				if ((btn_id_spilt != null) && !(btn_id_spilt[0].isEmpty())) {
					blogTagService blogTagNameSvc2 = new blogTagService();

					for (int i = 0; i < btn_id_spilt.length; i++) {
						if ((btn_id_spilt != null) && !(btn_id_spilt[i].isEmpty())) {
							blogTagNameSvc2.addBlogTag(blog_id, btn_id_spilt[i]);
						}
					}
				}

				/*************************** 3.修改完成,準備轉交(Send the Success view) *************/

				// req.setAttribute("blogVO", blogVO);
				String url = "/blog.do?action=myBlog&mem_id=" + mem_id;
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);

				/*************************** 其他可能的錯誤處理 *************************************/

			} catch (Exception e) {
				errorMsgs.add("修改資料失敗:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/blog/blog_update.jsp");
				failureView.forward(req, res);
			}
		}

		/*************************** 修改文章狀態-隱藏 **********************************/
		if ("hide".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			String mem_id = null;

			try {

				/*************************** 1.接收請求參數 ***************************************/

				MemberVO memVO = (MemberVO) req.getSession().getAttribute("memberVO");

				String blog_id = req.getParameter("blog_id");
				String whichPage = req.getParameter("whichPage");
				mem_id = memVO.getMem_Id();

				/*************************** 2.開始隱藏資料 ***************************************/
				// 先刪除有收藏此篇文章的資料
				blogCollectService blogCollectSvc = new blogCollectService();
				blogCollectSvc.deleteAll(blog_id);
				// 再隱藏文章
				blogService blogSvc = new blogService();
				blogSvc.hideBlog(1, blog_id);

				/*************************** 3.隱藏完成,準備轉交(Send the Success view) ***********/

				String url = "/blog.do?action=myBlog&mem_id=" + memVO.getMem_Id();
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);

				/*************************** 其他可能的錯誤處理 **********************************/

			} catch (Exception e) {
				errorMsgs.add("刪除資料失敗:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/blog.do?action=myBlog");
				failureView.forward(req, res);
			}
		}

		/*************************** 旅遊記文章依照發文日期由新到舊排列 ********************************/
		if ("article".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			try {

				/*************************** 1.接收請求參數 ***************************************/

				String blog_id = req.getParameter("blogID");

				/*************************** 2.開始增加瀏覽次數 ***************************************/

				blogService blogSvc = new blogService();
				blogSvc.updateBlogViews(blog_id);

				/*************************** 3.更新完成，準備轉交(Send the Success view) ***********/
				req.setAttribute("blog_id", blog_id);

				String url = "/front_end/blog/blog_article.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);

				/*************************** 其他可能的錯誤處理 **********************************/

			} catch (Exception e) {
				errorMsgs.add("找不到該篇文章:" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/blog.index");
				failureView.forward(req, res);
			}
		}

		/*************************** 收藏旅遊記文章 ********************************/
		if ("collect".equals(action)) {
			res.setContentType("text/html;charset=UTF-8");
			PrintWriter out = res.getWriter();
			try {

				/*************************** 1.接收請求參數 ***************************************/
				MemberVO memVO = (MemberVO) req.getSession().getAttribute("memberVO");
				String blog_id = req.getParameter("blog_id");
				String mem_id = req.getParameter("mem_id");
				String blog_id_Owner = req.getParameter("blog_id_Owner");

				/*************************** 2.開始增加收藏 ***************************************/

				if (memVO.getMem_Id().equals(blog_id_Owner)) {
					out.print("請不要收藏自己的文章!!");
					return;
				}

				blogCollectService blogSvc = new blogCollectService();
				int cnt = blogSvc.findByPrimaryKey(mem_id, blog_id);

				if (cnt == 0) {
					blogSvc.addBlogCollect(mem_id, blog_id);
					out.print("收藏成功");
				} else {
					blogSvc.deleteBlogCollect(mem_id, blog_id);
					out.print("取消收藏成功");
				}

				/*************************** 3.收藏完成，準備轉交(Send the Success view) ***********/

				// 此為AJAX寫法，不用轉交

				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				out.print("收藏失敗");
			}
		}

		/*************************** 新增留言 ********************************/

		if ("message".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			String blog_id = null;
			try {

				/*************************** 1.接收請求參數 ***************************************/

				blog_id = req.getParameter("blog_id");
				String mem_id = req.getParameter("mem_id");
				String blog_message = req.getParameter("blog_message");
				String scroll = req.getParameter("scroll");

				long token = Long.parseLong(req.getParameter("token")); // input送的token
				long tokenSession = Long.parseLong(req.getSession().getAttribute("token") + ""); // session存的token

				if (token == tokenSession) {
					// 如果是第一次送表單，產生新的token
					req.getSession().setAttribute("token", System.currentTimeMillis());
				} else {
					res.sendRedirect(req.getContextPath() + "/blog.do?action=article&blogID=" + blog_id);
					return;
				}

				/*************************** 2.開始新增留言 ***************************************/

				blogMessageService blogMessageSvc = new blogMessageService();
				blogMessageSvc.addBlogMessage(blog_id, mem_id, blog_message);

				/*************************** 3.新增留言完成，準備轉交(Send the Success view) ***********/

				String url = "/front_end/blog/blog_article.jsp?action=article&blogID=" + blog_id;
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);

				/*************************** 其他可能的錯誤處理 **********************************/

			} catch (NullPointerException e) {

			} catch (Exception e) {
				errorMsgs.add("留言失敗:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front_end/blog/blog_article.jsp?action=article&blogID=" + blog_id);
				failureView.forward(req, res);
			}
		}

		/*************************** 新增旅遊記檢舉 ********************************/

		if ("reportBlog".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			String blog_id = null;
			/*************************** 2.接收請求參數 ***************************************/

			try {

				MemberVO memVO = (MemberVO) req.getSession().getAttribute("memberVO");
				blog_id = req.getParameter("blog_id");
				String mem_id = req.getParameter("mem_id");
				String br_reason = req.getParameter("br_reason");
				String blog_id_Owner = req.getParameter("blog_id_Owner");

				long token = Long.parseLong(req.getParameter("token")); // input送的token
				long tokenSession = Long.parseLong(req.getSession().getAttribute("token") + ""); // session存的token

				if (token == tokenSession) {
					// 如果是第一次送表單，產生新的token
					req.getSession().setAttribute("token", System.currentTimeMillis());
				} else {
					res.sendRedirect(req.getContextPath() + "/blog.do?action=article&blogID=" + blog_id);
					return;
				}

				/*************************** 2.開始新增檢舉資料 ***************************************/

				if (memVO.getMem_Id().equals(blog_id_Owner)) {
					errorMsgs.add("請不要檢舉自己的文章!");
					errorMsgs.add("你是不是在找麻煩!");
				}

				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/front_end/blog/blog_article.jsp?action=article&blogID=" + blog_id);
					failureView.forward(req, res);
					return;
				}

				blogReportService blogReportSvc = new blogReportService();
				blog_reportVO blog_reportVO = blogReportSvc.getOne(blog_id, mem_id);

				if (blog_reportVO != null) {
					errorMsgs.add("您已檢舉過此篇文章!");
				} else {
					blogReportSvc.addBlogReport(blog_id, mem_id, br_reason);
					errorMsgs.add("提交檢舉成功!");
				}

				// 直接轉交 errorMsgs 告知使用者提交成功或失敗
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/front_end/blog/blog_article.jsp?action=article&blogID=" + blog_id);
					failureView.forward(req, res);
					return;
				}

				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				errorMsgs.add("檢舉失敗:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front_end/blog/blog_article.jsp?action=article&blogID=" + blog_id);
				failureView.forward(req, res);
			}
		}

		/*************************** 新增旅遊記留言檢舉 ********************************/

		if ("reportMessage".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			String blog_id = null;
			/*************************** 2.接收請求參數 ***************************************/

			try {

				blog_id = req.getParameter("blog_id");
				String message_id = req.getParameter("message_id");
				String mem_id = req.getParameter("mem_id");
				String bmr_reason = req.getParameter("bmr_reason");

				long token = Long.parseLong(req.getParameter("token")); // input送的token
				long tokenSession = Long.parseLong(req.getSession().getAttribute("token") + ""); // session存的token

				if (token == tokenSession) {
					// 如果是第一次送表單，產生新的token
					req.getSession().setAttribute("token", System.currentTimeMillis());
				} else {
					res.sendRedirect(req.getContextPath() + "/blog.do?action=article&blogID=" + blog_id);
					return;
				}

				/*************************** 2.開始新增檢舉資料 ***************************************/

				blogMessageReportService blogMessageReportSvc = new blogMessageReportService();
				blog_message_reportVO blog_message_reportVO = blogMessageReportSvc.getOne(mem_id, message_id);

				if (blog_message_reportVO != null) {
					errorMsgs.add("您已檢舉過此則留言!");
				} else {
					blogMessageReportSvc.addBlogMessageReport(mem_id, message_id, bmr_reason);
					errorMsgs.add("提交檢舉成功!");
				}

				// 直接轉交 errorMsgs 告知使用者提交成功或失敗
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/front_end/blog/blog_article.jsp?action=article&blogID=" + blog_id);
					failureView.forward(req, res);
					return;
				}

				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				errorMsgs.add("檢舉失敗:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front_end/blog/blog_article.jsp?action=article&blogID=" + blog_id);
				failureView.forward(req, res);
			}
		}

		/*************************** 修改留言狀態-隱藏 **********************************/
		if ("deleteMessage".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			String blog_id = null;

			try {

				/*************************** 1.接收請求參數 ***************************************/

				blog_id = req.getParameter("blog_id");
				String message_id = req.getParameter("message_id");
				String mem_id = req.getParameter("mem_id");
				String scroll = req.getParameter("scroll");

				/*************************** 2.開始隱藏資料 ***************************************/

				blogMessageService blogMessageSvc = new blogMessageService();
				blogMessageSvc.updateStatus(message_id, mem_id, 1);

				/*************************** 3.隱藏完成,準備轉交(Send the Success view) ***********/

				String url = "/front_end/blog/blog_article.jsp?action=article&blogID=" + blog_id;
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);

				/*************************** 其他可能的錯誤處理 **********************************/

			} catch (Exception e) {
				errorMsgs.add("刪除資料失敗:" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front_end/blog/blog_article.jsp?action=article&blogID=" + blog_id);
				failureView.forward(req, res);
			}
		}

		/*************************** 會員管理自己的旅遊記文章 ********************************/

		if ("myBlog".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			/*************************** 1.接收請求參數 ***************************************/

			try {
				MemberVO memVO = (MemberVO) req.getSession().getAttribute("memberVO");

				String mem_id = req.getParameter("mem_id");
				String whichPage = req.getParameter("whichPage");

				if (!memVO.getMem_Id().equals(mem_id)) {
					RequestDispatcher failureView = req.getRequestDispatcher("/front_end/blog/personal_area_blog.jsp");
					failureView.forward(req, res);
					return;
				}

				/*************************** 2.開始查詢資料 ***************************************/

				blogService blogSvc = new blogService();
				List<blogVO> list = blogSvc.findByMemId(mem_id);

				if (list.isEmpty()) { // 如果list是空的代表沒有資料
					errorMsgs.add("查無資料");
				}

				if (!errorMsgs.isEmpty()) { // 如果錯誤List不是空的就return，把錯誤資訊轉交到blog.jsp
					RequestDispatcher failureView = req.getRequestDispatcher("/front_end/blog/personal_area_blog.jsp");
					failureView.forward(req, res);
					return;
				}

				/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/

				req.setAttribute("action", "myBlog");
				req.setAttribute("list", list); // 將list存到req中
				String url = "/front_end/blog/personal_area_blog.jsp?whichPage=" + whichPage + "&mem_id=" + mem_id;
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res); // 把結果forward到blog.jsp

				/*************************** 其他可能的錯誤處理 **********************************/

			} catch (Exception e) {
				errorMsgs.add("無法取得資料" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/blog/personal_area_blog.jsp");
				failureView.forward(req, res);
			}
		}

		/*************************** 會員查看管理收藏旅遊記文章頁面 ********************************/

		if ("myCollect".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			/*************************** 1.接收請求參數 ***************************************/
			try {

				MemberVO memVO = (MemberVO) req.getSession().getAttribute("memberVO");
				String mem_id = req.getParameter("mem_id");
				String whichPage = req.getParameter("whichPage");

				if (!memVO.getMem_Id().equals(mem_id)) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/front_end/blog/personal_area_collect_blog.jsp");
					failureView.forward(req, res);
					return;
				}

				/*************************** 2.開始查詢資料 ***************************************/

				blogCollectService blogCollectSvc = new blogCollectService();
				List<blog_collectVO> list = blogCollectSvc.getAllByMem_id(mem_id);

				if (list.isEmpty()) { // 如果list是空的代表沒有資料
					errorMsgs.add("查無資料");
				}

				if (!errorMsgs.isEmpty()) { // 如果錯誤List不是空的就return，把錯誤資訊轉交到blog.jsp
					RequestDispatcher failureView = req
							.getRequestDispatcher("/front_end/blog/personal_area_collect_blog.jsp");
					failureView.forward(req, res);
					return;
				}

				/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/

				req.setAttribute("action", "myCollect");
				req.setAttribute("list", list); // 將list存到req中
				String url = "/front_end/blog/personal_area_collect_blog.jsp?whichPage=" + whichPage;
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res); // 把結果forward到blog.jsp

				/*************************** 其他可能的錯誤處理 **********************************/

			} catch (Exception e) {
				errorMsgs.add("無法取得資料" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/front_end/blog/personal_area_collect_blog.jsp");
				failureView.forward(req, res);
			}
		}

		/*************************** 會員刪除收藏的旅遊記文章 ********************************/

		if ("delCollect".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			String mem_id = null;
			/*************************** 1.接收請求參數 ***************************************/

			try {

				MemberVO memVO = (MemberVO) req.getSession().getAttribute("memberVO");
				mem_id = memVO.getMem_Id();
				String blog_id = req.getParameter("blog_id");

				/*************************** 2.開始刪除資料 ***************************************/

				blogCollectService blogCollectSvc = new blogCollectService();
				blogCollectSvc.deleteBlogCollect(mem_id, blog_id);

				/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/

				req.setAttribute("action", "myCollect");
				String url = "/blog.do?action=myCollect&mem_id=" + mem_id;
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res); // 把結果forward到blog.jsp

				/*************************** 其他可能的錯誤處理 **********************************/

			} catch (Exception e) {
				errorMsgs.add("無法取得資料" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/blog.do?action=myCollect&mem_id=" + mem_id);
				failureView.forward(req, res);
			}
		}

		/*************************** 管理員修改tag標籤 ********************************/

		if ("updateTag".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			String whichPage = null;
			/*************************** 1.接收請求參數 ***************************************/

			try {
				String btn_id = req.getParameter("blogTagNameID");
				String btn_class = req.getParameter("blogTagNameClass");
				String btn_name = req.getParameter("blogTagName");
				whichPage = req.getParameter("whichPage");

				/*************************** 2.開始修改資料 ***************************************/

				blogTagNameService blogTagNameSvc = new blogTagNameService();
				blogTagNameSvc.updateBlogTagName(btn_class, btn_name, btn_id);

				/*************************** 3.修改完成,準備轉交(Send the Success view) *************/

				String url = "/back_end/blog/blog_tag.jsp?whichPage=" + whichPage;
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);

				/*************************** 其他可能的錯誤處理 **********************************/

			} catch (Exception e) {
				errorMsgs.add("無法修改資料" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back_end/blog/blog_tag.jsp?whichPage=" + whichPage);
				failureView.forward(req, res);
			}
		}

		/*************************** 管理員刪除tag標籤 ********************************/

		if ("delTagName".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			String whichPage = null;

			/*************************** 1.接收請求參數 ***************************************/
			try {
				String btn_id = req.getParameter("btn_id");
				whichPage = req.getParameter("whichPage");

				/***************************
				 * 2.先刪除跟旅遊記有關聯的標籤
				 ***************************************/

				blogTagService blogTagSvc = new blogTagService();
				blogTagSvc.deleteAllByBtnID(btn_id);

				/*************************** 2.再刪除標籤 ***************************************/

				blogTagNameService blogTagNameSvc = new blogTagNameService();
				blogTagNameSvc.deleteBlogTagName(btn_id);

				/*************************** 3.刪除完成,準備轉交(Send the Success view) *************/

				String url = "/back_end/blog/blog_tag.jsp?whichPage=" + whichPage;
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);

				/*************************** 其他可能的錯誤處理 **********************************/

			} catch (Exception e) {
				errorMsgs.add("無法刪除資料" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back_end/blog/blog_tag.jsp?whichPage=" + whichPage);
				failureView.forward(req, res);
			}
		}

		/*************************** 管理員新增tag標籤 ********************************/

		if ("insertTag".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			String whichPage = null;

			/*************************** 1.接收請求參數 ***************************************/

			try {
				String btn_class = req.getParameter("blogTagNameClass");
				String btn_name = req.getParameter("blogTagName");
				whichPage = req.getParameter("whichPage");

				long token = Long.parseLong(req.getParameter("token")); // input送的token
				long tokenSession = Long.parseLong(req.getSession().getAttribute("token") + ""); // session存的token

				if (token == tokenSession) {
					// 如果是第一次送表單，產生新的token
					req.getSession().setAttribute("token", System.currentTimeMillis());
				} else {
					res.sendRedirect(req.getContextPath() + "/back_end/blog/blog_tag.jsp?whichPage=" + whichPage);
					return;
				}

				/*************************** 2.開始新增資料 ***************************************/

				blogTagNameService blogTagNameSvc = new blogTagNameService();
				blogTagNameSvc.addblogTagName(btn_class, btn_name);

				/*************************** 3.新增完成,準備轉交(Send the Success view) *************/

				String url = "/back_end/blog/blog_tag.jsp?whichPage=" + whichPage;
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);

				/*************************** 其他可能的錯誤處理 **********************************/

			} catch (Exception e) {
				errorMsgs.add("無法新增資料" + e.getMessage());
				RequestDispatcher failureView = req
						.getRequestDispatcher("/back_end/blog/blog_tag.jsp?whichPage=" + whichPage);
				failureView.forward(req, res);
			}
		}

		/*************************** 傳回全部旅遊記檢舉清單 ********************************/

		if ("blogReportManage".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			String whichPage = null;

			/*************************** 1.接收請求參數 ***************************************/

			try {
				whichPage = req.getParameter("whichPage");

				/*************************** 2.開始查詢資料 ***************************************/

				blogReportService blogReportSvc = new blogReportService();
				List<blog_reportVO> blogReportList = blogReportSvc.getAll();

				/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/

				req.setAttribute("blogReportList", blogReportList);
				String url = "/back_end/blog/blog_report.jsp?action=" + action + "&whichPage=" + whichPage;
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);

				/*************************** 其他可能的錯誤處理 **********************************/

			} catch (Exception e) {
				errorMsgs.add("查無資料" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher(
						"/back_end/blog/blog_report.jsp?action=" + action + "&whichPage=" + whichPage);
				failureView.forward(req, res);
			}
		}

		/*************************** 傳回全部旅遊記檢舉清單 ********************************/

		if ("blogMessageReportManage".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			String whichPage = null;

			/*************************** 1.接收請求參數 ***************************************/

			try {
				whichPage = req.getParameter("whichPage");

				/*************************** 2.開始查詢資料 ***************************************/

				blogMessageReportService blogMessageReportSvc = new blogMessageReportService();
				List<blog_message_reportVO> blogMessageReportList = blogMessageReportSvc.getAll();

				/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/

				req.setAttribute("blogMessageReportList", blogMessageReportList);
				String url = "/back_end/blog/blog_report.jsp?action=" + action + "&whichPage=" + whichPage;
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);

				/*************************** 其他可能的錯誤處理 **********************************/

			} catch (Exception e) {
				errorMsgs.add("查無資料" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher(
						"/back_end/blog/blog_report.jsp?action=" + action + "&whichPage=" + whichPage);
				failureView.forward(req, res);
			}
		}

		/*************************** 修改旅遊記檢舉狀態 ********************************/

		if ("updateBlogReportStatus".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			String whichPage = null;

			/*************************** 1.接收請求參數 ***************************************/

			try {
				int br_status = Integer.parseInt(req.getParameter("br_status"));
				String blog_id = req.getParameter("blog_id");
				String mem_id = req.getParameter("mem_id");

				/*************************** 2.開始修改資料 ***************************************/

				if (br_status == 1) {
					blogService blogSvc = new blogService();
					blogSvc.hideBlog(br_status, blog_id);

					blogReportService blogReportSvc = new blogReportService();
					blogReportSvc.updateBlogReport(br_status, blog_id, mem_id);

				} else if (br_status == 2) {
					blogService blogSvc = new blogService();
					blogSvc.hideBlog(0, blog_id);

					blogReportService blogReportSvc = new blogReportService();
					blogReportSvc.updateBlogReport(br_status, blog_id, mem_id);

				}

				/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/

				String url = "/blog.do?action=blogReportManage";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);

				/*************************** 其他可能的錯誤處理 **********************************/

			} catch (Exception e) {
				errorMsgs.add("送出審核失敗" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/blog.do?action=blogReportManage");
				failureView.forward(req, res);
			}
		}

		/*************************** 修改旅遊記留言檢舉狀態 ********************************/

		if ("updateBlogMessageReportStatus".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
			String whichPage = null;

			/*************************** 1.接收請求參數 ***************************************/

			try {
				int bmr_status = Integer.parseInt(req.getParameter("bmr_status"));
				String message_id = req.getParameter("message_id");
				String mem_id = req.getParameter("mem_id");

				/*************************** 2.開始修改資料 ***************************************/

				if (bmr_status == 1) {
					blogMessageService blogMessageSvc = new blogMessageService();
					blogMessageSvc.updateStatusForBackend(message_id, bmr_status);

					blogMessageReportService blogMessageReportSvc = new blogMessageReportService();
					blogMessageReportSvc.updateBlogMessageReport(bmr_status, mem_id, message_id);

				} else if (bmr_status == 2) {
					blogMessageService blogMessageSvc = new blogMessageService();
					blogMessageSvc.updateStatusForBackend(message_id, 0);

					blogMessageReportService blogMessageReportSvc = new blogMessageReportService();
					blogMessageReportSvc.updateBlogMessageReport(bmr_status, mem_id, message_id);

				}

				/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/

				String url = "/blog.do?action=blogMessageReportManage";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res);

				/*************************** 其他可能的錯誤處理 **********************************/

			} catch (Exception e) {
				errorMsgs.add("送出審核失敗" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/blog.do?action=blogMessageReportManage");
				failureView.forward(req, res);
			}
		}

		/*************************** 全站搜尋-全部 ********************************/

		if ("searchAll".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			try {

				/*************************** 1.接收請求參數 ***************************************/

				/*************************** 2.開始查詢資料 ***************************************/

				blogService blogSvc = new blogService();
				List<blogVO> blogList = blogSvc.getAllByKeywordOrderByViews(keyword);

				String[] temp = new String[1];
				temp[0] = keyword;
				Map map = new LinkedHashMap();
				map.put("att_name", temp);

				AttractionsService attractionsSvc = new AttractionsService();
				List<AttractionsVO> AttractionsList = attractionsSvc.getAll(map);

				/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/

				req.setAttribute("AttractionsList", AttractionsList);
				req.setAttribute("blogList", blogList);
				String url = "/front_end/search/search_index.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res); // 把結果forward到search_index.jsp

				/*************************** 其他可能的錯誤處理 **********************************/

			} catch (Exception e) {
				errorMsgs.add("無法取得資料" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/search/search_index.jsp");
				failureView.forward(req, res);
			}
		}

		if ("searchBlog".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			try {

				/*************************** 1.接收請求參數 ***************************************/

				/*************************** 2.開始查詢資料 ***************************************/

				blogService blogSvc = new blogService();
				List<blogVO> blogList = blogSvc.getAllByKeywordOrderByViews(keyword);

				/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/

				req.setAttribute("blogList", blogList);
				String url = "/front_end/search/search_blog.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res); // 把結果forward到search_index.jsp

				/*************************** 其他可能的錯誤處理 **********************************/

			} catch (Exception e) {
				errorMsgs.add("無法取得資料" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/search/search_blog.jsp");
				failureView.forward(req, res);
			}
		}

		if ("searchTour".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			try {

				/*************************** 1.接收請求參數 ***************************************/

				/*************************** 2.開始查詢資料 ***************************************/

				String[] temp = new String[1];
				temp[0] = keyword;
				Map map = new LinkedHashMap();
				map.put("att_name", temp);
				map.put("country", temp);
				map.put("att_address", temp);
				map.put("administrative_area", temp);

				AttractionsService attractionsSvc = new AttractionsService();
				List<AttractionsVO> AttractionsList = attractionsSvc.getAll(map);

				/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/

				req.setAttribute("AttractionsList", AttractionsList);
				String url = "/front_end/search/search_tour.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res); // 把結果forward到search_index.jsp

				/*************************** 其他可能的錯誤處理 **********************************/

			} catch (Exception e) {
				errorMsgs.add("無法取得資料" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/search/search_tour.jsp");
				failureView.forward(req, res);
			}
		}

		if ("searchMember".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			try {

				/*************************** 1.接收請求參數 ***************************************/

				/*************************** 2.開始查詢資料 ***************************************/

				MemberService memSvc = new MemberService();
				List<MemberVO> memList = memSvc.getAll_member(keyword);

				/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/

				req.setAttribute("memList", memList);
				String url = "/front_end/search/search_member.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res); // 把結果forward到search_index.jsp

				/*************************** 其他可能的錯誤處理 **********************************/

			} catch (Exception e) {
				errorMsgs.add("無法取得資料" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/search/search_member.jsp");
				failureView.forward(req, res);
			}
		}

		if ("searchTogether".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			try {

				/*************************** 1.接收請求參數 ***************************************/

				/*************************** 2.開始查詢資料 ***************************************/

				String[] temp = new String[1];
				temp[0] = keyword;
				Map map = new LinkedHashMap();
				map.put("TRIP_LOCALE", temp);
				map.put("TRIP_DETAILS", temp);

				GrpService grpSvc = new GrpService();
				List<AttractionsVO> grpList = grpSvc.getAll(map);

				/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/

				req.setAttribute("grpList", grpList);
				String url = "/front_end/search/search_together.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res); // 把結果forward到search_index.jsp

				/*************************** 其他可能的錯誤處理 **********************************/

			} catch (Exception e) {
				errorMsgs.add("無法取得資料" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/search/search_together.jsp");
				failureView.forward(req, res);
			}
		}

		if ("searchAsk".equals(action)) {
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			try {

				/*************************** 1.接收請求參數 ***************************************/

				/*************************** 2.開始查詢資料 ***************************************/

				QuestionService QuestionSvc = new QuestionService();
				List<QuestionVO> QuestionList = QuestionSvc.findByKeyword(keyword);

				/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/

				req.setAttribute("QuestionList", QuestionList);
				String url = "/front_end/search/search_ask.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);
				successView.forward(req, res); // 把結果forward到search_index.jsp

				/*************************** 其他可能的錯誤處理 **********************************/

			} catch (Exception e) {
				errorMsgs.add("無法取得資料" + e.getMessage());
				RequestDispatcher failureView = req.getRequestDispatcher("/front_end/search/search_ask.jsp");
				failureView.forward(req, res);
			}
		}

	}

	public String getFileNameFromPart(Part part) {
		String header = part.getHeader("content-disposition");
		// System.out.println("header=" + header); // 測試用
		String filename = new File(header.substring(header.lastIndexOf("=") + 2, header.length() - 1)).getName();
		// System.out.println("filename=" + filename); // 測試用
		// 取出副檔名
		String fnameExt = filename.substring(filename.lastIndexOf(".") + 1, filename.length()).toLowerCase();
		// System.out.println("fnameExt=" + fnameExt); // 測試用
		if (filename.length() == 0) {
			return null;
		}
		return fnameExt;
	}

	// import blob image to database
	public static byte[] getPictureByteArray(Part part) throws IOException {
		InputStream in = part.getInputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[8192];
		int i;
		while ((i = in.read(buffer)) != -1) {
			baos.write(buffer, 0, i);
		}
		baos.close();
		in.close();

		return baos.toByteArray();
	}
}
