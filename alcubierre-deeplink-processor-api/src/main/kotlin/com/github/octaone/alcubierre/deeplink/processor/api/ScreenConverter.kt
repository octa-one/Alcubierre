package com.github.octaone.alcubierre.deeplink.processor.api

public interface ScreenConverter {

    /**
     * @param _from набор плейсхолдеров, полученных
     * при сопоставлении диплинка с шаблонами из [Deeplink.patterns]
     *
     * @return наследник [FragmentScreen] с совпавшим [Deeplink]
     * @throws Exception при преобразовании плейсхолдеров из строк в типы конвертируемого объекта
     */
    public fun convert(_from: Map<String, String>): Any
}

public const val PARAM_FROM: String = "_from"
