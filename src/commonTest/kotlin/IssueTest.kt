import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass
import kotlin.test.Test

class IssueTest {
    @Test
    fun test() {
        val column = Column(String::class, listOf<String?>(null))
        Json.encodeToString(column)
    }
}

object ColumnSerializer : KSerializer<Column> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Packet") {
        element("dataType", serialDescriptor<String>())
        element("payload", buildClassSerialDescriptor("payload"))
    }

    override fun serialize(encoder: Encoder, value: Column) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.kClass.simpleName ?: error("?"))
            encodeSerializableElement(descriptor, 1, value.kClass.listSerializer, value.payload)
        }
    }

    override fun deserialize(decoder: Decoder): Column = decoder.decodeStructure(descriptor) {
        TODO()

    }
}

@Serializable(with = ColumnSerializer::class)
data class Column(val kClass: KClass<*>, val payload: List<Any?>)


private val serializers: Map<KClass<*>, KSerializer<List<Any?>>> = mapOf(
    pair<String>(),
    pair<Int>(),
    pair<Double>(),
    pair<Boolean>(),
)


@Suppress("UNCHECKED_CAST")
inline fun <reified T> pair(): Pair<KClass<*>, KSerializer<List<Any?>>> =
    T::class to serializer<List<T?>>() as KSerializer<List<Any?>>


val KClass<*>.listSerializer: KSerializer<List<Any?>>
    get() = serializers[this]
        ?: throw SerializationException("No registered serializer for class `${this.simpleName}`. Search for ${::serializers.name} and add it to the list.")
