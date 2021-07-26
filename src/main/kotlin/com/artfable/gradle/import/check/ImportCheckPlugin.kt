@file:Suppress("JAVA_MODULE_DOES_NOT_EXPORT_PACKAGE")
package com.artfable.gradle.import.check

import com.sun.tools.javac.api.JavacTool
import com.sun.tools.javac.file.JavacFileManager
import com.sun.tools.javac.tree.JCTree
import com.sun.tools.javac.util.Context
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.nio.charset.Charset
import java.util.*
import kotlin.collections.HashSet

class ImportCheckPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        val config = project.extensions.create("importCheck", ImportCheckExtensions::class.java)

        project.tasks.create("importCheck") {
            if (project.tasks.findByName("compileJava") != null) {
                this.shouldRunAfter("compileJava")
            }

            this.doFirst {
                var violated = false

                config.groups.forEach { group ->
                    if (group.source.isNullOrBlank()) {
                        throw IllegalArgumentException("checkImports task wasn't configured")
                    }

                    val context = Context()
                    val javacFileManager = JavacFileManager(context, true, Charset.forName("UTF-8"))
                    val javacTool = JavacTool.create()

                    val regex = group.patterns.map(::Regex)

                    val javacTask = javacTool
                        .getTask(null, javacFileManager, null, null, null, javacFileManager.getJavaFileObjects(*FileFinder(group.source!!)
                            .findFiles()))

                    javacTask.parse().forEach { codeTree ->
                        val violatedImports = mutableListOf<String>()
                        codeTree.imports.forEach { imp ->
                            if (regex.any(imp.qualifiedIdentifier.toString()::matches)) {
                                violatedImports.add(imp.toString())
                            }
                        }

                        if (violatedImports.isNotEmpty()) {
                            violated = true
                            println("${codeTree.packageName}.${(codeTree.typeDecls[0] as JCTree.JCClassDecl).name}".color(if (group.warning) Colour.YELLOW else Colour.RED))
                            violatedImports.forEach(::println)
                        }
                    }

                    violated = violated && !group.warning
                }

                if (violated && config.failBuild) {
                    throw IllegalArgumentException("Found restricted imports")
                }
            }
        }
    }
}

open class ImportCheckExtensions {
    val groups: MutableList<ImportCheckGroup> = LinkedList()
    var failBuild = true

    fun group(action: Action<ImportCheckGroup>) {
        groups.add(ImportCheckGroup().apply(action::execute))
    }
}

open class ImportCheckGroup {
    var warning = false
    var source: String? = null
    var patterns: Set<String> = HashSet()
}