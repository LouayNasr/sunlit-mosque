import com.example.sunlitmosques.models.Mosque
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class JsonParser {

    private val json = Json { ignoreUnknownKeys = true }

    fun parseJsonToMosqueList(jsonString: String): List<Mosque> {
        val jsonArray = json.parseToJsonElement(jsonString).jsonArray
        return jsonArray.map { parseJsonToMosque(it.jsonObject) }
    }

    fun parseJsonToMosque(jsonObject: JsonObject): Mosque {
        val accountNumber = jsonObject["account_number"]?.jsonPrimitive?.content.orEmpty()
        val minName = jsonObject["Min Name"]?.jsonPrimitive?.content.orEmpty()
        val totalOutstanding =
            jsonObject[" total_outstanding "]?.jsonPrimitive?.content?.toDoubleOrNull() ?: 0.0

        val monthlyConsumption = jsonObject
            .filterKeys { it.matches(Regex("\\d{2}-\\w+")) } // Ensure keys match pattern "dd-..."
            .mapValues { it.value.jsonPrimitive.content.toDoubleOrNull() ?: 0.0 }

        return Mosque(
            accountNumber = accountNumber,
            minName = minName,
            totalOutstanding = totalOutstanding,
            monthlyConsumption = monthlyConsumption
        )
    }
}