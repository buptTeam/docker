package basic;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.PushResponseItem;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.PullImageResultCallback;

public class MyDockerClientPullImage {

	/**
	 * @param args
	 */

	public static void main(String[] args) {
		DockerClient dockerClient = getDockerClient("10.10.10.6");
		Info info = dockerClient.infoCmd().exec();
		System.out.println("Client info: {}" + info.toString());

		int imgCount = info.getImages();
		System.out.println("imgCount1: {}" + dockerClient.listImagesCmd().exec().size());

		// This should be an image that is not used by other repositories
		// already
		// pulled down, preferably small in size. If tag is not used pull will
		// download all images in that repository but tmpImgs will only
		// deleted 'latest' image but not images with other tags
		String testImage = "10.10.10.3/hello";

		System.out.println("Removing image: {}" + testImage);

		try {
			dockerClient.removeImageCmd(testImage).withForce().exec();
		} catch (NotFoundException e) {
			// just ignore if not exist
		}

		info = dockerClient.infoCmd().exec();
		System.out.println("Client info: {}" + info.toString());

		imgCount = info.getImages();
		System.out.println("imgCount2: {}" + dockerClient.listImagesCmd().exec().size());

		System.out.println("Pulling image: {}" + testImage);

	dockerClient.pullImageCmd(testImage).withRepository("10.10.10.3/new11imageha12122h121221a").withTag("first").exec(new PullImageResultCallback()).awaitSuccess();

        info = dockerClient.infoCmd().exec();
       System.out.println("Client info after pull, {}"+ info.toString());

        assertThat(imgCount, lessThanOrEqualTo(info.getImages()));

        InspectImageResponse inspectImageResponse = dockerClient.inspectImageCmd(testImage).exec();
        System.out.println("Image Inspect: {}"+ inspectImageResponse.toString());
        assertThat(inspectImageResponse, notNullValue());

	
      

		

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
