package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.integration

import io.mockk.every
import io.mockk.mockkStatic
import io.opentelemetry.sdk.testing.assertj.OpenTelemetryAssertions.assertThat
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.test.context.jdbc.Sql
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.entity.DelegatedUserOTPEntity
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.service.randomAlphaNumericString
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class DelegatedUserOTPIntegrationTest : IntegrationTestBase() {

  private val fakeNow = LocalDateTime.parse("2024-02-12T14:33:26.520566")

  @Test
  @Sql("classpath:testdata/sql/clear-all-data.sql")
  @Sql("classpath:testdata/sql/seed-user.sql")
  fun `Create, Get, Delete OTP for Delegated User - Happy path `() {
    mockkStatic(LocalDateTime::class)
    every { LocalDateTime.now() } returns fakeNow

    mockkStatic(::randomAlphaNumericString)
    every {
      randomAlphaNumericString()
    } returns "1X3456"

    val crn = "abc"
    val userid = 1
    val expectedOutput = "[]"
    var expectedOutput1 = readFile("testdata/expectation/userotp-1.json")
    var expectedOutput2 = readFile("testdata/expectation/userotp-2.json")
    webTestClient.get()
      .uri("/person-on-probation-user/access/otp/$userid?email=user1@gmail.com")
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isNotFound

    webTestClient.post()
      .uri("/person-on-probation-user/access/otp")
      .bodyValue(
        mapOf(
          "userid" to 1,
          "email" to "user1@gmail.com",
        ),
      )
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType("application/json")
      .expectBody()
      .json(expectedOutput2)

    val getResponse = webTestClient.get()
      .uri("/person-on-probation-user/access/otp/$userid")
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .json("[ $expectedOutput2 ]")

    webTestClient.delete()
      .uri("/person-on-probation-user/expire/otp/1")
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .json(expectedOutput1)

    webTestClient.get()
      .uri("/person-on-probation-user/access/otp/$userid")
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isNotFound
  }

  private fun verifyOtpResponse(response: DelegatedUserOTPEntity) {
    val now = LocalDateTime.now()
    val inYearAtMidnight = now.plusDays(30).withHour(23).withMinute(59).withSecond(59)
    assertThat(response.id).isGreaterThanOrEqualTo(1)
    assertThat(response.otp).hasSize(6)
    assertThat(response.creationDate).isCloseTo(now, Assertions.within(10, ChronoUnit.SECONDS))
    assertThat(response.expiryDate).isCloseTo(inYearAtMidnight, Assertions.within(10, ChronoUnit.SECONDS))
  }

  @Test
  fun `Get OTP for Delegated User Id- NotFound`() {
    val userid = 1
    webTestClient.get()
      .uri("/person-on-probation-user/access/otp/$userid")
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isNotFound
  }

  @Test
  fun `Get OTP for Delegated User Id and Email - NotFound`() {
    val userid = 1
    webTestClient.get()
      .uri("/person-on-probation-user/access/otp/$userid?email=user2@gmail.com")
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isNotFound
  }

  @Test
  fun `Get OTP for Delegated User - Unauthorized`() {
    val userid = 1

    webTestClient.get()
      .uri("/person-on-probation-user/access/otp/$userid")
      .exchange()
      .expectStatus().isUnauthorized
  }

  @Test
  fun `Get OTP for Delegated User- Forbidden`() {
    val crn = "abc"
    val userid = 1

    webTestClient.get()
      .uri("/person-on-probation-user/access/otp/$userid")
      .headers(setAuthorisation())
      .exchange()
      .expectStatus().isForbidden
  }

  @Test
  fun `Delete OTP for Delegated  User by id  - NotFound`() {
    webTestClient.delete()
      .uri("/person-on-probation-user/expire/otp/2")
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isNotFound
  }

  @Test
  fun `Delete OTP for Delegated User by id - Unauthorized`() {
    val userid = 1
    webTestClient.delete()
      .uri("/person-on-probation-user/expire/otp/1")
      .exchange()
      .expectStatus().isUnauthorized
  }
}
