package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.service

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.entity.UserEntity
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.repository.UserRepository
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class UserServiceTest {
  private lateinit var userService: UserService

  @Mock
  private lateinit var userRepository: UserRepository

  private val testDate = LocalDateTime.parse("2023-08-16T12:00:00")
  private val fakeNow = LocalDateTime.parse("2023-08-17T12:00:01")

  @BeforeEach
  fun beforeEach() {
    userService = UserService(userRepository)
  }

  @Test
  fun `test getUserByCrnId - returns a user details`() {
    val userEntity = UserEntity(1, "abc", "123", "user1@gmail.com", false, LocalDateTime.parse("2024-02-12T14:33:26"), LocalDateTime.parse("2024-02-12T14:33:26"))
    Mockito.`when`(userRepository.findByCrn(userEntity.crn)).thenReturn(userEntity)
    val result = userService.getUserByCRN("abc")
    Assertions.assertEquals(userEntity, result)
  }

  @Test
  fun `test getAllUsers - returns all user details`() {
    val userEntity1 = UserEntity(1, "abc", "123", "user1@gmail.com", false, LocalDateTime.parse("2024-02-12T14:33:26"), LocalDateTime.parse("2024-02-12T14:33:26"))
    val userEntity2 = UserEntity(2, "xyz", "456", "user2@gmail.com", false, LocalDateTime.parse("2024-02-12T14:33:26"), LocalDateTime.parse("2024-02-12T14:33:26"))

    val userEntityList = emptyList<UserEntity>().toMutableList()
    userEntityList.add(userEntity1)
    userEntityList.add(userEntity2)
    Mockito.`when`(userRepository.findAll()).thenReturn(userEntityList)
    val result = userService.getAllUsers()
    Assertions.assertEquals(userEntityList, result)
  }
}
