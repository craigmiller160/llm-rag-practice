val projectName: String by settings

rootProject.name = projectName

include(
    "libraries:common-spring",
    "libraries:common-spring-openai",
    "libraries:common-milvus",
    "low-level-v1",
    "langchain-v1",
    "low-level-v2"
)
