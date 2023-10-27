package com.ecznt.initdemo;

import com.ecznt.initdemo.model.ProjectInput;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class GeneratorService {

    public byte[] getProjectStarter(ProjectInput projectInput) {
        RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://start.spring.io/starter.zip")
                .queryParam("type", projectInput.getType())
                .queryParam("language", projectInput.getLanguage())
                .queryParam("bootVersion", projectInput.getBootVersion())
                .queryParam("baseDir", projectInput.getBaseDir())
                .queryParam("groupId", projectInput.getGroupId())
                .queryParam("artifactId", projectInput.getArtifactId())
                .queryParam("name" ,projectInput.getName())
                .queryParam("description", projectInput.getDescription())
                .queryParam("packageName", projectInput.getPackageName())
                .queryParam("packaging", projectInput.getPackaging())
                .queryParam("javaVersion", projectInput.getJavaVersion())
                .queryParam("dependencies", projectInput.getDependencies());

        String url = builder.toUriString();
        System.out.println(url);

        return restTemplate.getForObject(url, byte[].class);

    }

    private static final String upload_folder = "upload/";

    public void configureApplicationProperties(byte[] projectFile) throws IOException {


            ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(projectFile));
            ZipEntry entry = null;

            while((entry = zis.getNextEntry()) != null) {
                String entryName = entry.getName();

                if (entry.isDirectory()) {
                    File dir = new File(upload_folder + entryName);
                    dir.mkdir();
                } else {
                    File file = new File(upload_folder + entryName);
                    file.createNewFile();

                    Files.copy(zis, file.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    Properties properties = new Properties();
                    properties.setProperty("server.port", "8181");

                    if (entryName.equals("application.properties")) {
                        properties.store(Files.newOutputStream(file.toPath()), null);
                    }
                }

                zis.closeEntry();;
                System.out.println("application.properties modified.");
            }
    }

}
