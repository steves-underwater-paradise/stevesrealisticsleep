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

    maven(url = "https://maven.gnomecraft.net/releases/")
}

cloche {
    metadata {
        val mod_id: String by project
        modId = mod_id
        name = "Steve's Early Loading Screen"
        license = "GPL-3.0"
        version = "1.0.0"

        val authors: String by project
        authors.split(',').forEach(::author)
    }

    val minecraft_version: String by project
    minecraftVersion.set(minecraft_version)

    mappings {
        official()

        val parchment_mappings_version: String by project
        parchment(parchment_mappings_version)
    }

    fabric {
        val fabric_loader_version: String by project
        loaderVersion = fabric_loader_version

        mixins.from(file("src/common/main/steves_realistic_sleep_common.mixins.json"))
        includedClient()

        runs {
            client()
        }

        dependencies {
            val fabric_api_version: String by project
            fabricApi(fabric_api_version)

            val mod_menu_version: String by project
            modRuntimeOnly("com.terraformersmc:modmenu:${mod_menu_version}")
        }
    }

    neoforge {
        val neoforge_loader_version: String by project
        loaderVersion = neoforge_loader_version

        mixins.from(file("src/common/main/steves_realistic_sleep_common.mixins.json"))
    }
}
