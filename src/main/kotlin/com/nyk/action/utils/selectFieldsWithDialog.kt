package com.nyk.action.utils

import com.nyk.declaration.DeclarationExtractor
import com.nyk.declaration.VariableDeclaration
import com.nyk.dialog.GenerateDialog
import com.intellij.openapi.project.Project
import com.jetbrains.lang.dart.psi.DartClassDefinition

/**
 * Returns null if user decided to cancel the operation
 */
fun selectFieldsWithDialog(
    project: Project,
    dartClass: DartClassDefinition
): List<VariableDeclaration>? {

    val declarations = DeclarationExtractor.extractDeclarationsFromClass(dartClass)

    val dialog = GenerateDialog(
        project,
        declarations
    )
    dialog.show()

    if (!dialog.isOK) {
        return null
    }

    return dialog.getSelectedFields()
}