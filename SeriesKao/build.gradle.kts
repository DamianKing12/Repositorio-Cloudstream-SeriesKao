plugins {
    id("com.android.library")
    kotlin("android")
    id("com.lagradost.cloudstream3.gradle")
}

android {
    namespace = "com.DamianKing12"
    compileSdk = 34
    defaultConfig { minSdk = 21 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

cloudstream {
    // Aquí registramos el nombre que aparecerá en las app
    setPluginClass("com.DamianKing12.SeriesKaoPlugin")
    setDescription("Series Kao Indexador - Buscador veloz")
}

dependencies {
    implementation("com.lagradost:cloudstream3:pre-release")
    implementation("org.jsoup:jsoup:1.15.3")
}