package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.resource

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.config.ErrorResponse
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.config.NoDataWithCodeFoundException
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.data.UserPatch
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.data.UserPost
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.entity.UserEntity
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.service.UserService

@RestController
@Validated
@RequestMapping("/person-on-probation-user", produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.IMAGE_JPEG_VALUE])
@PreAuthorize("hasRole('RESETTLEMENT_PASSPORT_EDIT')")
class UserResourceController(private val userService: UserService) {

  @GetMapping("/{crn}/user", produces = [MediaType.APPLICATION_JSON_VALUE])
  @Operation(summary = "Get User Info by CRN Id", description = "Person on Probation User Data based on crn id")
  @ApiResponses(
    value = [
      ApiResponse(
        responseCode = "200",
        description = "Successful Operation",
      ),
      ApiResponse(
        responseCode = "404",
        description = "Not found",
        content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE)],
      ),
      ApiResponse(
        responseCode = "401",
        description = "Unauthorized to access this endpoint",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))],
      ),
      ApiResponse(
        responseCode = "403",
        description = "Forbidden, requires an appropriate role",
        content = [
          Content(
            mediaType = "application/json",
            schema = Schema(implementation = ErrorResponse::class),
          ),
        ],
      ),
      ApiResponse(
        responseCode = "400",
        description = "Incorrect input options provided",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))],
      ),
    ],
  )
  fun getUserByCrn(
    @Schema(example = "123", required = true)
    @PathVariable("crn")
    @Parameter(required = true)
    crn: String,
  ) = userService.getUserByCRN(crn)

  @GetMapping("/{crn}/user/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
  @Operation(summary = "Get User Info by CRN Id and User ID", description = "Person on Probation User Data based on crn id and user id")
  @ApiResponses(
    value = [
      ApiResponse(
        responseCode = "200",
        description = "Successful Operation",
      ),
      ApiResponse(
        responseCode = "404",
        description = "Not found",
        content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE)],
      ),
      ApiResponse(
        responseCode = "401",
        description = "Unauthorized to access this endpoint",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))],
      ),
      ApiResponse(
        responseCode = "403",
        description = "Forbidden, requires an appropriate role",
        content = [
          Content(
            mediaType = "application/json",
            schema = Schema(implementation = ErrorResponse::class),
          ),
        ],
      ),
      ApiResponse(
        responseCode = "400",
        description = "Incorrect input options provided",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))],
      ),
    ],
  )
  fun getUserByCrnAndId(
    @Schema(example = "123", required = true)
    @PathVariable("crn")
    @Parameter(required = true)
    crn: String,
    @Schema(example = "123", required = true)
    @PathVariable("id")
    @Parameter(required = true)
    id: Long,
  ) = userService.getUserByCrnAndUserId(crn, id)

  @GetMapping("/users/all", produces = [MediaType.APPLICATION_JSON_VALUE])
  @Operation(summary = "Get All users details", description = "Person on Probation User List")
  @ApiResponses(
    value = [
      ApiResponse(
        responseCode = "200",
        description = "Successful Operation",
      ),
      ApiResponse(
        responseCode = "404",
        description = "Not found",
        content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE)],
      ),
      ApiResponse(
        responseCode = "401",
        description = "Unauthorized to access this endpoint",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))],
      ),
      ApiResponse(
        responseCode = "403",
        description = "Forbidden, requires an appropriate role",
        content = [
          Content(
            mediaType = "application/json",
            schema = Schema(implementation = ErrorResponse::class),
          ),
        ],
      ),
      ApiResponse(
        responseCode = "400",
        description = "Incorrect input options provided",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))],
      ),
    ],
  )
  fun getAllUsers() = userService.getAllUsers()

  @PostMapping("/user", produces = [MediaType.APPLICATION_JSON_VALUE])
  @Operation(summary = "Create a Person on Probation User", description = "Create a person on probation User")
  @ApiResponses(
    value = [
      ApiResponse(
        responseCode = "200",
        description = "Successful Operation",
      ),
      ApiResponse(
        responseCode = "404",
        description = "Not found",
        content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE)],
      ),
      ApiResponse(
        responseCode = "401",
        description = "Unauthorized to access this endpoint",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))],
      ),
      ApiResponse(
        responseCode = "403",
        description = "Forbidden, requires an appropriate role",
        content = [
          Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = Schema(implementation = ErrorResponse::class),
          ),
        ],
      ),
      ApiResponse(
        responseCode = "400",
        description = "Incorrect information provided",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))],
      ),
    ],
  )
  fun postUserByCrn(
    @RequestBody
    userPost: UserPost,
  ) = userService.createUser(userPost)

  @PatchMapping("/{crn}/user/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
  @Operation(summary = "Update Person on Probation User", description = "Update Person on Probation User")
  @ApiResponses(
    value = [
      ApiResponse(
        responseCode = "200",
        description = "Successful Operation",
      ),
      ApiResponse(
        responseCode = "404",
        description = "Not found",
        content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE)],
      ),
      ApiResponse(
        responseCode = "401",
        description = "Unauthorized to access this endpoint",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))],
      ),
      ApiResponse(
        responseCode = "403",
        description = "Forbidden, requires an appropriate role",
        content = [
          Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = Schema(implementation = ErrorResponse::class),
          ),
        ],
      ),
      ApiResponse(
        responseCode = "400",
        description = "Incorrect information provided",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))],
      ),
    ],
  )
  fun patchUserByCrn(
    @Schema(example = "AXXXS", required = true)
    @PathVariable("crn")
    crn: String,
    @PathVariable("id")
    @Parameter(required = true)
    id: String,
    @RequestBody
    userPatchDTO: UserPatch,
  ): UserEntity? {
    val userEntity = userService.getUserByCrnAndUserId(crn, id.toLong())
      ?: throw NoDataWithCodeFoundException(
        "UserId",
        id,
      )
    return userService.updateUser(userEntity, userPatchDTO)
  }

  @GetMapping("/onelogin/user/{urn}", produces = [MediaType.APPLICATION_JSON_VALUE])
  @Operation(summary = "Get User Info by One Login URN", description = "Person on Probation User Data based on one login urn")
  @ApiResponses(
    value = [
      ApiResponse(
        responseCode = "200",
        description = "Successful Operation",
      ),
      ApiResponse(
        responseCode = "404",
        description = "Not found",
        content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE)],
      ),
      ApiResponse(
        responseCode = "401",
        description = "Unauthorized to access this endpoint",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))],
      ),
      ApiResponse(
        responseCode = "403",
        description = "Forbidden, requires an appropriate role",
        content = [
          Content(
            mediaType = "application/json",
            schema = Schema(implementation = ErrorResponse::class),
          ),
        ],
      ),
      ApiResponse(
        responseCode = "400",
        description = "Incorrect input options provided",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))],
      ),
    ],
  )
  fun getUserByUrn(
    @Schema(example = "123", required = true)
    @PathVariable("urn")
    @Parameter(required = true)
    urn: String,
  ) = userService.getUserByUrn(urn)
}
