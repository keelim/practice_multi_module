include(
        ":keelchat",
        ":keelstudy",
        ":commonlibrary"
)

arrayOf(
        ":keelchat",
        ":keelstudy"
).forEach { name ->
     project(name).projectDir = File(rootDir, "features/${name.substring(startIndex = 1)}")
}