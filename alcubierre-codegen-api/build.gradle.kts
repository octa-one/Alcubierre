description = """
    Модуль содержит базовые сущности, от которых наследуется сгенерированный модулем codegen код.
    Данный модуль нужен потому, что это android-library модуль, а модуль с кодогенерацией должен оставаться pure-kotlin модулем
    """.trimIndent()

plugins {
    id(Conventions.KOTLIN_LIBRARY)
}
