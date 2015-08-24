package basic;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

import java.util.List;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.PushResponseItem;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.PushImageResultCallback;

public class MyDockerClientPushImage {

	/**
	 * @param args
	 */

	public static void main(String[] args) throws InterruptedException {

		DockerClient dockerClient = getDockerClient("10.10.10.6");
		  // AuthResponse response = dockerClient.authCmd().exec();
		  // System.out.println(response.getStatus());
		   
		List<com.github.dockerjava.api.model.Image> iamgesList= dockerClient.listImagesCmd().exec();
		for (int i = 0; i < iamgesList.size(); i++) {
			System.out.println(iamgesList.get(i).toString());
		}
		CreateContainerResponse container = dockerClient
				.createContainerCmd("10.10.10.3/hello5")
				.withCmd("true").exec();

		System.out.println("Created container {}" + container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		System.out.println("Committing container: {}" + container.toString());
		String imageId = dockerClient.commitCmd(container.getId())
				.withRepository("10.10.10.3/hello11").exec();

	dockerClient.pushImageCmd("10.10.10.3/hello11").withTag("latest").exec(new PushImageResultCallback()).awaitSuccess();
System.out.println("jelo");

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
