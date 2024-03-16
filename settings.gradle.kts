val projectName: String by settings

rootProject.name = projectName

include("libraries:common-spring", "libraries:common-spring-openai", "low-level-v1")
