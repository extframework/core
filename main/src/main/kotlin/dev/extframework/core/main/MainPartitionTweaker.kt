package dev.extframework.core.main

import com.durganmcbroom.jobs.Job
import com.durganmcbroom.jobs.job
import dev.extframework.tooling.api.environment.ExtensionEnvironment
import dev.extframework.tooling.api.environment.archiveGraph
import dev.extframework.tooling.api.environment.extract
import dev.extframework.tooling.api.environment.getOrNull
import dev.extframework.tooling.api.environment.partitionLoadersAttrKey
import dev.extframework.tooling.api.extension.ExtensionInitializer
import dev.extframework.tooling.api.extension.ExtensionPreInitializer
import dev.extframework.tooling.api.extension.ExtensionResolver
import dev.extframework.tooling.api.tweaker.EnvironmentTweaker

public class MainPartitionTweaker : EnvironmentTweaker {
    override fun tweak(
        environment: ExtensionEnvironment
    ): Job<Unit> = job {
        // Minecraft partition
        val partitionContainer = environment[partitionLoadersAttrKey].extract().container
        partitionContainer.register("main", MainPartitionLoader())

        environment += MainPreInit(
            environment.archiveGraph,
            environment[ExtensionResolver].extract(),
            environment[ExtensionPreInitializer].getOrNull()
        )

        // Extension init
        environment += MainInit(
            environment[ExtensionInitializer].getOrNull()

//            environment[mixinAgentsAttrKey].extract().filterIsInstance<MixinSubsystem>(), linker
        )
    }
}