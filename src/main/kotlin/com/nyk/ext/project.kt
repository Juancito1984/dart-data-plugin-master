import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project

const val WRITE_ACTION_NAME = "com.nyk.writeaction"

/**
 * groupId: for merging multiple changes into one
 */
fun Project.runWriteAction(
    @Suppress("UNUSED_PARAMETER") groupId: Any? = null,
    action: () -> Unit
) {
    WriteCommandAction.runWriteCommandAction(this) {
        action()
    }
}

