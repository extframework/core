package dev.extframework.core.capability

public inline fun <reified T : Capability> Capabilities.defining(
    // Nothing
): Capabilities.DefinitionDelegate<T> = definitionDelegate(T::class)