package andrasferenczi.templater

import andrasferenczi.ext.*
import com.intellij.codeInsight.template.Template
import com.intellij.codeInsight.template.TemplateManager

data class ToStringTemplateParams(
    val className: String,
    val variables: List<NamedVariableTemplateParam>
)

fun createToStringTemplate(
    templateManager: TemplateManager,
    params: ToStringTemplateParams
): Template {
    val (className, variables) = params

    return templateManager.createTemplate(
        TemplateType.ToString.templateKey,
        TemplateConstants.DART_TEMPLATE_GROUP
    ).apply {
        isToReformat = true

        addTextSegment("@override")
        addNewLine()
        addTextSegment("String")
        addSpace()
        addTextSegment(TemplateConstants.TO_STRING_METHOD_NAME)
        withParentheses { }
        addSpace()
        withCurlyBraces {
            addNewLine()
            addTextSegment("return")
            addSpace()

            val varsString = variables.joinToString(", ") { "${it.variableName}: \$${it.variableName}" }
            addTextSegment("'$className{$varsString}'")
            addSemicolon()
        }

        addSpace()
    }
}
