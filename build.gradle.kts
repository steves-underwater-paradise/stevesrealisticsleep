import earth.terrarium.cloche.api.metadata.CommonMetadata.Dependency
import earth.terrarium.cloche.api.metadata.CommonMetadata.Environment

plugins {
	id("earth.terrarium.cloche")
	id("maven-publish")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21

    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/steves-underwater-paradise/stevesrealisticsleep")
            credentials {
                username = providers.gradleProperty("gpr.user").orElse(providers.environmentVariable("GITHUB_ACTOR")).orNull
                password = providers.gradleProperty("gpr.key").orElse(providers.environmentVariable("GITHUB_TOKEN")).orNull
            }
        }
    }
}

repositories {
    cloche.librariesMinecraft()
    mavenCentral()

    cloche {
        main()
        mavenParchment()
        mavenFabric()
        mavenNeoforgedMeta()
        mavenNeoforged()
    }

    maven(url = "https://api.modrinth.com/maven/")
    maven(url = "https://maven.gnomecraft.net/releases/")
    maven(url = "https://maven.shedaniel.me/")
}

cloche {
    metadata {
        val mod_id = providers.gradleProperty("id")
        modId = mod_id.get()
        name = providers.gradleProperty("name").get()
        description = providers.gradleProperty("description").get()
        providers.gradleProperty("authors").get().split(',').forEach(::author)
        license = providers.gradleProperty("license_spdx").get()
        version = providers.gradleProperty("version").get()
        icon = "assets/${mod_id.get()}/icon.png"
        url = "https://www.curseforge.com/minecraft/mc-mods/${mod_id.get()}"
        sources = "https://github.com/steves-underwater-paradise/${mod_id.get()}"
        issues = "https://github.com/steves-underwater-paradise/${mod_id.get()}/issues"

        suggest("sodium", "*")
        suggest("better-clouds", "*")
        suggest("ticks", "*")

        markConflict("tawct", "*")
        markConflict("timecontrol", "*")

        markIncompatible("sleepwarp", "*")
        markIncompatible("sleeping-overhaul-2", "*")
        markIncompatible("fast-sleep", "*")
        markIncompatible("seamless-sleep", "*")
        markIncompatible("better-sleep", "*")
        markIncompatible("vanilla-outsider-true-sleep", "*")
        markIncompatible("sleep-timelapse", "*")
    }

    mappings {
        official()
    }

    val mixin_extras_version = providers.gradleProperty("mixin_extras_version")
    val mod_menu_version = providers.gradleProperty("mod_menu_version")
    common {
        dependencies {
            compileOnly("org.jetbrains:annotations:${providers.gradleProperty("jetbrains_annotations_version").get()}")

            val mixin_extras = "io.github.llamalad7:mixinextras-common:${mixin_extras_version.get()}"
            compileOnly(mixin_extras)
            annotationProcessor(mixin_extras)
        }

        mixins.from(file("src/common/main/steves_realistic_sleep_common.mixins.json"))
        accessWideners.from(file("src/common/main/resources/stevesrealisticsleep_common.classtweaker"))
    }

    val common1211Module = common("common:1.21.1") {
        mixins.from(file("src/common/1.21.1/main/steves_realistic_sleep_common_1211.mixins.json"))
    }
    val common12111Module = common("common:1.21.11") {
        mixins.from(file("src/common/1.21.11/main/steves_realistic_sleep_common_12111.mixins.json"))
    }

    val commonFabricModule = common("fabric") {
        metadata {
            custom(
                "modmenu" to mapOf(
                    "links" to mapOf(
                        "modmenu.discord" to "https://discord.gg/KbWxgGg"
                    )
                )
            )
        }
    }

    fabric("fabric:1.21.1") {
        dependsOn(commonFabricModule)
        dependsOn(common1211Module)
        minecraftVersion.set("1.21.1")

        metadata {
            entrypoint("main", "io.github.steveplays28.stevesrealisticsleep.fabric.StevesRealisticSleepFabric")
            entrypoint("client", "io.github.steveplays28.stevesrealisticsleep.fabric.client.StevesRealisticSleepFabricClient")
            entrypoint("modmenu", "io.github.steveplays28.stevesrealisticsleep.fabric.client.compat.modmenu.StevesRealisticSleepFabricClientModMenuCompat")

            require("minecraft", "1.21.1 <=1.21.1")
            require("cloth-config", "15.0.140")

            suggest("modmenu", "${providers.gradleProperty("mod_menu_version").get()}", environment = Environment.Client)
        }

        val fabric_loader_version = providers.gradleProperty("fabric_loader_version")
        loaderVersion = fabric_loader_version.get()

        includedClient()

        runs {
            client()
            server()
        }

        dependencies {
            val mixin_extras = "io.github.llamalad7:mixinextras-fabric:${mixin_extras_version.get()}"
            implementation(mixin_extras)
            annotationProcessor(mixin_extras)
            include(mixin_extras)

            val fabric_api_version = providers.gradleProperty("fabric_api_version")
            fabricApi(fabric_api_version.get())

            modApi("me.shedaniel.cloth:cloth-config-fabric:15.0.140") {
                exclude(group = "net.fabricmc.fabric-api")
            }
            modApi("com.terraformersmc:modmenu:${mod_menu_version.get()}")

            val server_i18n_api = "maven.modrinth:server-i18n-api:${providers.gradleProperty("server_i18n_api_version").get()}-1.21.8-fabric"
            modApi(server_i18n_api)
            include(server_i18n_api)
        }
    }

    fabric("fabric:1.21.11") {
        dependsOn(commonFabricModule)
        dependsOn(common12111Module)
        minecraftVersion.set("1.21.11")

        val mod_menu_version = "17.0.1-beta.1"
        val cloth_config_version = "21.11.153"
        val server_i18n_api_version = "21.11.153"
        metadata {
            entrypoint("main", "io.github.steveplays28.stevesrealisticsleep.fabric.StevesRealisticSleepFabric")
            entrypoint("client", "io.github.steveplays28.stevesrealisticsleep.fabric.client.StevesRealisticSleepFabricClient")
            entrypoint("modmenu", "io.github.steveplays28.stevesrealisticsleep.fabric.client.compat.modmenu.StevesRealisticSleepFabricClientModMenuCompat")

            require("minecraft", "1.21.11 <=1.21.11")
            require("cloth-config", cloth_config_version)

            suggest("modmenu", mod_menu_version, environment = Environment.Client)
        }

        val fabric_loader_version = providers.gradleProperty("fabric_loader_version")
        loaderVersion = fabric_loader_version.get()

        includedClient()

        runs {
            client()
            server()
        }

        dependencies {
            val mixin_extras = "io.github.llamalad7:mixinextras-fabric:${mixin_extras_version.get()}"
            implementation(mixin_extras)
            annotationProcessor(mixin_extras)
            include(mixin_extras)
            fabricApi("0.141.6")

            modApi("me.shedaniel.cloth:cloth-config-fabric:${cloth_config_version}") {
                exclude(group = "net.fabricmc.fabric-api")
            }
            modApi("com.terraformersmc:modmenu:${mod_menu_version}")

            val server_i18n_api = "maven.modrinth:server-i18n-api:${providers.gradleProperty("server_i18n_api_version").get()}-1.21.11-fabric"
            modApi(server_i18n_api)
            include(server_i18n_api)
        }
    }

    neoforge("neoforge:1.21.1") {
        dependsOn(common1211Module)
        minecraftVersion.set("1.21.1")

        metadata {
            dependency {
                modId.set("minecraft")
                type.set(Dependency.Type.Required)
                version {
                    start.set("1.21.1")
                    end.set("1.21.2")
                    startInclusive.set(true)
                    endExclusive.set(true)
                }
            }
            require("cloth_config", "15.0.140")
        }

        val neoforge_loader_version = providers.gradleProperty("neoforge_loader_version")
        loaderVersion = neoforge_loader_version.get()

        runs {
            client()
            server()
        }

        dependencies {
            val mixin_extras = "io.github.llamalad7:mixinextras-neoforge:${mixin_extras_version.get()}"
            implementation(mixin_extras)
            annotationProcessor(mixin_extras)
            include(mixin_extras)

            modApi("me.shedaniel.cloth:cloth-config-neoforge:15.0.140")

            val server_i18n_api = "maven.modrinth:server-i18n-api:${providers.gradleProperty("server_i18n_api_version").get()}-1.21.3-neoforge"
            modApi(server_i18n_api)
            include(server_i18n_api)
        }
    }
}
