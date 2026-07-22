rootProject.name = providers.gradleProperty("id").get()

pluginManagement {
	repositories {
		mavenCentral()
		gradlePluginPortal()

		maven(url = "https://maven.msrandom.net/repository/cloche/")
	}

	val foojay_resolver_convention_plugin_version = providers.gradleProperty("foojay_resolver_convention_plugin_version")
	val kotlin_jvm_plugin_version = providers.gradleProperty("kotlin_jvm_plugin_version")
	val auto_include_plugin_version = providers.gradleProperty("auto_include_plugin_version")
	val cloche_plugin_version = providers.gradleProperty("cloche_plugin_version")
	plugins {
		id("org.gradle.toolchains.foojay-resolver-convention").version(foojay_resolver_convention_plugin_version)
		kotlin("jvm").version(kotlin_jvm_plugin_version)
		id("com.pablisco.gradle.auto.include").version(auto_include_plugin_version)
		id("earth.terrarium.cloche").version(cloche_plugin_version).apply(false)
	}
}

// Gradle modules are automatically included using com.pablisco.gradle.auto.include
// See https://github.com/pablisco/auto-include/
