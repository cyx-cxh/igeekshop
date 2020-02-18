package com.igeek.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

import com.google.gson.Gson;
import com.igeek.domain.Cart;
import com.igeek.domain.CartItem;
import com.igeek.domain.Category;
import com.igeek.domain.Order;
import com.igeek.domain.OrderItem;
import com.igeek.domain.PageBean;
import com.igeek.domain.Product;
import com.igeek.domain.User;
import com.igeek.service.CategoryService;
import com.igeek.service.ProductService;
import com.igeek.service.impl.CategoryServiceImpl;
import com.igeek.service.impl.ProductServiceImpl;
import com.igeek.utils.CommonUtils;
import com.igeek.utils.JedisPoolUtils;

import redis.clients.jedis.Jedis;

/**
 * Servlet implementation class ProductServlet
 */
@WebServlet("/product")
public class ProductServlet extends BaseServelt {
	private static final long serialVersionUID = 1L;
 
	public void IndexServlet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		ProductService service=new ProductServiceImpl();
		CategoryService categoryService=new CategoryServiceImpl();
		try {
			List<Product> hotProductList = service.findHotProducts();
			List<Product> NewProductList = service.findNewProducts();
			List<Category> categoryList =categoryService.findCategoryList();
			request.setAttribute("hotProductList", hotProductList);
			request.setAttribute("NewProductList", NewProductList);
			request.setAttribute("categoryList", categoryList);
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public void productInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
	
	public void productList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
	
	       

		public void productinfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

	
	public void addCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ProductService service=new ProductServiceImpl();
		HttpSession session = request.getSession();
		String pid = request.getParameter("pid");
		Product product=service.findById(pid);
		String num = request.getParameter("buyNum");
		int buyNum=1;
		if (num!=null) {
			buyNum=Integer.parseInt(num);
			if (buyNum<=0) {
				request.setAttribute("error", "你输入的数值"+buyNum+"非法，请核对后再提交");
				request.getRequestDispatcher("error.jsp").forward(request, response);
				return;
			}
			
		}
		double subTotal=buyNum*product.getShop_price();
//		CartItem cartItem=new CartItem(product,buyNum,subTotal);
		double newsubTotal=subTotal;
		Cart cart =(Cart)session.getAttribute("cart");
		if (cart==null) {
			cart=new Cart();
		}
		Map<String, CartItem> cartItems=cart.getCartItems();
		if (cartItems.containsKey(pid)) {
			int oldBuyNum=cartItems.get(pid).getBuyNum();
			buyNum+=oldBuyNum;
			newsubTotal=buyNum*product.getShop_price();
		}
		CartItem cartItem=new CartItem(product,buyNum,newsubTotal);
		cart.getCartItems().put(pid,cartItem);
		cart.setTotal(cart.getTotal()+subTotal);
		
		session.setAttribute("cart", cart);
		
		response.sendRedirect(request.getContextPath()+"/cart.jsp");
		
	}


public void delFromCart(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
	HttpSession session = request.getSession();
	String pid = request.getParameter("pid");
	Cart cart = (Cart)session.getAttribute("cart");
	if (cart!=null) {
		Map<String, CartItem>list=cart.getCartItems();
		cart.setTotal(cart.getTotal()-list.get(pid).getSubTotal());
		list.remove(pid);
	}
	session.setAttribute("cart", cart);
	
	response.sendRedirect(request.getContextPath()+"/cart.jsp");
	}	
	
	
	public void clearCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.removeAttribute("cart");
		response.sendRedirect(request.getContextPath()+"/cart.jsp");
	}
	
	public void submitOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user=(User)session.getAttribute("loginUser");
		System.out.println(user);
		if (user==null) {
			response.sendRedirect(request.getContextPath()+"/login.jsp");
			return;
		}
		Order order=new Order();
		String oid=CommonUtils.getUUID();
		order.setOid(oid);
		order.setOrdertime(new Date());
		Cart cart =(Cart) session.getAttribute("cart");
		order.setTotal(cart.getTotal());
		order.setUser(user);
		Map<String, CartItem>list=cart.getCartItems();
		for ( Entry<String, CartItem>entry : list.entrySet()) {
			CartItem cartItem = entry.getValue();
			OrderItem orderItem = new OrderItem();
			orderItem.setItemid(CommonUtils.getUUID());
			orderItem.setCount(cartItem.getBuyNum());
			orderItem.setSubtotal(cartItem.getSubTotal());
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setOrder(order);
			order.getOrderItems().add(orderItem);
		}
		ProductService service=new ProductServiceImpl();
		service.submitOrders(order);
		session.setAttribute("order", order);
		response.sendRedirect(request.getContextPath()+"/order_info.jsp");
	}
	
	public void confirmOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		ProductService service=new ProductServiceImpl();
		HttpSession session = request.getSession();
		Order order = (Order)session.getAttribute("order");
		try {
			String attribute =(String) request.getParameter("address");
					System.out.println(attribute);
			System.out.println();
			BeanUtils.populate(order, request.getParameterMap());
			System.out.println(order);
			service.updateOrderInfo(order);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void categoryList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Jedis jedis=JedisPoolUtils.getJedis();
		String categoryListJson=jedis.get("categoryListJson");
		if (categoryListJson==null) {
			System.out.println("缓存没有，从数据库中获取");
		}
		CategoryService service=new CategoryServiceImpl();
		List<Category> categoryList=service.findCategoryList();
	
		Gson gson=new Gson();
		categoryListJson=gson.toJson(categoryList);
		jedis.set("categoryListJson", categoryListJson);
		
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().write(categoryListJson);
		
	}
	
	
	
}
