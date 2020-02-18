package com.igeek.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.igeek.domain.Product;
import com.igeek.service.ProductService;
import com.igeek.service.impl.ProductServiceImpl;

/**
 * Servlet implementation class ProductInfoServlet
 */
@WebServlet("/productInfo")
public class ProductInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pid = request.getParameter("pid");
		ProductService service=new ProductServiceImpl();
		Product product = service.findById(pid);
		String cid = request.getParameter("cid");
		String currentPage = request.getParameter("currentPage");
		request.setAttribute("product", product);
		request.setAttribute("cid", cid);
		request.setAttribute("currentPage", currentPage);

		String pids=pid;
		Cookie[] cookies=request.getCookies();
		if (cookies!=null) {
			for (Cookie cookie : cookies) {
				if ("pids".equals(cookie.getName())) {
					pids=cookie.getValue();
					String []strs=pids.split("#");
					List<String> arrList=Arrays.asList(strs);
					LinkedList<String> list=new LinkedList<>(arrList);
					if (list.contains(pid)) {
					list.remove(pid);	
					}
					list.addFirst(pid);
					StringBuffer sb=new StringBuffer();
					for (int i = 0; i < list.size()&&i<7; i++) {
						sb.append(list.get(i));
						sb.append("#");
					}
					sb.substring(0,sb.length()-1);
					pids = sb.toString();
				}
			}
		}
		System.out.println(pids);
		Cookie c=new Cookie("pids", pids);
		response.addCookie(c);
		
		request.getRequestDispatcher("/product_info.jsp").forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
