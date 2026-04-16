package com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.presentation

import ktsandroidkmp.composeapp.generated.resources.Res
import ktsandroidkmp.composeapp.generated.resources.issues_screen_check_permissions_error
import ktsandroidkmp.composeapp.generated.resources.issues_screen_create_issue_error
import ktsandroidkmp.composeapp.generated.resources.issues_screen_create_issue_success
import ktsandroidkmp.composeapp.generated.resources.issues_screen_create_issue_warning
import ktsandroidkmp.composeapp.generated.resources.issues_screen_issue_name_required
import org.jetbrains.compose.resources.StringResource

object Strings {
    val createIssueWarning: StringResource = Res.string.issues_screen_create_issue_warning
    val checkRoolsErr: StringResource = Res.string.issues_screen_check_permissions_error
    val issueNameIsRequired: StringResource = Res.string.issues_screen_issue_name_required
    val createIssueSuccessMessage: StringResource = Res.string.issues_screen_create_issue_success
    val createIssueError: StringResource = Res.string.issues_screen_create_issue_error
}