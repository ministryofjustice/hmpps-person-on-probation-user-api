package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.service

import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.config.DuplicateDataFoundException
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.config.ResourceNotFoundException
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.data.DelegatedAccess
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.entity.DelegatedAccessEntity
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.entity.DelegatedAccessPermissionEntity
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.entity.UserEntity
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.repository.DelegatedAccessPermissionRepository
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.repository.DelegatedAccessRepository
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.repository.UserRepository
import java.time.LocalDateTime
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class DelegatedAccessServiceTest {
  private lateinit var delegatedAccessService: DelegatedAccessService

  @Mock
  private lateinit var userRepository: UserRepository

  @Mock
  private lateinit var delegatedAccessRepository: DelegatedAccessRepository

  @Mock
  private lateinit var delegatedAccessPermissionRepository: DelegatedAccessPermissionRepository

  private val fakeNow = LocalDateTime.parse("2024-02-12T14:33:26")

  @BeforeEach
  fun beforeEach() {
    delegatedAccessService = DelegatedAccessService(delegatedAccessRepository, userRepository, delegatedAccessPermissionRepository)
  }

  @Test
  fun `test getAllAccessByUserId - returns list of all Access exists`() {
    val userEntity1 = UserEntity(1, "abc", "123", true, LocalDateTime.parse("2024-02-12T14:33:26"), LocalDateTime.parse("2024-02-12T14:33:26"), "G123", "urn:fdc:gov.uk:2022:T5fYp6sYl3DdYNF0tDfZtF-c4ZKewWRLw8YGcy6oEj8")
    val delegatedAccessEntity = DelegatedAccessEntity(1, 1, 2, LocalDateTime.parse("2024-02-12T14:33:26"), LocalDateTime.parse("2024-02-12T14:33:26"))
    val delegatedAccessList = emptyList<DelegatedAccessEntity>().toMutableList()
    delegatedAccessList.add(delegatedAccessEntity)
    Mockito.`when`(userEntity1.id?.let { delegatedAccessRepository.findByInitiatedUserId(it) }).thenReturn(delegatedAccessList)
    val result = delegatedAccessService.getAllAccessByInitiatorUserId(1)
    Assertions.assertEquals(delegatedAccessList, result)
  }

  @Test
  fun `test getActiveAccessByUserId - returns list of active Access exists`() {
    val userEntity1 = UserEntity(1, "abc", "123", true, LocalDateTime.parse("2024-02-12T14:33:26"), LocalDateTime.parse("2024-02-12T14:33:26"), "G123", "urn:fdc:gov.uk:2022:T5fYp6sYl3DdYNF0tDfZtF-c4ZKewWRLw8YGcy6oEj8")
    val delegatedAccessEntity = DelegatedAccessEntity(1, 1, 2, LocalDateTime.parse("2024-02-12T14:33:26"), null)
    val delegatedAccessList = emptyList<DelegatedAccessEntity>().toMutableList()
    delegatedAccessList.add(delegatedAccessEntity)
    Mockito.lenient().`when`(userEntity1.id?.let { delegatedAccessRepository.findByInitiatedUserIdAndDeletedDateIsNull(it) }).thenReturn(delegatedAccessList)
    val result = delegatedAccessService.getActiveAccessByInitiatorUserId(1)
    Assertions.assertEquals(delegatedAccessList, result)
  }

  @Test
  fun `test delegate access - creates and returns delegate access`() {
    mockkStatic(LocalDateTime::class)
    every { LocalDateTime.now() } returns fakeNow
    Mockito.`when`(userRepository.existsById(1)).thenReturn(true)
    Mockito.`when`(userRepository.existsById(2)).thenReturn(true)
    val delegatedAccessEntity = DelegatedAccessEntity(null, 1, 2, LocalDateTime.parse("2024-02-12T14:33:26"), null)
    val delegatedAccessPost = DelegatedAccess(
      initiatedUserId = 1,
      delegatedUserId = 2,
    )
    Mockito.`when`(delegatedAccessRepository.findByInitiatedUserIdAndDelegatedUserIdAndDeletedDateIsNull(any(), any())).thenReturn(null)
    Mockito.`when`(delegatedAccessRepository.save(any())).thenReturn(delegatedAccessEntity)
    val result = delegatedAccessService.createDelegatedAccess(delegatedAccessPost)
    Mockito.verify(delegatedAccessRepository).save(delegatedAccessEntity)
    Assertions.assertEquals(delegatedAccessEntity, result)
    unmockkStatic(LocalDateTime::class)
  }

  @Test
  fun `test delegate access permission  - creates and returns delegate access permission`() {
    mockkStatic(LocalDateTime::class)
    every { LocalDateTime.now() } returns fakeNow
    val delegatedAccessEntity = DelegatedAccessEntity(1, 1, 2, LocalDateTime.parse("2024-02-12T14:33:26"), null)
    val delegatedAccessPermissionEntity = DelegatedAccessPermissionEntity(null, 1, 1, LocalDateTime.parse("2024-02-12T14:33:26"), null)

    Mockito.`when`(delegatedAccessRepository.findById(1)).thenReturn(Optional.of(delegatedAccessEntity))
    Mockito.`when`(delegatedAccessPermissionRepository.findByDelegatedAccessIdAndPermissionIdAndGrantedIsNotNullAndRevokedIsNull(1, 1)).thenReturn(null)
    Mockito.`when`(delegatedAccessPermissionRepository.save(any())).thenReturn(delegatedAccessPermissionEntity)
    val result = delegatedAccessService.grantDelegatedAccessPermission(1, 1)
    Mockito.verify(delegatedAccessPermissionRepository).save(delegatedAccessPermissionEntity)
    Assertions.assertEquals(delegatedAccessPermissionEntity, result)
    unmockkStatic(LocalDateTime::class)
  }

  @Test
  fun `test remove delegate access - returns delegate access with deleted date`() {
    mockkStatic(LocalDateTime::class)
    every { LocalDateTime.now() } returns fakeNow
    val delegatedAccessEntity = DelegatedAccessEntity(1, 1, 2, LocalDateTime.parse("2024-02-12T14:33:26"), null)

    Mockito.`when`(delegatedAccessRepository.findByIdAndDeletedDateIsNull(any())).thenReturn(delegatedAccessEntity)
    delegatedAccessEntity.deletedDate = LocalDateTime.parse("2024-02-13T14:33:26")
    Mockito.`when`(delegatedAccessRepository.save(any())).thenReturn(delegatedAccessEntity)
    val result = delegatedAccessService.removeDelegatedAccess(1)
    Mockito.verify(delegatedAccessRepository).save(delegatedAccessEntity)
    Assertions.assertEquals(delegatedAccessEntity, result)
    unmockkStatic(LocalDateTime::class)
  }

  @Test
  fun `test remove delegate access permission - returns delegate access permission with revoked date`() {
    mockkStatic(LocalDateTime::class)
    every { LocalDateTime.now() } returns fakeNow
    val delegatedAccessEntity = DelegatedAccessEntity(1, 1, 2, LocalDateTime.parse("2024-02-12T14:33:26"), null)

    val delegatedAccessPermissionEntity = DelegatedAccessPermissionEntity(1, 1, 1, LocalDateTime.parse("2024-02-12T14:33:26"), null)
    Mockito.`when`(delegatedAccessPermissionRepository.findByDelegatedAccessIdAndPermissionIdAndGrantedIsNotNullAndRevokedIsNull(any(), any())).thenReturn(delegatedAccessPermissionEntity)
    delegatedAccessPermissionEntity.revoked = LocalDateTime.parse("2024-02-14T14:33:26")
    Mockito.`when`(delegatedAccessPermissionRepository.save(any())).thenReturn(delegatedAccessPermissionEntity)
    val result = delegatedAccessService.revokeDelegatedAccessPermission(delegatedAccessPermissionEntity.delegatedAccessId, 1)
    Assertions.assertEquals(delegatedAccessPermissionEntity, result)
    unmockkStatic(LocalDateTime::class)
  }

  @Test
  fun `test delegate access - returns already exists`() {
    mockkStatic(LocalDateTime::class)
    every { LocalDateTime.now() } returns fakeNow
    val userEntity1 = UserEntity(1, "abc", "123", true, LocalDateTime.parse("2024-02-12T14:33:26"), LocalDateTime.parse("2024-02-12T14:33:26"), "G123", "urn1")
    val userEntity2 = UserEntity(2, "NA", "NA", true, LocalDateTime.parse("2024-02-12T14:33:26"), LocalDateTime.parse("2024-02-12T14:33:26"), "NA", "urn2")

    Mockito.`when`(userRepository.existsById(1)).thenReturn(true)
    Mockito.`when`(userRepository.existsById(2)).thenReturn(true)
    val delegatedAccessEntity = DelegatedAccessEntity(1, 1, 2, LocalDateTime.parse("2024-02-12T14:33:26"), null)
    val delegatedAccessPost = DelegatedAccess(
      initiatedUserId = 1,
      delegatedUserId = 2,
    )
    Mockito.`when`(delegatedAccessRepository.findByInitiatedUserIdAndDelegatedUserIdAndDeletedDateIsNull(any(), any())).thenReturn(delegatedAccessEntity)
    assertThrows<DuplicateDataFoundException> { delegatedAccessService.createDelegatedAccess(delegatedAccessPost) }
    unmockkStatic(LocalDateTime::class)
  }

  @Test
  fun `test delegate access permission  - returns already exists`() {
    mockkStatic(LocalDateTime::class)
    every { LocalDateTime.now() } returns fakeNow
    val delegatedAccessEntity = DelegatedAccessEntity(1, 1, 2, LocalDateTime.parse("2024-02-12T14:33:26"), null)
    val delegatedAccessPermissionEntity = DelegatedAccessPermissionEntity(1, 1, 1, LocalDateTime.parse("2024-02-12T14:33:26"), null)

    Mockito.`when`(delegatedAccessRepository.findById(1)).thenReturn(Optional.of(delegatedAccessEntity))
    Mockito.`when`(delegatedAccessPermissionRepository.findByDelegatedAccessIdAndPermissionIdAndGrantedIsNotNullAndRevokedIsNull(1, 1)).thenReturn(delegatedAccessPermissionEntity)
    assertThrows<DuplicateDataFoundException> { delegatedAccessService.grantDelegatedAccessPermission(1, 1) }
    unmockkStatic(LocalDateTime::class)
  }

  @Test
  fun `test remove delegate access - when not exists`() {
    mockkStatic(LocalDateTime::class)
    every { LocalDateTime.now() } returns fakeNow
    Mockito.`when`(delegatedAccessRepository.findByIdAndDeletedDateIsNull(any())).thenReturn(null)
    assertThrows<ResourceNotFoundException> { delegatedAccessService.removeDelegatedAccess(1) }
    unmockkStatic(LocalDateTime::class)
  }

  @Test
  fun `test revoke delegate access permission  - when not exists`() {
    mockkStatic(LocalDateTime::class)
    every { LocalDateTime.now() } returns fakeNow
    Mockito.`when`(delegatedAccessPermissionRepository.findByDelegatedAccessIdAndPermissionIdAndGrantedIsNotNullAndRevokedIsNull(1, 1)).thenReturn(null)
    assertThrows<ResourceNotFoundException> { delegatedAccessService.revokeDelegatedAccessPermission(1, 1) }
    unmockkStatic(LocalDateTime::class)
  }
}