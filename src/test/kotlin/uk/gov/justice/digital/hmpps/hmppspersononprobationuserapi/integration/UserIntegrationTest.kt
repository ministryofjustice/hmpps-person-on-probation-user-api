package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.integration

import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.Test
import org.springframework.test.context.jdbc.Sql
import java.time.LocalDateTime

class UserIntegrationTest : IntegrationTestBase() {

  private val fakeNow = LocalDateTime.parse("2024-02-12T14:33:26.520566")

  @Test
  @Sql("classpath:testdata/sql/clear-all-data.sql")
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
    val expectedOutput = readFile("testdata/expectation/user-1.json")
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
  fun `Create and Update Person on Probation User by CRN Id - happy path`() {
    var crn = "axb"
    val id = 4
    val expectedOutput = readFile("testdata/expectation/postuser-1.json")
    val expectedOutput2 = readFile("testdata/expectation/patchuser-1.json")

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
        mapOf(
          "crn" to "axb",
          "cprId" to "123",
          "verified" to true,
          "nomsId" to "G123",
          "oneLoginUrn" to "urn6",
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
        mapOf(
          "crn" to "newcrn",
          "cprId" to "456345",
          "verified" to true,
          "nomsId" to "G12345",
          "oneLoginUrn" to "urn5",
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
    val expectedOutput = readFile("testdata/expectation/user-2.json")
    val expectedOutput2 = readFile("testdata/expectation/userWithSameCRN.json")

    val crn = "abc"
    mockkStatic(LocalDateTime::class)
    every { LocalDateTime.now() } returns fakeNow

    webTestClient.post()
      .uri("/person-on-probation-user/user")
      .bodyValue(
        mapOf(
          "crn" to "abc",
          "cprId" to "123456",
          "verified" to true,
          "nomsId" to "G12345",
          "oneLoginUrn" to "urn7",
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

  @Test
  fun `Get a Person on Probation all Users  - Unauthorized`() {
    webTestClient.get()
      .uri("/person-on-probation-user/users/all")
      .exchange()
      .expectStatus().isUnauthorized
  }

  @Test
  fun `Get a Person on Probation User by CRN - Unauthorized`() {
    val crn = "abc"

    webTestClient.get()
      .uri("/person-on-probation-user/$crn/user")
      .exchange()
      .expectStatus().isUnauthorized
  }

  @Test
  fun `Create  Person on Probation User by CRN Id - Unuthorized`() {
    val crn = "axb"
    webTestClient.post()
      .uri("/person-on-probation-user/$crn/user")
      .bodyValue(
        mapOf(
          "crn" to "abc",
          "cprId" to "123456",
          "verified" to true,
          "nomsId" to "G123",
          "oneLoginUrn" to "urn4",
        ),
      )
      .exchange()
      .expectStatus().isUnauthorized
  }

  @Test
  fun `Update Person on Probation User by CRN Id - Unuthorized`() {
    val crn = "axb"
    val id = 1
    webTestClient.patch()
      .uri("/person-on-probation-user/$crn/user/$id")
      .bodyValue(
        mapOf(
          "crn" to "newcrn",
          "cprId" to "456345",
          "verified" to true,
        ),
      )
      .exchange()
      .expectStatus().isUnauthorized
  }

  @Test
  fun `Get a Person on Probation User by Id - Forbidden`() {
    val crn = "abc"
    val id = 1

    webTestClient.get()
      .uri("/person-on-probation-user/$crn/user/$id")
      .headers(setAuthorisation())
      .exchange()
      .expectStatus().isForbidden
  }

  @Test
  fun `Get a Person on Probation all Users  - Forbidden`() {
    webTestClient.get()
      .uri("/person-on-probation-user/users/all")
      .headers(setAuthorisation())
      .exchange()
      .expectStatus().isForbidden
  }

  @Test
  fun `Get a Person on Probation User by CRN - Forbidden`() {
    val crn = "abc"

    webTestClient.get()
      .uri("/person-on-probation-user/$crn/user")
      .headers(setAuthorisation())
      .exchange()
      .expectStatus().isForbidden
  }

  @Test
  fun `Create  Person on Probation User by CRN Id - Forbidden`() {
    webTestClient.post()
      .uri("/person-on-probation-user/user")
      .headers(setAuthorisation())
      .bodyValue(
        mapOf(
          "crn" to "abc",
          "cprId" to "123456",
          "verified" to true,
          "nomsId" to "G123",
          "oneLoginUrn" to "urn:fdc:gov.uk:2022:T5fYp6sYl3DdYNF0tDfZtF-c4ZKewWRLw8YGcy6oEj8",
        ),
      )
      .exchange()
      .expectStatus().isForbidden
  }

  @Test
  fun `Update Person on Probation User by Id - Forbidden`() {
    val crn = "axb"
    val id = 1
    webTestClient.patch()
      .uri("/person-on-probation-user/$crn/user/$id")
      .headers(setAuthorisation())
      .bodyValue(
        mapOf(
          "crn" to "newcrn",
          "cprId" to "456345",
          "verified" to true,
        ),
      )
      .exchange()
      .expectStatus().isForbidden
  }

  @Test
  fun `Create Person on Probation User with duplicate one login urn `() {
    val crn = "abc"
    val id = 1
    val expectedOutput = readFile("testdata/expectation/user-1.json")
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
}
