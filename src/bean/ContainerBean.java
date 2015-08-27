package bean;

import java.io.Serializable;
import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Container;
import basic.Global;
import basic.HibernateBase;
import basic.MyDockerClient;
import basic.MyHttpClientGET;
import basic.ParseToReponse;

@Path("/container")
public class ContainerBean extends HibernateBase {
	public ContainerBean() throws HibernateException {
		super();
	}

	@POST
	public Response createContainer(
			@QueryParam("containerName") String containerName,
			@QueryParam("userId") String userId, @QueryParam("cmd") String cmd,
			@QueryParam("imageName") String imageName) throws HibernateException {
		DockerClient dockerClient = MyDockerClient.getDockerClient();
		try {
			String[] cmdarr =cmd.split(" ");
			CreateContainerResponse ccResponse = dockerClient
					.createContainerCmd(imageName).withName(containerName)
					.withTty(true).withStdinOpen(true).withCmd(cmdarr).exec();
			System.out.println("have created" + ccResponse.toString());
			List<Container> containers = dockerClient.listContainersCmd()
					.withShowAll(true).exec();
			String fullNameString = null;
			for (int i = 0; i < containers.size(); i++) {
				if (containers.get(i).getId().equals(ccResponse.getId())) {
					fullNameString = containers.get(i).getNames()[0];
					System.out.println("find it" + fullNameString);
				}
			}
			String hostName = fullNameString.split("/")[1];
			// Map<String, String> mes = new HashMap<String, String>();
			// mes.put("id", ccResponse.getId());
			// mes.put("assignHost", hostName);
			model.Container con = new model.Container(containerName,
					Integer.valueOf(userId), imageName, ccResponse.getId(),
					hostName);
			beginTransaction();
			Serializable s = session.save(con);

			endTransaction(true);
			System.out.println(s);

			return ParseToReponse.parse("1", "createContainer successfully",
					"id:" + s, 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ParseToReponse.parse("1", e.getMessage(), "id:", 0);
		}

		// String queryString = "from Image where name=?";
		// beginTransaction();
		// Query query = session.createQuery(queryString);
		// query.setParameter(0, imageFullNname);
		// List<Image> image1 = query.list();
		// if(image1.size()>0){
		// beginTransaction();
		// Image Image2 = (Image) session.load(Image.class,
		// image1.get(0).getId());
		// session.delete(Image2);
		// endTransaction(true);
		// }

		// Image image = new Image(imageFullNname, Integer.valueOf(isPublic),
		// Integer.valueOf(userId), imageLongId);
		// beginTransaction();
		// Serializable s = session.save(image);
		// endTransaction(true);
		// System.out.println(s);

	}

	@GET
	public Response getAllContainers() throws HibernateException {
		try {
			String queryString = "from Container";
			beginTransaction();
			Query query = session.createQuery(queryString);
			List<model.Container> containers = query.list();
			return ParseToReponse.parse("1", "all containers data", containers,
					containers.size());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ParseToReponse.parse("2", e.getMessage(), null, 0);
		}

	}

	@GET
	@Path("id/{id}")
	public Response getContainerByid(@PathParam("id") int id)
			throws HibernateException {
		try {
			String queryString = "from Container where id=?";
			beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter(0, id);
			List<model.Container> containers = query.list();
			return ParseToReponse.parse("1", "single container", containers,
					containers.size());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ParseToReponse.parse("2", e.getMessage(), null, 0);
		}

	}

	@GET
	@Path("info/id/{id}")
	public Response getContainerInfoByid(@PathParam("id") int id)
			throws HibernateException {
		try {
			System.out.println("hello");
			String queryString = "from Container where id=?";
			beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter(0, id);
			List<model.Container> containers = query.list();
			String Containerid = containers.get(0).getContainerId();
			String testUrlString = "http://" + Global.swarm_ip + ":"
					+ Global.swarm_port + "/containers/" + Containerid
					+ "/json";
			System.out.println("url" + testUrlString);
			String resString = MyHttpClientGET.exeGet(testUrlString);
			ObjectMapper mapper = new ObjectMapper();
			return ParseToReponse.parse("1", "single container", mapper.readTree(resString), 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ParseToReponse.parse("2", e.getMessage(), null, 0);
		}

	}
	
	@POST
	@Path("start/id/{id}")
	public Response startContainerInfoByid(@PathParam("id") int id)
			throws HibernateException {
		try {
			System.out.println("hello");
			String queryString = "from Container where id=?";
			beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter(0, id);
			List<model.Container> containers = query.list();
			String Containerid = containers.get(0).getContainerId();
			//Containerid="7fe7f250c7f9/stop"
			String testUrlString = "http://" + Global.swarm_ip + ":"
					+ Global.swarm_port + "/containers/" + Containerid
					+ "/start";
			System.out.println("url" + testUrlString);
			HttpClient httpClient = new HttpClient();
			PostMethod postMethod = new PostMethod(testUrlString);
			httpClient.executeMethod(postMethod);
			int StatusCode= postMethod.getStatusCode();
			String mes="";
			System.out.println("get status code");
			if(StatusCode==204){
				mes="start container successfully";
			}
			if(StatusCode==304){
				mes="container already started";
			}
			if(StatusCode==404){
				mes="no such container";
			}
			if(StatusCode==500){
				mes="server error";
			}
			//ObjectMapper mapper = new ObjectMapper();
			return ParseToReponse.parse("1", mes, StatusCode, 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ParseToReponse.parse("2", e.getMessage(), null, 0);
		}

	}
	
	
	@POST
	@Path("stop/id/{id}")
	public Response stopContainerInfoByid(@PathParam("id") int id)
			throws HibernateException {
		try {
			System.out.println("hello");
			String queryString = "from Container where id=?";
			beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter(0, id);
			List<model.Container> containers = query.list();
			String Containerid = containers.get(0).getContainerId();
			//Containerid="7fe7f250c7f9/stop"
			String testUrlString = "http://" + Global.swarm_ip + ":"
					+ Global.swarm_port + "/containers/" +Containerid
					+ "/stop?t=5";
			System.out.println("url" + testUrlString);
			HttpClient httpClient = new HttpClient();
			PostMethod postMethod = new PostMethod(testUrlString);
			httpClient.executeMethod(postMethod);
			int StatusCode= postMethod.getStatusCode();
			String mes="";
			System.out.println("get status code");
			if(StatusCode==204){
				mes="stop container successfully";
			}
			if(StatusCode==304){
				mes="container already stopped";
			}
			if(StatusCode==404){
				mes="no such container";
			}
			if(StatusCode==500){
				mes="server error";
			}
			//ObjectMapper mapper = new ObjectMapper();
			return ParseToReponse.parse("1", mes, StatusCode, 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ParseToReponse.parse("2", e.getMessage(), null, 0);
		}

	}
	

	//
	@GET
	@Path("userId/{id}")
	public Response getContainerByUserId(@PathParam("id") int id)
			throws HibernateException {
		try {
			String queryString = "from Container where ownerId=?";
			beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter(0, id);
			List<model.Container> containers = query.list();
			return ParseToReponse.parse("1", "user all containers", containers,
					containers.size());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ParseToReponse.parse("2", e.getMessage(), null, 0);
		}

	}

	@GET
	@Path("userId/{id}/containerName/{name}")
	public Response getUserByImageName(@PathParam("id") int id,
			@PathParam("name") String name) throws HibernateException {
		try {
			String queryString = "from Container where owner_id=? and name=?";
			beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter(0, id);
			query.setParameter(1, name);
			List<model.Container> containers = query.list();
			return ParseToReponse.parse("1", "single containers data",
					containers, containers.size());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ParseToReponse.parse("2", e.getMessage(), null, 0);
		}

	}
	
	@GET
	@Path("log/id/{id}")
	public Response getlogByImageName(@PathParam("id") int id,
			@PathParam("name") String name) throws HibernateException {
		try {
			System.out.println("hello");
			String queryString = "from Container where id=?";
			beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter(0, id);
			List<model.Container> containers = query.list();
			String Containerid = containers.get(0).getContainerId();
			//Containerid="7fe7f250c7f9/stop"
			String testUrlString = "http://" + Global.swarm_ip + ":"
					+ Global.swarm_port + "/containers/"+Containerid+"/logs?stderr=1&stdout=1&timestamps=1";
			System.out.println("url" + testUrlString);
			HttpClient httpClient = new HttpClient();
			GetMethod postMethod = new GetMethod(testUrlString);
			httpClient.executeMethod(postMethod);
			String text = postMethod.getResponseBodyAsString();
			int StatusCode= postMethod.getStatusCode();
			String mes="";
			System.out.println("get status code");
			if(StatusCode==101){
				mes="no error, hints proxy about hijacking";
			}
			if(StatusCode==200){
				mes="no error, no upgrade header found";
			}
			if(StatusCode==404){
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				mes="no such container";
			}
			if(StatusCode==500){
				mes="server error";
			}
			ObjectMapper mapper = new ObjectMapper();
			return ParseToReponse.parse("1", mes, text , 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ParseToReponse.parse("2", e.getMessage(), null, 0);
		}

	}
	
//	/containers/4fa6e0f0c678/logs?stderr=1&stdout=1&timestamps=1&follow=1&tail=10
	// @GET
	// @Path("search")
	// public String searchImage(@QueryParam("imageName") String name)
	// throws HibernateException {
	// try {
	// DockerClient dockerClient = MyDockerClient.getDockerClient();
	// List<SearchItem> items= dockerClient.searchImagesCmd(name).exec();
	// return ParseToReponse.parse("1", "all search info", items, items.size());
	// } catch (Exception e) {
	// // TODO: handle exception
	// e.printStackTrace();
	// return ParseToReponse.parse("2", e.getMessage(), null, 0);
	// }
	//
	// }
	//
	//
	@DELETE
	@Path("{id}")
	public Response deleteUser(@PathParam("id") int id) {
		try {
			String queryString = "from Container where id=?";
			beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter(0, id);
			List<model.Container> containers = query.list();
			if(containers.size()<1){
				return ParseToReponse.parse("3", "no such container",
						null, 0);
			}
			String nameString = containers.get(0).getHostName() + "/"
					+ containers.get(0).getName();
			DockerClient dockerClient = MyDockerClient.getDockerClient();
			// dockerClient.stopContainerCmd(nameString).exec();
			// dockerClient.killContainerCmd(nameString).exec();
			dockerClient.removeContainerCmd(nameString).exec();

			beginTransaction();
			model.Container container = (model.Container) session.load(
					model.Container.class, id);
			session.delete(container);
			endTransaction(true);
			return ParseToReponse.parse("1", "delete container successfully",
					null, 0);
		} catch (Exception e) {
			e.printStackTrace();
			return ParseToReponse.parse("2", e.getMessage(), null, 0);
			// TODO: handle exception
		}
	}
}
