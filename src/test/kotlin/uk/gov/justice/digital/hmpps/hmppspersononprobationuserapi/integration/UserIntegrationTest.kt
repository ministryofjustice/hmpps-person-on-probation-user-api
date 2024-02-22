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
          nomsId = "G123",
          oneLoginUrn = "urn6",
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
          nomsId = "G12345",
          oneLoginUrn = "urn5",
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
          email = "user5@gmail.com",
          cprId = "123456",
          verified = true,
          nomsId = "G12345",
          oneLoginUrn = "urn7",
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
    var crn = "axb"
    webTestClient.post()
      .uri("/person-on-probation-user/$crn/user")
      .bodyValue(
        UserPost(
          crn = "abc",
          email = "user4@gmail.com",
          cprId = "123456",
          verified = true,
          nomsId = "G123",
          oneLoginUrn = "urn4",
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
        UserPatch(
          crn = "newcrn",
          cprId = "456345",
          email = "test2@test.com",
          verified = true,
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
        UserPost(
          crn = "abc",
          email = "user4@gmail.com",
          cprId = "123456",
          verified = true,
          nomsId = "G123",
          oneLoginUrn = "urn:fdc:gov.uk:2022:T5fYp6sYl3DdYNF0tDfZtF-c4ZKewWRLw8YGcy6oEj8",
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
        UserPatch(
          crn = "newcrn",
          cprId = "456345",
          email = "test2@test.com",
          verified = true,
        ),
      )
      .exchange()
      .expectStatus().isForbidden
  }

  @Test
  fun `Create Person on Probation User with duplicate one login urn `() {
    var crn = "abc"
    val id = 3
    val expectedOutput = readFile("testdata/expectation/user2.json")
    mockkStatic(LocalDateTime::class)
    every { LocalDateTime.now() } returns fakeNow

    webTestClient.get()
      .uri("/person-on-probation-user/$crn/user/$id")
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .json(expectedOutput)

    expectedOutput.replace("user5@gmail.com", "user6@gmail.com")

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
