package com.github.octaone.alcubierre.codegen.processor

import com.github.octaone.alcubierre.codegen.api.Deeplink
import com.github.octaone.alcubierre.codegen.type.KNOWN_TYPES
import com.github.octaone.alcubierre.codegen.type.converter.generateConverter
import com.github.octaone.alcubierre.codegen.type.generateDeeplinkRegistry
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo

class DeeplinkProcessor(
    private val environment: SymbolProcessorEnvironment
) : SymbolProcessor {

    private val codeGenerator = environment.codeGenerator

    private val allowedTypes = environment.options[ARG_ALLOWED_TYPES]
        ?.split(',')
        ?.mapTo(mutableSetOf()) { ClassName.bestGuess(it.trim()) }
        .orEmpty()

    private val extractor = DeeplinkInformationExtractor()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val annotatedClasses = resolver
            .getSymbolsWithAnnotation(Deeplink::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>() // Deeplink target = class
            .onEach(::validateDeclaration)
            .toList()

        val classInformation = collectClassInformation(annotatedClasses)

        createScreenConverters(classInformation)
        createDeeplinkRegistry(classInformation)

        return emptyList()
    }

    @OptIn(KspExperimental::class)
    private fun collectClassInformation(
        annotatedClasses: List<KSClassDeclaration>
    ): List<DeeplinkInformation> =
        annotatedClasses.flatMap { classDeclaration ->
            val constructorParameters = if (classDeclaration.classKind != ClassKind.OBJECT) {
                requireNotNull(classDeclaration.primaryConstructor) {
                    "Classes marked with ${Deeplink::class.simpleName} must have a primary constructor."
                }.parameters
            } else {
                emptyList()
            }

            val annotation = classDeclaration.getAnnotationsByType(Deeplink::class).first()
            require(annotation.patterns.isNotEmpty()) { "Deeplink does not contain the patterns." }

            extractor.extract(annotation, classDeclaration, constructorParameters)
        }

    private fun createScreenConverters(classInformation: List<DeeplinkInformation>) {
        for (info in classInformation) {
            val converter = generateConverter(info)
            val dependencies = Dependencies(
                aggregating = false,
                sources = arrayOf(requireNotNull(info.classDeclaration.containingFile))
            )
            FileSpec.builder(info.screenConverter.packageName, requireNotNull(converter.name))
                .addType(converter)
                .build()
                .writeTo(codeGenerator, dependencies)
        }
    }

    private fun createDeeplinkRegistry(classInformation: List<DeeplinkInformation>) {
        if (classInformation.toList().isEmpty()) return
        val registryBase = environment.options[ARG_REGISTRY_BASE_NAME] ?: throw IllegalArgumentException(
            "$ARG_REGISTRY_BASE_NAME is not specified. Make sure that the convention navigation plugin is applied to the module."
        )
        val registry = generateDeeplinkRegistry(registryBase, classInformation)

        val dependencies = Dependencies(
            aggregating = true,
            sources = classInformation.map { requireNotNull(it.classDeclaration.containingFile) }.toTypedArray()
        )
        FileSpec.builder("alcubierre.codegen", requireNotNull(registry.name))
            .addType(registry)
            .build()
            .writeTo(codeGenerator, dependencies)
    }

    @Suppress("ReturnCount")
    private fun validateDeclaration(declaration: KSClassDeclaration) {
        for (type in declaration.superTypes) {
            val typeName = type.toTypeName()
            if (typeName !is ClassName) continue

            if (KNOWN_TYPES.contains(typeName)) return
            if (allowedTypes.contains(typeName)) return
        }

        error("${declaration.toClassName().simpleName}: Deeplink annotation can only be used on: ${(KNOWN_TYPES + allowedTypes).joinToString { it.simpleName }}"
        )
    }
}

private const val ARG_ALLOWED_TYPES = "alcubierre.allowedTypes"
private const val ARG_REGISTRY_BASE_NAME = "alcubierre.registryBaseName"
