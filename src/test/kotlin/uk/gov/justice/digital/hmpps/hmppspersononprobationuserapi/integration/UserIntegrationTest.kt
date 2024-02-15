package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.integration

import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.Test
import org.springframework.test.context.jdbc.Sql
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.data.UserPatch
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.data.UserPost
import java.time.LocalDateTime

class UserIntegrationTest : IntegrationTestBase() {

  private val fakeNow = LocalDateTime.parse("2024-02-12T14:33:26.520566")

  @Test
  @Sql("classpath:testdata/sql/seed-user.sql")
  fun `Get All Person on Probation Users - happy path`() {
    val expectedOutput = readFile("testdata/expectation/usersList.json")

    webTestClient.get()
      .uri("/person-on-probation-user/users/all")
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .json(expectedOutput)
  }

  @Test
  fun `Get a Person on Probation User by Id - happy path`() {
    val expectedOutput = readFile("testdata/expectation/user.json")
    val crn = "abc"
    val id = 1

    webTestClient.get()
      .uri("/person-on-probation-user/$crn/user/$id")
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .json(expectedOutput)
  }

  @Test
  fun `Get a Person on Probation User by Id - Unauthorized`() {
    val crn = "abc"
    val id = 1

    webTestClient.get()
      .uri("/person-on-probation-user/$crn/user/$id")
      .exchange()
      .expectStatus().isUnauthorized
  }

  @Test
  fun `Create Person on Probation User with duplicate email `() {
    var crn = "abc"
    val id = 1
    val expectedOutput = readFile("testdata/expectation/user.json")
    mockkStatic(LocalDateTime::class)
    every { LocalDateTime.now() } returns fakeNow

    webTestClient.get()
      .uri("/person-on-probation-user/$crn/user/$id")
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .json(expectedOutput)

    webTestClient.post()
      .uri("/person-on-probation-user/user")
      .bodyValue(
        expectedOutput,
      )
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isBadRequest
  }

  @Test
  fun `Create and Update Person on Probation User by CRN Id - happy path`() {
    var crn = "axb"
    val id = 4
    val expectedOutput = readFile("testdata/expectation/postuser.json")
    val expectedOutput2 = readFile("testdata/expectation/patchuser.json")

    mockkStatic(LocalDateTime::class)
    every { LocalDateTime.now() } returns fakeNow

    webTestClient.get()
      .uri("/person-on-probation-user/$crn/user/$id")
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isNotFound

    webTestClient.post()
      .uri("/person-on-probation-user/user")
      .bodyValue(
        UserPost(
          crn = "axb",
          email = "test1@test.com",
          cprId = "123",
          verified = true,
        ),
      )
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType("application/json")
      .expectBody()
      .json(expectedOutput)

    webTestClient.patch()
      .uri("/person-on-probation-user/$crn/user/$id")
      .bodyValue(
        UserPatch(
          crn = "newcrn",
          cprId = "456345",
          email = "test2@test.com",
          verified = true,
        ),
      )
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType("application/json")
      .expectBody()
      .json(expectedOutput2)

    crn = "newcrn"
    webTestClient.get()
      .uri("/person-on-probation-user/$crn/user/$id")
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .json(expectedOutput2)
  }

  @Test
  fun `Get Person on Probation Users by CRN - happy path`() {
    val expectedOutput = readFile("testdata/expectation/user2.json")
    val expectedOutput2 = readFile("testdata/expectation/userWithSameCRN.json")

    val crn = "abc"
    mockkStatic(LocalDateTime::class)
    every { LocalDateTime.now() } returns fakeNow

    webTestClient.post()
      .uri("/person-on-probation-user/user")
      .bodyValue(
        UserPost(
          crn = "abc",
          email = "user4@gmail.com",
          cprId = "123456",
          verified = true,
        ),
      )
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType("application/json")
      .expectBody()
      .json(expectedOutput)

    webTestClient.get()
      .uri("/person-on-probation-user/$crn/user")
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .json(expectedOutput2)
  }
}
