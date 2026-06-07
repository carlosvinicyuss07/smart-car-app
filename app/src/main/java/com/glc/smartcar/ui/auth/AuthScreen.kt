package com.glc.smartcar.ui.auth

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glc.smartcar.R
import com.glc.smartcar.ui.components.buttons.PrimaryButtonComponent
import com.glc.smartcar.ui.components.inputs.AuthInputComponent
import com.glc.smartcar.ui.theme.SmartCarTheme
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = koinViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collectLatest { effect ->
            when (effect) {
                is AuthUiSideEffect.NavigateToHome -> {
                    // Navegar para a tela principal

                    // navController.navigate("home_route") {
                    //     popUpTo("auth_route") { inclusive = true } // Limpa a pilha para não voltar pro login
                    // }
                }
                is AuthUiSideEffect.ShowSnackbar -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    AuthScreenContent(
        state = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun AuthScreenContent(
    state: AuthUiState,
    onEvent: (AuthUiEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(id = R.mipmap.ic_launcher_foreground),
            contentDescription = "SmartCar Logo",
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Sua jornada inteligente começa aqui.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (!state.isLoginMode) {
            AuthInputComponent(
                label = "Nome completo",
                placeholder = "Digite seu nome",
                icon = Icons.Outlined.Person,
                value = state.nome,
                onValueChange = { onEvent(AuthUiEvent.OnNomeChanged(it)) }
            )
            state.formErrors.nomeError?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        AuthInputComponent(
            label = "Endereço de email",
            placeholder = "Digite seu email",
            icon = Icons.Outlined.Email,
            value = state.email,
            onValueChange = { onEvent(AuthUiEvent.OnEmailChanged(it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        state.formErrors.emailError?.let {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        AuthInputComponent(
            label = "Senha",
            placeholder = if (state.isLoginMode) "Digite sua senha" else "Crie uma senha",
            icon = Icons.Outlined.Lock,
            value = state.senha,
            onValueChange = { onEvent(AuthUiEvent.OnSenhaChanged(it)) },
            isPassword = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        state.formErrors.senhaError?.let {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (!state.isLoginMode) {
            Spacer(modifier = Modifier.height(16.dp))
            AuthInputComponent(
                label = "Confirmar senha",
                placeholder = "Confirme sua senha",
                icon = Icons.Outlined.Lock,
                value = state.confirmarSenha,
                onValueChange = { onEvent(AuthUiEvent.OnConfirmarSenhaChanged(it)) },
                isPassword = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            state.formErrors.confirmarSenhaError?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (state.isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        } else {
            PrimaryButtonComponent(
                text = if (state.isLoginMode) "Login" else "Criar uma conta",
                onClick = { onEvent(AuthUiEvent.OnSubmitClick) }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Alternar modo (Login/Cadastro)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (state.isLoginMode) "Não tem uma conta? " else "Já tem uma conta? ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
            Text(
                text = if (state.isLoginMode) "Cadastre-se" else "Login",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.clickable { onEvent(AuthUiEvent.OnToggleModeClick) }
            )
        }
    }
}

@Preview(name = "Login - Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Login - Dark Mode"
)
@Composable
private fun AuthScreenLoginPreview() {
    SmartCarTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            AuthScreenContent(
                state = AuthUiState(
                    isLoginMode = true,
                    email = "carlos@smartcar.io",
                    senha = "123456"
                ),
                onEvent = {}
            )
        }
    }
}

@Preview(name = "Register - Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Register - Dark Mode"
)
@Composable
private fun AuthScreenRegisterPreview() {
    SmartCarTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            AuthScreenContent(
                state = AuthUiState(
                    isLoginMode = false,
                    nome = "Carlos Vinícyus",
                    email = "carlos@smartcar.io",
                    senha = "123456",
                    confirmarSenha = "123456"
                ),
                onEvent = {}
            )
        }
    }
}