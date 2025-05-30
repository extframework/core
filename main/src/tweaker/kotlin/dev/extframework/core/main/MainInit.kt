package dev.extframework.core.main

import com.durganmcbroom.jobs.Job
import com.durganmcbroom.jobs.job
import com.durganmcbroom.jobs.mapException
import com.durganmcbroom.jobs.result
import dev.extframework.boot.archive.ArchiveGraph
import dev.extframework.tooling.api.exception.StructuredException
import dev.extframework.tooling.api.extension.ExtensionNode
import dev.extframework.tooling.api.extension.ExtensionResolver
import dev.extframework.tooling.api.extension.artifact.ExtensionDescriptor
import dev.extframework.tooling.api.extension.descriptor
import dev.extframework.tooling.api.extension.partition.ExtensionPartitionContainer
import dev.extframework.tooling.api.extension.partition.artifact.PartitionArtifactRequest
import dev.extframework.tooling.api.extension.partition.artifact.partition
import dev.extframework.tooling.api.uber.*

public class MainInit(
//    public val delegate: ExtensionInitializer?,
    private val extResolver: ExtensionResolver,
    private val graph: ArchiveGraph,
    private val environment: String
) {
    private val initialized = ArrayList<ExtensionDescriptor>()
    // A map of our subsystems. Any or Object is included additionally as a reference to the default.

    public fun init(nodes: List<ExtensionNode>): Job<Unit> = job {
        val mainDescriptor = UberDescriptor("Main partitions")
        val request = UberArtifactRequest(
            mainDescriptor,
            nodes
                .filter { it.runtimeModel.namedPartitions.contains("main") }
                .map { node ->
                    val request = PartitionArtifactRequest(node.descriptor, "main", environment)

                    UberParentRequest(
                        request,
                        extResolver.accessBridge.repositoryFor(node.descriptor),
                        extResolver.partitionResolver,
                    )
                }
        )

        graph.cache(
            request,
            UberRepositorySettings,
            UberResolver
        )().merge()

        graph.get(
            mainDescriptor,
            UberResolver
        )().merge()

        runMainInit(nodes)
    }

    private fun runMainInit(nodes: List<ExtensionNode>) {
        nodes
            .filter { initialized.add(it.descriptor) }
            .forEach { node ->
                // Run init on main partitions
                result {
                    val mainPartition = graph.getNode(
                        node.descriptor.partition(
                            "main",
                            environment
                        )
                    ) as? ExtensionPartitionContainer<MainPartitionNode, MainPartitionMetadata>

                    mainPartition?.node?.entrypoint?.init()
                }.mapException {
                    StructuredException(
                        ExtensionInitialization,
                        cause = it,
                        message = "Exception initializing extension"
                    ) {
                        node.runtimeModel.descriptor.name asContext "Extension name"
                    }
                }.getOrThrow()

            }
    }
}