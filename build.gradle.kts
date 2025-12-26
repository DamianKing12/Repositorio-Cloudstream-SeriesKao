// 1. EL BLOQUE BUILDSCRIPT SIEMPRE PRIMERO Y SOLO UNO
buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
    dependencies {
        classpath("com.github.recloudstream:gradle:-SNAPSHOT")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.20")
    }
}

// 2. PLUGINS (Opcional en la raíz si se aplican en los subproyectos)
plugins {
    id("com.lagradost.cloudstream3.gradle") version "1.0.0" apply false
}

// 3. REPOSITORIOS PARA TODOS LOS PROYECTOS
allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

// 4. TAREAS DE LIMPIEZA
tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}