package app.futured.academyproject

import app.futured.academyproject.data.NetworkClient
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class Task2Test {

    private lateinit var networkClient: NetworkClient

    @Before
    fun setup() {
        networkClient = NetworkClient()
    }

    @Test
    fun sendReturnsZero() = runTest {
        assert(networkClient.send() == 0)
    }

    @Test
    fun sendAndReturnErrorThrowsError() = runTest {
        var threwError = false
        try {
            networkClient.sendAndReturnError()
        } catch (e : Exception) {
            threwError = true
        }

        assert(threwError)
    }
}
