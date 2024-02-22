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
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.data.UserPatch
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.data.UserPost
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.entity.UserEntity
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.repository.UserRepository
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class UserServiceTest {
  private lateinit var userService: UserService

  @Mock
  private lateinit var userRepository: UserRepository

  private val fakeNow = LocalDateTime.parse("2024-02-12T14:33:26")

  @BeforeEach
  fun beforeEach() {
    userService = UserService(userRepository)
  }

  @Test
  fun `test getUserByCrn - returns list of user details`() {
    val userEntity1 = UserEntity(1, "abc", "123", "user1@gmail.com", true, LocalDateTime.parse("2024-02-12T14:33:26"), LocalDateTime.parse("2024-02-12T14:33:26"), "G123", "urn:fdc:gov.uk:2022:T5fYp6sYl3DdYNF0tDfZtF-c4ZKewWRLw8YGcy6oEj8")
    val userEntity2 = UserEntity(2, "abc", "123456", "user2@gmail.com", true, LocalDateTime.parse("2024-02-12T14:33:26"), LocalDateTime.parse("2024-02-12T14:33:26"), "G12345", "urn:fdc:gov.uk:2022:T5fYp6sYl3DdYNF0tDfZtF-c4ZKewWRLw8YGcy6oEj8")
    val userEntityList = emptyList<UserEntity>().toMutableList()
    userEntityList.add(userEntity1)
    userEntityList.add(userEntity2)
    Mockito.`when`(userRepository.findByCrn(userEntity1.crn)).thenReturn(userEntityList)
    val result = userService.getUserByCRN("abc")
    Assertions.assertEquals(userEntityList, result)
  }

  @Test
  fun `test getAllUsers - returns all user details`() {
    val userEntity1 = UserEntity(1, "abc", "123", "user1@gmail.com", false, LocalDateTime.parse("2024-02-12T14:33:26"), LocalDateTime.parse("2024-02-12T14:33:26"), "G123", "urn:fdc:gov.uk:2022:T5fYp6sYl3DdYNF0tDfZtF-c4ZKewWRLw8YGcy6oEj8")
    val userEntity2 = UserEntity(2, "xyz", "456", "user2@gmail.com", false, LocalDateTime.parse("2024-02-12T14:33:26"), LocalDateTime.parse("2024-02-12T14:33:26"), "G12345", "urn:fdc:gov.uk:2022:T5fYp6sYl3DdYNF0tCfZtF-c4ZKewWRLw8YGcy6oEj8")

    val userEntityList = emptyList<UserEntity>().toMutableList()
    userEntityList.add(userEntity1)
    userEntityList.add(userEntity2)
    Mockito.`when`(userRepository.findAll()).thenReturn(userEntityList)
    val result = userService.getAllUsers()
    Assertions.assertEquals(userEntityList, result)
  }

  @Test
  fun `test getUserByCrnAndId - returns a user details`() {
    val userEntity1 = UserEntity(1, "abc", "123", "user1@gmail.com", false, LocalDateTime.parse("2024-02-12T14:33:26"), LocalDateTime.parse("2024-02-12T14:33:26"), "G123", "urn1")
    val userEntity2 = UserEntity(2, "abc", "123456", "user2@gmail.com", false, LocalDateTime.parse("2024-02-12T14:33:26"), LocalDateTime.parse("2024-02-12T14:33:26"), "G12345", "urn2")
    val userEntityList = emptyList<UserEntity>().toMutableList()
    userEntityList.add(userEntity1)
    userEntityList.add(userEntity2)
    Mockito.`when`(userEntity1.id?.let { userRepository.findByIdAndCrn(it, userEntity1.crn) }).thenReturn(userEntity1)
    val result = userService.getUserByCrnAndUserId("abc", 1)
    Assertions.assertEquals(userEntity1, result)
  }

  @Test
  fun `test user - creates and returns user`() {
    mockkStatic(LocalDateTime::class)
    every { LocalDateTime.now() } returns fakeNow

    Mockito.`when`(userRepository.findByEmail("user1@gmail.com")).thenReturn(null)

    val userEntity1 = UserEntity(id = null, crn = "abc", cprId = "123", email = "user1@gmail.com", verified = true, creationDate = fakeNow, modifiedDate = fakeNow, nomsId = "G123", oneLoginUrn = "urn1")
    val userPost = UserPost(
      crn = "abc",
      cprId = "123",
      email = "user1@gmail.com",
      verified = true,
      nomsId = "G123",
      oneLoginUrn = "urn1",
    )

    Mockito.`when`(userRepository.save(any())).thenReturn(userEntity1)
    val result = userService.createUser(userPost)
    Mockito.verify(userRepository).save(userEntity1)
    Assertions.assertEquals(userEntity1, result)
    unmockkStatic(LocalDateTime::class)
  }

  @Test
  fun `test updateUser - updates and returns user`() {
    mockkStatic(LocalDateTime::class)
    every { LocalDateTime.now() } returns fakeNow
    val userEntity1 = UserEntity(id = null, crn = "abc", cprId = "123", email = "user1@gmail.com", verified = true, creationDate = fakeNow, modifiedDate = fakeNow, nomsId = "G123", oneLoginUrn = "urn1")
    val expectedUserEntity = UserEntity(id = null, crn = "abc", cprId = "12345", email = "user1@gmail.com", verified = true, creationDate = fakeNow, modifiedDate = fakeNow, nomsId = "G12345", oneLoginUrn = "urn2")
    val userPatch = UserPatch(
      crn = "abc",
      cprId = "12345",
      email = "user1@gmail.com",
      verified = true,
      nomsId = "G12345",
      oneLoginUrn = "urn2",
    )

    Mockito.`when`(userRepository.save(any())).thenReturn(expectedUserEntity)
    val result = userService.updateUser(userEntity1, userPatch)
    Mockito.verify(userRepository).save(expectedUserEntity)
    Assertions.assertEquals(expectedUserEntity, result)
    unmockkStatic(LocalDateTime::class)
  }

  @Test
  fun `test user - creates user with duplicate email check`() {
    mockkStatic(LocalDateTime::class)
    every { LocalDateTime.now() } returns fakeNow

    Mockito.`when`(userRepository.findByEmail("user1@gmail.com")).thenReturn(null)

    val userEntity1 = UserEntity(id = null, crn = "abc", cprId = "123", email = "user1@gmail.com", verified = true, creationDate = fakeNow, modifiedDate = fakeNow, nomsId = "G123", oneLoginUrn = "urn1")
    val userPost = UserPost(
      crn = "abc",
      cprId = "123",
      email = "user1@gmail.com",
      verified = true,
      nomsId = "G123",
      oneLoginUrn = "urn1",
    )

    Mockito.`when`(userRepository.save(any())).thenReturn(userEntity1)
    val result = userService.createUser(userPost)
    Mockito.verify(userRepository).save(userEntity1)
    Assertions.assertEquals(userEntity1, result)
    val userPost2 = UserPost(
      crn = "abc",
      cprId = "123",
      email = "user1@gmail.com",
      verified = true,
      nomsId = "G123",
      oneLoginUrn = "urn2",
    )
    Mockito.`when`(userRepository.findByEmail(any())).thenReturn(userEntity1)
    assertThrows<DuplicateDataFoundException> { userService.createUser(userPost2) }
    unmockkStatic(LocalDateTime::class)
  }

  @Test
  fun `test user - creates user with duplicate one login urn check`() {
    mockkStatic(LocalDateTime::class)
    every { LocalDateTime.now() } returns fakeNow

    Mockito.`when`(userRepository.findByEmail("user1@gmail.com")).thenReturn(null)

    val userEntity1 = UserEntity(id = null, crn = "abc", cprId = "123", email = "user1@gmail.com", verified = true, creationDate = fakeNow, modifiedDate = fakeNow, nomsId = "G123", oneLoginUrn = "urn1")
    val userPost = UserPost(
      crn = "abc",
      cprId = "123",
      email = "user1@gmail.com",
      verified = true,
      nomsId = "G123",
      oneLoginUrn = "urn1",
    )

    Mockito.`when`(userRepository.save(any())).thenReturn(userEntity1)
    val result = userService.createUser(userPost)
    Mockito.verify(userRepository).save(userEntity1)
    Assertions.assertEquals(userEntity1, result)
    val userPost2 = UserPost(
      crn = "abc",
      cprId = "123",
      email = "user2@gmail.com",
      verified = true,
      nomsId = "G123",
      oneLoginUrn = "urn1",
    )
    Mockito.`when`(userRepository.findByOneLoginUrn(any())).thenReturn(userEntity1)

    assertThrows<DuplicateDataFoundException> { userService.createUser(userPost2) }
    unmockkStatic(LocalDateTime::class)
  }
}
