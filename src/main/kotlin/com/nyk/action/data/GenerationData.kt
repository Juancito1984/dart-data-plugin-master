package com.nyk.action.data

import com.nyk.action.init.ActionData
import com.nyk.declaration.VariableDeclaration
import com.jetbrains.lang.dart.psi.DartClassDefinition

data class GenerationData(
    val actionData: ActionData,
    val dartClass: DartClassDefinition,
    val declarations: List<VariableDeclaration>
)