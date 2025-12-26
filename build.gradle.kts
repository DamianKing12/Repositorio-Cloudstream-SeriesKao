buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io") // IMPORTANTE: Aquí es donde Gradle busca el plugin de Cloudstream
    }
    dependencies {
        // Esta línea es la que realmente importa para que funcione el comando 'make'
        classpath("com.github.recloudstream:gradle:-SNAPSHOT")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.20")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

// Eliminamos el bloque plugins { ... } de aquí arriba porque 
// ya lo estamos inyectando vía classpath en el buildscript.

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
