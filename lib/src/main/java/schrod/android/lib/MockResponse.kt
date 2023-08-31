package schrod.android.lib

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
annotation class MockResponse(val jsonFileName: String)
