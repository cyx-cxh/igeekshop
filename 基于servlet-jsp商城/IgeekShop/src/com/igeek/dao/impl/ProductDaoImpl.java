/**
 * 
 */
package com.igeek.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.igeek.dao.ProductDao;
import com.igeek.domain.Order;
import com.igeek.domain.OrderItem;
import com.igeek.domain.Product;

import com.igeek.utils.JDBCUtils;
import java.sql.Connection;



/**
 * @author cxh
 *
 */
public class ProductDaoImpl implements ProductDao {
private QueryRunner qr=new QueryRunner(JDBCUtils.getDataSource());
	@Override
	public List<Product> findAll() {
		String sql="select * from product";
		try {
			return qr.query(sql, new BeanListHandler<Product>(Product.class));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("查询所有的商品失败");
		}
		
	}
	@Override
	public void release(Connection conn,String cid) {
		String sql="update product set cid= null where cid=?";
		try {
			qr.update(conn,sql,cid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	@Override
	public Product findId(String pid) {
	String sql="select * from product where pid =?";
	try {
		return qr.query(sql, new BeanHandler<Product>(Product.class),pid);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
		
	}
	@Override
	public void update(Product p) {
		String sql="update product set pname=?,market_price=?,shop_price=?,is_hot=?,pdesc=?,pflag=?,cid=? where pid=?";
		try {
			qr.update(sql,p.getPname(),p.getMarket_price(),p.getShop_price(),p.getIs_hot(),p.getPdesc(),p.getPflag(),p.getCid(),p.getPid());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public int findTotalCount(Map<String, String[]> condition) {
		String sql = "select count(*) from product where 1=1";
		StringBuilder sb = new StringBuilder(sql);

		// 定义参数的集合
		List<Object> params = new ArrayList<Object>();
		// 遍历
		Set<String> keySet = condition.keySet();
		for (String key : keySet) {
			//排除分页条件参数
			if("currentPage".equals(key) || "rows".equals(key)||"method".equals(key)) {
				continue;
			}
			// 获取value
			String value = condition.get(key)[0];
			// and key like value
			if(value != null && !"".equals(value)) {
				sb.append(" and " + key + " like ? ");
				params.add("%"+value+"%");
			}
		}

try {
	int intValue = ((Long)qr.query(sb.toString(), new ScalarHandler(),params.toArray())).intValue();
	return intValue;
} catch (SQLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
	throw new RuntimeException(e);
}
		
	}
	@Override
	public List<Product> findUserByPage(int start, int rows, Map<String, String[]> condition) {
		String sql = "select * from product where 1=1";
		StringBuilder sb = new StringBuilder(sql);
		// 定义参数的集合
		List<Object> params = new ArrayList<Object>();
		// 遍历
		Set<String> keySet = condition.keySet();
		for (String key : keySet) {
			//排除分页条件参数
			if("currentPage".equals(key) || "rows".equals(key)||"method".equals(key)) {
				continue;
			}
			// 获取value
			String value = condition.get(key)[0];
			// and key like value
			if(value != null && !"".equals(value)) {
				sb.append(" and " + key + " like ? ");
				params.add("%"+value+"%");
			}
		}
		//添加分页查询条件
		sb.append(" limit ?,?");
		params.add(start);
		params.add(rows);
		try {
			return qr.query(sb.toString(), new BeanListHandler<Product>(Product.class),params.toArray());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		
	}
	@Override
	public void delete(String pid) {
		String sql="delete from product where pid =?";
		try {
			
			qr.update(JDBCUtils.getConnection(),sql,pid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("删除操作失败");
		}
	}
	@Override
	public void add(Product product) {
		String sql="insert into product values(?,?,?,?,?,?,?,?,?,?)";
		try {
			qr.update(sql,product.getPid(),product.getPname(),product.getMarket_price(),product.getShop_price(),product.getPimage(),product.getPdate(),product.getIs_hot(),product.getPdesc(),product.getPflag(),product.getCid());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Override
	public List<Product> findHotProducts() throws SQLException {
		String sql="select * from product where is_hot=? limit ?,?";
		
		return qr.query(sql, new BeanListHandler<Product>(Product.class),1,0,9);
	}
	@Override
	public List<Product> findNewProducts() throws SQLException {
		
		String sql="select * from product order by pdate desc limit ?,?";
		return qr.query(sql, new BeanListHandler<Product>(Product.class),0,9);
	}
	@Override
	public void addOrders(Order order) {
		String sql="insert into orders values(?,?,?,?,?,?,?,?)";
		try {
			Connection conn = JDBCUtils.getConnection();
			qr.update(conn,sql,order.getOid(),order.getOrdertime(),order.getTotal(),
					order.getState(),order.getAddress(),order.getName(),
					order.getTelephone(),order.getUser().getUid());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	@Override
	public void addOderItems(Order order) {
		try {
			Connection conn = JDBCUtils.getConnection();
			for (OrderItem item : order.getOrderItems()) {
			String sql="insert into orderitem values(?,?,?,?,?)";
			qr.update(conn,sql,item.getItemid(),item.getCount(),item.getSubtotal(),
					item.getProduct().getPid(),order.getOid());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	@Override
	public void updateOrderInfo(Order order) {
	String sql="update orders set address=?,name=?,telephone=? where oid=?";
	try {
		qr.update(sql,order.getAddress(),order.getName(),order.getTelephone(),order.getOid());
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
	}

}
