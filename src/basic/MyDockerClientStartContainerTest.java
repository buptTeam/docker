package basic;

import java.util.List;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.core.DockerClientBuilder;

public class MyDockerClientStartContainerTest {

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
//		CreateContainerResponse ccResponse = dockerClient
//				.createContainerCmd("busybox").withCmd("true").exec();
//		System.out.println(ccResponse.getId());
		DockerClient dockerClient1 = getDockerClient("10.10.10.4");
		InspectContainerResponse containers= dockerClient1.inspectContainerCmd("f145ded020ed9bdf7ef30feeede2285c2be63f2ec151abfa15ffef27fd70e2de").exec();
		System.out.println("allInfo"+containers.toString());
//		for (int i = 0; i < containers.size(); i++) {
//			System.out.println(containers.get(i).getId());
//			//dockerClient1.execStartCmd(containers.get(i).getId()).exec();
//			dockerClient1.startContainerCmd(containers.get(i).getId()).exec();
//		}
		
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
