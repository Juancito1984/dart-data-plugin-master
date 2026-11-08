package com.nyk.action

import com.nyk.action.data.GenerationData
import com.nyk.action.data.PerformAction
import com.nyk.action.init.ActionData
import com.nyk.action.utils.createCopyWithDeleteCall
import com.nyk.action.utils.selectFieldsWithDialog
import com.nyk.configuration.ConfigurationDataManager
import com.nyk.declaration.allMembersFinal
import com.nyk.declaration.fullTypeName
import com.nyk.declaration.isNullable
import com.nyk.declaration.variableName
import com.nyk.ext.psi.extractClassName
import com.nyk.templater.AliasedVariableTemplateParam
import com.nyk.templater.AliasedVariableTemplateParamImpl
import com.nyk.templater.CopyWithTemplateParams
import com.nyk.templater.createCopyWithConstructorTemplate
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.jetbrains.lang.dart.psi.DartClassDefinition

class DartCopyWithAction : BaseAnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun processAction(
        event: AnActionEvent,
        actionData: ActionData,
        dartClass: DartClassDefinition
    ): PerformAction? {
        val declarations = selectFieldsWithDialog(actionData.project, dartClass) ?: return null

        return Companion.processAction(
            GenerationData(actionData, dartClass, declarations)
        )
    }

    companion object : StaticActionProcessor {

        override fun processAction(generationData: GenerationData): PerformAction {
            val (actionData, dartClass, declarations) = generationData

            val (project, _, _, _) = actionData

            val variableNames: List<AliasedVariableTemplateParam> = declarations
                .map {
                    AliasedVariableTemplateParamImpl(
                        variableName = it.variableName,
                        type = it.fullTypeName
                            ?: throw RuntimeException("No type is available - this variable should not be assignable from constructor"),
                        publicVariableName = it.publicVariableName,
                        isNullable = it.isNullable
                    )
                }

            val templateManager = TemplateManager.getInstance(project)
            val configuration = ConfigurationDataManager.retrieveData(project)
            val dartClassName = dartClass.extractClassName()
            val generateOptimizedCopy = configuration.optimizeConstCopy && declarations.allMembersFinal()

            val template = createCopyWithConstructorTemplate(
                templateManager,
                CopyWithTemplateParams(
                    className = dartClassName,
                    variables = variableNames,
                    copyWithMethodName = configuration.copyWithMethodName,
                    useNewKeyword = configuration.useNewKeyword,
                    generateOptimizedCopy = generateOptimizedCopy,
                    nullSafety = configuration.nullSafety
                )
            )

            val copyWithDeleteCall = createCopyWithDeleteCall(
                dartClass,
                configuration.copyWithMethodName
            )

            return PerformAction(
                copyWithDeleteCall,
                template
            )
        }

    }
}