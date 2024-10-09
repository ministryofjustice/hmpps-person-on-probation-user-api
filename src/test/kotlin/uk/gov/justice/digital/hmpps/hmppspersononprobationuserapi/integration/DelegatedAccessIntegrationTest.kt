package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.integration

import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.Test
import org.springframework.test.context.jdbc.Sql
import java.time.LocalDateTime

class DelegatedAccessIntegrationTest : IntegrationTestBase() {

  private val fakeNow = LocalDateTime.parse("2024-09-30T09:04:52.814839")

  @Test
  @Sql("classpath:testdata/sql/seed-2-user.sql")
  fun `Get All Delegate Access - happy path Empty List`() {
    val expectedOutput = "[]"
    val userid = 1

    webTestClient.get()
      .uri("/person-on-probation-user/all/access/$userid")
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .json(expectedOutput)
  }

  @Test
  fun `Get All Delegate Access Permission- happy path Empty List`() {
    val expectedOutput = "[]"
    val userid = 1

    webTestClient.get()
      .uri("/person-on-probation-user/all/permission/$userid")
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .json(expectedOutput)
  }

  @Test
  fun `Post Access & Permission, Get Access & Permission, Delete Access & Permission - happy path`() {
    mockkStatic(LocalDateTime::class)
    every { LocalDateTime.now() } returns fakeNow

    val expectedOutput1 = readFile("testdata/expectation/postAccess.json")
    val expectedOutput2 = readFile("testdata/expectation/postAccessPermission.json")
    val expectedOutput3 = readFile("testdata/expectation/accessList.json")
    val expectedOutput4 = readFile("testdata/expectation/accessPermissionList.json")
    val expectedOutput5 = readFile("testdata/expectation/deleteAccessPermission.json")
    val expectedOutput6 = readFile("testdata/expectation/deleteAccess.json")

    val userid = 1
    val delegatedUserId = 2

    webTestClient.post()
      .uri("/person-on-probation-user/delegate/access")
      .bodyValue(
        mapOf(
          "initiatedUserId" to 1,
          "delegatedUserId" to 2,
        ),
      )
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType("application/json")
      .expectBody()
      .json(expectedOutput1)

    webTestClient.post()
      .uri("/person-on-probation-user/grant/permission/$userid")
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType("application/json")
      .expectBody()
      .json(expectedOutput2)

    webTestClient.get()
      .uri("/person-on-probation-user/all/access/$userid")
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .json(expectedOutput3)

    webTestClient.get()
      .uri("/person-on-probation-user/all/access/delegated/$delegatedUserId")
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .json(expectedOutput3)

    webTestClient.get()
      .uri("/person-on-probation-user/all/permission/$userid")
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .json(expectedOutput4)

    webTestClient.get()
      .uri("/person-on-probation-user/all/permission/delegated/$delegatedUserId")
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .json(expectedOutput4)

    webTestClient.get()
      .uri("/person-on-probation-user/active/permission/$userid")
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .json(expectedOutput4)

    webTestClient.get()
      .uri("/person-on-probation-user/active/permission/delegated/$delegatedUserId")
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .json(expectedOutput4)

    webTestClient.get()
      .uri("/person-on-probation-user/access/$userid/permission/$delegatedUserId")
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .json(expectedOutput4)

    webTestClient.delete()
      .uri("/person-on-probation-user/revoke/permission/$userid")
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType("application/json")
      .expectBody()
      .json(expectedOutput5)

    webTestClient.delete()
      .uri("/person-on-probation-user/remove/access/$userid")
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isOk
      .expectHeader().contentType("application/json")
      .expectBody()
      .json(expectedOutput6)
  }

  @Test
  @Sql("classpath:testdata/sql/seed-2-user.sql")
  fun `Gives 404 if initiatedUser does not exist`() {
    webTestClient.post()
      .uri("/person-on-probation-user/delegate/access")
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .bodyValue(
        mapOf(
          "initiatedUserId" to "43234",
          "delegatedUserId" to "2",
        ),
      )
      .exchange()
      .expectStatus().isNotFound
  }

  @Test
  @Sql("classpath:testdata/sql/seed-2-user.sql")
  fun `Gives 404 if delegatedUser does not exist`() {
    webTestClient.post()
      .uri("/person-on-probation-user/delegate/access")
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .bodyValue(
        mapOf(
          "initiatedUserId" to "1",
          "delegatedUserId" to "243455",
        ),
      )
      .exchange()
      .expectStatus().isNotFound
  }

  @Test
  @Sql("classpath:testdata/sql/seed-2-user.sql")
  fun `Gives 404 if delegatedUser or InitiatedUser does not exist`() {
    webTestClient.get()
      .uri("/person-on-probation-user/access/1/permission/23455")
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isNotFound

    webTestClient.get()
      .uri("/person-on-probation-user/access/23455/permission/2")
      .headers(setAuthorisation(roles = listOf("ROLE_RESETTLEMENT_PASSPORT_EDIT")))
      .exchange()
      .expectStatus().isNotFound
  }
}
