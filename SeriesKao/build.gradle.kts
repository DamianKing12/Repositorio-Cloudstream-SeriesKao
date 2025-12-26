plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.lagradost.cloudstream3.gradle")
}

android {
    // Asegúrate de que este namespace coincida con tu carpeta de src
    namespace = "com.DamianKing12"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

cloudstream {
    // Este es el nombre de la CLASE dentro de tu archivo .kt
    setPluginClass("SeriesKaoProvider")
    setDisplayName("SeriesKao")
    setDescription("Buscador veloz para navegar en SeriesKao")
}

dependencies {
    implementation("com.lagradost:cloudstream3:pre-release")
    implementation("org.jsoup:jsoup:1.15.3")
}
