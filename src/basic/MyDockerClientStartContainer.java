package basic;

import java.util.List;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DockerClientBuilder;

public class MyDockerClientStartContainer {

	/**
	 * @param 
	 *        https://github.com/docker-java/docker-java/blob/master/src/test/java
	 *        /com/github/dockerjava/core/command/BuildImageCmdImplTest.java
	 */

	public static void main(String[] args) {

		DockerClient dockerClient = getDockerClient();
		// AuthResponse response = dockerClient.authCmd().exec();
		// System.out.println(response.getStatus());
		System.out.println("start");
		CreateContainerResponse ccResponse = dockerClient
				.createContainerCmd("busybox").withCmd("true").exec();
		System.out.println(ccResponse.getId());
		DockerClient dockerClient1 = getDockerClient("10.10.10.5");
		List<Container> containers= dockerClient1.listContainersCmd().withShowAll(true).exec();
		System.out.println("allcontainers");
		for (int i = 0; i < containers.size(); i++) {
			System.out.println(containers.get(i).getId());
			//dockerClient1.execStartCmd(containers.get(i).getId()).exec();
			dockerClient1.startContainerCmd(containers.get(i).getId()).exec();
		}
		
		System.out.println("over");
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
