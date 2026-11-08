package com.nyk.action

import com.nyk.action.init.ActionData
import com.nyk.action.data.GenerationData
import com.nyk.action.data.PerformAction
import com.nyk.action.utils.createMapDeleteCall
import com.nyk.action.utils.selectFieldsWithDialog
import com.nyk.configuration.ConfigurationDataManager
import com.nyk.declaration.fullTypeName
import com.nyk.declaration.isNullable
import com.nyk.declaration.variableName
import com.nyk.ext.psi.extractClassName
import com.nyk.templater.AliasedVariableTemplateParam
import com.nyk.templater.AliasedVariableTemplateParamImpl
import com.nyk.templater.MapTemplateParams
import com.nyk.templater.createMapTemplate
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.jetbrains.lang.dart.psi.DartClassDefinition

class MapAction : BaseAnAction() {

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

            val template = createMapTemplate(
                templateManager,
                MapTemplateParams(
                    className = dartClassName,
                    variables = variableNames,
                    useNewKeyword = configuration.useNewKeyword,
                    addKeyMapper = configuration.addKeyMapperForMap,
                    noImplicitCasts = configuration.noImplicitCasts
                )
            )

            val deleteCall = createMapDeleteCall(dartClass)

            return PerformAction(
                deleteCall,
                template
            )
        }

    }
}