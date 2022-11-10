package com.github.octaone.alcubierre.codegen.api

interface ScreenConverter {

    /**
     * @param from набор плейсхолдеров, полученных
     * при сопоставлении диплинка с шаблонами из [Deeplink.patterns]
     *
     * @return наследник [FragmentScreen] с совпавшим [Deeplink]
     * @throws Exception при преобразовании плейсхолдеров из строк в типы конвертируемого объекта
     */
    fun convert(from: Map<String, String>): Any
}
