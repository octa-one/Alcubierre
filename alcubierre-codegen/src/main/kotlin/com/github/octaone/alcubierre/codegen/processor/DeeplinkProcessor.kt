package com.github.octaone.alcubierre.codegen.processor

import com.github.octaone.alcubierre.codegen.api.Deeplink
import com.github.octaone.alcubierre.codegen.type.DIALOG
import com.github.octaone.alcubierre.codegen.type.SCREEN
import com.github.octaone.alcubierre.codegen.type.converter.generateConverter
import com.github.octaone.alcubierre.codegen.type.generateDeeplinkRegistry
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo

class DeeplinkProcessor(
    private val environment: SymbolProcessorEnvironment
) : SymbolProcessor {

    private val codeGenerator = environment.codeGenerator

    private val extractor = DeeplinkInformationExtractor()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val annotatedClasses = resolver
            .getSymbolsWithAnnotation(requireNotNull(Deeplink::class.qualifiedName))
            .filterIsInstance<KSClassDeclaration>() // аннотация Deeplink имеет @Target = class
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
    ) = annotatedClasses.flatMap { classDeclaration ->
        val constructorParameters = if (classDeclaration.classKind != ClassKind.OBJECT) {
            requireNotNull(classDeclaration.primaryConstructor) {
                "Классы, помеченные аннотацией ${Deeplink::class.simpleName} обязаны иметь primary конструктор"
            }.parameters
        } else {
            emptyList()
        }

        val annotation = classDeclaration.getAnnotationsByType(Deeplink::class).first()
        if (annotation.patterns.isEmpty()) throw IllegalArgumentException("В аннотации Deeplink не заполнен параметр patterns")

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
                .addAnnotation(SUPPRESSIONS)
                .build()
                .writeTo(codeGenerator, dependencies)
        }
    }

    private fun createDeeplinkRegistry(classInformation: List<DeeplinkInformation>) {
        if (classInformation.toList().isEmpty()) return
        val registryBase = environment.options[REGISTRY_BASE_NAME_OPTION] ?: throw IllegalArgumentException(
            "Не указан параметр $REGISTRY_BASE_NAME_OPTION, необходимый для кодогенерации диплинков. " +
                "Убедитесь, что к модулю подключен convention plugin навигации"
        )
        val registry = generateDeeplinkRegistry(registryBase, classInformation)

        val dependencies = Dependencies(
            aggregating = true,
            sources = classInformation.map { requireNotNull(it.classDeclaration.containingFile) }.toTypedArray()
        )
        FileSpec.builder("com.github.octaone.alcubierre.codegen", requireNotNull(registry.name))
            .addType(registry)
            .addAnnotation(SUPPRESSIONS)
            .build()
            .writeTo(codeGenerator, dependencies)
    }

    private fun validateDeclaration(declaration: KSClassDeclaration) {
        require(declaration.getAllSuperTypes().any {
            val typeName = it.toTypeName()
            typeName == SCREEN || typeName == DIALOG
        }) {
            "Аннотация Deeplink может быть использована только на наследниках класса Screen или Dialog"
        }
    }
}

private const val REGISTRY_BASE_NAME_OPTION = "deeplink.registry.base.name"

private val SUPPRESS_NAMES = arrayOf(
    "Formatting",
    "ClassName",
    "UNNECESSARY_NOT_NULL_ASSERTION",
    "SpacingAroundParens",
    "NoBlankLineBeforeRbrace",
    "UnsafeCallOnNullableType",
)

private val SUPPRESSIONS = AnnotationSpec.builder(Suppress::class)
    .useSiteTarget(AnnotationSpec.UseSiteTarget.FILE)
    .addMember(
        SUPPRESS_NAMES.indices.joinToString { "%S" },
        *SUPPRESS_NAMES
    )
    .build()
