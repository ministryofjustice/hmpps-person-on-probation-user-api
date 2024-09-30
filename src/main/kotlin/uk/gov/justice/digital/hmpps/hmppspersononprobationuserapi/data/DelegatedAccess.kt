package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.data

data class DelegatedAccessPost(
  var initiatedUserId: Int,
  var delegatedUserId: Int,
)

data class DelegatedAccessPatch(
  var delegatedUserId: Int,
)
