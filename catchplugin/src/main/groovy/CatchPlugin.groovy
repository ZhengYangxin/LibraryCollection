import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.pipeline.TransformManager
import org.gradle.api.Plugin
import org.gradle.api.Project

class CatchPlugin extends Transform implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.logger.debug "================自定义插件成功！=========="

        def android = project.extensions.getByType(AppExtension)
        android.registerTransform(this)
    }

    @Override
    String getName() {
        return "CatchPlugin"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return null
    }

    @Override
    boolean isIncremental() {
        return false
    }
}