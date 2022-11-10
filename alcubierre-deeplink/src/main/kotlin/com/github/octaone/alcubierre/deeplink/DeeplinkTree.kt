package com.github.octaone.alcubierre.deeplink

/**
 * Корень дерева, которое содержит все шаблоны диплинков.
 * Для шаблонов
 * app://host1/{id}
 * app://host1/path1/path11
 * app://host2/{id}
 * дерево будет выглядеть так
 *
 *                 app
 *             /        \
 *          host1      host2
 *          /   \         \
 *        path1 {id}     {id}
 *        /
 *     path11
 *
 * Список [uris] должен быть отсортирован (к примеру, на этапе кодогенерации, чтобы не тратить на это время при создании объекта)
 * Сортировка нужна для того, чтобы обеспечить приоритетность в сопоставлении диплинков с шаблонами
 * @see [sortedByPlaceholders]
 */
class DeeplinkTreeRoot(uris: List<DeeplinkUri>) {

    val node = TreeNode("__ROOT__")

    init {
        uris.forEach(::add)
    }

    private fun add(uri: DeeplinkUri) {
        var node = node.addIfAbsent(TreeNode(uri.scheme))
            .addIfAbsent(TreeNode(uri.host))

        uri.pathSegments.forEach { pathSegment ->
            node = node.addIfAbsent(TreeNode(pathSegment))
            validateAmbiguousPlaceholder(node)
        }

        /**
         * после того, как набор [TreeNode] был добавлен в дерево, последний узел помечается как "хвост шаблона"
         * если узел уже помечен, значит, мы только что добавили в дерево дубликат
         * @see [TreeNode.addIfAbsent]
         */
        require(node.matchingUri == null) { "Обнаружен дубликат. Значения [${uri.pattern}, ${node.matchingUri?.pattern}]" }
        node.matchingUri = uri
    }

    /**
     * Запрещаем добавление шаблонов вида scheme://host/{p1}", "scheme://host/{p2}
     * шаблоны разные, но на самом деле одинаковые, к примеру,
     * диплинк scheme://host/42 подходит под оба шаблона, что создает неоднозначность в мэтчинге
     */
    private fun validateAmbiguousPlaceholder(node: TreeNode) {
        val parentNode = node.parent ?: return
        require(parentNode.childrenPlaceholdersCount <= 1) {
            val links = parentNode.childrenList.map { ".../${parentNode.value}/${it.value}/" }
            "В списке шаблонов $links обнаружена неоднозначность"
        }
    }
}

/**
 * Узел дерева шаблонов. Содержит служебную информацию о типе узла, к примеру, является ли узел плейсхолдером
 */
data class TreeNode(val value: String) {

    private val children = mutableSetOf<TreeNode>()

    val childrenList by lazy(LazyThreadSafetyMode.NONE) { children.toList() }

    var parent: TreeNode? = null
        private set

    /**
     * Uri, для которого составлялась ветвь дерева
     * Свойство проставляется только у замыкающего ветвь узла
     */
    var matchingUri: DeeplinkUri? = null

    /**
     * Является ли узел плейсхолдером, т.е содержащим текстовку вида {param}
     */
    var isPlaceholder = value.isPlaceholder()

    /**
     * Содержимое фигурных скобок для узла, у которого [isPlaceholder] true
     */
    val placeholderValue by lazy(LazyThreadSafetyMode.NONE) { value.extractPlaceholder() }

    /**
     * Количество дочерних узлов, являющихся плейсхолдерами
     * @see DeeplinkTreeRoot.validateAmbiguousPlaceholder
     */
    var childrenPlaceholdersCount = 0
        private set

    /**
     * Добавляет [node] только если такого узла нет среди [children]
     * Используется для работы с дублирующимися/иерархическими цепочками
     * @see DeeplinkTreeRoot.add
     */
    fun addIfAbsent(node: TreeNode): TreeNode {
        return if (children.add(node)) node.also(::onChildInsert) else children.first { it == node }
    }

    private fun onChildInsert(node: TreeNode) {
        node.parent = this
        if (node.isPlaceholder) childrenPlaceholdersCount++
    }
}