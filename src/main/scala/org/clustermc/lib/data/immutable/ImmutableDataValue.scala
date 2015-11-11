package org.clustermc.lib.data.immutable

import org.clustermc.lib.data.DataValueImpl
import org.clustermc.lib.utils.GenericOps

class ImmutableDataValue[T](private[this] val value: Option[T], override val innerClass: Class[T])
    extends DataValueImpl[T](value, innerClass)

object ImmutableDataValue {
    import GenericOps.option

    import scala.reflect.ClassTag

    def apply[T: ClassTag](value: T)(implicit clazz: Class[T]) = {
        new ImmutableDataValue(option(value), clazz)
    }
}