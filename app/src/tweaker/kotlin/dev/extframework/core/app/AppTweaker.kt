package dev.extframework.core.app

import dev.extframework.core.app.api.ApplicationTarget
import dev.extframework.core.app.internal.internalExtraAppConfigAttrKey
import dev.extframework.tooling.api.ExtensionLoader
import dev.extframework.tooling.api.environment.*
import dev.extframework.tooling.api.tweaker.EnvironmentTweaker

public class AppTweaker : EnvironmentTweaker {
    override fun tweak(
        environment: ExtensionEnvironment
    ) {
        // Target linker/resolver
        val linker = TargetLinker()

        linker.target = environment[ApplicationTarget]
        environment += linker

        val targetLinkerResolver = TargetLinkerResolver(linker)
        environment += targetLinkerResolver
        environment[ExtensionLoader].graph.resolvers.register(targetLinkerResolver)

        environment.find(internalExtraAppConfigAttrKey)?.value?.invoke(environment)
    }
}