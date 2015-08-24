package bean;

import java.io.Serializable;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import model.User;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import basic.HibernateBase;
import basic.ParseToReponse;


@Path("/user")
public class UserBean extends HibernateBase {
	public UserBean() throws HibernateException {
		super();
	}

	@POST
	public String addCourse(@FormParam("name") String name,
			@FormParam("password") String password,
			@FormParam("mail") String mail) throws HibernateException {
		try {
			User user = new User(name, password, mail);
			beginTransaction();
			Serializable s= session.save(user);
			endTransaction(true);
			System.out.println(s);
			return ParseToReponse.parse("1", "add user successfully", "id:"+s, 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ParseToReponse.parse("2", e.getMessage(), null, 0);
		}
	}

	@GET
	public String getAllUsers() throws HibernateException {
		try {
			String queryString = "from User";
			beginTransaction();
			Query query = session.createQuery(queryString);
			List<User> users = query.list();
			System.out.println(users.size());
			
			return ParseToReponse.parse("1", "all users data", users, users.size());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
			return ParseToReponse.parse("2", e.getMessage(), null, 0);
		}
		

	}

	@GET
	@Path("id/{id}")
	public String getUserByid(@PathParam("id") int id)
			throws HibernateException {
		try {
			String queryString = "from User where id=?";
			beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter(0, id);
			List<User> users = query.list();
			return ParseToReponse.parse("1", "single user", users, users.size());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ParseToReponse.parse("2", e.getMessage(), null, 0);
		}
		
	}

	@GET
	@Path("name/{name}")
	public String getUserByname(@PathParam("name") String name)
			throws HibernateException {
		try {
			String queryString = "from User where name=?";
			beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter(0, name);
			List<User> users = query.list();
			return ParseToReponse.parse("1", "single user", users, users.size());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ParseToReponse.parse("2", e.getMessage(), null, 0);
		}
		
	}

	@GET
	@Path("mail/{name}")
	public String getUserBymail(@PathParam("name") String name)
			throws HibernateException {
		try {
			String queryString = "from User where mail=?";
			beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter(0, name);
			List<User> users = query.list();
			return ParseToReponse.parse("1", "single user", users, users.size());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ParseToReponse.parse("2", e.getMessage(), null, 0);
		}
		
	}

	@DELETE
	@Path("{id}")
	public String deleteUser(@PathParam("id") int id) {
		try {
			beginTransaction();
			User user = (User) session.load(User.class, id);
			session.delete(user);
			endTransaction(true);
			return ParseToReponse.parse("1", "delete user successfully", null,
					0);
		} catch (Exception e) {
			e.printStackTrace();
			
			return ParseToReponse.parse("2", e.getMessage(), null, 0);
			// TODO: handle exception
		}

	}
}
