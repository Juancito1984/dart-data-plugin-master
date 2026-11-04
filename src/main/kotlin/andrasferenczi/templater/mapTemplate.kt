package andrasferenczi.templater

import andrasferenczi.ext.*
import com.intellij.codeInsight.template.Template
import com.intellij.codeInsight.template.TemplateManager

data class MapTemplateParams(
    val className: String,
    val variables: List<AliasedVariableTemplateParam>,
    val useNewKeyword: Boolean,
    val addKeyMapper: Boolean,
    val noImplicitCasts: Boolean
)

// The 2 will be generated with the same function
fun createMapTemplate(
    templateManager: TemplateManager,
    params: MapTemplateParams
): Template {

    return templateManager.createTemplate(
        TemplateType.MapTemplate.templateKey,
        TemplateConstants.DART_TEMPLATE_GROUP
    ).apply {
        addToMap(params)
        addNewLine()
        addNewLine()
        addFromMap(params)
    }
}

private fun Template.addAssignKeyMapperIfNotValid() {
    addTextSegment(TemplateConstants.KEYMAPPER_VARIABLE_NAME)
    addSpace()
    addTextSegment("??=")
    addSpace()
    withParentheses {
        addTextSegment(TemplateConstants.KEY_VARIABLE_NAME)
    }
    addSpace()
    addTextSegment("=>")
    addSpace()
    addTextSegment(TemplateConstants.KEY_VARIABLE_NAME)
    addSemicolon()
    addNewLine()
    addNewLine()
}

private fun Template.addToMap(params: MapTemplateParams) {
    val (_, variables, _, addKeyMapper, _) = params

    isToReformat = true

    addTextSegment("Map<String, dynamic>")
    addSpace()
    addTextSegment(TemplateConstants.TO_MAP_METHOD_NAME)
    withParentheses {
        if (addKeyMapper) {
            withCurlyBraces {
                addNewLine()
                addTextSegment("String Function(String key)? ${TemplateConstants.KEYMAPPER_VARIABLE_NAME}")
                addComma()
                addNewLine()
            }
        }
    }
    addSpace()
    withCurlyBraces {

        if (addKeyMapper) {
            addAssignKeyMapperIfNotValid()
        }

        addTextSegment("return")
        addSpace()
        withCurlyBraces {
            addNewLine()

            variables.forEach {
                "'${it.mapKeyString}'".also { keyParam ->
                    if (addKeyMapper) {
                        addTextSegment(TemplateConstants.KEYMAPPER_VARIABLE_NAME)
                        withParentheses {
                            addTextSegment(keyParam)
                        }
                    } else {
                        addTextSegment(keyParam)
                    }
                }

                addTextSegment(":")
                addSpace()
                addTextSegment(it.variableName)
                addComma()
                addNewLine()
            }
        }
        addSemicolon()
    }
}

private fun Template.addFromMap(
    params: MapTemplateParams
) {
    val (className, variables, useNewKeyword, addKeyMapper, noImplicitCasts) = params

    isToReformat = true

    addTextSegment("factory")
    addSpace()
    addTextSegment(className)
    addTextSegment(".")
    addTextSegment(TemplateConstants.FROM_MAP_METHOD_NAME)
    withParentheses {
        if (addKeyMapper) {
            addNewLine()
            // New line does not format, no matter what is in this if statement
            addSpace()
        }
        addTextSegment("Map<String, dynamic>")
        addSpace()
        addTextSegment(TemplateConstants.MAP_VARIABLE_NAME)

        if (addKeyMapper) {
            addComma()
            addSpace()
            withCurlyBraces {
                addNewLine()
                addTextSegment("String Function(String ${TemplateConstants.KEY_VARIABLE_NAME})?")
                addSpace()
                addTextSegment(TemplateConstants.KEYMAPPER_VARIABLE_NAME)
                addComma()
                addNewLine()
            }
        }
    }
    addSpace()
    withCurlyBraces {

        if (addKeyMapper) {
            addAssignKeyMapperIfNotValid()
        }

        addTextSegment("return")
        addSpace()
        if (useNewKeyword) {
            addTextSegment("new")
            addSpace()
        }
        addTextSegment(className)
        withParentheses {
            addNewLine()
            variables.forEach { param ->
                addTextSegment(param.publicVariableName)
                addTextSegment(":")
                addSpace()

                val keyAccess = StringBuilder().apply {
                    append(TemplateConstants.MAP_VARIABLE_NAME)
                    append("[")
                    if (addKeyMapper) {
                        append("${TemplateConstants.KEYMAPPER_VARIABLE_NAME}('${param.mapKeyString}')")
                    } else {
                        append("'${param.mapKeyString}'")
                    }
                    append("]")
                }.toString()

                val rawType = param.type.trim()
                val isNullable = param.isNullable || rawType.endsWith("?")
                val cleanType = rawType.removeSuffix("?")

                when (cleanType) {
                    "String" -> {
                        if (isNullable) {
                            addTextSegment("$keyAccess as String?")
                        } else {
                            addTextSegment("$keyAccess as String? ?? ''")
                        }
                    }
                    "int" -> {
                        if (isNullable) {
                            addTextSegment("($keyAccess as num?)?.toInt()")
                        } else {
                            addTextSegment("($keyAccess as num?)?.toInt() ?? 0")
                        }
                    }
                    "double" -> {
                        if (isNullable) {
                            addTextSegment("($keyAccess as num?)?.toDouble()")
                        } else {
                            addTextSegment("($keyAccess as num?)?.toDouble() ?? 0.0")
                        }
                    }
                    "num" -> {
                        if (isNullable) {
                            addTextSegment("$keyAccess as num?")
                        } else {
                            addTextSegment("$keyAccess as num? ?? 0")
                        }
                    }
                    "bool" -> {
                        if (isNullable) {
                            addTextSegment("$keyAccess as bool?")
                        } else {
                            addTextSegment("$keyAccess as bool? ?? false")
                        }
                    }

                    // Cambio para eliminar exigencia de CloudFirestore
                    "DateTime" -> {
                        if (isNullable) {
                            addTextSegment("$keyAccess != null ? ($keyAccess is DateTime ? $keyAccess as DateTime : ($keyAccess.runtimeType.toString() == 'Timestamp' ? ($keyAccess as dynamic).toDate() as DateTime : DateTime.parse($keyAccess.toString()))) : null")
                        } else {
                            addTextSegment("$keyAccess != null ? ($keyAccess is DateTime ? $keyAccess as DateTime : ($keyAccess.runtimeType.toString() == 'Timestamp' ? ($keyAccess as dynamic).toDate() as DateTime : DateTime.parse($keyAccess.toString()))) : DateTime.now()")
                        }
                    }
                    else -> {
                        if (noImplicitCasts) {
                            if (isNullable) {
                                addTextSegment("$keyAccess as $cleanType?")
                            } else {
                                addTextSegment("$keyAccess as $cleanType")
                            }
                        } else {
                            addTextSegment(keyAccess)
                        }
                    }
                }

                addComma()
                addNewLine()
            }
        }
        addSemicolon()
    }
}
