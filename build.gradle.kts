val projectGroup: String by project
val projectVersion: String by project

allprojects {
    version = projectVersion
    group = projectGroup
}