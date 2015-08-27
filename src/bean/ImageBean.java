package bean;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import model.Image;
import model.User;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.SearchItem;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import com.github.dockerjava.core.command.PushImageResultCallback;

import basic.Global;
import basic.HibernateBase;
import basic.MyDockerClient;
import basic.ParseToReponse;

@Path("/image")
public class ImageBean extends HibernateBase {
	public ImageBean() throws HibernateException {
		super();
	}

	@POST
	public Response addImage(@QueryParam("dockerFilePath") String dockerFilePath,
			@QueryParam("repertory") String repertory,
			@QueryParam("userId") String userId,
			@QueryParam("imageName") String imageName,
			@QueryParam("imageTag") String imageTag,
			@QueryParam("isPublic") String isPublic) throws HibernateException {

		URL xmlpath = this.getClass().getClassLoader().getResource("/");
		System.out.println(xmlpath.getFile());

		try {
			File file1 = new File(xmlpath.getFile());
			String rootPath = file1.getParentFile().getParent() + "/";
			System.out.println(rootPath + dockerFilePath);
			File file = new File(rootPath + dockerFilePath);
			if (file.exists()) {
				System.out.print("文件存在");
			} else {
				System.out.print("文件不存在");
				return ParseToReponse
						.parse("2", "dockerFile not exist!", "", 0);
			}
			DockerClient dockerClient = MyDockerClient
					.getDockerClient(Global.buildImage_ip);
			String imageFullNname = Global.local_registry_ip + "/" + repertory
					+ "/" + imageName + ":" + imageTag;
			System.out.println("builting image");
			dockerClient.buildImageCmd(new File(rootPath + dockerFilePath))
					.withNoCache().withTag(imageFullNname)
					.exec(new BuildImageResultCallback()).awaitImageId();
			System.out.println("have built");
			System.out.println("pushing image");

			dockerClient
					.pushImageCmd(
							Global.local_registry_ip + "/" + repertory + "/"
									+ imageName).withTag(imageTag)
					.exec(new PushImageResultCallback()).awaitSuccess();
			System.out.println("have pushed");
			List<com.github.dockerjava.api.model.Image> imagelist = dockerClient
					.listImagesCmd().exec();
			String imageLongId = "";
			for (int i = 0; i < imagelist.size(); i++) {
				String imageNameTemp = imagelist.get(i).getRepoTags()[0];
				if (imageNameTemp.equals(imageFullNname)) {
					imageLongId = imagelist.get(i).getId();
				}
				//System.out.println(imageNameTemp);

			}
			
			//查询是否有相同名字得镜像 如果有则删除
			String queryString = "from Image where name=?";
			beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter(0, imageFullNname);
			List<Image> image1 = query.list();
			if(image1.size()>0){
				beginTransaction();
				Image Image2 = (Image) session.load(Image.class, image1.get(0).getId());
				session.delete(Image2);
				endTransaction(true);
			}
			
			
			
			Image image = new Image(imageFullNname, Integer.valueOf(isPublic),
					Integer.valueOf(userId), imageLongId);
			beginTransaction();
			Serializable s = session.save(image);
			endTransaction(true);
			System.out.println(s);
			return ParseToReponse.parse("1", "id:" + s, "id:", 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ParseToReponse.parse("2", e.getMessage(), null, 0);
		}
	}

	@GET
	public Response getAllImages() throws HibernateException {
		try {
			String queryString = "from Image";
			beginTransaction();
			Query query = session.createQuery(queryString);
			List<Image> iamges = query.list();
			return  ParseToReponse.parse("2", "all images", iamges, iamges.size());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ParseToReponse.parse("2", e.getMessage(), null, 0);
		}

	}

	@GET
	@Path("id/{id}")
	public Response getUserByid(@PathParam("id") int id)
			throws HibernateException {
		try {
			String queryString = "from Image where id=?";
			beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter(0, id);
			List<Image> images = query.list();
			return ParseToReponse
					.parse("1", "single image", images, images.size());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ParseToReponse.parse("2", e.getMessage(), null, 0);
		}

	}

	@GET
	@Path("userId/{id}")
	public Response getUserByUserId(@PathParam("id") int id)
			throws HibernateException {
		try {
			String queryString = "from Image where ownerId=?";
			beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter(0, id);
			List<Image> images = query.list();
			return ParseToReponse
					.parse("1", "user all images", images, images.size());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ParseToReponse.parse("2", e.getMessage(), null, 0);
		}

	}

	@GET
	@Path("imageName")
	public Response getUserByImageName(@QueryParam("imageName") String name)
			throws HibernateException {
		try {
			String queryString = "from Image where name=?";
			beginTransaction();
			Query query = session.createQuery(queryString);
			query.setParameter(0, name);
			List<Image> images = query.list();
			return ParseToReponse
					.parse("1", "single image data", images, images.size());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ParseToReponse.parse("2", e.getMessage(), null, 0);
		}

	}
	@GET
	@Path("search")
	public Response searchImage(@QueryParam("imageName") String name)
			throws HibernateException {
		try {
			DockerClient dockerClient = MyDockerClient.getDockerClient();
			List<SearchItem> items= dockerClient.searchImagesCmd(name).exec();
			return ParseToReponse.parse("1", "all search info", items, items.size());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ParseToReponse.parse("2", e.getMessage(), null, 0);
		}

	}
	

	@DELETE
	@Path("{id}")
	public Response deleteUser(@PathParam("id") int id) {
		try {
			beginTransaction();
			Image image = (Image) session.load(Image.class, id);
			session.delete(image);
			endTransaction(true);
			return ParseToReponse.parse("1", "delete image successfully", null,
					0);
		} catch (Exception e) {
			e.printStackTrace();
			return ParseToReponse.parse("2",e.getMessage(), null, 0);
			// TODO: handle exception
		}

	}
}
