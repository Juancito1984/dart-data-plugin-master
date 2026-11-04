package com.nyk.action.my

import com.nyk.action.StaticActionProcessor
import com.nyk.action.data.GenerationData
import com.nyk.action.data.PerformAction
import com.nyk.declaration.isNullable
import com.nyk.declaration.variableName
import com.nyk.ext.psi.extractClassName
import com.nyk.ext.psi.findMethodsByName
import com.nyk.templater.EqualsTemplateParams
import com.nyk.templater.NamedVariableTemplateParamImpl
import com.nyk.templater.TemplateConstants
import com.nyk.templater.createEqualsTemplate
import com.intellij.codeInsight.template.TemplateManager
import com.jetbrains.lang.dart.psi.DartClassDefinition

class MyEqualsAction {

    companion object : StaticActionProcessor {

        private fun createDeleteCall(dartClass: DartClassDefinition): (() -> Unit)? {
            val equals = dartClass.findMethodsByName(TemplateConstants.EQUALS_OPERATOR_METHOD_NAME)
                .firstOrNull()
                ?: return null

            return { equals.delete() }
        }

        override fun processAction(generationData: GenerationData): PerformAction? {
            val (actionData, dartClass, declarations) = generationData

            val project = actionData.project

            val templateManager = TemplateManager.getInstance(project)
            val dartClassName = dartClass.extractClassName()

            val template = createEqualsTemplate(
                templateManager,
                EqualsTemplateParams(
                    className = dartClassName,
                    variables = declarations.map {
                        NamedVariableTemplateParamImpl(
                            it.variableName,
                            isNullable = it.isNullable
                        )
                    }
                )
            )

            return PerformAction(
                createDeleteCall(dartClass),
                template
            )
        }
    }

}