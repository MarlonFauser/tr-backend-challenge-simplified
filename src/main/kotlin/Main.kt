import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import handler.InstrumentEventHandler
import handler.QuoteEventHandler
import org.apache.log4j.BasicConfigurator

fun main() {
    println("starting up")
    BasicConfigurator.configure();

    val server = Server()

    InstrumentStream().connect { event -> InstrumentEventHandler.handle(event) }
    QuoteStream().connect { event -> QuoteEventHandler.handle(event) }

    server.start()
}

val jackson: ObjectMapper =
    jacksonObjectMapper()
        .registerModule(JavaTimeModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
