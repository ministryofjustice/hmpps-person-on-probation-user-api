# hmpps-person-on-probation-user-api
[![repo standards badge](https://img.shields.io/badge/dynamic/json?color=blue&style=flat&logo=github&label=MoJ%20Compliant&query=%24.result&url=https%3A%2F%2Foperations-engineering-reports.cloud-platform.service.justice.gov.uk%2Fapi%2Fv1%2Fcompliant_public_repositories%2Fhmpps-person-on-probation-user-api)](https://operations-engineering-reports.cloud-platform.service.justice.gov.uk/public-github-repositories.html#hmpps-person-on-probation-user-api "Link to report")
[![CircleCI](https://circleci.com/gh/ministryofjustice/hmpps-person-on-probation-user-api/tree/main.svg?style=svg)](https://circleci.com/gh/ministryofjustice/hmpps-person-on-probation-user-api)
[![Docker Repository on ghcr ](https://img.shields.io/badge/ghcr.io-repository-2496ED.svg?logo=docker)](https://ghcr.io/ministryofjustice/hmpps-person-on-probation-user-api)
[![API docs](https://img.shields.io/badge/API_docs_-view-85EA2D.svg?logo=swagger)](https://hmpps-person-on-probation-user-api-dev.hmpps.service.justice.gov.uk/webjars/swagger-ui/index.html?configUrl=/v3/api-docs)

This is a skeleton project from which to create new kotlin projects from.

# Instructions

If this is a HMPPS project then the project will be created as part of bootstrapping - 
see https://github.com/ministryofjustice/hmpps-project-bootstrap.

## Creating a CloudPlatform namespace

When deploying to a new namespace, you may wish to use this template kotlin project namespace as the basis for your new namespace:

<https://github.com/ministryofjustice/cloud-platform-environments/tree/main/namespaces/live.cloud-platform.service.justice.gov.uk/hmpps-person-on-probation-user-api>

Copy this folder, update all the existing namespace references, and submit a PR to the CloudPlatform team. Further instructions from the CloudPlatform team can be found here: <https://user-guide.cloud-platform.service.justice.gov.uk/#cloud-platform-user-guide>

## Renaming from Hmpps Person On Probation User Api - github Actions

Once the new repository is deployed. Navigate to the repository in github, and select the `Actions` tab.
Click the link to `Enable Actions on this repository`.

Find the Action workflow named: `rename-project-create-pr` and click `Run workflow`.  This workflow will
execute the `rename-project.bash` and create Pull Request for you to review.  Review the PR and merge.

Note: ideally this workflow would run automatically however due to a recent change github Actions are not
enabled by default on newly created repos. There is no way to enable Actions other then to click the button in the UI.
If this situation changes we will update this project so that the workflow is triggered during the bootstrap project.
Further reading: <https://github.community/t/workflow-isnt-enabled-in-repos-generated-from-template/136421>

## Manually renaming from Hmpps Person On Probation User Api

Run the `rename-project.bash` and create a PR.

The `rename-project.bash` script takes a single argument - the name of the project and calculates from it:
* The main class name (project name converted to pascal case) 
* The project description (class name with spaces between the words)
* The main package name (project name with hyphens removed)

It then performs a search and replace and directory renames so the project is ready to be used.

## Filling in the `productId`

To allow easy identification of an application, the product Id of the overall product should be set in `values.yaml`. 
The Service Catalogue contains a list of these IDs and is currently in development here https://developer-portal.hmpps.service.justice.gov.uk/products
