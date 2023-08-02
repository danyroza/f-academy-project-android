package app.futured.academyproject.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NetworkClient {

    suspend fun send(): Int {
        delay(300)
        return 0
    }

    suspend fun sendAndReturnError() {
        delay(300)
        throw Exception("Vyhazuju chybu!")
    }
}
