dependencies {
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    
    // ✅ VERSIÓN FIJA - NO USES pre-release
    implementation("com.lagradost:cloudstream3:4.6.0")
}

version = 1

cloudstream {
    description = "Plugin para SeriesKao"
    authors = listOf("DamianKing12")
    status = 1
    tvTypes = listOf("TvSeries", "Movie")
    requiresResources = false
    language = "es"
    iconUrl = "https://www.google.com/s2/favicons?domain=serieskao.top&sz=%size%"
}

android {
    namespace = "com.DamianKing12"
    buildFeatures {
        buildConfig = true
        viewBinding = false
    }
}
