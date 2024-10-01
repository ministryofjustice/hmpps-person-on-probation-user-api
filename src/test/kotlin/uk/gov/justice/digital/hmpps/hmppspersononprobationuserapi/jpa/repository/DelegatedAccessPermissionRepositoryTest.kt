package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.repository

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.helpers.TestBase
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.entity.DelegatedAccessEntity
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.entity.DelegatedAccessPermissionEntity
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.entity.UserEntity
import java.time.LocalDateTime

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = ["classpath:testdata/sql/clear-all-data.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class DelegatedAccessPermissionRepositoryTest : TestBase() {

  @Autowired
  lateinit var userRepository: UserRepository

  @Autowired
  lateinit var delegatedAccessRepository: DelegatedAccessRepository

  @Autowired
  lateinit var delegatedAccessPermissionRepository: DelegatedAccessPermissionRepository

  @BeforeEach
  @AfterEach
  fun beforeEach() {
    delegatedAccessPermissionRepository.deleteAll()
  }

  @Test
  fun `test get saved delegated access permission`() {
    val userEntity1 = UserEntity(null, "abc", "123", true, LocalDateTime.parse("2024-02-12T14:33:26"), LocalDateTime.parse("2024-02-12T14:33:26"), "G123", "crn1")
    userRepository.save(userEntity1)
    val userEntity2 = UserEntity(null, "NA", "NA", true, LocalDateTime.parse("2024-02-12T14:33:26"), LocalDateTime.parse("2024-02-12T14:33:26"), "NA", "crn2")
    userRepository.save(userEntity2)
    val delegatedAccessEntity = DelegatedAccessEntity(null, 1, 2, LocalDateTime.parse("2024-02-12T14:33:26"), null)
    delegatedAccessRepository.save(delegatedAccessEntity)
    val delegatedAccessPermissionEntity = DelegatedAccessPermissionEntity(1, 1, 1, LocalDateTime.parse("2024-02-12T14:33:26"), null)
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
    val delegatedAccessEntity = DelegatedAccessEntity(null, 1, 2, LocalDateTime.parse("2024-02-12T14:33:26"), null)
    delegatedAccessRepository.save(delegatedAccessEntity)
    val delegatedAccessPermissionEntity = DelegatedAccessPermissionEntity(1, 1, 1, LocalDateTime.parse("2024-02-12T14:33:26"), null)
    delegatedAccessPermissionRepository.save(delegatedAccessPermissionEntity)

    val assessmentFromDatabase = delegatedAccessPermissionRepository.findAll()[0]

    Assertions.assertThat(assessmentFromDatabase).usingRecursiveComparison().ignoringFieldsOfTypes(LocalDateTime::class.java).isEqualTo(delegatedAccessPermissionEntity)
  }
}
