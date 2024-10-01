package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.service

import jakarta.transaction.Transactional
import jakarta.validation.ValidationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.config.DuplicateDataFoundException
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.config.ResourceNotFoundException
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.data.DelegatedAccess
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.entity.DelegatedAccessEntity
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.entity.DelegatedAccessPermissionEntity
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.repository.DelegatedAccessPermissionRepository
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.repository.DelegatedAccessRepository
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.repository.UserRepository
import java.time.LocalDateTime

@Service
class DelegatedAccessService(private val delegatedAccessRepository: DelegatedAccessRepository, private val userRepository: UserRepository, private val delegatedAccessPermissionRepository: DelegatedAccessPermissionRepository) {

  @Transactional
  fun createDelegatedAccess(delegatePost: DelegatedAccess): DelegatedAccessEntity {
    val now = LocalDateTime.now()

    val initiatedUser = userRepository.findByIdOrNull(delegatePost.initiatedUserId.toLong()) ?: throw ResourceNotFoundException("User with id ${delegatePost.initiatedUserId} not found in database ")
    val delegatedUser = userRepository.findByIdOrNull(delegatePost.delegatedUserId.toLong()) ?: throw ResourceNotFoundException("User with id ${delegatePost.delegatedUserId} not found in database ")

    val delegatedAccessAlreadyExists =
      delegatedAccessRepository.findByInitiatedUserIdAndDelegatedUserIdAndDeletedDateIsNull(
        initiatedUser.id!!,
        delegatedUser.id!!,
      )
    if (delegatedAccessAlreadyExists != null) {
      throw DuplicateDataFoundException("Delegated Access already exists and active!")
    }
    val delegatedAccessEntity = DelegatedAccessEntity(
      id = null,
      initiatedUserId = initiatedUser.id,
      delegatedUserId = delegatedUser.id,
      createdDate = now,
      deletedDate = null,
    )
    return delegatedAccessRepository.save(delegatedAccessEntity)
  }

  @Transactional
  fun removeDelegatedAccess(id: Long): DelegatedAccessEntity {
    val now = LocalDateTime.now()
    val delegatedAccessEntity = delegatedAccessRepository.findByIdAndDeletedDateIsNull(id.toLong()) ?: throw ResourceNotFoundException("Given Id $id not found in the database or access already removed!")
    val delegatedAccessPermissionList = delegatedAccessEntity.id?.let {
      delegatedAccessPermissionRepository.findByDelegatedAccessIdAndGrantedIsNotNullAndRevokedIsNull(
        it.toLong(),
      )
    }
    if (!delegatedAccessPermissionList.isNullOrEmpty()) {
      throw ValidationException("Revoke all permissions granted, before removing the access id $id")
    }
    delegatedAccessEntity.deletedDate = now
    return delegatedAccessRepository.save(delegatedAccessEntity)
  }

  @Transactional
  fun getAllAccessByInitiatorUserId(id: Long): List<DelegatedAccessEntity> {
    val accessList = delegatedAccessRepository.findByInitiatedUserId(id.toLong())
    return accessList
  }

  @Transactional
  fun getActiveAccessByInitiatorUserId(id: Long): List<DelegatedAccessEntity> {
    val accessList = delegatedAccessRepository.findByInitiatedUserIdAndDeletedDateIsNull(id)
    return accessList
  }

  @Transactional
  fun grantDelegatedAccessPermission(accessId: Long, permissionId: Long): DelegatedAccessPermissionEntity {
    val now = LocalDateTime.now()
    val delegatedAccess = delegatedAccessRepository.findById(accessId.toLong()) ?: throw ResourceNotFoundException("User with id $accessId not found in database ")
    val delegatedAccessPermissionAlreadyExists = delegatedAccessPermissionRepository.findByDelegatedAccessIdAndPermissionIdAndGrantedIsNotNullAndRevokedIsNull(accessId.toLong(), permissionId.toLong())
    if (delegatedAccessPermissionAlreadyExists != null) {
      throw DuplicateDataFoundException("Permission already granted and active!")
    }
    val delegatedAccessPermissionEntity = DelegatedAccessPermissionEntity(
      id = null,
      delegatedAccessId = accessId,
      permissionId = permissionId,
      granted = now,
      revoked = null,
    )
    return delegatedAccessPermissionRepository.save(delegatedAccessPermissionEntity)
  }

  @Transactional
  fun revokeDelegatedAccessPermission(accessId: Long, permissionId: Long): DelegatedAccessPermissionEntity {
    val now = LocalDateTime.now()
    val delegatedAccessPermissionEntity = delegatedAccessPermissionRepository.findByDelegatedAccessIdAndPermissionIdAndGrantedIsNotNullAndRevokedIsNull(accessId.toLong(), permissionId.toLong()) ?: throw ResourceNotFoundException("Given access id $accessId and permission id $permissionId not found in the database or permission already revoked!")
    delegatedAccessPermissionEntity.revoked = now
    return delegatedAccessPermissionRepository.save(delegatedAccessPermissionEntity)
  }

  @Transactional
  fun getAllAccessPermissionByUserId(userId: Long): MutableList<DelegatedAccessPermissionEntity?> {
    val accessPermissionList = mutableListOf<DelegatedAccessPermissionEntity?>()
    getAllAccessByInitiatorUserId(userId).forEach {
      val list = it.id?.let { it1 -> delegatedAccessPermissionRepository.findByDelegatedAccessId(it1) }
      if (list != null) {
        accessPermissionList.addAll(list)
      }
    }
    return accessPermissionList
  }

  @Transactional
  fun getActiveAccessPermissionByInitiatorUserId(userId: Long): MutableList<DelegatedAccessPermissionEntity?> {
    val accessPermissionList = mutableListOf<DelegatedAccessPermissionEntity?>()
    getActiveAccessByInitiatorUserId(userId).forEach {
      val list = it.id?.let { it1 -> delegatedAccessPermissionRepository.findByDelegatedAccessIdAndGrantedIsNotNullAndRevokedIsNull(it1) }
      if (list != null) {
        accessPermissionList.addAll(list)
      }
    }
    return accessPermissionList
  }
}
