package com.github.octaone.alcubierre.codegen.processor

import com.github.octaone.alcubierre.codegen.api.Deeplink
import com.github.octaone.alcubierre.codegen.api.DeeplinkParam
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ksp.toClassName

class DeeplinkInformationExtractor {

    @OptIn(KspExperimental::class, com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview::class)
    fun extract(
        annotation: Deeplink,
        classDeclaration: KSClassDeclaration,
        constructorParameters: List<KSValueParameter>
    ): List<DeeplinkInformation> {

        /**
         * Группируем шаблоны по их плейсхолдерам, по каждой группировке генерируется конвертер.
         * Группировать нужно потому, что часто для одного экрана используют разом два шаблона:
         * апплинк и диплинк c одинаковыми плейсхолдерами - для этой пары достаточно одного конвертера.
         * Если в список щаблонов добавить еще один, с отличным набором плейсхолдеров,
         * то сгенерируется отдельный конвертер
         */
        val groupPatterns = annotation.patterns.groupBy { pattern ->
            PLACEHOLDER_REGEX
                .findAll(pattern)
                .map { it.groupValues[1] }
                .toList()
        }

        val namedParameters = constructorParameters.map { param ->
            val paramAnnotation = param.getAnnotationsByType(DeeplinkParam::class).firstOrNull()
            val paramName = requireNotNull(param.name).asString()
            val paramAnnotationName = paramAnnotation?.name ?: paramName

            val resolvedType = param.type.resolve()
            val parameter = ConstructorParameter(
                isMarkedNullable = resolvedType.isMarkedNullable,
                hasDefault = param.hasDefault,

                name = paramName,
                placeholder = null, // заполняется в цикле ниже

                className = resolvedType.toClassName(),
                isEnum = (resolvedType.declaration as KSClassDeclaration).classKind == ClassKind.ENUM_CLASS
            )
            paramAnnotationName to parameter
        }

        val className = classDeclaration.toClassName()

        return groupPatterns.entries.mapIndexed { i, (placeholders, patterns) ->
            var converterName = "${className.simpleName}_Converter"
            if (i > 0) converterName += i

            DeeplinkInformation(
                classDeclaration = classDeclaration,
                targetClass = className,
                screenConverter = ClassName(className.packageName, converterName),
                patterns = patterns,
                placeholders = placeholders,
                constructorParams = namedParameters.map { (name, parameter) ->
                    val placeholder = if (placeholders.contains(name)) name else null
                    parameter.copy(placeholder = placeholder)
                },
                targetIsObject = classDeclaration.classKind == ClassKind.OBJECT
            )
        }
    }
}

@Suppress("RegExpRedundantEscape")
private val PLACEHOLDER_REGEX = Regex("\\{(.+?)\\}")
