plugins {
    id("com.github.node-gradle.node") version "7.1.0"
}

apply(plugin = "base")
apply(plugin = "com.github.node-gradle.node")

// https://github.com/node-gradle/gradle-node-plugin/blob/master/src/test/resources/fixtures/kotlin/build.gradle.kts
node {
    download.set(false)
}

tasks.getByName<Delete>("clean") {
    delete.add("node_modules")
    delete.add("dist")
}

// configure task dependencies for npm_run_build
tasks.getByName("yarn_build") {
    dependsOn("yarn_install")
    inputs.dir("app")
    inputs.dir("components")
    inputs.dir("public")
    inputs.dir("services")
    inputs.file("theme.ts")
    inputs.file("tsconfig.json")
    outputs.dir(".next")
}

// create the webapp gradle configuration
val webapp by configurations.creating {
    isCanBeConsumed = true
    isCanBeResolved = false
}

artifacts {
    // npm_run_build is defined by the node-gradle plugin
    // define the dist directory to this configuration and make it a build result of npm_run_build
    val yarnBuildTask = tasks.getByName("yarn_build")
    add("webapp", file("dist")) {
        builtBy(yarnBuildTask)
    }
}
