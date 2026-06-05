plugins {
  `java-library`
  `maven-publish`
}

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(25)
  }

  withJavadocJar()
  withSourcesJar()
}

dependencies {
  compileOnly(platform(libs.adventure.bom))
  compileOnlyApi(libs.adventure.api)

  compileOnly(platform(libs.guava.bom))
  compileOnlyApi(libs.guava)

  compileOnly(libs.spigot.api) {
    exclude("commons-lang", "commons-lang")
    exclude("org.yaml", "snakeyaml")
    exclude("com.google.code.gson", "gson")
    exclude("junit", "junit")
  }

  libs.lombok.let {
    compileOnlyApi(it)
    annotationProcessor(it)
  }
}

publishing {
  publications.create<MavenPublication>("maven") {
    from(components["java"])

    groupId = project.group.toString()
    artifactId = project.name
    version = project.version.toString()
  }
}
