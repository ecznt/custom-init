package com.ecznt.initdemo.model;


import lombok.Data;

@Data
public class ProjectInput {
    private String type;
    private String language;
    private String bootVersion;
    private String baseDir;
    private String groupId;
    private String artifactId;
    private String name;
    private String description;
    private String packageName;
    private String packaging;
    private String javaVersion;
    private String dependencies;
}
