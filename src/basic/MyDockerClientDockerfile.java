package basic;

import java.io.File;
import java.io.IOException;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import com.github.dockerjava.core.command.PushImageResultCallback;

public class MyDockerClientDockerfile {

	/**
	 * @param 
	 *        https://github.com/docker-java/docker-java/blob/master/src/test/java
	 *        /com/github/dockerjava/core/command/BuildImageCmdImplTest.java
	 */

	public static void main(String[] args) {

		DockerClient dockerClient = getDockerClient("10.10.10.6");
		// AuthResponse response = dockerClient.authCmd().exec();
		// System.out.println(response.getStatus());

		String directory = "files";
		System.out.println("building {}" + directory);
		 File file = new File("dockerfile");
		 if (file.exists()) {
		 System.out.print("文件存在");
		 } else {
		 System.out.print("文件不存在");
		 try {
		 file.createNewFile();
		 } catch (IOException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 }// 不存在则创建
		 }
		String nameString = "10.10.10.3/new11imageha12122h121221a:first";
		dockerClient.buildImageCmd(new File("files/dockerfile")).withNoCache().withTag(nameString)
				.exec(new BuildImageResultCallback()).awaitImageId();

		Image lastCreatedImage = dockerClient.listImagesCmd().exec().get(0);

		String repository = lastCreatedImage.getRepoTags()[0];

		System.out.println("created {} {}" + lastCreatedImage.getId()
				+ repository);
		dockerClient.pushImageCmd(nameString.split(":")[0]).withTag(nameString.split(":")[1]).exec(new PushImageResultCallback()).awaitSuccess();
		String containerId = dockerClient
				.createContainerCmd(lastCreatedImage.getId()).exec().getId();

		System.out.println("starting {}" + containerId);

		dockerClient.startContainerCmd(containerId).exec();

	}

	public static DockerClient getDockerClient() {
		return getDockerClient(Global.swarm_ip);
	}

	public static DockerClient getDockerClient(String ip) {
		String urlString = "http://" + ip + ":2375";
		return DockerClientBuilder.getInstance(urlString).build();
	}

	public static DockerClient getDockerClient(String ip, String port) {
		String urlString = "http://" + ip + ":" + port;
		return DockerClientBuilder.getInstance(urlString).build();
	}

}
