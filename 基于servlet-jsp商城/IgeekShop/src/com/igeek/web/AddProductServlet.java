package com.igeek.web;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import com.igeek.domain.Product;
import com.igeek.service.ProductService;
import com.igeek.service.impl.ProductServiceImpl;


@WebServlet("/addproduct")
public class AddProductServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	request.setCharacterEncoding("utf-8");
	Product product=new Product();

	String pname = request.getParameter("pname");
	String market_price=request.getParameter("market_price");
	String shop_price = request.getParameter("shop_price");
	String pimage = request.getParameter("pimage");
	String is_hot = request.getParameter("is_hot");
	String pdesc = request.getParameter("pdesc");
	String cid = request.getParameter("cid");
	product.setPid(UUID.randomUUID().toString().replace("-", ""));
	product.setPname(pname);
	product.setMarket_price(Double.valueOf(market_price));
	product.setShop_price(Double.valueOf(shop_price));
	product.setPimage(pimage);
	product.setIs_hot(Integer.valueOf(is_hot));
	product.setPdesc(pdesc);
	product.setCid(cid);
	ProductService productService=new ProductServiceImpl();
	productService.add(product);
	response.sendRedirect(request.getContextPath()+"/adminFindAllGoods");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
