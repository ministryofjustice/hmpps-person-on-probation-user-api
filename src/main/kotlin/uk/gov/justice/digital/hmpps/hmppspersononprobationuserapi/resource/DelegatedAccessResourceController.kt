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
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.config.ErrorResponse
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.data.DelegatedAccess
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.entity.DelegatedAccessEntity
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.entity.DelegatedAccessPermissionEntity
import uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.service.DelegatedAccessService

@RestController
@Validated
@RequestMapping("/person-on-probation-user", produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.IMAGE_JPEG_VALUE])
@PreAuthorize("hasRole('RESETTLEMENT_PASSPORT_EDIT')")
class DelegatedAccessResourceController(private val delegatedService: DelegatedAccessService) {

  @PostMapping("/delegate/access", produces = [MediaType.APPLICATION_JSON_VALUE])
  @Operation(summary = "Delegate Access to a Person for Probation User", description = "Create access to a  delegated person for a probation User")
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
  fun createDelegatedAccess(
    @RequestBody
    delegatedAccessPost: DelegatedAccess,
  ) = delegatedService.createDelegatedAccess(delegatedAccessPost)

  @DeleteMapping("/remove/access/{accessId}", produces = [MediaType.APPLICATION_JSON_VALUE])
  @Operation(summary = "Revoke Delegate Access for a Probation User", description = "Revoke delegate access for a probation user")
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
  fun removeDelegatedAccess(
    @PathVariable("accessId")
    @Parameter(required = true)
    accessId: Long,
  ): DelegatedAccessEntity? {
    return delegatedService.removeDelegatedAccess(accessId)
  }

  @GetMapping("/all/access/{userid}", produces = [MediaType.APPLICATION_JSON_VALUE])
  @Operation(summary = "Get all delegated access list by person on probation user id", description = "Person on Probation User All Delegated access")
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
  fun getDelegatedAccessListByUserId(
    @Schema(example = "123", required = true)
    @PathVariable("userid")
    @Parameter(required = true)
    userid: Long,
  ) = delegatedService.getAllAccessByInitiatorUserId(userid)

  @GetMapping("/active/access/{userid}", produces = [MediaType.APPLICATION_JSON_VALUE])
  @Operation(summary = "Get active delegated access list by person on probation user id", description = "Person on Probation User Active Delegated access")
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
  fun getActiveDelegatedAccessListByUserId(
    @Schema(example = "123", required = true)
    @PathVariable("userid")
    @Parameter(required = true)
    userid: Long,
  ) = delegatedService.getActiveAccessByInitiatorUserId(userid)

  @PostMapping("/grant/permission/{accessId}", produces = [MediaType.APPLICATION_JSON_VALUE])
  @Operation(summary = "Grant Permission for a Person to access", description = "Create permission for a  delegated person to access probation user data")
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
  fun grantDelegatedUserPermission(
    @Schema(example = "123", required = true)
    @PathVariable("accessId")
    @Parameter(required = true)
    accessId: Long,
  ) = delegatedService.grantDelegatedAccessPermission(accessId, 1)

  @DeleteMapping("/revoke/permission/{accessId}", produces = [MediaType.APPLICATION_JSON_VALUE])
  @Operation(summary = "Revoke Permission for a Person to access", description = "Remove permission for the  delegated person to access probation user data")
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
  fun revokeDelegatedUserPermission(
    @Schema(example = "123", required = true)
    @PathVariable("accessId")
    @Parameter(required = true)
    accessId: Long,
  ): DelegatedAccessPermissionEntity? {
    return delegatedService.revokeDelegatedAccessPermission(accessId, 1)
  }

  @GetMapping("/all/permission/{userid}", produces = [MediaType.APPLICATION_JSON_VALUE])
  @Operation(summary = "Get all delegated permission list by person on probation user id", description = "Person on Probation User Delegated permissions")
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
  fun getDelegatedPermissionListByUserId(
    @Schema(example = "123", required = true)
    @PathVariable("userid")
    @Parameter(required = true)
    userid: Long,
  ) = delegatedService.getAllAccessPermissionByUserId(userid)

  @GetMapping("/active/permission/{userid}", produces = [MediaType.APPLICATION_JSON_VALUE])
  @Operation(summary = "Get delegated active permission list by person on probation user id", description = "Person on Probation User Delegated Active Permissions")
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
  fun getActiveDelegatedPermissionListByUserId(
    @Schema(example = "123", required = true)
    @PathVariable("userid")
    @Parameter(required = true)
    userid: Long,
  ) = delegatedService.getActiveAccessPermissionByInitiatorUserId(userid)

  @GetMapping("/all/access/delegated/{userid}", produces = [MediaType.APPLICATION_JSON_VALUE])
  @Operation(summary = "Get all delegated access list by non-probation user id", description = "All delegated access list by non-probation user id")
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
  fun getDelegatedAccessListByDelegatedUserId(
    @Schema(example = "123", required = true)
    @PathVariable("userid")
    @Parameter(required = true)
    userid: Long,
  ) = delegatedService.getAllAccessByDelegatedUserId(userid)

  @GetMapping("/active/access/delegated/{userid}", produces = [MediaType.APPLICATION_JSON_VALUE])
  @Operation(summary = "Get active delegated access list by non-probation user id", description = "Active delegated access list by non-probation user id")
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
  fun getActiveDelegatedAccessListByDelegatedUserId(
    @Schema(example = "123", required = true)
    @PathVariable("userid")
    @Parameter(required = true)
    userid: Long,
  ) = delegatedService.getActiveAccessByDelegatedUserId(userid)

  @GetMapping("/all/permission/delegated/{userid}", produces = [MediaType.APPLICATION_JSON_VALUE])
  @Operation(summary = "Get all delegated permission list by non-probation user id", description = "All delegated permission list by non-probation user id")
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
  fun getDelegatedPermissionListByDelegatedUserId(
    @Schema(example = "123", required = true)
    @PathVariable("userid")
    @Parameter(required = true)
    userid: Long,
  ) = delegatedService.getAllAccessPermissionByDelegatedUserId(userid)

  @GetMapping("/active/permission/delegated/{userid}", produces = [MediaType.APPLICATION_JSON_VALUE])
  @Operation(summary = "Get all delegated permission list by non-probation user id", description = "All delegated permission list by non-probation user id")
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
  fun getActiveDelegatedPermissionListByDelegatedUserId(
    @Schema(example = "123", required = true)
    @PathVariable("userid")
    @Parameter(required = true)
    userid: Long,
  ) = delegatedService.getActiveAccessPermissionByDelegatedUserId(userid)
}
