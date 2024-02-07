package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.config

import org.slf4j.LoggerFactory
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait

object PostgresContainer {
  val instance: PostgreSQLContainer<Nothing>? by lazy { startPostgresqlContainer() }

  private fun startPostgresqlContainer(): PostgreSQLContainer<Nothing>? {
    log.info("Creating a Postgres database")
    return PostgreSQLContainer<Nothing>("postgres").apply {
      withEnv("HOSTNAME_EXTERNAL", "localhost")
      withDatabaseName("person-on-probation-user")
      withUsername("person-on-probation-user")
      withPassword("person-on-probation-user")
      setWaitStrategy(Wait.forListeningPort())
      withReuse(true)

      start()
    }
  }

  private val log = LoggerFactory.getLogger(this::class.java)
}
