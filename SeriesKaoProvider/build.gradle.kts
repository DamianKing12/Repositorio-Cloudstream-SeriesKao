plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.lagradost.cloudstream3.gradle")
}

android {
    namespace = "com.DamianKing12"
    
    defaultConfig {
        minSdk = 21
        targetSdk = 35
    }
    
    buildFeatures {
        buildConfig = true
        viewBinding = false
    }
}

cloudstream {
    description = "Plugin para SeriesKao"
    authors = listOf("DamianKing12")
    status = 1
    tvTypes = listOf("TvSeries", "Movie")
    requiresResources = false
    language = "es"
    iconUrl = "https://www.google.com/s2/favicons?domain=serieskao.top&sz=%size%"
}

dependencies {
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    
    // USAMOS LA RUTA DIRECTA DE GITHUB PARA EVITAR ERRORES DE JITPACK
    implementation("com.github.recloudstream:cloudstream:pre-release")
    
    implementation(kotlin("stdlib"))
    implementation("com.github.Blatzar:NiceHttp:0.4.11")
    implementation("org.jsoup:jsoup:1.18.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")
}
