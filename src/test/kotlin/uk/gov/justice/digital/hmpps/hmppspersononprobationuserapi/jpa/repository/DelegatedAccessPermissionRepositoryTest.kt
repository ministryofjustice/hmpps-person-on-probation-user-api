package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.helpers.TestBase
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.entity.DelegatedAccessEntity
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.entity.DelegatedAccessPermissionEntity
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.entity.UserEntity
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = ["classpath:testdata/sql/clear-all-data.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class DelegatedAccessPermissionRepositoryTest : TestBase() {

  @Autowired
  lateinit var userRepository: UserRepository

  @Autowired
  lateinit var delegatedAccessRepository: DelegatedAccessRepository

  @Autowired
  lateinit var delegatedAccessPermissionRepository: DelegatedAccessPermissionRepository

  @Test
  fun `test get saved delegated access permission`() {
    val userEntity1 = UserEntity(null, "abc", "123", true, LocalDateTime.parse("2024-02-12T14:33:26"), LocalDateTime.parse("2024-02-12T14:33:26"), "G123", "crn1")
    userRepository.save(userEntity1)
    val userEntity2 = UserEntity(null, "NA", "NA", true, LocalDateTime.parse("2024-02-12T14:33:26"), LocalDateTime.parse("2024-02-12T14:33:26"), "NA", "crn2")
    userRepository.save(userEntity2)
    val delegatedAccessEntity = DelegatedAccessEntity(null, 1, 2, LocalDateTime.parse("2024-02-12T14:33:26"), null)
    delegatedAccessRepository.save(delegatedAccessEntity)
    val delegatedAccessPermissionEntity = DelegatedAccessPermissionEntity(null, 1, 1, LocalDateTime.parse("2024-02-12T14:33:26"), null)
    delegatedAccessPermissionRepository.save(delegatedAccessPermissionEntity)
    val accessList = delegatedAccessPermissionRepository.findAll()
    assertThat(accessList).isNotEmpty()
  }

  @Test
  fun `test persist delegated access permission`() {
    val userEntity1 = UserEntity(null, "abc", "123", true, LocalDateTime.parse("2024-02-12T14:33:26"), LocalDateTime.parse("2024-02-12T14:33:26"), "G123", "crn1")
    userRepository.save(userEntity1)
    val userEntity2 = UserEntity(null, "NA", "NA", true, LocalDateTime.parse("2024-02-12T14:33:26"), LocalDateTime.parse("2024-02-12T14:33:26"), "NA", "crn2")
    userRepository.save(userEntity2)
    val delegatedAccessEntity = userEntity1.id?.let {
      userEntity2.id?.let { it1 ->
        DelegatedAccessEntity(
          null,
          it,
          it1,
          LocalDateTime.parse("2024-02-12T14:33:26"),
          null,
        )
      }
    }
    if (delegatedAccessEntity != null) {
      delegatedAccessRepository.save(delegatedAccessEntity)
    }
    val delegatedAccessPermissionEntity = DelegatedAccessPermissionEntity(null, 1, 1, LocalDateTime.parse("2024-02-12T14:33:26"), null)
    delegatedAccessPermissionRepository.save(delegatedAccessPermissionEntity)

    val assessmentFromDatabase = delegatedAccessPermissionRepository.findAll()[0]

    assertThat(assessmentFromDatabase).usingRecursiveComparison().ignoringFieldsOfTypes(LocalDateTime::class.java).isEqualTo(delegatedAccessPermissionEntity)
  }
}
