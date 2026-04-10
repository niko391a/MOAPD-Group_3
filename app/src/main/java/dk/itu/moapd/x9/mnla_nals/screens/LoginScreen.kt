package dk.itu.moapd.x9.mnla_nals.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dk.itu.moapd.x9.mnla_nals.R
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import dk.itu.moapd.x9.mnla_nals.AppNavigationBar
import dk.itu.moapd.x9.mnla_nals.ViewModels.AuthViewModel
import dk.itu.moapd.x9.mnla_nals.ui.theme.X9Theme

@Composable
fun LoginScreen(authViewModel: AuthViewModel = viewModel()) {
    val context = LocalContext.current
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0F172A))
        ) {
        // Background Gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0xFF1E293B)),
                        startY = 300f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Branding
            Image(
                painter = painterResource(id = R.drawable.x9_logo),
                contentDescription = "X9 Logo",
                modifier = Modifier
                    .size(120.dp) // Adjusted size for an image
                    .clip(RoundedCornerShape(24.dp)),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Real-time Traffic Intelligence",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
//                maxLines = 1,
//                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Know the road before you go.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.LightGray.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(64.dp))

            // Google Sign-In Button
            Button(
                onClick = { authViewModel.signInWithGoogle(context) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Continue with Google",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Guest Sign-In Button
            Button(
                onClick = { authViewModel.signInAsGuest() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Continue as Guest",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}