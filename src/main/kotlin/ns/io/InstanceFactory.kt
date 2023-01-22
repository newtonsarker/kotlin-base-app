package ns.io

import java.util.concurrent.ConcurrentHashMap

@Suppress("UNCHECKED_CAST")
object InstanceFactory {

    private val instances = ConcurrentHashMap<String, Any>()

    fun <T: Any> get(clazz: Class<T>): T {
        return instances[clazz.canonicalName] as T
    }

    fun <T: Any> create(clazz: Class<T>, instance: T): T {
        return instances.computeIfAbsent(clazz.canonicalName) {
            instance
        } as T
    }

}
