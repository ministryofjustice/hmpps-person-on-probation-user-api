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
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.config.ErrorResponse
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.data.UserOTP
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.entity.DelegatedUserOTPEntity
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.service.DelegatedUserOTPService

@RestController
@Validated
@RequestMapping("/person-on-probation-user", produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.IMAGE_JPEG_VALUE])
@PreAuthorize("hasRole('RESETTLEMENT_PASSPORT_EDIT')")
class DelegatedUserOTPResourceController(private val delegatedUserOTPService: DelegatedUserOTPService) {

  @PostMapping("/access/otp", produces = [MediaType.APPLICATION_JSON_VALUE])
  @Operation(summary = "Create an OTP for non-probation user", description = "Create an otp for a non-probation user")
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
  fun postUserOTPByUserId(
    @RequestBody
    userOTP: UserOTP,
  ) = delegatedUserOTPService.createDelegatedUserOTP(userOTP)

  @DeleteMapping("/expire/otp/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
  @Operation(summary = "OTP Expiry for non-probation user", description = "OTP expired for non-probation user")
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
  fun deleteDelegatedUserOTP(
    @PathVariable("id")
    @Parameter(required = true)
    id: Long,
  ) = delegatedUserOTPService.deleteDelegatedUserOTP(id)

  @GetMapping("/access/otp/{userid}", produces = [MediaType.APPLICATION_JSON_VALUE])
  @Operation(summary = "Get User OTP by user id", description = "Delegated User OTP Data based on user id")
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
  fun getUserOTPByUserIdAndEmail(
    @Schema(example = "123", required = true)
    @PathVariable("userid")
    @Parameter(required = true)
    userid: Long,
    @RequestParam(value = "email")
    email: String = null.toString(),
  ): List<DelegatedUserOTPEntity> {
    return if (email != "null" && email.isNotEmpty() && email.isNotBlank()) {
      delegatedUserOTPService.getUserOTPByUserIdAndEmail(userid, email)
    } else {
      delegatedUserOTPService.getUserOTPByUserId(userid)
    }
  }
}
