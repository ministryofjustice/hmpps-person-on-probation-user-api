package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.data

data class DelegatedAccess(
  var initiatedUserId: Long,
  var delegatedUserId: Long,
)
