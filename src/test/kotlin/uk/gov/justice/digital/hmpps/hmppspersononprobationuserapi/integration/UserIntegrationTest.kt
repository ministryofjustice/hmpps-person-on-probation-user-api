package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.integration

import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.Test
import org.springframework.test.context.jdbc.Sql
import java.time.LocalDateTime

class UserIntegrationTest : IntegrationTestBase() {

  private val fakeNow = LocalDateTime.parse("2024-02-12T14:33:26.520566")

  @Test
  @Sql("classpath:testdata/sql/seed-user.sql")
  fun `Get All Person on Probation Users - happy path`() {
    mockkStatic(LocalDateTime::class)
    every { LocalDateTime.now() } returns fakeNow

    val expectedOutput = readFile("testdata/expectation/usersList.json")

    webTestClient.get()
      .uri("/person-on-probation-user/users/all")
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .json(expectedOutput)
  }

  fun `Get a Person on Probation User by CRN Id - happy path`() {
    mockkStatic(LocalDateTime::class)
    every { LocalDateTime.now() } returns fakeNow

    val expectedOutput = readFile("testdata/expectation/user.json")
    val crn = "abc"

    webTestClient.get()
      .uri("/person-on-probation-user/user/$crn")
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .json(expectedOutput)
  }

  fun `Get a Person on Probation User by CRN Id - Unauthorized`() {
    mockkStatic(LocalDateTime::class)
    every { LocalDateTime.now() } returns fakeNow

    val expectedOutput = readFile("testdata/expectation/user.json")
    val crn = "abc"

    webTestClient.get()
      .uri("/person-on-probation-user/user/$crn")
      .headers(setAuthorisation(roles = listOf("ROLE_NOT_VALID")))
      .exchange()
      .expectStatus().isUnauthorized
  }
}
