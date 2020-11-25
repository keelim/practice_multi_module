object Apps {
    const val compileSdk = 30
    const val minSdk = 24
    const val targetSdk = 30
    const val versionCode = 1
    const val versionName = "1.0.0"
}

object Versions {
    const val gradle = "4.2.0-alpha16"
    const val kotlin = "1.4.20"

    const val appcompat = "1.2.0"
    const val junit = "4.12"
}

object Libs {
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
}

object TestLibs {
    const val junit = "junit:junit:${Versions.junit}"
}
