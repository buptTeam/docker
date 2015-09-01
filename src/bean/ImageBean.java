package bean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import model.Image;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.wink.common.internal.utils.MediaTypeUtils;
import org.apache.wink.common.model.multipart.InMultiPart;
import org.apache.wink.common.model.multipart.InPart;
import org.hibernate.HibernateException;
import org.hibernate.Query;


import com.fasterxml.jackson.databind.ObjectMapper;
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

	@Path("uploadDockerfile")
	@POST
	@Produces( MediaType.TEXT_PLAIN)
	@Consumes( MediaTypeUtils.MULTIPART_FORM_DATA)
	public Response uploadFiles( @Context HttpServletRequest request,InMultiPart inMP) throws IOException {
		String mesString=null;
		String fileNameString=null;
		  try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		  DiskFileItemFactory factory = new DiskFileItemFactory();
		  ServletFileUpload upload = new ServletFileUpload(factory);
		  try {
		   List items = upload.parseRequest(request);
		   Iterator itr = items.iterator();
		   while (itr.hasNext()) {
		    FileItem item = (FileItem) itr.next();
		    if (item.isFormField()) {
		     System.out.println("��������:" + item.getFieldName() + "��������ֵ:" + item.getString("UTF-8"));
		    } else {
		     if (item.getName() != null && !item.getName().equals("")) {
		      System.out.println("�ϴ��ļ��Ĵ�С:" + item.getSize());
		      System.out.println("�ϴ��ļ�������:" + item.getContentType());
		      // item.getName()�����ϴ��ļ��ڿͻ��˵�����·������
		      System.out.println("�ϴ��ļ�������:" + item.getName());
		      fileNameString=item.getName();
		      File tempFile = new File(item.getName());

		      File file = new File(request.getSession().getServletContext().getRealPath("/files"), item.getName());
		      System.out.println(request.getSession().getServletContext().getRealPath("/files"));

		      item.write(file);
		     // request.setAttribute("upload.message", );
		      mesString="successfully";
		     }else{
		    	 mesString="no file selected";
		    //  request.setAttribute("upload.message", );
		     }
		    }
		   }
		  }catch(FileUploadException e){
		   e.printStackTrace();
		   return ParseToReponse.parse("2",  e.getMessage(),fileNameString ,0);
		  } catch (Exception e) {
		   e.printStackTrace();
		   request.setAttribute("upload.message", "�ϴ��ļ�ʧ�ܣ�");
		   return ParseToReponse.parse("2",  e.getMessage(),null , 0);
		  }
		  
		  return ParseToReponse.parse("1",  mesString,fileNameString , 0);
		
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
			String rootPath = file1.getParentFile().getParent() + "/files/";
			System.out.println(rootPath + dockerFilePath);
			File file = new File(rootPath + dockerFilePath);
			if (file.exists()) {
				System.out.print("�ļ�����");
			} else {
				System.out.print("�ļ�������");
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
			
			//��ѯ�Ƿ�����ͬ���ֵþ��� �������ɾ��
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
