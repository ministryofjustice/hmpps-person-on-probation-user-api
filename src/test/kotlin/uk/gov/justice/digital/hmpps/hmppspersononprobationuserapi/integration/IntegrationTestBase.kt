package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.integration

import com.google.common.io.Resources
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.SqlMergeMode
import org.springframework.test.web.reactive.server.WebTestClient
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.helpers.JwtAuthHelper
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.helpers.TestBase

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
abstract class IntegrationTestBase : TestBase() {

  @Autowired
  lateinit var webTestClient: WebTestClient

  @Autowired
  protected lateinit var jwtAuthHelper: JwtAuthHelper

  init {
    // Resolves an issue where Wiremock keeps previous sockets open from other tests causing connection resets
    System.setProperty("http.keepAlive", "false")
  }

  protected fun setAuthorisation(
    user: String = "PERSONONPROBATION_ADM",
    roles: List<String> = listOf(),
    scopes: List<String> = listOf(),
    authSource: String = "none",
  ): (HttpHeaders) -> Unit = jwtAuthHelper.setAuthorisation(user, roles, scopes, authSource)
}

fun readFile(file: String): String = Resources.getResource(file).readText()
