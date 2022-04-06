package com.season.portal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest
class PortalApplicationTests {

	@Test
	void contextLoads() {
		String home = System.getProperty("user.home");
		String dir = System.getProperty("user.dir");
		Path root = Paths.get(System.getProperty("user.dir")).getFileSystem()
				.getRootDirectories().iterator().next();
		String end="";
	}

}
