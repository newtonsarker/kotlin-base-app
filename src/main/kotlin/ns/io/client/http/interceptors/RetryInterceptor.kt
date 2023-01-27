package ns.io.client.http.interceptors

import java.io.IOException
import okhttp3.Interceptor
import okhttp3.Response

class RetryInterceptor(private val maxRetries: Int) : Interceptor {

    private var retryCount = 0

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        var response = try {
            chain.proceed(request)
        } catch (exception: IOException) {
            if (++retryCount <= maxRetries) {
                intercept(chain)
            } else {
                throw exception
            }
        }

        if (!response.isSuccessful && retryCount <= maxRetries) {
            retryCount++
            response = intercept(chain)
        }
        retryCount = 0
        return response
    }

}