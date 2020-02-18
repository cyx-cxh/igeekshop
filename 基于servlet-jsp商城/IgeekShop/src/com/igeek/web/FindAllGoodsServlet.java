package com.igeek.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.igeek.domain.Page;
import com.igeek.domain.PageBean;
import com.igeek.domain.Product;
import com.igeek.domain.User;

import com.igeek.service.ProductService;
import com.igeek.service.impl.ProductServiceImpl;

/**
 * Servlet implementation class FindAllGoodsServlet
 */
@WebServlet("/FindAllGoodsServlet")
public class FindAllGoodsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		//1.获取参数
				String currentPage = request.getParameter("currentPage");
				String rows = request.getParameter("rows");
				
				if(currentPage == null || "".equals(currentPage)) {
					currentPage = "1";
				}
				
				if(rows == null || "".equals(rows)) {
					rows = "15";
				}
				//获取条件查询参数
				Map<String, String[]> condition = request.getParameterMap();
				//2.调用service
				ProductService service = new ProductServiceImpl();
				PageBean<Product> pd= service.findUserByPage(currentPage,rows,condition);
				System.out.println(pd.getPages());
				//3.存储
				request.setAttribute("pd", pd);
				
				request.setAttribute("condition", condition);
				//4.转发
				Cookie [] cookies=request.getCookies();
				List<Product> historyList=new ArrayList<Product>();
				if (cookies!=null) {
					for (Cookie c : cookies) {
						if ("pids".equals(c.getName())) {
							String pids=c.getValue();
							String[] pids_arr = pids.split("#");
							for (String pid : pids_arr) {
								System.out.println(pid);
								Product product = service.findById(pid);
								historyList.add(product);
							}
						}
					}
				}
				request.setAttribute("historyList", historyList);
				request.getRequestDispatcher("/product_list.jsp").forward(request, response);
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
