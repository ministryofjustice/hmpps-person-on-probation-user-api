package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.config

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class LoggingWebFilter : WebFilter {

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }
  override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
    if (log.isDebugEnabled) {
      log.debug("[{}] Request received for path [{}]", exchange.request.id, exchange.request.uri)
    }
    val clientSessionId = exchange.request.headers.getFirst("SessionID")
    if (clientSessionId != null) {
      log.info("SessionID [{}]. [{}] Request received for path [{}]", clientSessionId, exchange.request.id, exchange.request.uri)
    }
    return chain.filter(exchange)
  }
}
