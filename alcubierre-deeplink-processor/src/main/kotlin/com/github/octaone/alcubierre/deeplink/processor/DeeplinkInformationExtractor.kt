package com.github.octaone.alcubierre.deeplink.processor

import com.github.octaone.alcubierre.deeplink.processor.api.DeeplinkParam
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName

public class DeeplinkInformationExtractor {

    @OptIn(KspExperimental::class)
    public fun extract(
        patterns: List<String>,
        classDeclaration: KSClassDeclaration,
        constructorParameters: List<KSValueParameter>
    ): List<DeeplinkInformation> {

        /**
         * Group templates by their placeholders, a converter is generated for each group.
         * Grouping is necessary because multiple templates can be used for one screen.
         */
        val groupPatterns = patterns.groupBy { pattern ->
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
                placeholder = null, // will be filled later
                className = resolvedType.toClassName(),
                type = resolvedType.toTypeName(),
                isEnum = (resolvedType.declaration as KSClassDeclaration).classKind == ClassKind.ENUM_CLASS
            )
            paramAnnotationName to parameter
        }

        val className = classDeclaration.toClassName()

        return groupPatterns.entries.mapIndexed { i, (placeholders, patterns) ->
            var converterName = "${className.simpleName}Converter"
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

private val PLACEHOLDER_REGEX = "\\{(.+?)\\}".toRegex()
