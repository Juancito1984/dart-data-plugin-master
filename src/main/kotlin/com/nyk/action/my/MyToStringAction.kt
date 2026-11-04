package com.nyk.action.my

import com.nyk.action.StaticActionProcessor
import com.nyk.action.data.GenerationData
import com.nyk.action.data.PerformAction
import com.nyk.declaration.isNullable
import com.nyk.declaration.variableName
import com.nyk.ext.psi.extractClassName
import com.nyk.ext.psi.findMethodsByName
import com.nyk.templater.NamedVariableTemplateParamImpl
import com.nyk.templater.TemplateConstants
import com.nyk.templater.ToStringTemplateParams
import com.nyk.templater.createToStringTemplate
import com.intellij.codeInsight.template.TemplateManager
import com.jetbrains.lang.dart.psi.DartClassDefinition

class MyToStringAction {
    companion object : StaticActionProcessor {

        private fun createDeleteCall(dartClass: DartClassDefinition): (() -> Unit)? {
            val toString = dartClass.findMethodsByName(TemplateConstants.TO_STRING_METHOD_NAME)
                .firstOrNull()
                ?: return null

            return { toString.delete() }
        }

        override fun processAction(generationData: GenerationData): PerformAction? {
            val (actionData, dartClass, declarations) = generationData

            val project = actionData.project

            val templateManager = TemplateManager.getInstance(project)
            val dartClassName = dartClass.extractClassName()

            val template = createToStringTemplate(
                templateManager = templateManager,
                params = ToStringTemplateParams(
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
