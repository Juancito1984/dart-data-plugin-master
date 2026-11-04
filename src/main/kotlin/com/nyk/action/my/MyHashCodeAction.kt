package com.nyk.action.my

import com.nyk.action.StaticActionProcessor
import com.nyk.action.data.GenerationData
import com.nyk.action.data.PerformAction
import com.nyk.declaration.isNullable
import com.nyk.declaration.variableName
import com.nyk.ext.psi.findChildrenByType
import com.nyk.templater.HashCodeTemplateParams
import com.nyk.templater.NamedVariableTemplateParamImpl
import com.nyk.templater.TemplateConstants
import com.nyk.templater.createHashCodeTemplate
import com.intellij.codeInsight.template.TemplateManager
import com.jetbrains.lang.dart.psi.DartClassDefinition
import com.jetbrains.lang.dart.psi.DartGetterDeclaration

class MyHashCodeAction {

    companion object : StaticActionProcessor {

        private fun createDeleteCall(dartClass: DartClassDefinition): (() -> Unit)? {
            val hashCode = dartClass.findChildrenByType<DartGetterDeclaration>()
                .filter { it.name == TemplateConstants.HASHCODE_NAME }
                .firstOrNull()
                ?: return null

            return { hashCode.delete() }
        }

        override fun processAction(generationData: GenerationData): PerformAction? {
            val (actionData, dartClass, declarations) = generationData

            val project = actionData.project

            val templateManager = TemplateManager.getInstance(project)

            val template = createHashCodeTemplate(
                templateManager = templateManager,
                params = HashCodeTemplateParams(
                    declarations.map {
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
