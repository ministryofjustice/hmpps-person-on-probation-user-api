package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.helpers.TestBase
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.entity.DelegatedUserOTPEntity
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.entity.UserEntity
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = ["classpath:testdata/sql/clear-all-data.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class DelegatedUserOTPRepositoryTest : TestBase() {
  @Autowired
  lateinit var delegatedUserOTPRepository: DelegatedUserOTPRepository

  @Autowired
  lateinit var userRepository: UserRepository

  @Test
  fun `test get saved user`() {
    val userEntity1 = UserEntity(null, "abc", "123", true, LocalDateTime.parse("2024-02-12T14:33:26"), LocalDateTime.parse("2024-02-12T14:33:26"), "G123", "crn1")
    userRepository.save(userEntity1)
    val delegatedUserOTPEntity1 = DelegatedUserOTPEntity(null, userEntity1.id!!, LocalDateTime.parse("2024-02-12T14:33:26"), LocalDateTime.parse("2025-02-12T14:33:26"), "G123", "user2@gmail.com")
    delegatedUserOTPRepository.save(delegatedUserOTPEntity1)
    val delegatedUserOTPList = delegatedUserOTPRepository.findAll()

    assertThat(delegatedUserOTPList).isNotEmpty()
  }

  @Test
  fun `test persist new user OTP`() {
    val userEntity1 = UserEntity(null, "abc", "123", true, LocalDateTime.parse("2024-02-12T14:33:26"), LocalDateTime.parse("2024-02-12T14:33:26"), "G123", "crn1")
    userRepository.save(userEntity1)
    val delegatedUserOTPEntity1 = DelegatedUserOTPEntity(null, userEntity1.id!!, LocalDateTime.parse("2024-02-12T14:33:26"), LocalDateTime.parse("2025-02-12T14:33:26"), "G123", "user2@gmail.com")
    delegatedUserOTPRepository.save(delegatedUserOTPEntity1)

    val assessmentFromDatabase = delegatedUserOTPRepository.findAll()[0]

    assertThat(assessmentFromDatabase).usingRecursiveComparison().ignoringFieldsOfTypes(LocalDateTime::class.java).isEqualTo(delegatedUserOTPEntity1)
  }
}
