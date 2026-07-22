import earth.terrarium.cloche.api.metadata.CommonMetadata.Environment

plugins {
	id("earth.terrarium.cloche")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21

    withSourcesJar()
    withJavadocJar()
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

        markConflict("tawct", "*")
        markConflict("timecontrol", "*")
    }

    minecraftVersion.set(providers.gradleProperty("minecraft_version").get())

    mappings {
        official()
        parchment(providers.gradleProperty("parchment_mappings_version").get())
    }

    val mixin_extras_version = providers.gradleProperty("mixin_extras_version")
    val cloth_config_version = providers.gradleProperty("cloth_config_version")
    val mod_menu_version = providers.gradleProperty("mod_menu_version")
    common {
        dependencies {
            compileOnly("org.jetbrains:annotations:${providers.gradleProperty("jetbrains_annotations_version").get()}")

            val mixin_extras = "io.github.llamalad7:mixinextras-common:${mixin_extras_version.get()}"
            compileOnly(mixin_extras)
            annotationProcessor(mixin_extras)
        }
    }

    fabric {
        metadata {
            entrypoint("main", "io.github.steveplays28.stevesrealisticsleep.fabric.StevesRealisticSleepFabric")
            entrypoint("client", "io.github.steveplays28.stevesrealisticsleep.fabric.client.StevesRealisticSleepFabricClient")
            entrypoint("modmenu", "io.github.steveplays28.stevesrealisticsleep.fabric.client.compat.modmenu.StevesRealisticSleepFabricClientModMenuCompat")

            custom(
                "modmenu" to mapOf(
                    "links" to mapOf(
                        "modmenu.discord" to "https://discord.gg/KbWxgGg"
                    )
                )
            )

            require("cloth-config", "${providers.gradleProperty("cloth_config_version").get()}")

            suggest("modmenu", "${providers.gradleProperty("mod_menu_version").get()}", environment = Environment.Client)
        }

        val fabric_loader_version = providers.gradleProperty("fabric_loader_version")
        loaderVersion = fabric_loader_version.get()

        mixins.from(file("src/common/main/steves_realistic_sleep_common.mixins.json"))
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

            modApi("me.shedaniel.cloth:cloth-config-fabric:${cloth_config_version.get()}") {
                exclude(group = "net.fabricmc.fabric-api")
            }
            modApi("com.terraformersmc:modmenu:${mod_menu_version.get()}")

            val server_i18n_api = "maven.modrinth:server-i18n-api:${providers.gradleProperty("server_i18n_api_version").get()}-1.21.8-fabric"
            modApi(server_i18n_api)
            include(server_i18n_api)
        }
    }

    neoforge {
        metadata {
            require("cloth_config", "${providers.gradleProperty("cloth_config_version").get()}")
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

            modApi("me.shedaniel.cloth:cloth-config-neoforge:${cloth_config_version.get()}")

            val server_i18n_api = "maven.modrinth:server-i18n-api:${providers.gradleProperty("server_i18n_api_version").get()}-1.21.3-neoforge"
            modApi(server_i18n_api)
            include(server_i18n_api)
        }

        mixins.from(file("src/common/main/steves_realistic_sleep_common.mixins.json"))
    }
}
