package com.glc.smartcar.ui.profile

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.glc.smartcar.ui.components.cards.ProfileMenuCardComponent
import com.glc.smartcar.ui.components.inputs.ProfileInputComponent
import com.glc.smartcar.ui.components.profile.ProfileHeaderComponent
import com.glc.smartcar.ui.components.text.SubSectionTitleComponent
import com.glc.smartcar.ui.components.topbar.TopBarComponent
import com.glc.smartcar.ui.theme.SmartCarTheme
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    onNavigateToLogin: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collectLatest { effect ->
            when (effect) {
                is ProfileUiSideEffect.NavigateToLogin -> {
                    onNavigateToLogin()
                }
                is ProfileUiSideEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    ProfileScreenContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun ProfileScreenContent(
    state: ProfileUiState,
    onEvent: (ProfileUiEvent) -> Unit
) {

    if (state.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { onEvent(ProfileUiEvent.OnDismissDeleteDialogClick) },
            title = { Text("Excluir Conta") },
            text = { Text("Essa ação removerá permanentemente sua conta e todas as avaliações salvas de nossos servidores. Tem certeza de que deseja prosseguir?") },
            confirmButton = {
                TextButton(
                    onClick = { onEvent(ProfileUiEvent.OnConfirmDeleteAccountClick) },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onEvent(ProfileUiEvent.OnDismissDeleteDialogClick) }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                TopBarComponent(title = "Perfil")
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                            RoundedCornerShape(16.dp)
                        )
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    Column {
                        ProfileHeaderComponent(
                            name = state.nome,
                            subtitle = state.membroDesde
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        ProfileInputComponent(
                            label = "Nome",
                            icon = Icons.Outlined.Person,
                            value = state.nome,
                            onValueChange = {},
                            isEditable = false
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        ProfileInputComponent(
                            label = "Email",
                            icon = Icons.Outlined.Email,
                            value = state.email,
                            onValueChange = {},
                            isEditable = false
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    SubSectionTitleComponent(
                        title = "Gerenciamento de conta",
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    ProfileMenuCardComponent(
                        text = "Logout",
                        icon = Icons.AutoMirrored.Outlined.Logout,
                        onClick = { onEvent(ProfileUiEvent.OnLogoutClick) }
                    )

                    ProfileMenuCardComponent(
                        text = "Excluir meus dados",
                        icon = Icons.Outlined.Delete,
                        description = "Essa ação removerá permanentemente sua conta e todas as avaliações salvas de nossos servidores.",
                        isDestructive = true,
                        onClick = { onEvent(ProfileUiEvent.OnDeleteAccountClick) }
                    )
                }
            }
        }
    }
}

@Preview(name = "User Profile - Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "User Profile - Dark Mode"
)
@Composable
private fun ProfileScreenContentPreview() {
    SmartCarTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ProfileScreenContent(
                state = ProfileUiState(
                    nome = "Carlos Vinícyus",
                    email = "carlosvinicyus@smartcar.io",
                    dataCriacao = "2026-06-29T12:34:56Z",
                    showDeleteDialog = true
                )
            ) {}
        }
    }
}