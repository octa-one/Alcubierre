@file:Suppress("ReturnCount", "NestedBlockDepth")

package com.github.octaone.alcubierre.deeplink

import java.net.URLDecoder

/**
 * Сушность, инкапсулирующая сопоставление диплинка с заданным шаблоном
 * @param [root] дерево, содержащее шаблоны вида app://host/{arg1}?query={arg2} где {arg1}, {arg2} - плейсхолдеры
 */
class DeeplinkTreeMatcher(private val root: DeeplinkTreeRoot) {

    fun match(deeplink: DeeplinkUri): DeeplinkMatch? {
        val segments = with(deeplink) { buildList { add(scheme); add(host); addAll(pathSegments) } }
        val match = treeMatch(root.node, segments) ?: return null

        val queryParameters = mutableMapOf<String, String>()
        // query параметры могут дублироваться, не поддерживаем
        for (name in deeplink.queryParameterNames) {
            val value = deeplink.getQueryParameter(name)
            if (value != null) queryParameters[name] = URLDecoder.decode(value, "UTF-8")
        }

        return match.copy(placeholders = match.placeholders + queryParameters)
    }

    /**
     * рекурсивное сопоставление диплинка с шаблонами, собранными в дерево
     * @param deeplinkSegments диплинк, разбитый по сегментам (scheme, host, path)
     * @param segmentIndex индекс сегмента диплинка, который будет сравниваться с дочерними узлами из [root]
     * @param placeholders совпадения плейсхолдеров из шаблона с содержимым диплинка
     */
    private fun treeMatch(
        root: TreeNode,
        deeplinkSegments: List<String>,
        segmentIndex: Int = 0,
        placeholders: MutableMap<String, String> = mutableMapOf()
    ): DeeplinkMatch? {
        var match: DeeplinkMatch? = null
        var nodeIndex = 0

        do {
            val node = root.childrenList.getOrNull(nodeIndex) ?: return null
            val segment = deeplinkSegments[segmentIndex]

            if (equals(node, segment)) {
                var localPlaceholders = placeholders
                if (node.isPlaceholder) {
                    // placeholders нельзя использовать напрямую, т.к
                    // алгоритм может пройти через несколько неудачных веток рекурсии с частичным совпадением плейсхолдеров
                    localPlaceholders = localPlaceholders.toMutableMap()
                    localPlaceholders[node.placeholderValue] = URLDecoder.decode(segment, "UTF-8")
                }

                if (segmentIndex + 1 < deeplinkSegments.size) {
                    // узел совпал с сегментом диплинка, но сравнили еще не все сегменты
                    match = treeMatch(node, deeplinkSegments, segmentIndex + 1, localPlaceholders)
                } else {
                    // если диплинк совпал полностью, то нужно проверить, является ли совпавший узел
                    // замыкающим в шаблоне, иначе получится, что диплинк совпал лишь с частью шаблона
                    val uri = node.matchingUri
                    if (uri != null) {
                        match = DeeplinkMatch(uri.pattern, localPlaceholders)
                    }
                }
            }

            if (match != null) return match
            nodeIndex += 1
        } while (nodeIndex < root.childrenList.size)

        return null
    }

    private fun equals(node: TreeNode, deeplinkSegment: String): Boolean = when {
        node.isPlaceholder -> true
        else -> node.value.contentEquals(deeplinkSegment, true)
    }
}

/**
 * Результат сопоставления диплинка с шаблоном.
 * @property matchedPattern шаблон диплинка, с которым произошло совпадение
 * @property placeholders плейсходеры из path, и все квери параметры
 */
data class DeeplinkMatch(val matchedPattern: String, val placeholders: Map<String, String>)