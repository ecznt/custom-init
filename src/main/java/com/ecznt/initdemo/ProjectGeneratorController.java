package com.ecznt.initdemo;

import com.ecznt.initdemo.model.ProjectInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.Yaml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Controller
public class ProjectGeneratorController {

    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    GeneratorService generatorService;

    @GetMapping("/generate-project")
    public ResponseEntity<byte[]> generateProject(@RequestBody ProjectInput projectInput) throws IOException {


        //Convert applicaton.properties to application.yml
//        ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(projectFile));
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ZipOutputStream zos = new ZipOutputStream(baos);
//
//        Yaml yaml = new Yaml();
//
//        ZipEn

        byte[] projectFile = generatorService.getProjectStarter(projectInput);

        generatorService.configureApplicationProperties(projectFile);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename(projectInput.getBaseDir() + ".zip").build());

        return new ResponseEntity<>(projectFile, headers, HttpStatus.OK);
    }
}
