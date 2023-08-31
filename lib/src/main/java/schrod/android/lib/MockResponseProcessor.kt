package schrod.android.lib

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*

class MockResponseProcessor(
    private val codeGenerator: CodeGenerator,
    val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(MockResponse::class.qualifiedName!!).toList()

        symbols.forEach { symbol ->
            if (symbol is KSFunctionDeclaration) {
                val jsonFileName: String? = symbol.annotations
                    .firstOrNull { it.shortName.asString() == "MockResponse" }
                    ?.arguments
                        ?.firstOrNull { it.name?.asString() == "jsonFileName" }
                        ?.value as? String

                jsonFileName?.let { generateMockCode(symbol, it) }
            }
        }

        return emptyList()
    }

    private fun generateMockCode(symbol: KSFunctionDeclaration, jsonFileName: String) {
        val packageName = symbol.containingFile?.packageName?.asString() ?: return
        val className = symbol.containingFile?.fileName?.substringBefore(".kt") ?: return

        val file = codeGenerator.createNewFile(Dependencies(true), packageName, className + "Mock")
        
        file.writer().use { writer ->
            writer.append("package $packageName\n\n")
            writer.append("fun ${symbol.simpleName.asString()}Mock(): String {\n")
            writer.append("    // This is just a stub for demonstration. Implement logic to read $jsonFileName and return its content.")
            writer.append("    return \"{}\"\n")  // Default empty JSON object. Replace this with actual file read logic.
            writer.append("}\n")
        }
    }
}
