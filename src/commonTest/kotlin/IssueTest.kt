import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.elementDescriptors
import kotlinx.serialization.serializer
import kotlin.reflect.typeOf
import kotlin.test.Test
import kotlin.test.assertTrue

class IssueTest {
    /** This fails with jvm/1.8.20 */
    @Test
    fun test_issue() {
        assertTrue(listOfNullable<String>().descriptor.elementDescriptors.first().isNullable)
    }

    /** This works with all the combinations mentioned, i.e., js/jvm/1.8.20 and js/jvm/1.8.10 */
    @Test
    fun test_workaround() {
        assertTrue(workaround<String>().descriptor.elementDescriptors.first().isNullable)
    }
}

inline fun <reified T> listOfNullable(): KSerializer<List<Any?>> = serializer<List<T?>>() as KSerializer<List<Any?>>
inline fun <reified T> workaround(): KSerializer<List<Any?>> = serializer(typeOf<List<T?>>()) as KSerializer<List<Any?>>
